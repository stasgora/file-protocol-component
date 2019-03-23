package edu.file.protocol.component.interfaces;

public interface ProtocolService {

	String getPublicRSAKey();

	void onReceiveFile(byte[] file, String config);

}
