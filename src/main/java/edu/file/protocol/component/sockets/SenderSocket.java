package edu.file.protocol.component.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.file.protocol.component.enums.ConnectionStatus;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderSocket extends TransferSocket {

	private static final Logger LOGGER = Logger.getLogger(SenderSocket.class.getName());

	private final InetAddress address;
	private final byte[] file;

	public SenderSocket(ConnectionEventHandler eventHandler, InetAddress address, byte[] file) {
		super(eventHandler);
		this.address = address;
		this.file = file;
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(address, PORT)) {
			initializeSocket(socket);
			String clientRSAKey = input.readUTF();
			String parameters = objectMapper.writeValueAsString(cryptoComponent.getParameters());
			output.writeUTF(parameters);
			output.writeUTF(cryptoComponent.RSAEncrypt(cryptoComponent.getSessionKey(), clientRSAKey));
			output.write(cryptoComponent.AESEncrypt(file, cryptoComponent.getSessionKey()));
		} catch (SocketTimeoutException e) {
			LOGGER.log(Level.WARNING, "Socket timeout", e);
			eventHandler.reportStatus(ConnectionStatus.TIMEOUT);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Socket error", e);
			eventHandler.reportStatus(ConnectionStatus.ERROR);
		}
	}

}
