package clientApp.waybill;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import clientApp.client.Client;
import clientApp.parcel.Parcel;

public class Waybill {

	private final String NEW_LINE = System.getProperty("line.separator");
	private Parcel parcel;
	private Client client;
	private String fileName;
	private File file;
	private FileWriter fileWriter;
	
	public Waybill(Parcel parcel) {
		this.parcel = parcel;
		this.client = parcel.getSender();
		createWaybill();
	}
	
	private void createWaybill() {
		createFile();
		writeToFile();
	}
	
	public void createFile() {
		setFileName();
		file = new File(fileName);
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setFileName() {
		this.fileName = parcel.getDeliveryAdress() + ".txt";
	}
	
	private void writeToFile() {
		try {
			fileWriter.append("List przewozowy");
			fileWriter.append(NEW_LINE);
			fileWriter.append("--------");
			fileWriter.append(NEW_LINE);
			fileWriter.append("Nadawca:");
			fileWriter.append(NEW_LINE);
			fileWriter.append(client.getFirstName() + " " + client.getLastName());
			fileWriter.append(NEW_LINE);
			fileWriter.append(client.getAdress());
			fileWriter.append(NEW_LINE);
			fileWriter.append("--------");
			fileWriter.append(NEW_LINE);
			fileWriter.append("Odbiorca");
			fileWriter.append(NEW_LINE);
			fileWriter.append(parcel.getDeliveryAdress());
			fileWriter.append(NEW_LINE);
			fileWriter.append("--------");
			fileWriter.append(NEW_LINE);
			fileWriter.append("Nadano: " + parcel.getSendDate());
			fileWriter.append(NEW_LINE);
			if (parcel.getPaymentStatus().equals(Parcel.PAYMENT_PENDING_STATUS)){
				fileWriter.append(parcel.getPaymentStatus() + " kwota: " + parcel.getPrice());
			}
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
