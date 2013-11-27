import java.sql.Connection;
import java.sql.Date;
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

			    //Extract data from result set
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
	 * Returns the number of rows affected. */
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
				
				stmt.close();
				
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
				
				stmt.close();
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				close();
			}
		}
	}
	
	/* Takes in a last name and telephone number, one of which will be null. Searches for a client
	 * based on the parameters and returns a ResultSet with the values to display. 
	 * Null is returned if can't connect or no values to display. */
	public ResultSet searchClient(String lName, String telephone) {
		ResultSet rs = null;
		if (connect()) {	
			try {
				
				System.out.println("Search for Clients:");
				PreparedStatement stmt = con.prepareStatement("SELECT Last, First, Street, City, State, "
						+ "Zip, ApartmentNum, Phone, Start, CID FROM Client NATURAL JOIN (SELECT CID, COUNT(*) "
						+ "AS “Size” FROM FamilyMember GROUP BY CID) AS T WHERE Last = '" + lName + "' OR "
						+ "Phone = '" + telephone + "'");
				rs = stmt.executeQuery();
				
				//Extract data from result set
			    while(rs.next()){
			        //Retrieve by column name
			    	String last = rs.getString("Last");
			        String first = rs.getString("First");
			        String address = rs.getString("apartmentNum") + " " + rs.getString("Street") + " " 
			        		+ rs.getString("City") + " " + rs.getString("State") + " " + rs.getString("zip");
			        String phone = rs.getString("Phone");
			        Date start = rs.getDate("Start");

			        //Display values
			        System.out.print("Last: " + last);
			        System.out.print(", First: " + first);
			        System.out.print(", Address: " + address);
			        System.out.print(", Phone: " + phone);
			        System.out.print(", Start: " + start);
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
	
	/* Takes in a  */
	public void addClient(String bagType, int pickup, String first, String last, String gender, Date dob,
			String street, int apt, String city, String state, int zip, String phone, String finAid,
			Date start, int pDay) {
		if (connect()) {	
			try {
				ResultSet rs;
				System.out.println("Add a Client:");
				PreparedStatement stmt = con.prepareStatement("SELECT BID FROM Bag WHERE Name = '" + bagType + "'");
				rs = stmt.executeQuery();
				rs.next();
				int bid = rs.getInt("BID");
				
				stmt = con.prepareStatement("SELECT FID FROM FinancialAid WHERE Name = '" + finAid + "'");
				rs = stmt.executeQuery();
				rs.next();
				int fid = rs.getInt("FID");
				
				stmt = con.prepareStatement("INSERT INTO Client(First, Last, Phone, BID, FID, Gender, DOB, "
						+ "Start, PDay, Street, City, State, Zip, ApartmentNum) VALUES('" + first + "', '" + last + "', '" 
						+ phone + "', '" + bid + "', '" + fid + "', '" + gender + "', '" + dob + "', '" + start + "', '"
						+ pDay + "', '" + street + "', '" + city + "', '" + state + "', '" + zip + "', '" + apt + "')");
				stmt.executeUpdate();
				
				stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				close();
			}
		}
	}
				
	
	

	
	
	
	
}
