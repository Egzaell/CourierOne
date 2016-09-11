package serverApp.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import clientApp.dataContainer.DataContainer;
import clientApp.networking.Networking;

public class ServerNetworking extends Networking {

	private boolean isConnected = true;
	
	public ServerNetworking(DataContainer dataContainer, Socket socket) {
		super(dataContainer);
		this.socket = socket;
	}

	protected void connect() {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			synchronizeData();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		
	}
	
	protected void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void synchronizeData() throws ClassNotFoundException, IOException {
		while (dataContainer.isProgramRunning) {
			dataContainer.lock();
			getLocalData();
			getServerData();
			dataContainer.unlock();
			if(!isConnected) {
				break;
			}
			sendData();
		}
	}
	
	protected void getServerData() throws ClassNotFoundException, IOException {
		String label = "label";
		while (!label.equals("END")) {
			label = (String) in.readObject();
			if (label.equals("CLIENT")) {
				receiveClient();
			} else if (label.equals("PARCEL")) {
				receiveParcel();
			} else if (label.equals("END_OF_CONNECTION")) {
				isConnected = false;
			}
		}
	}

}