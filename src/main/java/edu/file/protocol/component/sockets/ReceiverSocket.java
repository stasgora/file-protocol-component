package edu.file.protocol.component.sockets;

import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileReceivedEvent;

import java.io.IOException;
import java.net.ServerSocket;

public class ReceiverSocket extends TransferSocket {

	private ServerSocket serverSocket;
	private final FileReceivedEvent fileReceivedEvent;

	public ReceiverSocket(ConnectionEventHandler eventHandler, FileReceivedEvent fileReceivedEvent) throws IOException {
		super(eventHandler);
		this.fileReceivedEvent = fileReceivedEvent;

		serverSocket = new ServerSocket(PORT);
		socket = serverSocket.accept();
		getSocketStreams();
	}

	@Override
	public void run() {

	}

}
