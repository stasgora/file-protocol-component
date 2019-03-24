package edu.file.protocol.component.sockets;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderSocket extends TransferSocket {

	private static final Logger LOGGER = Logger.getLogger(SenderSocket.class.getName());

	private final InetAddress address;
	private final byte[] file;
	private final String properties;

	public SenderSocket(ConnectionEventHandler eventHandler, InetAddress address, byte[] file, String properties) {
		super(eventHandler);
		this.address = address;
		this.file = file;
		this.properties = properties;
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(address, PORT)) {
			getSocketStreams(socket);

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Socket error", e);
		}
	}

}
