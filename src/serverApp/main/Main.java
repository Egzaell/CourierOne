package serverApp.main;

import java.io.IOException;
import java.net.ServerSocket;

import clientApp.dataContainer.DataContainer;
import serverApp.dataBaseConnector.DataBaseConnector;
import serverApp.networking.ServerNetworking;
import serverApp.serverManagement.ServerManagement;


public class Main {
	
	public static void main(String[] args) throws IOException {
		DataContainer dataContainer = new DataContainer();
		ServerSocket serverSocket = new ServerSocket(9000);
		DataBaseConnector dbConnector = new DataBaseConnector(dataContainer);
		ServerManagement serverManagement = new ServerManagement(dataContainer);
		Thread dbConnectorThread = new Thread(dbConnector);
		serverManagement.start();
		dbConnectorThread.start();
		while (dataContainer.isProgramRunning) {
			new Thread(new ServerNetworking(dataContainer, serverSocket.accept())).start();
		}
	}

}
