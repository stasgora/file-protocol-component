package edu.file.protocol.component.test;

import edu.file.encryption.component.CryptoComponent;
import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.protocol.component.FileSender;
import edu.file.protocol.component.enums.ConnectionStatus;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class SenderTest {
	static class Handler implements ConnectionEventHandler {
		@Override
		public void reportTransferProgress(double progress) {
			System.out.println("Progress: " + progress);
		}

		@Override
		public void reportStatus(ConnectionStatus status) {
			System.out.println("Status: " + status.name());
		}
	}

	public static void main(String[] args) {
		Handler handler = new Handler();
		ICryptoComponent cryptoComponent = new CryptoComponent("user", "pass");
		try {
			FileSender sender = new FileSender(handler, cryptoComponent, Inet4Address.getByName("127.0.0.1"));
			//sender.
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
