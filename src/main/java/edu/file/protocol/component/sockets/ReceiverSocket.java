package edu.file.protocol.component.sockets;

import edu.file.encryption.component.exceptions.NoRSAKeyFoundException;
import edu.file.encryption.component.exceptions.WrongKeyException;
import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.encryption.component.model.EncryptionParameters;
import edu.file.protocol.component.enums.ConnectionStatus;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileReceivedEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverSocket extends TransferSocket {

	private static final Logger LOGGER = Logger.getLogger(ReceiverSocket.class.getName());

	private final FileReceivedEvent fileReceivedEvent;

	public ReceiverSocket(ConnectionEventHandler eventHandler, ICryptoComponent cryptoComponent, FileReceivedEvent fileReceivedEvent) {
		super(eventHandler, cryptoComponent);
		this.fileReceivedEvent = fileReceivedEvent;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try (ServerSocket serverSocket = new ServerSocket(PORT);
			     Socket socket = serverSocket.accept()) {
				initializeSocket(socket);
				try{
					output.writeUTF(cryptoComponent.getPublicRSAKey());
					LOGGER.log(Level.INFO, "Before params");
					String parametersString = cryptoComponent.RSADecrypt(receiveBytes(), cryptoComponent.getPrivateRSAKey());
					EncryptionParameters parameters = objectMapper.readValue(parametersString, EncryptionParameters.class);
					LOGGER.log(Level.INFO, parametersString);
					cryptoComponent.setParameters(parameters);

					LOGGER.log(Level.INFO, "Before sessionkey");
					String sessionKey = cryptoComponent.RSADecrypt(receiveBytes(), cryptoComponent.getPrivateRSAKey());
					byte[] file = receiveFile(sessionKey, parameters);
					fileReceivedEvent.onFileReceived(file);
				}catch(NoRSAKeyFoundException e){
					LOGGER.log(Level.SEVERE, "Failed to get RSA key", e);
				}catch(WrongKeyException e){
					// Wrong key - returning file with random data
					byte[] file = new byte[1024];
					new Random().nextBytes(file);
					fileReceivedEvent.onFileReceived(file);
				}
			} catch (SocketTimeoutException e) {
				LOGGER.log(Level.WARNING, "Socket timeout", e);
				eventHandler.reportStatus(ConnectionStatus.TIMEOUT);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Socket error", e);
				eventHandler.reportStatus(ConnectionStatus.ERROR);
			}
		}
	}

	private byte[] receiveBytes() throws IOException {
		byte[] bytes = new byte[input.readInt()];
		if(input.read(bytes) != bytes.length) {
			LOGGER.log(Level.WARNING, "Did no read all the bytes");
		}
		return bytes;
	}

	private byte[] receiveFile(String sessionKey, EncryptionParameters parameters) throws IOException {
		byte[] file;
		int bytesReadSum = 0;
		try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead;
			while ((bytesRead = input.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesRead);
				bytesReadSum += bytesRead;
				eventHandler.reportTransferProgress(bytesReadSum / (double) parameters.fileLength);
			}
			try {
				file = cryptoComponent.AESDecrypt(outputStream.toByteArray(), sessionKey, parameters.cipherAlgMode);
			}catch(WrongKeyException e){
				// Wrong key - returning file with random data
				file = new byte[1024];
				new Random().nextBytes(file);
				fileReceivedEvent.onFileReceived(file);
			}
		}
		return file;
	}

}
