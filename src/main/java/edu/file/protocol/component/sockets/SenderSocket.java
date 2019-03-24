package edu.file.protocol.component.sockets;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SenderSocket extends TransferSocket {

	private final byte[] file;
	private final String properties;

	public SenderSocket(ConnectionEventHandler eventHandler, InetAddress address, byte[] file, String properties) throws IOException {
		super(eventHandler);
		socket = new Socket(address, PORT);
		getSocketStreams();

		this.file = file;
		this.properties = properties;
	}

	@Override
	public void run() {

	}

}
