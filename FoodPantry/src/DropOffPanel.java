import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DropOffPanel extends JPanel {
	private JButton completeDropOffButton;

	public DropOffPanel(final ActionListener returnListener, ActionListener completeDropOffListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridBagLayout());
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/DropOffBanner.png"));
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
		String[] columnNames = { "Product", "Source", "Quantity" };
		Object[][] data = { { "Cereal", "Krogers", new Integer(100) }, { "Baked Beans", "Donation", new Integer(6) } };
		JTable table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.add(new JButton("Return") {
			{
				addActionListener(returnListener);
			}
		});
		completeDropOffButton = new JButton("Complete Drop Off");
		completeDropOffButton.addActionListener(completeDropOffListener);
		southPanel.add(completeDropOffButton);
		add(southPanel, BorderLayout.SOUTH);
	}
}
