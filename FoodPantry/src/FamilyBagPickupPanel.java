import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class FamilyBagPickupPanel extends JPanel {
	private JButton completePickupButton;
	private JButton returnButton;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel nameLabel, dateLabel;
	private String placeholderName = "Mary Smith";
	private String placeholderDate = "September 5, 2013";

	private int cid;

	public FamilyBagPickupPanel(ActionListener completePickupListener, ActionListener returnListener, int clientID) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridLayout(1, 2));
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/Home_Pickups.png"));
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

		JPanel headerMetaPanel = new JPanel(new GridLayout(2, 1));
		nameLabel = new JLabel(placeholderName);
		dateLabel = new JLabel(placeholderDate);
		headerMetaPanel.add(nameLabel);
		headerMetaPanel.add(dateLabel);
		headerPanel.add(headerMetaPanel);
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		Object[][] data = {};
		String[] columnNames = { "Product", "Quantity" };
		table = new JTable(data, columnNames);

		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		completePickupButton = new JButton("Complete Pickup");
		completePickupButton.addActionListener(completePickupListener);
		buttonPanel.add(completePickupButton);
		returnButton = new JButton("Return to Pickups Page");
		returnButton.addActionListener(returnListener);
		buttonPanel.add(returnButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void setData(ResultSet rs, String name, int cid) {
		this.cid = cid;
		nameLabel.setText(name);
		dateLabel.setText(new SimpleDateFormat("MMMM dd, yyyy").format(Calendar.getInstance().getTime()));
		System.out.println("Updating family bag panel with search results...");
		remove(scrollPane);

		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		try {
			while (rs.next()) {
				// Retrieve by column name
				String product = rs.getString("Name");
				String quantity = rs.getString("CurrentMonthQty");
				// Display values
				rows.add(new Object[] { product, quantity });
				System.out.println(Arrays.toString(rows.get(rows.size() - 1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] columnNames = { "Product", "Quantity" };
		Object[][] data = rows.toArray(new Object[0][2]);
		table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		invalidate();
		validate();
		repaint();
	}

	public int getCID() {
		return cid;
	}
}
