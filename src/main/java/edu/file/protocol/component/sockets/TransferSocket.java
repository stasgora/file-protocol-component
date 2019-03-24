package edu.file.protocol.component.sockets;

import java.net.Socket;
import java.util.function.Consumer;

abstract class TransferSocket implements Runnable {

	static final int PORT = 5432;

	Socket socket;
	Consumer<Double> progressReportCallback;

	TransferSocket(Consumer<Double> progressReportCallback) {
		this.progressReportCallback = progressReportCallback;
	}

}
