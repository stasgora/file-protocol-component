package edu.file.protocol.component.sockets;

import edu.file.encryption.component.ICryptoComponent;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

abstract class TransferSocket implements Runnable {

	static final int PORT = 5432;
	private static final int SOCKET_TIMEOUT = 10 * 1000;

	DataInputStream input;
	DataOutputStream output;

	ConnectionEventHandler eventHandler;
	ICryptoComponent cryptoComponent;

	TransferSocket(ConnectionEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	void initializeSocket(Socket socket) throws IOException {
		socket.setSoTimeout(SOCKET_TIMEOUT);

		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}

}
