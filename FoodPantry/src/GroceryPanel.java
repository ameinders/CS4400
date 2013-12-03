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

public class GroceryPanel extends JPanel {
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
		JTable table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
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
}
