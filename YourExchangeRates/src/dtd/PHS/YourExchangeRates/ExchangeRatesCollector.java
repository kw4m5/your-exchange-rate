package dtd.PHS.YourExchangeRates;

import java.util.ArrayList;

public abstract class ExchangeRatesCollector {
	
	
	public ExchangeRatesCollector(String URLStr) {
		this.URLStr = URLStr;
	}
	public String getDate() { return this.date; }
	public ArrayList<String> getCurrencies() { return this.currencies; }
	public ArrayList<Double> getRates() { return this.rates; }
	
	protected ArrayList<String> currencies;
	protected ArrayList<Double> rates;
	protected String date;
	protected String URLStr;
}
