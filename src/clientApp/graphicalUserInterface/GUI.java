package clientApp.graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import clientApp.client.Client;
import clientApp.dataContainer.DataContainer;
import clientApp.parcel.Parcel;
import clientApp.waybill.Waybill;

public class GUI {

	public Client user;
	public DefaultListModel parcelModel = new DefaultListModel();
	private DataContainer dataContainer;
	private JFrame mainFrame;
	private JPanel loginPanel;
	private JLabel loginPanelLoginLabel;
	private JTextField loginPanelLoginTextField;
	private JLabel loginPanelPasswordLabel;
	private JTextField loginPanelPasswordTextField;
	private JButton loginPanelLoginButton;
	private JButton loginPanelExitButton;
	private JButton loginPanelRegisterButton;
	private JPanel registerPanel;
	private JLabel registerPanelFirstNameLabel;
	private JTextField registerPanelFirstNameTextField;
	private JLabel registerPanelLastNameLabel;
	private JTextField registerPanelLastNameTextField;
	private JLabel registerPanelLoginLabel;
	private JTextField registerPanelLoginTextField;
	private JLabel registerPanelPasswordLabel;
	private JTextField registerPanelPasswordTextField;
	private JLabel registerPanelAdressLabel;
	private JTextField registerPanelAdressTextField;
	private JButton registerPanelRegisterButton;
	private JButton registerPanelLoginButton;
	private JPanel mainPanel;
	private JPanel userPanel;
	private JPanel parcelViewPanel;
	private JLabel pacelViewPanelParcelListLabel;
	private JList parcelViewPanelParcelList;
	private JScrollPane parcelViewPanelScrollPane;
	private JPanel parcelPanel;
	private JPanel newParcelPanel;
	private JLabel newParcelPanelLabel;
	private JLabel newParcelPanelPauseLabel;
	private JLabel newParcelPanelDeliveryAdressLabel;
	private JTextField newParcelPanelDeliveryAdressTextField;
	private JCheckBox newParcelPanelCheckBox;
	private JLabel newParcelPanelWeightLabel;
	private JTextField newParcelPanelWeightTextField;
	private JButton newParcelPanelCreateParcelButton;
	private JPanel adminPanel;
	private JPanel parcelManagingPanel;
	private JButton parcelManagingPanelInDeliveryStatusButton;
	private JButton parcelManagingPanelDeliveredStatusButton;
	private JButton parcelManagingPanelLostStatusButton;
	private JButton parcelManagingPanelDestroyedStatusButton;
	private JButton parcelManagingPanelExitButton;
	private JButton newParcelPanelExitButton;

	public GUI(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
		init();
	}
	
	public DefaultListModel getParcelList() {
		return parcelModel;
	}

	private void init() {
		mainFrame = new JFrame("Kurier");
		mainFrame.setSize(700, 350);
		mainFrame.setLayout(new BorderLayout());
		mainPanel = makeLoginPanel();
		mainFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		mainFrame.setVisible(true);
	}

	private JPanel makeLoginPanel() {
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.setSize(300, 300);
		loginPanelLoginLabel = new JLabel("LOGIN");
		loginPanelLoginTextField = new JTextField("");
		loginPanelPasswordLabel = new JLabel("HASLO");
		loginPanelPasswordTextField = new JTextField("");
		loginPanelLoginButton = new JButton("ZALOGUJ");
		loginPanelLoginButton.addActionListener(new LoginListener());
		loginPanelRegisterButton = new JButton("STWORZ KONTO");
		loginPanelRegisterButton.addActionListener(new GoToRegistrationListener());
		loginPanelExitButton = new JButton("ZAKONCZ");
		loginPanelExitButton.addActionListener(new ExitListener());
		loginPanel.add(loginPanelLoginLabel);
		loginPanel.add(loginPanelLoginTextField);
		loginPanel.add(loginPanelPasswordLabel);
		loginPanel.add(loginPanelPasswordTextField);
		loginPanel.add(loginPanelLoginButton);
		loginPanel.add(loginPanelRegisterButton);
		loginPanel.add(loginPanelExitButton);
		return loginPanel;
	}

	private JPanel makeRegisterPanel() {
		registerPanel = new JPanel();
		registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
		registerPanelFirstNameLabel = new JLabel("IMIE");
		registerPanelFirstNameTextField = new JTextField("");
		registerPanelLastNameLabel = new JLabel("NAZWISKO");
		registerPanelLastNameTextField = new JTextField("");
		registerPanelLoginLabel = new JLabel("LOGIN");
		registerPanelLoginTextField = new JTextField("");
		registerPanelPasswordLabel = new JLabel("HASLO");
		registerPanelPasswordTextField = new JTextField("");
		registerPanelAdressLabel = new JLabel("ADRES");
		registerPanelAdressTextField = new JTextField("");
		registerPanelRegisterButton = new JButton("REJESTRACJA");
		registerPanelRegisterButton.addActionListener(new RegisterListener());
		registerPanelLoginButton = new JButton("LOGOWANIE");
		registerPanelLoginButton.addActionListener(new GoToLoginListener());
		registerPanel.add(registerPanelFirstNameLabel);
		registerPanel.add(registerPanelFirstNameTextField);
		registerPanel.add(registerPanelLastNameLabel);
		registerPanel.add(registerPanelLastNameTextField);
		registerPanel.add(registerPanelLoginLabel);
		registerPanel.add(registerPanelLoginTextField);
		registerPanel.add(registerPanelPasswordLabel);
		registerPanel.add(registerPanelPasswordTextField);
		registerPanel.add(registerPanelAdressLabel);
		registerPanel.add(registerPanelAdressTextField);
		registerPanel.add(registerPanelRegisterButton);
		registerPanel.add(registerPanelLoginButton);
		return registerPanel;
	}

	private JPanel makeUserPanel() {
		userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(1, 2));
		parcelPanel = makeParcelViewPanel();
		newParcelPanel = makeNewParcelPanel();
		userPanel.add(parcelPanel);
		userPanel.add(newParcelPanel);
		return userPanel;
	}

	private JPanel makeParcelViewPanel() {
		parcelViewPanel = new JPanel();
		parcelViewPanel.setLayout(new BorderLayout());
		pacelViewPanelParcelListLabel = new JLabel("NADANE PACZKI");
		parcelViewPanelParcelList = new JList(parcelModel);
		parcelViewPanelParcelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		parcelViewPanelScrollPane = new JScrollPane(parcelViewPanelParcelList);
		parcelViewPanel.add(BorderLayout.NORTH, pacelViewPanelParcelListLabel);
		parcelViewPanel.add(BorderLayout.CENTER, parcelViewPanelScrollPane);
		return parcelViewPanel;
	}

	private JPanel makeNewParcelPanel() {
		newParcelPanel = new JPanel();
		newParcelPanel.setLayout(new BoxLayout(newParcelPanel, BoxLayout.Y_AXIS));
		newParcelPanelLabel = new JLabel("NOWA PACZKA");
		newParcelPanelPauseLabel = new JLabel("----------------------");
		newParcelPanelDeliveryAdressLabel = new JLabel("ADRES DOSTAWY");
		newParcelPanelDeliveryAdressTextField = new JTextField("");
		newParcelPanelCheckBox = new JCheckBox("ZA POBRANIEM");
		newParcelPanelWeightLabel = new JLabel("WAGA");
		newParcelPanelWeightTextField = new JTextField("");
		newParcelPanelCreateParcelButton = new JButton("NADAJ");
		newParcelPanelCreateParcelButton.addActionListener(new CreateParcelListener());
		newParcelPanelExitButton = new JButton("ZAKONCZ PROGRAM");
		newParcelPanelExitButton.addActionListener(new ExitListener());
		newParcelPanel.add(newParcelPanelLabel);
		newParcelPanel.add(newParcelPanelPauseLabel);
		newParcelPanel.add(newParcelPanelDeliveryAdressLabel);
		newParcelPanel.add(newParcelPanelDeliveryAdressTextField);
		newParcelPanel.add(newParcelPanelCheckBox);
		newParcelPanel.add(newParcelPanelWeightLabel);
		newParcelPanel.add(newParcelPanelWeightTextField);
		newParcelPanel.add(newParcelPanelCreateParcelButton);
		newParcelPanel.add(newParcelPanelExitButton);
		return newParcelPanel;
	}

	private JPanel makeAdminPanel() {
		adminPanel = new JPanel();
		adminPanel.setLayout(new GridLayout(1, 2));
		parcelPanel = makeParcelViewPanel();
		parcelManagingPanel = makeParcelManagingPanel();
		adminPanel.add(parcelPanel);
		adminPanel.add(parcelManagingPanel);
		return adminPanel;
	}

	private JPanel makeParcelManagingPanel() {
		parcelManagingPanel = new JPanel();
		ParcelStatusListener parcelStatusListener = new ParcelStatusListener();
		parcelManagingPanel.setLayout(new BoxLayout(parcelManagingPanel, BoxLayout.Y_AXIS));
		parcelManagingPanelInDeliveryStatusButton = new JButton("W DORECZENIU");
		parcelManagingPanelInDeliveryStatusButton.addActionListener(parcelStatusListener);
		parcelManagingPanelDeliveredStatusButton = new JButton("DORECZONO");
		parcelManagingPanelDeliveredStatusButton.addActionListener(parcelStatusListener);
		parcelManagingPanelLostStatusButton = new JButton("ZAGINIONA");
		parcelManagingPanelLostStatusButton.addActionListener(parcelStatusListener);
		parcelManagingPanelDestroyedStatusButton = new JButton("ZNISZCZONA");
		parcelManagingPanelDestroyedStatusButton.addActionListener(parcelStatusListener);
		parcelManagingPanelExitButton = new JButton("ZAKONCZ PROGRAM");
		parcelManagingPanelExitButton.addActionListener(new ExitListener());
		parcelManagingPanel.add(parcelManagingPanelInDeliveryStatusButton);
		parcelManagingPanel.add(parcelManagingPanelDeliveredStatusButton);
		parcelManagingPanel.add(parcelManagingPanelLostStatusButton);
		parcelManagingPanel.add(parcelManagingPanelDestroyedStatusButton);
		parcelManagingPanel.add(parcelManagingPanelExitButton);
		return parcelManagingPanel;
	}

	class LoginListener implements ActionListener {
		boolean isAdmin;

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			String login = loginPanelLoginTextField.getText();
			String password = loginPanelPasswordTextField.getText();
			boolean isLogged = dataContainer.loginClient(login, password);
			if (isLogged) {
				dataContainer.isUserLogged = true;
				user = dataContainer.getClientByLogin(login);
				if (login.equals("admin") && password.equals("admin")) {
					isAdmin = true;
				}
				newView();
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "ZLY LOGIN LUB HASLO");
			}
			dataContainer.unlock();
		}

		private void newView() {
			mainFrame.remove(mainPanel);
			if (isAdmin) {
				mainPanel = makeAdminPanel();
			} else {
				mainPanel = makeUserPanel();
			}
			mainFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
			mainFrame.setVisible(true);
		}
	}

	class GoToRegistrationListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			newView();
		}

		private void newView() {
			mainFrame.remove(mainPanel);
			mainPanel = makeRegisterPanel();
			mainFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
			mainFrame.setVisible(true);
		}
	}

	class RegisterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			boolean isOperationSuccessful = false;
			String firstName = registerPanelFirstNameTextField.getText();
			String lastName = registerPanelLastNameTextField.getText();
			String login = registerPanelLoginTextField.getText();
			String password = registerPanelPasswordTextField.getText();
			String adress = registerPanelPasswordTextField.getText();
			Client client = new Client(firstName, lastName, login, password, adress);
			isOperationSuccessful = dataContainer.registerClient(client);
			if (isOperationSuccessful) {
				JOptionPane.showMessageDialog(new JFrame(), "ZAREJESTROWANO NOWEGO UZYTKOWNIKA");
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "COS POSZLO NIE TAK");
			}
			dataContainer.unlock();
		}
	}

	class GoToLoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			newView();
		}

		private void newView() {
			mainFrame.remove(mainPanel);
			mainPanel = makeLoginPanel();
			mainFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
			mainFrame.setVisible(true);
		}
	}

	class CreateParcelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			String deliveryAdress = newParcelPanelDeliveryAdressTextField.getText();
			double weight = Double.parseDouble(newParcelPanelWeightTextField.getText());
			boolean isPaymentMade = newParcelPanelCheckBox.isSelected();
			Parcel parcel = new Parcel(deliveryAdress, user, weight, isPaymentMade);
			boolean isOperationSucceded = dataContainer.registerParcel(parcel);
			if (!isOperationSucceded) {
				JOptionPane.showMessageDialog(new JFrame(), "COS POSZLO NIE TAK");
			}
			new Waybill(parcel);
			dataContainer.unlock();
		}
	}

	class ParcelStatusListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			Object source = e.getSource();
			int index = parcelViewPanelParcelList.getSelectedIndex();
			Parcel parcel = dataContainer.getSelectedParcel(index);
			String status;
			if (source.equals(parcelManagingPanelDeliveredStatusButton)) {
				status = parcel.DELIVERED_STATUS;
				parcel.setStatus(status);
				parcel.setDeliveryDate();
			} else if (source.equals(parcelManagingPanelInDeliveryStatusButton)) {
				status = parcel.IN_DELIVERY_STATUS;
				parcel.setStatus(status);
			} else if (source.equals(parcelManagingPanelLostStatusButton)) {
				status = parcel.LOST_STATUS;
				parcel.setStatus(status);
			} else if (source.equals(parcelManagingPanelDestroyedStatusButton)) {
				status = parcel.DESTROYED_STATUS;
				parcel.setStatus(status);
			}
			dataContainer.unlock();
		}
	}

	class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			dataContainer.isProgramRunning = false;
			dataContainer.unlock();
			System.exit(1);
		}
	}
}
