package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileReceivedEvent;
import edu.file.protocol.component.sockets.ReceiverSocket;

import java.io.IOException;

public class FileReceiver {

	public FileReceiver(ConnectionEventHandler eventHandler, FileReceivedEvent fileReceivedEvent) throws IOException {
		ReceiverSocket socket = new ReceiverSocket(eventHandler, fileReceivedEvent);
		new Thread(socket).run();
	}

}
