
public class FoodPantry {

	//Alex: ALL OF THESE NEED VARIABLES AS THEIR PARAMETERS
	public static void main(String[] args) {
		Database dB = new Database();
		dB.login("mFilly", "ehsie4n2");
		dB.viewPickups(5);
		//These are commented out because they are inserting which gives an error if the row already exists
		//System.out.println(dB.confirmPickup(2, 1) + " rows affected.");
		//dB.dropoff("Potatoes", "Kroger", 150);
		
	}
}
