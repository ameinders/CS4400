import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FoodPantry {

	// Alex: ALL OF THESE NEED VARIABLES AS THEIR PARAMETERS
	public static void main(String[] args) {
		Database dB = new Database();
		dB.login("mFilly", "ehsie4n2");
		dB.viewPickups(25);
		// These are commented out because they are inserting which gives an error if the row already exists
		// System.out.println(dB.confirmPickup(8, 1) + " rows affected.");
		// dB.dropoff("Cereal", "Kroger", 150);
		dB.searchClient(null, "703-455-4222");
		dB.bagContents(0, 1);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date utilDate = null;
		java.util.Date utilDate2 = null;
		try {
			utilDate = sdf.parse("6/18/1976");
			utilDate2 = sdf.parse("7/1/2007");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); // These don't actually give the right date
		java.sql.Date sqlDate2 = new java.sql.Date(utilDate.getTime()); // These don't actually give the right date
		// Alex: What is the input 'delivery' - I just put that as pDay for now
		// dB.addClient("Individual", 4, "Karen", "Yin", "Female", sqlDate, "Letter Lane", 632, "Marietta", "GA", 73627, "666-292-3988", "Pell Grant", sqlDate2, 4);
		// d = new Date(1989, 1, 26); //These don't actually give the right date
		// dB.addFamily(4, "Hary", "Yin", "Male", d);
		dB.viewBags();
		dB.editBagProduct(1, "Rice", 10);
		dB.listProducts(null);
		// dB.addProduct("Mushrooms", "Kroger", .50);
		dB.msr("root", false);
		dB.groceryReport("root");
	}
}