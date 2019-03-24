package edu.file.protocol.component;

import edu.file.protocol.component.sockets.SenderSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Consumer;

public class FileSender {

	private InetAddress address;
	private Consumer<Double> progressReportCallback;

	public FileSender(InetAddress address) {
		this.address = address;
	}

	public FileSender(InetAddress address, Consumer<Double> progressReportCallback) {
		this(address);
		this.progressReportCallback = progressReportCallback;
	}

	public void sendFile(byte[] file, String properties) throws IOException {
		SenderSocket socket = new SenderSocket(address, file, properties, progressReportCallback);
		new Thread(socket).run();
	}

}
