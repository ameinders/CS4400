import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class PickupsPanel extends JPanel {
	private JTextField dateField = null;
	private JButton returnButton = null;

	private int[] clientIDs;

	public PickupsPanel(ActionListener returnButtonListener, ActionListener selectClientListener) {
		super(new BorderLayout());
		/* North Panel */
		dateField = new JTextField(10);
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
		JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));// new
																		// GridBagLayout());
		inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		inputPanel.add(new JLabel("Enter day:"));
		inputPanel.add(dateField);
		northPanel.add(inputPanel);
		add(northPanel, BorderLayout.NORTH);

		/* Table Panel */
		Object[][] data = { { "Test", "Smith", "Mary", new Integer(4), "300 North Side Dr, Atlanta, GA 30332", "404-335-1122", new Integer(5) } };
		String[] columnNames = { "Sign in", "Last Name", "Last Name", "Size", "Address", "Telephone", "Monthly Pickup Day" };
		JTable table = new JTable(data, columnNames);

		table.getColumnModel().getColumn(2).setPreferredWidth(2);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);

		table.getColumn("Sign in").setCellRenderer(new ButtonRenderer());
		table.getColumn("Sign in").setCellEditor(new ButtonEditor(new JCheckBox(), selectClientListener));

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		returnButton = new JButton("Return");
		returnButton.addActionListener(returnButtonListener);
		JPanel southPanel = new JPanel(new GridBagLayout());
		southPanel.add(returnButton);
		add(southPanel, BorderLayout.SOUTH);
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
				listener.actionPerformed(new ActionEvent(PickupsPanel.this, 0, "Button " + label + " pressed.\n\tShould direct to " + client + "'s bag."));
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

}
