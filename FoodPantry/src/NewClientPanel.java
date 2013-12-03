import java.awt.BorderLayout;
import java.awt.GridBagLayout;
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

public class NewClientPanel extends JPanel {
	private JButton saveClientButton;

	public NewClientPanel(ActionListener saveClientListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridBagLayout());
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/NewClientBanner.png"));
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
		String[] columnNames = { "Attribute", "Value" };
		Object[][] data = { { "Bag Type", " " }, { "Pick Up Day of Month", " " }, { "First Name", " " }, { "Last Name", " " }, { "Gender", " " },
				{ "Date of Birth", " " }, { "Street", " " }, { "Apartment #", " " }, { "City", " " }, { "State", " " }, { "Zipcode", " " },
				{ "Telephone", " " }, { "Financial Aid", " " }, { "Start Date", " " }, { "Delivery", " " } };
		JTable table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel southPanel = new JPanel();
		saveClientButton = new JButton("Save Client");
		saveClientButton.addActionListener(saveClientListener);
		southPanel.add(saveClientButton);
		add(southPanel, BorderLayout.SOUTH);
	}
}
