package serverApp.serverManagement;

import java.util.Scanner;

import clientApp.dataContainer.DataContainer;

public class ServerManagement extends Thread {

	DataContainer dataContainer;
	Scanner scanner = new Scanner(System.in);
	int choice;

	public ServerManagement(DataContainer dataContainer){
		this.dataContainer = dataContainer;
	}
	 
	public void run() {
		while (dataContainer.isProgramRunning) {
			System.out.println("1. Zapisz dane do bazy, 2. Zakoncz dzialanie serwera");
			choice = scanner.nextInt();
			if (choice == 1) {
				dataContainer.lock();
				dataContainer.checkPoint = true;
				dataContainer.unlock();
			} else if (choice == 2) {
				dataContainer.lock();
				dataContainer.isProgramRunning = false;
				dataContainer.unlock();
			}
		}
	}
}
