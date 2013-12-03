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

public class AddFamilyPanel extends JPanel {
	private JButton saveFamilyMembersButton;

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
		JTable table = new JTable(data, columnNames);

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
}
