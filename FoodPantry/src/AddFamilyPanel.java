import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AddFamilyPanel extends JPanel {
	private JButton saveFamilyMembersButton;
	private JTable table;

	public AddFamilyPanel(ActionListener saveFamilyListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel headerPanel = new JPanel(new GridBagLayout());
		headerPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/Home_Clients.png"));
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
		// maybe put a label for the name of the family we're adding to
		add(headerPanel, BorderLayout.NORTH);

		/* Center Panel */
		String[] columnNames = { "First Name", "Last Name", "Gender", "Date of Birth" };
		Object[][] data = { { " ", " ", " ", " " }, { " ", " ", " ", " " }, { " ", " ", " ", " " }, { " ", " ", " ", " " } };
		table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel southPanel = new JPanel();
		saveFamilyMembersButton = new JButton("Save Family Members");
		saveFamilyMembersButton.addActionListener(saveFamilyListener);
		southPanel.add(saveFamilyMembersButton);
		add(southPanel, BorderLayout.SOUTH);

	}

	public Object[][] getData() {
		ArrayList<Object[]> members = new ArrayList<Object[]>();
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getValueAt(i, 0) != "")
				members.add(new Object[] { table.getValueAt(i, 0), table.getValueAt(i, 1), table.getValueAt(i, 2), table.getValueAt(i, 3) });
		}
		Object[][] data = members.toArray(new Object[0][4]);
		return data;
	}
}
