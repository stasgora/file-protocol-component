package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.sockets.SenderSocket;

import java.io.File;
import java.net.InetAddress;

public class FileSender {

	private Thread socketThread;

	private final InetAddress address;
	private final ConnectionEventHandler eventHandler;

	public FileSender(ConnectionEventHandler eventHandler, InetAddress address) {
		this.eventHandler = eventHandler;
		this.address = address;
	}

	public void sendFile(File file) {
		SenderSocket socket = new SenderSocket(eventHandler, address, file);
		socketThread = new Thread(socket);
		socketThread.run();
	}

}
