public class Client {
	int BagID, pickupDay, clientID, finAidID;
	String firstName, lastName, Gender, DoB, street, apartmentNum, city, state, zip, telephoneNum, startDate, delivery;

	public Client(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;

		// BEST PRACTICES BE DAMNED
	}

	public Client(int BagID, int pickupDay, String firstName, String lastName, String Gender, String DoB, String street, String apartmentNum, String city,
			String state, String zip, String telephoneNum, int finAidID, String startDate, String delivery, int clientID) {
		this.BagID = BagID;
		this.pickupDay = pickupDay;
		this.firstName = firstName;
		this.lastName = lastName;
		this.Gender = Gender;
		this.DoB = DoB;
		this.street = street;
		this.apartmentNum = apartmentNum;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.telephoneNum = telephoneNum;
		this.finAidID = finAidID;
		this.startDate = startDate;
		this.delivery = delivery;
		this.clientID = clientID;
	}

	public String toString() {
		return firstName + " " + lastName;
	}
}
