package edu.file.protocol.component;

import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileReceivedEvent;
import edu.file.protocol.component.sockets.ReceiverSocket;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReceiver {

	private static final Logger LOGGER = Logger.getLogger(FileReceiver.class.getName());

	private Thread socketThread;

	public FileReceiver(ConnectionEventHandler eventHandler, ICryptoComponent cryptoComponent, FileReceivedEvent fileReceivedEvent) {
		ReceiverSocket socket = new ReceiverSocket(eventHandler, cryptoComponent, fileReceivedEvent);
		socketThread = new Thread(socket);
	}

	public void startSocket() {
		socketThread.start();
	}

	public void close() {
		socketThread.interrupt();
	}

}
