package edu.file.protocol.component.sockets;

import edu.file.protocol.component.interfaces.ProtocolService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class ReceiverSocket extends TransferSocket {

	private ServerSocket serverSocket;
	private final ProtocolService protocolService;

	public ReceiverSocket(ProtocolService protocolService, Consumer<Double> progressReportCallback) throws IOException {
		super(progressReportCallback);
		this.protocolService = protocolService;
		this.progressReportCallback = progressReportCallback;

		serverSocket = new ServerSocket(PORT);
		socket = serverSocket.accept();
	}

	@Override
	public void run() {

	}

}
