package edu.file.protocol.component.sockets;

import edu.file.encryption.component.enums.CipherAlgorithmMode;
import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.encryption.component.model.EncryptionParameters;
import edu.file.protocol.component.enums.ConnectionStatus;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileSentEvent;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderSocket extends TransferSocket {

	private static final Logger LOGGER = Logger.getLogger(SenderSocket.class.getName());

	private final FileSentEvent fileSentEvent;
	private final InetAddress address;
	private final File file;
	private final CipherAlgorithmMode algorithmMode;
	private final String recipient;

	public SenderSocket(ConnectionEventHandler eventHandler, ICryptoComponent cryptoComponent, FileSentEvent fileSentEvent,
	                    InetAddress address, File file, CipherAlgorithmMode algorithmMode, String recipient) {
		super(eventHandler, cryptoComponent);
		this.fileSentEvent = fileSentEvent;
		this.address = address;
		this.file = file;
		this.algorithmMode = algorithmMode;
		this.recipient = recipient;
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(address, PORT)) {
			initializeSocket(socket);
			output.writeUTF(recipient);
			String clientRSAKey = input.readUTF();

			long fileEncStartTime = System.currentTimeMillis();
			byte[] encryptedFile = cryptoComponent.AESEncrypt(Files.readAllBytes(file.toPath()), cryptoComponent.getSessionKey(), algorithmMode);
			LOGGER.log(Level.INFO, "File encryption time: " + (System.currentTimeMillis() - fileEncStartTime) / 1000f + "s");

			sendParameters(encryptedFile.length, clientRSAKey);
			sendBytes(cryptoComponent.RSAEncrypt(cryptoComponent.getSessionKey(), clientRSAKey));

			long fileSendStartTime = System.currentTimeMillis();
			sendFile(encryptedFile);
			LOGGER.log(Level.INFO, "File send time: " + (System.currentTimeMillis() - fileSendStartTime) / 1000f + "s");

			fileSentEvent.onFileSent();
		} catch (SocketTimeoutException e) {
			LOGGER.log(Level.WARNING, "Socket timeout", e);
			eventHandler.reportStatus(ConnectionStatus.TIMEOUT);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Socket error", e);
			eventHandler.reportStatus(ConnectionStatus.ERROR);
		}
	}

	private void sendBytes(byte[] bytes) throws IOException {
		output.writeInt(bytes.length);
		output.write(bytes);
	}

	private void sendFile(byte[] file) throws IOException {
		int bytesSent = 0;
		while (bytesSent < file.length) {
			int nextByteIndex = bytesSent + BUFFER_SIZE <= file.length ? bytesSent + BUFFER_SIZE : file.length;
			output.write(Arrays.copyOfRange(file, bytesSent, nextByteIndex));
			bytesSent = nextByteIndex;
			eventHandler.reportTransferProgress(bytesSent / (double) file.length);
		}
	}

	private void sendParameters(int fileLength, String clientRSAKey) throws IOException {
		EncryptionParameters parameters = cryptoComponent.getParameters();
		parameters.fileLength = fileLength;
		parameters.cipherAlgMode = algorithmMode;
		int dotIndex = file.getName().lastIndexOf('.');
		if(dotIndex >= 0)
			parameters.fileExtension = file.getName().substring(dotIndex);
		parameters.recipient = recipient;
		String parametersString = objectMapper.writeValueAsString(parameters);
		sendBytes(cryptoComponent.RSAEncrypt(parametersString, clientRSAKey));
	}

}
