package clientApp.client;

import java.io.Serializable;

public class Client implements Serializable {

	private String firstName;
	private String lastName;
	private String login;
	private String password;
	private String adress;
	
	public Client(){
		firstName = null;
		lastName = null;
		login = null;
		password = null;
		adress = null;
	}
	
	public Client(String firstName, String lastName, String login, String password, String adress){
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.adress = adress;
	}
	
	public boolean equals(Object anClient){
		Client client = (Client) anClient;
		String testLogin = client.getLogin();
		return login.equals(testLogin);
	}
	
	public String toString() {
		return firstName + " " + lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getAdress() {
		return adress;
	}
	
	public String getLogin(){
		return login;
	}
	
	public String getPassword(){
		return password;
	}
}
