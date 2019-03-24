package edu.file.protocol.component.sockets;

import edu.file.protocol.component.interfaces.ProtocolService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class ReceiverSocket implements Runnable {

	static final int PORT = 5432;

	private ServerSocket serverSocket;
	private Socket socket;

	private final ProtocolService protocolService;
	private final Consumer<Double> progressReportCallback;

	public ReceiverSocket(ProtocolService protocolService, Consumer<Double> progressReportCallback) throws IOException {
		this.protocolService = protocolService;
		this.progressReportCallback = progressReportCallback;

		serverSocket = new ServerSocket(PORT);
		socket = serverSocket.accept();
	}

	@Override
	public void run() {

	}

}
