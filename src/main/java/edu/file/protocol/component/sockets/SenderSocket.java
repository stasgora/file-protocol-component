package edu.file.protocol.component.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class SenderSocket extends TransferSocket {

	private final byte[] file;
	private final String properties;

	public SenderSocket(InetAddress address, byte[] file, String properties, Consumer<Double> progressReportCallback) throws IOException {
		super(progressReportCallback);
		socket = new Socket(address, PORT);
		getSocketStreams();

		this.file = file;
		this.properties = properties;
	}

	@Override
	public void run() {

	}

}
