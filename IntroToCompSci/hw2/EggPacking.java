class EggPacking {
	public static void main(String[] args) {
		int dozenEggs = 12;
		double priceOfCartons = 0.31;
		int numOfEggs = Integer.parseInt(args[0]);
		int numOfCartons = numOfEggs/dozenEggs;
		double totalPrice = numOfCartons * priceOfCartons;
		int numOfEggsInCartons = numOfCartons * dozenEggs;
		int remainingEggs = numOfEggs - numOfEggsInCartons;
		System.out.println(numOfEggsInCartons + " were packing into " + numOfCartons + " cartons");
		System.out.println("There were " + remainingEggs + " leftover eggs");
		System.out.println("The total amount spent on egg cartons: $" + totalPrice);
	}
}