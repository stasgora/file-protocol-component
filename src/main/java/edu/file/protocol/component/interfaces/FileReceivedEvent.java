package edu.file.protocol.component.interfaces;

public interface FileReceivedEvent {

	void onFileReceived(byte[] file, String extension);

}
