package edu.file.protocol.component;

import edu.file.encryption.component.interfaces.ICryptoComponent;
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

	private ICryptoComponent cryptoComponent;
	private final InetAddress address;
	private final ConnectionEventHandler eventHandler;

	public FileSender(ConnectionEventHandler eventHandler, ICryptoComponent cryptoComponent, InetAddress address) {
		this.eventHandler = eventHandler;
		this.cryptoComponent = cryptoComponent;
		this.address = address;
	}

	public void sendFile(File file) {
		try (FileInputStream input = new FileInputStream(file)) {
			byte[] fileArray = Files.readAllBytes(file.toPath());
			SenderSocket socket = new SenderSocket(eventHandler, cryptoComponent, address, fileArray);
			socketThread = new Thread(socket);
			socketThread.run();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Input file read error", e);
		}
	}

}
