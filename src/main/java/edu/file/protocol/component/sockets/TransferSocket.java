package edu.file.protocol.component.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

abstract class TransferSocket implements Runnable {

	protected static final int PORT = 5432;
	protected static final int BUFFER_SIZE = 64;
	private static final int SOCKET_TIMEOUT = 10 * 1000;

	DataInputStream input;
	DataOutputStream output;

	ConnectionEventHandler eventHandler;
	ICryptoComponent cryptoComponent;

	protected ObjectMapper objectMapper = new ObjectMapper();

	TransferSocket(ConnectionEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	void initializeSocket(Socket socket) throws IOException {
		socket.setSoTimeout(SOCKET_TIMEOUT);

		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}

}
