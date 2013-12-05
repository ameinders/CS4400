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
import javax.swing.JTextField;

public class ProductListPanel extends JPanel {
	private JScrollPane scrollPane;
	private JTable table;
	private Object[][] data;
	private JTextField productNameField;

	public ProductListPanel(ActionListener returnListener, ActionListener addProductListner, final ActionListener searchButtonListener, final ActionListener allListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridLayout(1, 2));
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/Home_Products.png"));
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
		final JPanel searchPanel = new JPanel(new GridLayout(2, 2));

		searchPanel.add(new JLabel("Product Name: "));
		productNameField = new JTextField();
		searchPanel.add(productNameField);
		searchPanel.add(new JButton("List All Products") {
			{
				addActionListener(allListener);
			}
		});
		searchPanel.add(new JButton("Search") {
			{
				addActionListener(searchButtonListener);
			}
		});

		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				add(searchPanel);
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

		JButton returnButton = new JButton("Return");
		returnButton.addActionListener(returnListener);
		southPanel.add(returnButton);
		JButton addProductButton = new JButton("Add Product");
		addProductButton.addActionListener(addProductListner);
		southPanel.add(addProductButton);
		add(southPanel, BorderLayout.SOUTH);
	}

	public void setData(ResultSet rs) {
		System.out.println("Updating product list panel with search results...");
		remove(scrollPane);

		String[] columnNames = { "Product Name", "Quantity", "Cost per Unit" };
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		// cids = new ArrayList<Integer>();
		try {
			while (rs.next()) {
				// Retrieve by column name
				String name = rs.getString("Name");
				int quantity = rs.getInt("Quantity");
				double cost = rs.getDouble("Cost");

				// Display values
				rows.add(new Object[] { name, quantity, cost });
				// cids.add(rs.getInt("CID"));
				System.out.print("name: " + name);
				System.out.print(", quantity: " + quantity);
				System.out.print(", cost: " + cost);
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

	public String searchString() {
		return productNameField.getText();
	}
}