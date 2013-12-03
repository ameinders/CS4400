import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class SearchClientPanel extends JPanel {

	private JButton newClientButton, addFamilyButton, returnButton;
	private JTextField lastNameField, telephoneField;

	public SearchClientPanel(ActionListener newClientListener, ActionListener addFamilyListener, ActionListener returnListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridLayout(1, 2));
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/Home_Clients.png"));
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
				add(new JLabel(new ImageIcon(pickupImage)) {
					{
						setBorder(BorderFactory.createRaisedBevelBorder());
					}
				});
			}
		});
		final JPanel searchPanel = new JPanel(new GridLayout(1, 4));
		searchPanel.add(new JLabel("Last Name: "));
		lastNameField = new JTextField();
		searchPanel.add(lastNameField);
		searchPanel.add(new JLabel("Telephone: "));
		telephoneField = new JTextField();
		searchPanel.add(telephoneField);
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				add(searchPanel);
			}
		});
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		String[] columnNames = { "Last Name", "First Name", "Size", "Address", "Telephone", "Start Date" };
		Object[][] data = { { "Smith", "Mary", new Integer(4), "300 North Side Dr, Atlanta, GA 30332", "404-335-1122", "May-30-2013" } };

		JTable table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		returnButton = new JButton("Return");
		returnButton.addActionListener(returnListener);
		newClientButton = new JButton("New Client");
		newClientButton.addActionListener(newClientListener);
		addFamilyButton = new JButton("Add Family Members");
		addFamilyButton.addActionListener(addFamilyListener);
		// SHOULDN"T WE REMOVE TOO
		buttonPanel.add(returnButton);
		buttonPanel.add(newClientButton);
		buttonPanel.add(addFamilyButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
}
