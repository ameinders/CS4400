import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class NewProductPanel extends JPanel {
	private HashMap<String, Integer> sids;
	private JTable table;
	private JScrollPane scrollPane;
	private JComboBox<String> comboBox;

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
		String[] columnNames = { "Name", "Source", "Cost Per Unit" };
		Object[][] data = { { " ", " ", " " } };
		table = new JTable(data, columnNames);

		scrollPane = new JScrollPane(table);
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

	public void setSources(ResultSet rs) {
		sids = new HashMap<String, Integer>();
		try {
			comboBox = new JComboBox<String>();
			boolean first = true;
			String initSource = "";
			while (rs.next()) {
				System.out.println(rs.getString("Name"));
				if (first) {
					initSource = rs.getString("Name");
					first = false;
				}
				comboBox.addItem(rs.getString("Name"));
				sids.put(rs.getString("Name"), rs.getInt("SID"));
			}
			table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

			table.setValueAt(initSource, 0, 1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Object[] getData() {
		if (table.isEditing())
			table.getCellEditor().stopCellEditing();
		return new Object[] { table.getValueAt(0, 0), sids.get(table.getValueAt(0, 1)), table.getValueAt(0, 2) };
	}
}
