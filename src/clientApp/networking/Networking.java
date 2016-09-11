package clientApp.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import clientApp.client.Client;
import clientApp.dataContainer.DataContainer;
import clientApp.parcel.Parcel;

public class Networking implements Runnable {

	protected DataContainer dataContainer;
	protected Socket socket;
	protected List<Client> clientList = new ArrayList<>();
	protected List<Parcel> parcelList = new ArrayList<>();
	protected ObjectOutputStream out;
	protected ObjectInputStream in;

	public Networking(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
	}

	@Override
	public void run() {
		connect();
	}

	protected void connect() {
		try {
			socket = new Socket("127.0.0.1", 9000);
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
			sendConnectionEndLabel();
			sendEndLabel();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void synchronizeData() throws IOException, ClassNotFoundException {
		while (dataContainer.isProgramRunning) {
			dataContainer.lock();
			getLocalData();
			sendData();
			getServerData();
			dataContainer.unlock();
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
				dataContainer.isProgramRunning = false;
			}
		}
	}

	protected void receiveClient() {
		try {
			Client client = (Client) in.readObject();
			dataContainer.registerClient(client);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	protected void receiveParcel() {
		try {
			Parcel parcel = (Parcel) in.readObject();
			if (parcelList.contains(parcel)) {
				dataContainer.registerStatusChange(parcel);
			} else {
				dataContainer.registerParcel(parcel);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	protected void getLocalData() {
		clearLists();
		getClients();
		getParcels();
	}

	protected void sendData() throws IOException {
		sendClients();
		sendParcels();
		sendEndLabel();
	}

	protected void sendClients() throws IOException {
		Object[] clientArray = clientList.toArray();
		for (Object client : clientArray) {
			out.writeObject("CLIENT");
			out.flush();
			out.writeObject(client);
			out.flush();
		}
	}

	protected void sendParcels() throws IOException {
		Object[] parcelArray = parcelList.toArray();
		for (Object parcel : parcelArray) {
			out.writeObject("PARCEL");
			out.flush();
			out.writeUnshared(parcel);
			out.flush();
		}
	}

	protected void sendEndLabel() throws IOException {
		String endLabel = "END";
		out.writeObject(endLabel);
		out.flush();
	}

	protected void sendConnectionEndLabel() throws IOException {
		String connectionEndLabel = "END_OF_CONNECTION";
		out.writeObject(connectionEndLabel);
		out.flush();
	}

	protected void clearLists() {
		clientList.clear();
		parcelList.clear();
	}

	protected void getClients() {
		Object[] clientArray = dataContainer.getClientArray();
		for (Object anClient : clientArray) {
			Client client = (Client) anClient;
			clientList.add(client);
		}
	}

	protected void getParcels() {
		Object[] parcelArray = dataContainer.getParcelArray();
		for (Object anParcel : parcelArray) {
			Parcel parcel = (Parcel) anParcel;
			parcelList.add(parcel);
		}
	}
}
