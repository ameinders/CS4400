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

public class ProductListPanel extends JPanel {
	public ProductListPanel(ActionListener returnListener, ActionListener addProductListner) {
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
		final JPanel searchPanel = new JPanel(new GridLayout(2, 1));
		searchPanel.add(new JPanel(new GridLayout(1, 2)) {
			{
				searchPanel.add(new JLabel("Product Name: "));
				JTextField productNameField = new JTextField();
				searchPanel.add(productNameField);
			}
		});
		searchPanel.add(new JButton("List All Products"));
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				add(searchPanel);
			}
		});
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		String[] columnNames = { "Product Name", "Quantity" };
		Object[][] data = { { "Can Fruit Pears", new Integer(3) }, { "Cereal", new Integer(1) } };
		JTable table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
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
}