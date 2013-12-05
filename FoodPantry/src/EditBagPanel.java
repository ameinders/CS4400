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

public class EditBagPanel extends JPanel {
	private JTable table;
	private JScrollPane scrollPane;
	private Object[][] data;
	private int bid;

	public EditBagPanel(ActionListener returnListener, ActionListener saveBagListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridBagLayout());
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/Home_Bags.png"));
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
		String[] columnNames = { "Product Name", "Quantity" };
		Object[][] data = { { "Can Fruit Pears", new Integer(3) }, { "Cereal", new Integer(1) } };
		table = new JTable(data, columnNames);

		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel southPanel = new JPanel(new GridLayout(1, 2));

		JButton returnButton = new JButton("Cancel");
		returnButton.addActionListener(returnListener);
		southPanel.add(returnButton);
		JButton saveBagButton = new JButton("Save Bag");
		saveBagButton.addActionListener(saveBagListener);
		southPanel.add(saveBagButton);
		add(southPanel, BorderLayout.SOUTH);
	}

	public Object[][] getData() {
		table.getCellEditor().stopCellEditing();
		for (int i = 0; i < table.getRowCount(); i++) {
			data[i][1] = table.getModel().getValueAt(i, 1);
			System.out.println("Data updated to " + data[i][1]);
		}
		return data;
	}

	public int getBID() {
		return bid;
	}

	public void setData(ResultSet rs, int bid) {
		this.bid = bid;
		System.out.println("Updating bag edit panel with search results...");
		remove(scrollPane);

		String[] columnNames = { "Product Name", "Quantity" };
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		// cids = new ArrayList<Integer>();
		try {
			while (rs.next()) {
				// Retrieve by column name
				String product = rs.getString("Name");
				String quantity = rs.getString("CurrentMonthQty");

				// Display values
				rows.add(new Object[] { product, quantity });
				// cids.add(rs.getInt("CID"));
				System.out.print("product: " + product);
				System.out.print(", quantity: " + quantity);
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		data = rows.toArray(new Object[0][2]);
		table = new JTable(data, columnNames);

		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		invalidate();
		validate();
		repaint();
	}
}
