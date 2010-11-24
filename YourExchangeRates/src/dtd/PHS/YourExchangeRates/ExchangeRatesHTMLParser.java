package dtd.PHS.YourExchangeRates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ExchangeRatesHTMLParser extends ExchangeRatesCollector {

	public ExchangeRatesHTMLParser(String URLStr) throws IOException {
		super(URLStr);
		URL internetURL = new URL(this.URLStr);
		BufferedReader in = new BufferedReader(new InputStreamReader(internetURL.openStream()));
		String inputLine="ABC";
		while (inputLine != null) {
			inputLine = in.readLine();
			if (inputLine.contains(("Cube"))) {
				break;
			}
		}

		char markChar = '\'';
		//Inputdate line
		inputLine = in.readLine();
		this.date = MyUtility.changeDateFormat(inputLine.substring(inputLine.indexOf(markChar)+1, inputLine.lastIndexOf(markChar)));

		//		String nameCurrencies = "";
		this.currencies = new ArrayList<String>();
		this.rates = new ArrayList<Double>();
		while (inputLine != null) {
			inputLine = in.readLine();

			//No more info needed
			if (inputLine.contains("</Cube")) break;

			int bpos = inputLine.indexOf(markChar);
			int epos = inputLine.indexOf(markChar,bpos+1);
			String currency = inputLine.substring(bpos+1, epos);
			this.currencies.add(currency);

			//			nameCurrencies +=currency+",";
			bpos = inputLine.indexOf(markChar, epos+1);
			epos = inputLine.indexOf(markChar,bpos+1);
			String rateStr = inputLine.substring(bpos+1, epos);
			this.rates.add(Double.parseDouble(rateStr));

//			mapStr2ExchangeRate.put(currency, new MyExchangeRateToStandardCurrency(Double.parseDouble(rateStr)));
		}
		in.close();

	}

}
