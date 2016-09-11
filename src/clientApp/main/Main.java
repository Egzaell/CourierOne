package clientApp.main;

import clientApp.client.Client;
import clientApp.dataContainer.DataContainer;
import clientApp.graphicalUserInterface.GUI;
import clientApp.modelRefresher.ModelRefresher;
import clientApp.networking.Networking;

public class Main {

	public static void main(String[] args) {
		Client client = new Client("admin", "admin", "admin", "admin", "admin");
		DataContainer dataContainer = new DataContainer();
		dataContainer.registerClient(client);
		GUI gui = new GUI(dataContainer);
		Networking networking = new Networking(dataContainer);
		Thread networkingThread = new Thread(networking);
		ModelRefresher modelRefresher = new ModelRefresher(dataContainer, gui);
		Thread modelRefresherThread = new Thread(modelRefresher);
		modelRefresherThread.start();
		networkingThread.start();
	}
}
