import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.NumberFormatter;

public class PickupsPanel extends ApplicationPanel {
	private JFormattedTextField dateField;
	private JTable table;
	private JScrollPane scrollPane;
	private ActionListener selectClientListener;
	private ArrayList<Integer> cids;

	public PickupsPanel(ActionListener returnButtonListener, ActionListener selectClientListener, ActionListener daySelectListener) {
		super(new BorderLayout());
		this.selectClientListener = selectClientListener;
		/* North Panel */
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(31);
		// If you want the value to be committed on each keystroke instead of focus lost
		formatter.setCommitsOnValidEdit(true);
		dateField = new JFormattedTextField(formatter);
		dateField.setColumns(10);
		dateField.setText("5");
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(daySelectListener);

		JPanel northPanel = new JPanel(new GridLayout(1, 2));
		northPanel.add(new JPanel(new GridBagLayout()) {
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
		final JPanel inputPanel = new JPanel(new GridLayout(2, 2));
		inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		inputPanel.add(new JLabel("Enter day:"));
		inputPanel.add(dateField);
		inputPanel.add(Box.createGlue());
		inputPanel.add(searchButton);
		northPanel.add(new JPanel(new GridBagLayout()) {
			{
				add(inputPanel);
			}
		});
		add(northPanel, BorderLayout.NORTH);

		/* Table Panel */
		Object[][] data = { { "Test", "Smith", "Mary", new Integer(4), "300 North Side Dr, Atlanta, GA 30332", "404-335-1122", new Integer(5) } };
		String[] columnNames = { "Sign in", "Last Name", "Last Name", "Size", "Address", "Telephone", "Monthly Pickup Day" };
		table = new JTable(data, columnNames);

		table.getColumn("Sign in").setCellRenderer(new ButtonRenderer());
		table.getColumn("Sign in").setCellEditor(new ButtonEditor(new JCheckBox(), selectClientListener));

		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JButton returnButton = new JButton("Return");
		returnButton.addActionListener(returnButtonListener);
		JPanel southPanel = new JPanel(new GridBagLayout());
		southPanel.add(returnButton);
		add(southPanel, BorderLayout.SOUTH);
	}

	public int getSearchDay() {
		return Integer.parseInt(dateField.getText());
	}

	public void setData(ResultSet rs) {
		System.out.println("Updating pickuppanel with search results...");
		remove(scrollPane);

		String[] columnNames = { "Sign in", "Last Name", "Last Name", "Size", "Address", "Telephone", "Monthly Pickup Day" };
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		cids = new ArrayList<Integer>();
		try {
			while (rs.next()) {
				// Retrieve by column name
				String last = rs.getString("Last");
				String first = rs.getString("First");
				int size = rs.getInt("Size");
				String address = rs.getString("apartmentNum") + " " + rs.getString("Street") + " " + rs.getString("City") + " " + rs.getString("State") + " " + rs.getString("zip");
				String phone = rs.getString("Phone");
				int pday = rs.getInt("PDay");

				// Display values
				rows.add(new Object[] { "Sign in", last, first, size, address, phone, pday });
				cids.add(Integer.parseInt(rs.getString("CID")));
				System.out.print("CID: " + cids.get(cids.size() - 1));
				System.out.print(", Last: " + last);
				System.out.print(", First: " + first);
				System.out.print(", Size: " + size);
				System.out.print(", Address: " + address);
				System.out.print(", Phone: " + phone);
				System.out.print(", PDay: " + pday);
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Object[][] data = rows.toArray(new Object[0][7]);
		table = new JTable(data, columnNames);

		table.getColumnModel().getColumn(2).setPreferredWidth(2);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);

		table.getColumn("Sign in").setCellRenderer(new ButtonRenderer());
		table.getColumn("Sign in").setCellEditor(new ButtonEditor(new JCheckBox(), selectClientListener));
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		invalidate();
		validate();
		repaint();
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	private class ButtonEditor extends DefaultCellEditor {
		protected JButton button;
		private String label;
		private boolean isPushed;
		private ActionListener listener;
		private JTable table;
		private int row;

		public ButtonEditor(JCheckBox checkBox, ActionListener listener) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
			this.listener = listener;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			this.table = table;
			label = (value == null) ? "" : value.toString();
			this.row = row;
			button.setText(label);
			isPushed = true;
			return button;
		}

		public Object getCellEditorValue() {
			if (isPushed) {
				String f = (String) table.getValueAt(row, 2);
				String l = (String) table.getValueAt(row, 1);
				Client client = new Client(f, l); // change to use clientIDs
				System.out.println("Button " + label + " pressed.\n\tShould direct to " + client + "'s bag.");
				System.out.println(cids.get(row));
				listener.actionPerformed(new ActionEvent(PickupsPanel.this, cids.get(row), f + " " + l));
			}
			isPushed = false;
			return new String(label);
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}

	private static class JTableButtonMouseListener extends MouseAdapter {
		private final JTable table;

		public JTableButtonMouseListener(JTable table) {
			this.table = table;
		}

		public void mouseClicked(MouseEvent e) {
			int column = table.getColumnModel().getColumnIndexAtX(e.getX());
			int row = e.getY() / table.getRowHeight();

			if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
				Object value = table.getValueAt(row, column);
				if (value instanceof JButton) {
					((JButton) value).doClick();
				}
			}
		}
	}

}
