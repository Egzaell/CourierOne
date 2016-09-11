package clientApp.parcel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import clientApp.client.Client;

public class Parcel implements Serializable {

	public static final String READY_TO_DELIVER_STATUS = "GOTOWA DO NADANIA";
	public static final String IN_DELIVERY_STATUS = "W DORECZENIU";
	public static final String DELIVERED_STATUS = "DORECZONO";
	public static final String LOST_STATUS = "ZAGINIONA";
	public static final String DESTROYED_STATUS = "ZNISZCZONA";
	public static final String PAYMENT_MADE_STATUS = "OPLACONA";
	public static final String PAYMENT_PENDING_STATUS = "POBRANIOWA";
	public List<String> previousStatusList = new ArrayList<>();
	private String deliveryAdress;
	private Client sender;
	private String sendDate;
	private String deliveryDate;
	private String status;
	private String paymentStatus;
	private double weight;
	private double price;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public Parcel(String deliveryAdress, Client sender, double weight, boolean isPaymentMade) {
		this.deliveryAdress = deliveryAdress;
		this.sender = sender;
		this.weight = weight;
		sendDate = dateFormat.format(new Date());
		status = READY_TO_DELIVER_STATUS;
		setPrize();
		if (isPaymentMade) {
			paymentStatus = PAYMENT_PENDING_STATUS;
		} else {
			paymentStatus = PAYMENT_MADE_STATUS;
		}
	}
	
	public Parcel(String deliveryAdress, Client sender, String sendDate, String status,
			String paymentStatus, double weight, double price) {
		this.deliveryAdress = deliveryAdress;
		this.sender = sender;
		this.sendDate = sendDate;
		this.status = status;
		this.paymentStatus = paymentStatus;
		this.weight = weight;
		this.price = price;
	}

	public Parcel(String deliveryAdress, Client sender, String sendDate, String deliveryDate, String status,
			String paymentStatus, double weight, double price) {
		this.deliveryAdress = deliveryAdress;
		this.sender = sender;
		this.sendDate = sendDate;
		this.deliveryDate = deliveryDate;
		this.status = status;
		this.paymentStatus = paymentStatus;
		this.weight = weight;
		this.price = price;
	}

	public boolean equals(Object aParcel) {
		Parcel testParcel = (Parcel) aParcel;
		return deliveryAdress.equals(testParcel.getDeliveryAdress()) && sender.equals(testParcel.getSender());
	}

	public String toString() {
		if (status.equals(DELIVERED_STATUS)) {
			return deliveryAdress + ", wyslane przez: " + sender + ", dnia: " + sendDate + status + " dnia : "
					+ deliveryDate;
		} else {
			return deliveryAdress + ", wyslane przez: " + sender + ", dnia: " + sendDate + ", aktualny status: "
					+ status + " : " + paymentStatus;
		}
	}

	public Double getWeight() {
		return weight;
	}

	public Double getPrice() {
		return price;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public String getDeliveryAdress() {
		return deliveryAdress;
	}

	public Client getSender() {
		return sender;
	}

	public String getSendDate() {
		return sendDate;
	}

	public String getDeliveyDate() {
		return deliveryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setDeliveryDate() {
		deliveryDate = dateFormat.format(new Date());
	}

	public void setStatus(String status) {
		previousStatusList.add(this.status);
		this.status = status;
	}

	private void setPrize() {
		if (weight < 5) {
			price = 10;
		} else if (weight >= 5 && weight < 10) {
			price = 20;
		} else if (weight >= 10 && weight < 20) {
			price = 30.75;
		} else if (weight >= 20) {
			price = 45.32;
		}
	}
}
