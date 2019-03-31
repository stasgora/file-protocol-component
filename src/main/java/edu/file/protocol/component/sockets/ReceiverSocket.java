package edu.file.protocol.component.sockets;

import edu.file.encryption.component.model.EncryptionParameters;
import edu.file.protocol.component.enums.ConnectionStatus;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileReceivedEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverSocket extends TransferSocket {

	private static final Logger LOGGER = Logger.getLogger(ReceiverSocket.class.getName());

	private final FileReceivedEvent fileReceivedEvent;

	public ReceiverSocket(ConnectionEventHandler eventHandler, FileReceivedEvent fileReceivedEvent) {
		super(eventHandler);
		this.fileReceivedEvent = fileReceivedEvent;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try (ServerSocket serverSocket = new ServerSocket(PORT);
			     Socket socket = serverSocket.accept()) {
				initializeSocket(socket);
				output.writeUTF(cryptoComponent.getPublicRSAKey());
				EncryptionParameters parameters = objectMapper.readValue(input.readUTF(), EncryptionParameters.class);
				cryptoComponent.setParameters(parameters);
				String sessionKey = cryptoComponent.RSADecrypt(input.readUTF());

				byte[] file;
				try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead;
					while ((bytesRead = input.read(buffer)) > 0) {
						outputStream.write(buffer, 0, bytesRead);
					}
					file = cryptoComponent.AESDecrypt(outputStream.toByteArray(), sessionKey);
				}
				fileReceivedEvent.onFileReceived(file, "name");
			} catch (SocketTimeoutException e) {
				LOGGER.log(Level.WARNING, "Socket timeout", e);
				eventHandler.reportStatus(ConnectionStatus.TIMEOUT);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Socket error", e);
				eventHandler.reportStatus(ConnectionStatus.ERROR);
			}
		}
	}

}
