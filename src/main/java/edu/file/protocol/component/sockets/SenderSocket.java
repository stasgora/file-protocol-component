package edu.file.protocol.component.sockets;

import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.encryption.component.model.EncryptionParameters;
import edu.file.protocol.component.enums.ConnectionStatus;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderSocket extends TransferSocket {

	private static final Logger LOGGER = Logger.getLogger(SenderSocket.class.getName());

	private final InetAddress address;
	private final File file;

	public SenderSocket(ConnectionEventHandler eventHandler, ICryptoComponent cryptoComponent, InetAddress address, File file) {
		super(eventHandler, cryptoComponent);
		this.address = address;
		this.file = file;
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(address, PORT)) {
			initializeSocket(socket);
			String clientRSAKey = input.readUTF();
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			sendParameters(fileBytes.length, clientRSAKey);
			output.writeUTF(cryptoComponent.RSAEncrypt(cryptoComponent.getSessionKey(), clientRSAKey));
			output.write(cryptoComponent.AESEncrypt(fileBytes, cryptoComponent.getSessionKey()));
		} catch (SocketTimeoutException e) {
			LOGGER.log(Level.WARNING, "Socket timeout", e);
			eventHandler.reportStatus(ConnectionStatus.TIMEOUT);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Socket error", e);
			eventHandler.reportStatus(ConnectionStatus.ERROR);
		}
	}

	private void sendParameters(int fileLength, String clientRSAKey) throws IOException {
		EncryptionParameters parameters = cryptoComponent.getParameters();
		parameters.fileName = file.getName();
		parameters.fileLength = fileLength;
		String parametersString = objectMapper.writeValueAsString(parameters);
		output.writeUTF(cryptoComponent.RSAEncrypt(parametersString, clientRSAKey));
	}

}
