package clientApp.modelRefresher;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import clientApp.client.Client;
import clientApp.dataContainer.DataContainer;
import clientApp.graphicalUserInterface.GUI;
import clientApp.parcel.Parcel;

public class ModelRefresher implements Runnable {

	DataContainer dataContainer;
	GUI gui;
	Client user;
	private DefaultListModel parcelModel;
	private List<Parcel> parcelList = new ArrayList<>();
	private List<String> parcelStatusList = new ArrayList<>();
	private int parcelQuantity = 0;
	private boolean isParcelQuantityChanged;
	private boolean isAnyStatusChanged;

	public ModelRefresher(DataContainer dataContainer, GUI gui) {
		this.dataContainer = dataContainer;
		this.gui = gui;
		this.parcelModel = gui.getParcelList();
	}

	public void run() {
		while (dataContainer.isProgramRunning) {
			isParcelQuantityChanged = false;
			isAnyStatusChanged = false;
			dataContainer.lock();
			if (dataContainer.isUserLogged) {
				updateParcelList();
				isParcelQuantityChanged = isParcelQuantityChanged();
				isAnyStatusChanged = isParcelStatusChanged();
				if (isParcelQuantityChanged || isAnyStatusChanged) {
					updateProperModel();
					updateParcelStatusList();
				}
			}
			dataContainer.unlock();
		}
	}

	private void updateProperModel() {
		user = gui.user;
		String login = user.getLogin();
		if (login.equals("admin")) {
			updateAdminParcelList();
		} else {
			updateCientParcelList();
		}
	}

	private void updateCientParcelList() {
		parcelModel.clear();
		for (Parcel parcel : parcelList) {
			Client sender = parcel.getSender();
			if (sender.equals(gui.user)) {
				parcelModel.addElement(parcel);
			}
		}
	}

	private void updateAdminParcelList() {
		parcelModel.clear();
		for (Parcel parcel : parcelList) {
			parcelModel.addElement(parcel);
		}
	}

	private void updateParcelList() {
		parcelList.clear();
		Object[] objectArray = dataContainer.getParcelArray();
		for (Object anParcel : objectArray) {
			Parcel parcel = (Parcel) anParcel;
			parcelList.add(parcel);
		}
	}

	private void updateParcelStatusList() {
		parcelStatusList.clear();
		for (Parcel parcel : parcelList) {
			String status = parcel.getStatus();
			parcelStatusList.add(status);
		}
	}

	private boolean isParcelQuantityChanged() {
		boolean isQuantityChanged = false;
		int parcelListSize = parcelList.size();
		if (parcelQuantity != parcelListSize) {
			isQuantityChanged = true;
			parcelQuantity = parcelListSize;
		}
		return isQuantityChanged;
	}

	private boolean isParcelStatusChanged() {
		boolean isAnyStatusChanged = false;
		if (parcelStatusList.size() != parcelList.size()) {
			isAnyStatusChanged = true;
		} else {
			for (Parcel parcel : parcelList) {
				int index = parcelList.indexOf(parcel);
				String actualStatus = parcel.getStatus();
				String previousStatus = parcelStatusList.get(index);
				if (!actualStatus.equals(previousStatus)) {
					isAnyStatusChanged = true;
				}
			}
		}
		return isAnyStatusChanged;
	}
}
