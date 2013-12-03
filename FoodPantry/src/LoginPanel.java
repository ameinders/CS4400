import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {
	private JTextField username;
	private JPasswordField password;
	private JButton loginButton;

	public LoginPanel(ActionListener button_listener) {
		this();
		addLoginListener(button_listener);
	}

	public LoginPanel() {
		super(new GridBagLayout());
		username = new JTextField(10);
		password = new JPasswordField();
		loginButton = new JButton("Login");

		JPanel contentPanel = new JPanel(new BorderLayout());
		JPanel loginPanel = new JPanel(new GridLayout(3, 2) {
			{
				setVgap(5);
			}
		});
		loginPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		loginPanel.add(new JLabel("Username:", SwingConstants.CENTER));
		loginPanel.add(username);
		loginPanel.add(new JLabel("Password:", SwingConstants.CENTER));
		loginPanel.add(password);
		loginPanel.add(Box.createGlue());
		loginPanel.add(loginButton);
		Image loginSplash = null;
		try {
			loginSplash = ImageIO.read(new File("src/LoginSplash.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		contentPanel.add(new JLabel(new ImageIcon(loginSplash)),
				BorderLayout.NORTH);
		contentPanel.add(loginPanel);
		add(contentPanel);
	}

	public void addLoginListener(ActionListener button_listener) {
		password.addActionListener(button_listener);
		loginButton.addActionListener(button_listener);
	}

	public Login getLoginInfo() {
		return new Login(username.getText(), new String(password.getPassword()));
	}
}
