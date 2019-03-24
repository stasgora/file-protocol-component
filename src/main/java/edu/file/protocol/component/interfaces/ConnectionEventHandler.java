package edu.file.protocol.component.interfaces;

import edu.file.protocol.component.enums.ConnectionStatus;

public interface ConnectionEventHandler {

	String getPublicRSAKey();

	void reportTranferProgress(double progress);

	void reportStatus(ConnectionStatus status);

}
