import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class BagListPanel extends JPanel {
	public BagListPanel(ActionListener selectBagListener, final ActionListener returnButtonListener) {
		super(new BorderLayout());
		/* North Panel */
		JPanel northPanel = new JPanel(new GridBagLayout());
		northPanel.add(new JPanel(new GridBagLayout()) {
			{
				Image pickupImage = null;
				try {
					pickupImage = ImageIO.read(new File("src/Home_Bags.png"));
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
		add(northPanel, BorderLayout.NORTH);

		/* Table Panel */
		String[] columnNames = { "View/Edit", "Bag Name", "# Items", "# Clients", "Cost" };
		Object[][] data = { { "  ", "Individual", new Integer(10), new Integer(5), new Integer(0) },
				{ " ", "Family of Two", new Integer(25), new Integer(12), new Integer(0) } };
		JTable table = new JTable(data, columnNames);

		table.getColumn("View/Edit").setCellRenderer(new ButtonRenderer());
		table.getColumn("View/Edit").setCellEditor(new ButtonEditor(new JCheckBox(), selectBagListener));

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		add(scrollPane);

		/* South Panel */
		JPanel southPanel = new JPanel(new GridBagLayout());
		southPanel.add(new JButton("Return") {
			{
				addActionListener(returnButtonListener);
			}
		});
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
				listener.actionPerformed(new ActionEvent(BagListPanel.this, 0, "Bag " + row + " selected for viewing."));
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
