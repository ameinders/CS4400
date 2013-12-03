import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MSRPanel extends JPanel {
	public MSRPanel(final ActionListener returnListener, final ActionListener groceryListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridLayout(1, 2));
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/MSRBanner.png"));
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
				add(new JLabel(new ImageIcon(pickupImage)));
			}
		});
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				add(new JPanel(new GridLayout(2, 1)) {
					{
						add(new JButton("Active MSR"));
						add(new JButton("Last Month MSR"));
					}
				});
			}
		});
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		String[] columnNames = { "Week", "# of Households", "Under 18 Years", "18-64 Years", "65 years & Older", "Total People", "Food Cost ($)" };
		Object[][] data = { { new Integer(1), new Integer(56), new Integer(60), new Integer(40), new Integer(25), new Integer(125), new Double(73.50) } };
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
		southPanel.add(new JButton("Grocery List") {
			{
				addActionListener(groceryListener);
			}
		});
		add(southPanel, BorderLayout.SOUTH);
	}
}
