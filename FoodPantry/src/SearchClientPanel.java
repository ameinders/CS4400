import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class SearchClientPanel extends JPanel {

	private JButton newClientButton, addFamilyButton, returnButton, searchButton;
	private JTextField lastNameField, telephoneField;
	private JScrollPane scrollPane;
	private JTable table;
	ArrayList<Integer> cids;

	public SearchClientPanel(ActionListener newClientListener, ActionListener addFamilyListener, ActionListener returnListener, final ActionListener searchListener) {
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
		final JPanel searchPanel = new JPanel(new GridLayout(3, 2));
		searchPanel.add(new JLabel("Last Name: "));
		lastNameField = new JTextField();
		searchPanel.add(lastNameField);
		searchPanel.add(new JLabel("Telephone: "));
		telephoneField = new JTextField();
		searchPanel.add(telephoneField);
		searchPanel.add(Box.createGlue());
		searchPanel.add(new JButton("Search") {
			{
				addActionListener(searchListener);
			}
		});
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				add(searchPanel);
			}
		});
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		String[] columnNames = { "Last Name", "First Name", "Size", "Address", "Telephone", "Start Date" };
		Object[][] data = { { "Smith", "Mary", new Integer(4), "300 North Side Dr, Atlanta, GA 30332", "404-335-1122", "May-30-2013" } };

		table = new JTable(data, columnNames);

		scrollPane = new JScrollPane(table);
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

	public void setData(ResultSet rs) {
		System.out.println("Updating client search panel with search results...");
		remove(scrollPane);

		String[] columnNames = { "Last Name", "First Name", "Address", "Telephone", "Start Date" };
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		cids = new ArrayList<Integer>();
		try {
			while (rs.next()) {
				// Retrieve by column name
				String last = rs.getString("Last");
				String first = rs.getString("First");
				String address = rs.getString("apartmentNum") + " " + rs.getString("Street") + " " + rs.getString("City") + " " + rs.getString("State") + " " + rs.getString("zip");
				String phone = rs.getString("Phone");
				int pday = rs.getInt("Start");

				// Display values
				rows.add(new Object[] { last, first, address, phone, pday });
				cids.add(rs.getInt("CID"));
				System.out.print("\tLast: " + last);
				System.out.print(", First: " + first);
				System.out.print(", Address: " + address);
				System.out.print(", Phone: " + phone);
				System.out.print(", Start: " + pday);
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Object[][] data = rows.toArray(new Object[0][6]);
		table = new JTable(data, columnNames);

		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		invalidate();
		validate();
		repaint();
	}

	public String getSearchName() {
		return lastNameField.getText();
	}

	public String getSearchTelephone() {
		return telephoneField.getText();
	}

	public int getSelectedCID() {
		if (table.getSelectedRow() == -1)
			return -1;
		return cids.get(table.getSelectedRow());
	}
}
