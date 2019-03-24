package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.sockets.SenderSocket;

import java.io.IOException;
import java.net.InetAddress;

public class FileSender {

	private final InetAddress address;
	private final ConnectionEventHandler eventHandler;

	public FileSender(ConnectionEventHandler eventHandler, InetAddress address) {
		this.eventHandler = eventHandler;
		this.address = address;
	}

	public void sendFile(byte[] file, String properties) throws IOException {
		SenderSocket socket = new SenderSocket(eventHandler, address, file, properties);
		new Thread(socket).run();
	}

}
