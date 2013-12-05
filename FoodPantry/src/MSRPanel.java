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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MSRPanel extends JPanel {
	private JScrollPane scrollPane;
	private JTable table;

	public MSRPanel(final ActionListener returnListener, final ActionListener groceryListener, final ActionListener activeListener, final ActionListener pastListener) {
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
						add(new JButton("Active MSR") {
							{
								addActionListener(activeListener);
							}
						});
						add(new JButton("Last Month MSR") {
							{
								addActionListener(pastListener);
							}
						});
					}
				});
			}
		});
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		String[] columnNames = { "Week", "# of Households", "Under 18 Years", "18-64 Years", "65 years & Older", "Total People", "Food Cost ($)" };
		Object[][] data = { { new Integer(1), new Integer(56), new Integer(60), new Integer(40), new Integer(25), new Integer(125), new Double(73.50) } };
		table = new JTable(data, columnNames);

		scrollPane = new JScrollPane(table);
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

	public void setData(ResultSet rs) {

		System.out.println("Updating MSR panel with search results...");
		remove(scrollPane);

		String[] columnNames = { "Week", "# of Households", "Under 18 years", "18-64 years", "65 yesars & Older", "Total People" };
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		try {
			while (rs.next()) {
				// Retrieve by column name
				int week = rs.getInt("Week");
				int households = rs.getInt("Households");
				int less18 = rs.getInt("Under 18 Years");
				int midage = rs.getInt("18-64 years");
				int old = rs.getInt("65 years & Older");
				int total = less18 + midage + old;

				// Display values
				rows.add(new Object[] { week, households, less18, midage, old, total });
				System.out.print("Week: " + week);
				System.out.print(", Households: " + households);
				System.out.print(", <18: " + less18);
				System.out.print(", 18-64: " + midage);
				System.out.print(", 64+: " + old);
				System.out.print(", total: " + total);
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
