package clientApp.dataContainer;

import java.util.ArrayList;
import java.util.List;

import clientApp.client.Client;
import clientApp.lock.Lock;
import clientApp.parcel.Parcel;

public class DataContainer {

	public boolean isProgramRunning = true;
	public boolean isUserLogged = false;
	public boolean checkPoint = false;
	private Lock lock = new Lock();
	private List<Client> clientList = new ArrayList<>();
	private List<Parcel> parcelList = new ArrayList<>();

	public boolean loginClient(String login, String password) {
		boolean isLoginSuccessfuly = false;
		boolean isClientOnList = isClientOnTheList(login);
		if (isClientOnList) {
			boolean isLoginCorrect = isLoginDataCorrect(login, password);
			if (isLoginCorrect) {
				isLoginSuccessfuly = true;
			}
		}
		return isLoginSuccessfuly;
	}

	public boolean registerClient(Client client) {
		boolean registrationComplete = false;
		String clientLogin = client.getLogin();
		boolean isClientOnList = isClientOnTheList(clientLogin);
		if (isClientOnList) {
			registrationComplete = false;
		} else {
			clientList.add(client);
			registrationComplete = true;
		}
		return registrationComplete;
	}

	public boolean registerParcel(Parcel parcel) {
		boolean registrationComplete = false;
		boolean isParcelOnList = isParcelOnTheList(parcel);
		if (isParcelOnList) {
			registrationComplete = false;
		} else {
			parcelList.add(parcel);
			registrationComplete = true;
		}
		return registrationComplete;
	}

	public void registerStatusChange(Parcel parcel) {
		int index = parcelList.indexOf(parcel);
		Parcel previousParcel = parcelList.get(index);
		Parcel actualParcel = parcel;
		updateParcel(previousParcel, actualParcel);
	}

	public Object[] getClientArray() {
		Object[] clientArray = clientList.toArray();
		return clientArray;
	}

	public Object[] getParcelArray() {
		Object[] parcelArray = parcelList.toArray();
		return parcelArray;
	}

	public Parcel getSelectedParcel(int index) {
		Parcel parcel = parcelList.get(index);
		return parcel;
	}

	public Parcel getSpecificParcel(Parcel parcel) {
		int index = parcelList.indexOf(parcel);
		Parcel specificParcel = parcelList.get(index);
		return specificParcel;
	}

	public Client getClientByLogin(String login) {
		Client client = new Client();
		Object[] clientArray = getClientArray();
		for (Object existingClient : clientArray) {
			Client testClient = (Client) existingClient;
			String existingLogin = testClient.getLogin();
			if (login.equals(existingLogin)) {
				client = testClient;
			}
		}
		return client;
	}

	public void lock() {
		try {
			lock.lock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void unlock() {
		lock.unlock();
	}

	private void updateParcel(Parcel previousParcel, Parcel actualParcel) {
		String previousStatus = previousParcel.getStatus();
		String actualStatus = actualParcel.getStatus();
		if (!previousStatus.equals(actualStatus)) {
			if (!previousParcel.previousStatusList.contains(actualStatus)) {
				int index = parcelList.indexOf(previousParcel);
				parcelList.remove(previousParcel);
				parcelList.add(index, actualParcel);
			}
		}
	}

	private boolean isClientOnTheList(String login) {
		boolean isClientOnList = false;
		Object[] clientArray = getClientArray();
		for (Object client : clientArray) {
			Client testClient = (Client) client;
			String clientLogin = testClient.getLogin();
			if (login.equals(clientLogin)) {
				isClientOnList = true;
			}
		}
		return isClientOnList;
	}

	private boolean isLoginDataCorrect(String login, String password) {
		boolean isLoginDataCorrect = false;
		Object[] clientArray = getClientArray();
		for (Object client : clientArray) {
			Client testClient = (Client) client;
			String clientLogin = testClient.getLogin();
			String clientPassword = testClient.getPassword();
			if (login.equals(clientLogin) && password.equals(clientPassword)) {
				isLoginDataCorrect = true;
			}
		}
		return isLoginDataCorrect;
	}

	private boolean isParcelOnTheList(Parcel parcel) {
		boolean isParcelOnList = false;
		if (parcelList.contains(parcel)) {
			isParcelOnList = true;
		}
		return isParcelOnList;
	}
}
