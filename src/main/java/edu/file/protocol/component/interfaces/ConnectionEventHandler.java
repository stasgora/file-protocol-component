package edu.file.protocol.component.interfaces;

import edu.file.protocol.component.enums.ConnectionStatus;

public interface ConnectionEventHandler {

	void reportTranferProgress(double progress);

	void reportStatus(ConnectionStatus status);

}
