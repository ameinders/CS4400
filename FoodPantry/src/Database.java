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
			con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_25", "cs4400_Group_25", "UZNgIGrN");
			
			if (!con.isClosed()) {
				System.out.println("Successfully connected to " + "MySQL server.");
				return true;
			}
			
		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		
		return false;
	}
	
	/*public void close() {
		try {
			if (con != null)
				con.close();
		} catch(SQLException e) {};
	}*/
	
	
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
					//stmt.close();
					return true;
				}
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
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
			     
				//Union Client and FamilyMember tables
				PreparedStatement stmt = con.prepareStatement("CREATE OR REPLACE VIEW TotalFamily AS SELECT CID, "
						+ "First, Last FROM Client UNION SELECT CID, First, Last FROM FamilyMember");
				stmt.executeUpdate();
				
				//Get size of unioned table
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW FamilySize AS SELECT CID,"
						+ " COUNT(*) AS Size FROM TotalFamily GROUP BY CID;");
				stmt.executeUpdate();
				
				//Display the clients with the specified pay day
			    stmt = con.prepareStatement( "SELECT Last, First, Size, ApartmentNum, Street, City,"
			    		+ " State, Zip, Phone, PDay FROM Client c LEFT JOIN FamilySize f ON c.CID = f.CID "
			    		+ "WHERE PDay = '" + pDay +"'");
			    rs = stmt.executeQuery();

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
			        System.out.println();
			     }
			      
			     //stmt.close();
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
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
				
				//stmt.close();
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
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
				
				//stmt.close();
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
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
						+ "AS Size FROM FamilyMember GROUP BY CID) AS T WHERE Last = '" + lName + "' OR "
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
			    //stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
		return rs;
	}
	
	/* Takes in the data for the client and creates the client.  */
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
				
				stmt = con.prepareStatement("INSERT INTO Client(First, Last, Phone, BID, Gender, DOB, "
						+ "Start, PDay, Street, City, State, Zip, ApartmentNum) VALUES('" + first + "', '" + last + "', '" 
						+ phone + "', '" + bid + "', '" + gender + "', '" + dob + "', '" + start + "', '"
						+ pDay + "', '" + street + "', '" + city + "', '" + state + "', '" + zip + "', '" + apt + "')");
				stmt.executeUpdate();
				
				stmt = con.prepareStatement("SELECT CID FROM Client WHERE First = '" + first + "' AND Last = '" + last + "'");
				rs = stmt.executeQuery();
				rs.next();
				int cid = rs.getInt("CID");
				
				stmt = con.prepareStatement("INSERT INTO HasAid(FID, CID)VALUES('" + fid + "', '" + cid + "')");
				stmt.executeUpdate();
				
				//stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
	}
				
	
	/* Takes in the CID of the client, and the data for the family member and creates the family member. */
	public void addFamily(int cid, String first, String last, String gender, Date dob) {
		if (connect()) {	
			try {
				ResultSet rs;
				System.out.println("Add a Family Member:");
				PreparedStatement stmt = con.prepareStatement("INSERT INTO FamilyMember(CID, First, Last, DOB, Gender)"
						+ " VALUES('" + cid + "', '" + first + "', '" + last + "', '" + dob + "', '" + gender + "')");
				stmt.executeUpdate();
				
				//stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
	}
	
	
	/* Returns a result set which holds all the information for each bag type. */
	public ResultSet viewBags() {
		ResultSet rs = null;
		if (connect()) {	
			try {
				System.out.println("View Bags:");
				
				//a view for the number of items for each BID
				PreparedStatement stmt = con.prepareStatement("CREATE OR REPLACE VIEW ItemsInBag AS SELECT BID, "
						+ "count(*) AS NumItems FROM Holds WHERE CurrentMonthQty > 0 GROUP BY BID");
				stmt.executeUpdate();
				
				//a view for the number of clients for each BID
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW ClientsForBag AS SELECT BID, count(*) "
						+ "AS NumClients FROM Client GROUP BY BID");
				stmt.executeUpdate();
				
				//a view for the cost for each product in each bag
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW CostOfItems AS SELECT BID, PID, CurrentMonthQty,"
						+ " Cost FROM Holds NATURAL JOIN Product");
				stmt.executeUpdate();
				
				//a view for the cost for each BID
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW CostOfBags AS SELECT BID, SUM(Cost) AS Cost "
						+ "FROM CostOfItems GROUP BY BID");
				stmt.executeUpdate();
				
				//join all the tables together with the bag name
				stmt = con.prepareStatement("SELECT Name, NumItems, NumClients, Cost FROM ((Bag NATURAL JOIN ItemsInBag) "
						+ "NATURAL JOIN ClientsForBag) NATURAL JOIN CostOfBags");
				rs = stmt.executeQuery();
				
				//stmt.close();
				
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
		return rs;
	}
	
	/* Returns a result set which holds all the contents of a bag type. */
	public ResultSet bagContents(int bid) {
		ResultSet rs = null;
		if (connect()) {
			try {
				System.out.println("View a bag's contents:");
				
				//displays the quantity of each product with a quantity > 0 in the bag
				PreparedStatement stmt = con.prepareStatement("SELECT Name, CurrentMonthQty FROM Product "
						+ "NATURAL JOIN (SELECT PID, CurrentMonthQty FROM Holds WHERE BID = '" + bid + 
						"' AND CurrentMonthQty > 0) AS Qty");
				rs = stmt.executeQuery();
				
				//stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
		return rs;
	}

	/* Updates the quantity of a product for a bag, given the product name, and the quantity. */
	public void editBagProduct(int bid, String product, int quantity) {
		if (connect()) {
			try {
				ResultSet rs = null;
				int pid = 0;
				System.out.println("Edit Bag Product:");
				PreparedStatement stmt = con.prepareStatement("SELECT PID FROM Product WHERE Name = '" + product + "'");
				rs = stmt.executeQuery();
				rs.next();
				pid = rs.getInt("PID");

				if (pid == 0)
					System.out.println("Error: Product doesn't exist");
				else
				//update last month and current month quantities
				stmt = con.prepareStatement("UPDATE Holds SET LastMonthQty = CurrentMonthQty, CurrentMonthQty = '" + quantity 
						+ "' WHERE BID = '" + bid + "' AND PID = '" + pid + "'");
				stmt.executeUpdate();
				
				//stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
	}
	
	/* Takes in a product. Searches for the product and returns a ResultSet with the values to display. 
	 * All products returned if parameter is null. Null is returned if can't connect or no values to display. */
	@SuppressWarnings("resource")
	public ResultSet listProducts(String product) {
		ResultSet rs = null;
		if (connect()) {
			try {
				System.out.println("List Products:");
				
				//get the dropoff quantities for each product of last month
				PreparedStatement stmt = con.prepareStatement("CREATE OR REPLACE VIEW LstMthDQty"
						+ " AS SELECT PID, SUM(Qty) AS TotalDQty FROM DropoffTransaction "
						+ "NATURAL JOIN Dropoff WHERE Month(Date) = MONTH(CURDATE())-1 GROUP BY PID");
				stmt.executeUpdate();

				//get the pickup quantities for each product of last month
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW LstMthPTQtw AS SELECT PID, "
						+ "SUM(LastMonthQty) AS TotalPTQty FROM PickupTransaction NATURAL JOIN Holds "
						+ "WHERE Month(Date) = MONTH(CURDATE())-1 GROUP BY PID");
				stmt.executeUpdate();
				
				//subtract the dropoff and pickup quantities
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW One AS SELECT d.PID, (ifnull(TotalDQty, 0) "
						+ "- ifnull(TotalPTQty, 0)) AS TotalLMQty FROM LstMthDQty d LEFT JOIN LstMthPTQtw p ON "
						+ "d.PID = p.PID UNION SELECT p.PID, (ifnull(TotalDQty, 0) - ifnull(TotalPTQty, 0))"
						+ " AS TotalLMQty FROM LstMthDQty d RIGHT JOIN LstMthPTQtw p ON d.PID = p.PID");
				stmt.executeUpdate();
				
				//get the dropoff quantities for each product of curr month
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW CurrMthDQty AS SELECT PID, SUM(Qty) "
						+ "AS TotalDQty FROM DropoffTransaction NATURAL JOIN Dropoff WHERE Month(Date) = "
						+ "MONTH(CURDATE()) GROUP BY PID");
				stmt.executeUpdate();
				
				//get the pickup quantities for each product of curr month
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW CurrMthPTQtw AS SELECT PID, "
						+ "SUM(LastMonthQty) AS TotalPTQty FROM PickupTransaction NATURAL JOIN Holds "
						+ "WHERE Month(Date) = MONTH(CURDATE()) GROUP BY PID;");
				stmt.executeUpdate();
				
				//subtract the dropoff and pickup quantities
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW Two AS SELECT d.PID, (ifnull(TotalDQty, 0)"
						+ " - ifnull(TotalPTQty, 0)) AS TotalCMQty FROM CurrMthDQty d LEFT JOIN CurrMthPTQtw p "
						+ "ON d.PID = p.PID UNION SELECT p.PID, (ifnull(TotalDQty, 0) - ifnull(TotalPTQty, 0)) "
						+ "AS TotalCMQty FROM CurrMthDQty d RIGHT JOIN CurrMthPTQtw p ON d.PID = p.PID");
				stmt.executeUpdate();
				
				//add last month's and this month's quantities
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW Three AS SELECT o.PID, (ifnull(TotalLMQty, 0) "
						+ "+ ifnull(TotalCMQty, 0)) AS Quantity FROM One o LEFT JOIN Two t ON o.PID = t.PID UNION "
						+ "SELECT t.PID, (ifnull(TotalLMQty, 0) + ifnull(TotalCMQty, 0)) AS Quantity FROM One o "
						+ "RIGHT JOIN Two t ON o.PID = t.PID");
				stmt.executeUpdate();
				
				//display name, quantity, and cost
				if (product == null)
					stmt = con.prepareStatement("SELECT Name, Quantity, Cost FROM Product NATURAL JOIN Three");
				else
					stmt = con.prepareStatement("SELECT Name, Quantity, Cost FROM Product NATURAL JOIN Three WHERE Name = '" + product + "'");
				rs = stmt.executeQuery();
				
				//stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
		return rs;
	}
	
	/* Adds the new product, given the product name, source, and cost per unit. */
	public void addProduct(String product, String source, double cost) {
		if (connect()) {
			try {
				ResultSet rs = null;
				System.out.println("Add a Product:");
				
				PreparedStatement stmt = con.prepareStatement("SELECT SID FROM Source WHERE name = '" + source + "'");
				rs = stmt.executeQuery();
				int sid = 0;
				if (rs.next())
					sid = rs.getInt("SID");
				
				//if source doesn’t exist, first add it in the Source table
				if (sid == 0) {
					stmt = con.prepareStatement("INSERT INTO Source (Name) VALUES ('" + source + "')");
					stmt.executeUpdate();
					
					stmt = con.prepareStatement("SELECT SID FROM Source WHERE name = '" + source + "'");
					rs = stmt.executeQuery();
					rs.next();
					sid = rs.getInt("SID");
				}
				
				stmt = con.prepareStatement("INSERT INTO Product (Name, SID, Cost) VALUES ('" + product 
						+ "' ,'" + sid + "' ,'" + cost + "')");
				stmt.executeUpdate();
				
				//stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
	}

	
	public ResultSet msr(/*month*/) {
		//TODO
		ResultSet rs = null;
		return rs;
	}
	
	/*  */
	public ResultSet groceryReport() {
		ResultSet rs = null;
		if (connect()) {
			try {
				System.out.println("Display Grocery Report:");
				
				//creates a view of the transactions which occurred last month
				PreparedStatement stmt = con.prepareStatement("CREATE OR REPLACE VIEW LastMthPT"
						+ " AS SELECT BID, CID FROM PickupTransaction WHERE Month(Date) = MONTH(CURDATE())-1");
				stmt.executeUpdate();

				//displays the quantities of each product for the last month
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW LastMthQT "
						+ "AS SELECT Name, SUM(LastMonthQty) AS LastMonthQuantity FROM (LastMthPT "
						+ "NATURAL JOIN Holds) NATURAL JOIN Product GROUP BY Name");
				stmt.executeUpdate();
				
				//creates a view of the bags to be picked up this month
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW CurrMthPT AS SELECT BID, CID FROM Client");
				stmt.executeUpdate();
				
				//displays the quantities of each product for the current month
				stmt = con.prepareStatement("CREATE OR REPLACE VIEW CurrMthQT AS SELECT Name, SUM(CurrentMonthQty) "
						+ "AS CurrentMonthQuantity FROM (CurrMthPT NATURAL JOIN Holds) NATURAL JOIN Product "
						+ "GROUP BY Name");
				stmt.executeUpdate();
				
				//displays the final table
				stmt = con.prepareStatement("SELECT c.NAME, CurrentMonthQuantity, LastMonthQuantity FROM "
						+ "CurrMthQT c LEFT JOIN LastMthQT l ON c.Name = l.Name UNION SELECT l.NAME, "
						+ "CurrentMonthQuantity, LastMonthQuantity FROM CurrMthQT c RIGHT JOIN "
						+ "LastMthQT l ON c.Name = l.Name");
				rs = stmt.executeQuery();
				
				//stmt.close();
			    
			} catch(Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			} finally {
				//close();
			}
		}
		return rs;
	}
	
}
