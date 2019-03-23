package edu.file.protocol.component;

import edu.file.protocol.component.interfaces.FileSender;

public class FileSenderProtocol implements FileSender {

	private ProtocolSocket protocolSocket = new ProtocolSocket();

	@Override
	public boolean sendFile(byte[] file, String properties) {
		return false;
	}

}
