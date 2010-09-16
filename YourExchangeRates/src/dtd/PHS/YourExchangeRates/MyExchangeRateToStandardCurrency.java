package dtd.PHS.YourExchangeRates;

public class MyExchangeRateToStandardCurrency {
	double rate;
	public MyExchangeRateToStandardCurrency(double rateTo) {
		this.rate = rateTo;
	}
	
	public double getRateTo() {
		return this.rate;
	}
	
	public double getRateIn() {
		return ((double)1.0)/this.rate;
	}

}
