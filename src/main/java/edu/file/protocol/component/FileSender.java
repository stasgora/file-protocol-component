package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.sockets.SenderSocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSender {

	private static final Logger LOGGER = Logger.getLogger(FileSender.class.getName());

	private Thread socketThread;

	private final InetAddress address;
	private final ConnectionEventHandler eventHandler;

	public FileSender(ConnectionEventHandler eventHandler, InetAddress address) {
		this.eventHandler = eventHandler;
		this.address = address;
	}

	public void sendFile(File file) {
		byte[] fileArray = new byte[(int) file.length()];
		try (FileInputStream input = new FileInputStream(file)) {
			byte[] bytes = Files.readAllBytes(file.toPath());
			input.read(fileArray);
			SenderSocket socket = new SenderSocket(eventHandler, address, fileArray);
			socketThread = new Thread(socket);
			socketThread.run();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Input file read error", e);
		}
	}

}
