import java.awt.LayoutManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class ApplicationPanel extends JPanel {
	public ApplicationPanel() {
	}

	public ApplicationPanel(LayoutManager layoutManager) {
		super(layoutManager);
	}

	protected static DefaultTableModel buildTableModel(ResultSet rs) {
		Vector<String> columnNames = new Vector<String>();
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try {
			ResultSetMetaData metaData = rs.getMetaData();

			// names of columns
			int columnCount = metaData.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				columnNames.add(metaData.getColumnName(column));
			}

			// data of the table
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return new DefaultTableModel(data, columnNames);

	}
}
