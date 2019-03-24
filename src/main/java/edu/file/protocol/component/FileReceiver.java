package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.ProtocolService;
import edu.file.protocol.component.sockets.ReceiverSocket;

import java.io.IOException;
import java.util.function.Consumer;

public class FileReceiver {

	public FileReceiver(ProtocolService protocolService) throws IOException {
		this(protocolService, null);
	}

	public FileReceiver(ProtocolService protocolService, Consumer<Double> progressReportCallback) throws IOException {
		ReceiverSocket socket = new ReceiverSocket(protocolService, progressReportCallback);
		new Thread(socket).run();
	}

}
