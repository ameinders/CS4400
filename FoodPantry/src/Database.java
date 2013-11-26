import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Daddysmousey
 *
 */
public class Database {

	Connection con;
	int CID, BID;
	
	public Database() {
		con = null;
	}
	
	public boolean connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cs4400", "root", "sowarm2");
			
			if (!con.isClosed()) {
				System.out.println("Successfully connected to " + "MySQL server.");
				return true;
			}
			
		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		
		return false;
	}
	
	public void close() {
		try {
			if (con != null)
				con.close();
		} catch(SQLException e) {};
	}
	
	
	/* Checks the login credentials. Returns true if the login is valid. False otherwise. */
	public boolean login(String user, String pass) {
		if (connect()) {	
			try {
				System.out.println("Checking Login...");
			    String sql;
			    
				sql = "SELECT * FROM User WHERE Username = '" + user +"' AND Password = '" + pass +"'";
				PreparedStatement stmt = con.prepareStatement(sql);
				//stmt.setString(1, user);
				//stmt.setString(2, "Smith");
				  
				stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery(sql);
				
				//STEP 5: Extract data from result set
				if (!rs.isBeforeFirst()) {
					System.out.println("Login failed");
					stmt.close();
					return false;
				}
				else {
					System.out.println("Login successful");
					stmt.close();
					return true;
				}
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				close();
			}
		}
		return false;

	}
	
	/* View clients who are scheduled for pickups. Returns a ResultSet with the values to display. 
	 * Null is returned if can't connect or no values to display. */
	public ResultSet viewPickups(int pDay) {
		ResultSet rs = null;
		if (connect()) {	
			try {
				System.out.println("Pickup:");
			     
				String sqlCreate = "CREATE OR REPLACE VIEW FamilySize AS SELECT CID, COUNT(*)+1 AS Size FROM FamilyMember";
				
			    String sql;
			    sql = "SELECT Last, First, Size, ApartmentNum, Street, City, State, Zip, Phone, PDay FROM Client "
			    		+ "NATURAL JOIN FamilySize WHERE PDay = '" + pDay +"'";
			    PreparedStatement stmt = con.prepareStatement(sql);
			      
			    stmt = con.prepareStatement(sql);
			    stmt.executeUpdate(sqlCreate);
			    rs = stmt.executeQuery(sql);

			    //STEP 5: Extract data from result set
			    while(rs.next()){
			        //Retrieve by column name
			    	String last = rs.getString("Last");
			        String first = rs.getString("First");
			        int size = rs.getInt("Size");
			        String address = rs.getString("apartmentNum") + " " + rs.getString("Street") + " " 
			        		+ rs.getString("City") + " " + rs.getString("State") + " " + rs.getString("zip");
			        String phone = rs.getString("Phone");
			        int pday = rs.getInt("PDay");

			        //Display values
			        System.out.print("Last: " + last);
			        System.out.print(", First: " + first);
			        System.out.print(", Size: " + size);
			        System.out.print(", Address: " + address);
			        System.out.print(", Phone: " + phone);
			        System.out.print(", PDay: " + pday);
			     }
			      
			     stmt.close();
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				close();
			}
		}
	return rs;
	}
	
	/* Confirms the pickup for the selected client with the specific bag on the current date. 
	 * Returns the rows affected. */
	public int confirmPickup(int cid, int bid) {
		int rowsAffec = 0;
		if (connect()) {	
			try {
				System.out.println("Confirm a Pickup:");
			    String sql;
			    
				sql = "INSERT INTO PickupTransaction (CID, BID, Date) VALUES ('" + cid + "', '" + bid + "', CURDATE())";
				PreparedStatement stmt = con.prepareStatement(sql);
				  
				stmt = con.prepareStatement(sql);
				rowsAffec = stmt.executeUpdate(sql);
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				close();
			}
		}
		return rowsAffec;
	}
	
	/* Enters a product into the dropoff table */
	public void dropoff(String product, String source, int quantity) {
		if (connect()) {	
			try {
				ResultSet rs;
				System.out.println("Dropoff a product:");
				PreparedStatement stmt = con.prepareStatement("SELECT PID FROM Product WHERE Name = '" + product + "'");
				rs = stmt.executeQuery();
				rs.next();
				int pid = rs.getInt("PID");
				
				stmt = con.prepareStatement("SELECT SID FROM Source WHERE Name = '" + source + "'");
				rs = stmt.executeQuery();
				rs.next();
				int sid = rs.getInt("SID");
				
				stmt = con.prepareStatement("SELECT DID FROM DropoffTransaction WHERE SID = '" + sid + "' AND Date = CURDATE()");
				rs = stmt.executeQuery();
				int did = 0;
				if (rs.next())
					did = rs.getInt("DID");
				
				//if a transaction from this source today doesn’t exist, create it
				if (did == 0) {
					stmt = con.prepareStatement("INSERT INTO DropoffTransaction (SID, Date) VALUES ('" + sid + "', CURDATE());");
					stmt.executeUpdate();
					
					stmt = con.prepareStatement("SELECT DID FROM DropoffTransaction WHERE SID = '" + sid + "' AND Date = CURDATE()");
					rs = stmt.executeQuery();
					rs.next();
					did = rs.getInt("DID");
				}
				
				stmt = con.prepareStatement("INSERT INTO Dropoff (DID, PID, Qty) VALUES ('" + did + "', '" + pid + "', '" + quantity + "');");
				stmt.executeUpdate();
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				close();
			}
		}
	}
	
	

}
