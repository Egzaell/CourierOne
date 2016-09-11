package serverApp.dataBaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import clientApp.client.Client;
import clientApp.dataContainer.DataContainer;
import clientApp.parcel.Parcel;

public class DataBaseConnector implements Runnable {

	private List<Client> clientList = new ArrayList<>();
	private List<Parcel> parcelList = new ArrayList<>();
	private DataContainer dataContainer;
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:db.db";
	private Connection connection;
	private Statement statement;

	public DataBaseConnector(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
		getJDBCDriverClass();
		connection = connectToDataBase();
		statement = createStatement();
		createTables();
		selectDataFromDataBase();
		updateDataContainer();
		clearLists();
	}

	@Override
	public void run() {
		while(dataContainer.isProgramRunning){
			dataContainer.lock();
			if (dataContainer.checkPoint){
				dropTables();
				createTables();
				setClientList();
				setParcelList();
				insertDataIntoDataBase();
				dataContainer.checkPoint = false;
			}
			dataContainer.unlock();
		}
	}

	private void insertDataIntoDataBase() {
		for (Client client : clientList) {
			insertClient(client);
		}
		for (Parcel parcel : parcelList) {
			insertParcel(parcel);
		}
		System.out.println("Dane umieszczono w bazie");
	}

	private void updateDataContainer() {
		for (Client client : clientList) {
			dataContainer.registerClient(client);
		}
		for (Parcel parcel : parcelList) {
			dataContainer.registerParcel(parcel);
		}
	}

	private void selectDataFromDataBase() {
		selectClients();
		selectParcels();
	}

	private void clearLists() {
		clientList.clear();
		parcelList.clear();
	}

	private void setParcelList() {
		Object[] parcelArray = dataContainer.getParcelArray();
		for (Object object : parcelArray) {
			Parcel parcel = (Parcel) object;
			parcelList.add(parcel);
		}
	}

	private void setClientList() {
		Object[] clientArray = dataContainer.getClientArray();
		for (Object object : clientArray) {
			Client client = (Client) object;
			clientList.add(client);
		}
	}

	private void createTables() {
		String createClients = "CREATE TABLE IF NOT EXISTS CLIENTS (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName VARCHAR(255), lastName VARCHAR(255), login VARCHAR(255), password VARCHAR(255), adress VARCHAR(255))";
		String createParcels = "CREATE TABLE IF NOT EXISTS PARCELS (id INTEGER PRIMARY KEY AUTOINCREMENT, deliveryAdress VARCHAR(255), sender_id INTEGER references CLIENTS(id), sendDate VARCHAR(255), deliveryDate VARCHAR(255), status VARCHAR(255), paymentStatus VARCHAR(255), weight DECIMAL(8.2), price DECIMAL(8.2))";
		try {
			statement.execute(createClients);
			statement.execute(createParcels);
		} catch (SQLException e) {
			System.err.println("blad podczas tworzenia tabel");
			e.printStackTrace();
		}
	}

	private void dropTables() {
		String dropClients = "DROP TABLE CLIENTS";
		String dropParcels = "DROP TABLE PARCELS";
		try {
			statement.execute(dropClients);
			statement.execute(dropParcels);
		} catch (SQLException e) {
			System.err.println("blad przy usowaniu tabel");
			e.printStackTrace();
		}
	}

	private void insertClient(Client client) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO CLIENTS VALUES(NULL, ?, ?, ?, ?, ?);");
			preparedStatement.setString(1, client.getFirstName());
			preparedStatement.setString(2, client.getLastName());
			preparedStatement.setString(3, client.getLogin());
			preparedStatement.setString(4, client.getPassword());
			preparedStatement.setString(5, client.getAdress());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("blad przy wstawianiu klienta");
			e.printStackTrace();
		}
	}

	private void insertParcel(Parcel parcel) {
		Client sender = parcel.getSender();
		int senderIndex = clientList.indexOf(sender) + 1;
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO PARCELS VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?);");
			preparedStatement.setString(1, parcel.getDeliveryAdress());
			preparedStatement.setInt(2, senderIndex);
			preparedStatement.setString(3, parcel.getSendDate());
			preparedStatement.setString(4, parcel.getDeliveyDate());
			preparedStatement.setString(5, parcel.getStatus());
			preparedStatement.setString(6, parcel.getPaymentStatus());
			preparedStatement.setDouble(7, parcel.getWeight());
			preparedStatement.setDouble(8, parcel.getPrice());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("blad przy wstawiaiu paczki");
			e.printStackTrace();
		}
	}

	private void selectClients() {
		int id;
		String firstName;
		String lastName;
		String login;
		String password;
		String adress;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM CLIENTS");
			while (result.next()) {
				id = result.getInt("id");
				firstName = result.getString("firstName");
				lastName = result.getString("lastName");
				login = result.getString("login");
				password = result.getString("password");
				adress = result.getString("adress");
				Client client = new Client(firstName, lastName, login, password, adress);
				clientList.add(client);
			}
		} catch (SQLException e) {
			System.err.println("blad przy pobieraniu klienta");
			e.printStackTrace();
		}
	}

	private void selectParcels() {
		int id;
		int idSender;
		String deliveryAdress;
		Client sender;
		String sendDate;
		String deliveryDate;
		String status;
		String paymentStatus;
		double weight;
		double price;
		Parcel parcel;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM PARCELS");
			while (result.next()) {
				id = result.getInt("id") - 1;
				deliveryAdress = result.getString("deliveryAdress");
				idSender = result.getInt("sender_id") - 1;
				sender = clientList.get(idSender);
				sendDate = result.getString("sendDate");
				deliveryDate = result.getString("deliveryDate");
				status = result.getString("status");
				paymentStatus = result.getString("paymentStatus");
				weight = result.getDouble("weight");
				price = result.getDouble("price");
				if (status.equals(Parcel.DELIVERED_STATUS)) {
					parcel = new Parcel(deliveryAdress, sender, sendDate, deliveryDate, status, paymentStatus,
							weight, price);
				} else {
					parcel = new Parcel(deliveryAdress, sender, sendDate, status, paymentStatus, weight, price);
				}
				parcelList.add(id, parcel);
			}
		} catch (SQLException e) {
			System.err.println("blad przy pobieraniu paczki");
			e.printStackTrace();
		}
	}
	
	private Class getJDBCDriverClass() {
		Class jdbcClass = null;
		try {
			jdbcClass = Class.forName(DataBaseConnector.DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("brak sterownika");
			e.printStackTrace();
		}
		return jdbcClass;
	}
	
	private Connection connectToDataBase() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DB_URL);
		} catch (SQLException e) {
			System.err.println("problem z polaczeniem");
			e.printStackTrace();
		}
		return connection;
	}
	
	private Statement createStatement() {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.err.println("blad statement");
			e.printStackTrace();
		}
		return statement;
	}
}
