package cccp;

public class PercentageDiscount implements Discount{
	
	private final double percentage;
	
	public PercentageDiscount(double percentage) {
		this.percentage = percentage;
	}

	@Override
	public double apply(double totalPrice) {
		// TODO Auto-generated method stub
		return totalPrice - (totalPrice * (percentage/100));
	}

}
