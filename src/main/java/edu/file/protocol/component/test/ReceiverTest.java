package edu.file.protocol.component.test;

import edu.file.encryption.component.CryptoComponent;
import edu.file.encryption.component.interfaces.ICryptoComponent;
import edu.file.protocol.component.FileReceiver;
import edu.file.protocol.component.enums.ConnectionStatus;
import edu.file.protocol.component.interfaces.ConnectionEventHandler;

public class ReceiverTest {
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
		SenderTest.Handler handler = new SenderTest.Handler();
		ICryptoComponent cryptoComponent = new CryptoComponent("user", "pass");
		FileReceiver receiver = new FileReceiver(handler, cryptoComponent, (file, name) -> System.out.println("received"));
	}
}
