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
import javax.swing.text.TableView.TableRow;

public class DropOffPanel extends JPanel {
	private JTable table;
	private HashMap<String, Integer> pids, sids;

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
		Object[][] data = { { " ", " ", " " }, { " ", " ", " " }, { " ", " ", " " }, { " ", " ", " " }, { " ", " ", " " } };
		table = new JTable(data, columnNames);
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
		JButton completeDropOffButton = new JButton("Complete Drop Off");
		completeDropOffButton.addActionListener(completeDropOffListener);
		southPanel.add(completeDropOffButton);
		add(southPanel, BorderLayout.SOUTH);
	}

	public void setSources(ResultSet sources, ResultSet products) {
		pids = new HashMap<String, Integer>();
		sids = new HashMap<String, Integer>();
		try {
			JComboBox<String> comboBox = new JComboBox<String>();
			boolean first = true;
			String initProduct = "";
			while (products.next()) {
				System.out.println(products.getString("Name"));
				if (first) {
					initProduct = products.getString("Name");
					first = false;
				}
				comboBox.addItem(products.getString("Name"));
				pids.put(products.getString("Name"), products.getInt("PID"));
			}
			table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBox));

			comboBox = new JComboBox<String>();
			first = true;
			String initSource = "";
			while (sources.next()) {
				System.out.println(sources.getString("Name"));
				if (first) {
					initSource = sources.getString("Name");
					first = false;
				}
				comboBox.addItem(sources.getString("Name"));
				sids.put(sources.getString("Name"), sources.getInt("SID"));
			}
			for (int i = 0; i < table.getRowCount(); i++) {
				table.setValueAt(initProduct, i, 0);
				table.setValueAt(initSource, i, 1);
			}
			table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<int[]> getDropoffInfo() throws Exception {
		ArrayList<int[]> info = new ArrayList<int[]>();
		for (int i = 0; i < table.getRowCount(); i++) {
			info.add(new int[] { pids.get(table.getValueAt(i, 0)), sids.get(table.getValueAt(i, 1)), Integer.parseInt(((String) table.getValueAt(i, 2)).trim()) });
		}
		return info;
	}
}
