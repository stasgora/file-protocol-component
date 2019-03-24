package edu.file.protocol.component.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class SenderSocket implements Runnable {

	private Socket socket;

	private final byte[] file;
	private final String properties;
	private Consumer<Double> progressReportCallback;

	public SenderSocket(InetAddress address, byte[] file, String properties, Consumer<Double> progressReportCallback) throws IOException {
		socket = new Socket(address, ReceiverSocket.PORT);

		this.file = file;
		this.properties = properties;
		this.progressReportCallback = progressReportCallback;
	}

	@Override
	public void run() {

	}

}
