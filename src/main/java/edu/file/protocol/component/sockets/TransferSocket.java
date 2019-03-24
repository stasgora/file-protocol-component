package edu.file.protocol.component.sockets;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

abstract class TransferSocket implements Runnable {

	static final int PORT = 5432;

	DataInputStream input;
	DataOutputStream output;

	private ConnectionEventHandler eventHandler;

	TransferSocket(ConnectionEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	void getSocketStreams(Socket socket) throws IOException {
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}

}
