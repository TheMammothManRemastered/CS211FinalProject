public class Main {

	public static void main(String[] args) {
		System.out.println("this is simply a test of architecture");
		System.out.println("and this is a test of transferability");
		Bain.minecraft();

		try {
			Floor floor = new Floor(20,20);
			floor.printFloor();
		} catch (FloorInitializationException e) {
			e.printStackTrace();
		}

	}

}
