package edu.file.protocol.component;

import edu.file.encryption.component.enums.CipherAlgorithmMode;
import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;
import edu.file.protocol.component.interfaces.FileSentEvent;
import edu.file.protocol.component.sockets.SenderSocket;

import java.io.File;
import java.net.InetAddress;

public class FileSender {

	private Thread socketThread;

	private FileSentEvent fileSentEvent;
	private ICryptoComponent cryptoComponent;
	private final InetAddress address;
	private final ConnectionEventHandler eventHandler;

	public FileSender(ConnectionEventHandler eventHandler, FileSentEvent fileSentEvent, ICryptoComponent cryptoComponent, InetAddress address) {
		this.eventHandler = eventHandler;
		this.fileSentEvent = fileSentEvent;
		this.cryptoComponent = cryptoComponent;
		this.address = address;
	}

	public void sendFile(File file, CipherAlgorithmMode algorithmMode, String recipient) {
		SenderSocket socket = new SenderSocket(eventHandler, cryptoComponent, fileSentEvent, address, file, algorithmMode, recipient);
		socketThread = new Thread(socket);
		socketThread.start();
	}

}
