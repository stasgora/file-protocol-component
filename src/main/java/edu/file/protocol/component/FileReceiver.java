package edu.file.protocol.component;

import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileReceivedEvent;
import edu.file.protocol.component.sockets.ReceiverSocket;

public class FileReceiver {

	private Thread socketThread;

	public FileReceiver(ConnectionEventHandler eventHandler, ICryptoComponent cryptoComponent, FileReceivedEvent fileReceivedEvent) {
		ReceiverSocket socket = new ReceiverSocket(eventHandler, cryptoComponent, fileReceivedEvent);
		socketThread = new Thread(socket);
		socketThread.start();
	}

	public void close() {
		socketThread.interrupt();
	}

}
