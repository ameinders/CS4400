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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GroceryPanel extends JPanel {
	private JTable table;
	private JScrollPane scrollPane;

	public GroceryPanel(final ActionListener returnListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridBagLayout());
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/GroceryBanner.png"));
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
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		String[] columnNames = { "Product", "Quantity", "Last Month Quantity" };
		Object[][] data = { { "Bread Mix", new Integer(75), new Integer(25) }, { "Can Fruit Pears", new Integer(55), new Integer(65) } };
		table = new JTable(data, columnNames);

		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel southPanel = new JPanel(new GridBagLayout());
		southPanel.add(new JButton("Return") {
			{
				addActionListener(returnListener);
			}
		});
		add(southPanel, BorderLayout.SOUTH);
	}

	public void setData(ResultSet rs) {
		System.out.println("Updating Grocery panel with search results...");
		remove(scrollPane);

		String[] columnNames = { "Product", "Quantity", "Last Month Quantity" };
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		try {
			while (rs.next()) {
				// Retrieve by column name
				String name = rs.getString("NAME");
				int curquan = rs.getInt("CurrentMonthQuantity");
				int lastquan = rs.getInt("LastMonthQuantity");

				// Display values
				rows.add(new Object[] { name, curquan, lastquan });
				System.out.print("Product: " + name);
				System.out.print(", Quantity: " + curquan);
				System.out.print(", Previous Quantity: " + lastquan);
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
}
