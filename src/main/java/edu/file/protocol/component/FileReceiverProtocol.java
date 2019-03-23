package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.FileReceiver;
import edu.file.protocol.component.interfaces.ProtocolService;

public class FileReceiverProtocol implements FileReceiver {

	private ProtocolService protocolService;
	private ProtocolSocket protocolSocket = new ProtocolSocket();

	public FileReceiverProtocol(ProtocolService protocolService) {
		this.protocolService = protocolService;
	}

	public String getPublicRSAKey() {
		return protocolService.getPublicRSAKey();
	}

}
