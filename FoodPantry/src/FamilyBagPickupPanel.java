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

public class FamilyBagPickupPanel extends JPanel {
	private JButton completePickupButton;
	private JButton returnButton;

	String placeholderName = "Mary Smith";
	String placeholderDate = "September 5, 2013";

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
		JLabel nameLabel = new JLabel(placeholderName);
		JLabel dateLabel = new JLabel(placeholderDate);
		headerMetaPanel.add(nameLabel);
		headerMetaPanel.add(dateLabel);
		headerPanel.add(headerMetaPanel);
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		Object[][] data = { { "Cake Mix", new Integer(1) }, { "Canned Stew", new Integer(6) } };
		String[] columnNames = { "Product", "Quantity" };
		JTable table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
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
}
