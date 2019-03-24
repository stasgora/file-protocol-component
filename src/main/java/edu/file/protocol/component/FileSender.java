package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.sockets.SenderSocket;

import java.net.InetAddress;

public class FileSender {

	private Thread socketThread;

	private final InetAddress address;
	private final ConnectionEventHandler eventHandler;

	public FileSender(ConnectionEventHandler eventHandler, InetAddress address) {
		this.eventHandler = eventHandler;
		this.address = address;
	}

	public void sendFile(byte[] file, String properties) {
		SenderSocket socket = new SenderSocket(eventHandler, address, file, properties);
		socketThread = new Thread(socket);
		socketThread.run();
	}

}
