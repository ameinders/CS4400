import java.sql.Date;


public class FoodPantry {

	//Alex: ALL OF THESE NEED VARIABLES AS THEIR PARAMETERS
	public static void main(String[] args) {
		Database dB = new Database();
		dB.login("mFilly", "ehsie4n2");
		dB.viewPickups(5);
		//These are commented out because they are inserting which gives an error if the row already exists
		//System.out.println(dB.confirmPickup(2, 1) + " rows affected.");
		//dB.dropoff("Potatoes", "Kroger", 150);
		dB.searchClient(null, "703-455-4222");
		Date d = new Date(1976, 6, 18);	//These don't actually give the right date
		Date d2 = new Date(2007, 7, 1);  //These don't actually give the right date
		//Alex: What is the input 'delivery' - I just put that as pDay for now
		//dB.addClient("Individual", 4, "Karen", "Yin", "Female", d, "Letter Lane", 632, "Marietta", "GA", 73627, "666-292-3988", "Pell Grant", d2, 4);
	}
}
