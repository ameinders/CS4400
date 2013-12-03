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

public class NewProductPanel extends JPanel {
	public NewProductPanel(ActionListener returnListener, ActionListener saveProductListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridBagLayout());
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/NewProductBanner.png"));
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
		String[] columnNames = { "Create New Product", " " };
		Object[][] data = { { "Name", " " }, { "Source", " " }, { "Cost Per Unit", " " } };
		JTable table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel southPanel = new JPanel(new GridLayout(1, 2));

		JButton returnButton = new JButton("Cancel");
		returnButton.addActionListener(returnListener);
		southPanel.add(returnButton);
		JButton saveProductButton = new JButton("Save Product");
		saveProductButton.addActionListener(saveProductListener);
		southPanel.add(saveProductButton);
		add(southPanel, BorderLayout.SOUTH);
	}
}
