package dtd.PHS.YourExchangeRates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;

public class DataProvider {
	protected String mainCurrency;


	//Map currency name to its exchange rate
	//Take extreme care about USD (chosen as standard currency)
	protected HashMap<String, MyExchangeRateToStandardCurrency> mapStr2ExchangeRate;

	protected Context context;

	private String inputDate;


	public DataProvider(Context context, String mainCurrency) throws Exception {
		this.mainCurrency = mainCurrency;
		this.context = context;
		mapStr2ExchangeRate = new HashMap<String, MyExchangeRateToStandardCurrency>();

		//This rate doesn't depends on which currency is chosen to be main currency
		//Standard currency is the currency is chosen to download rates from x-rates.com
		mapStr2ExchangeRate.put(context.getString(R.string.ChoosenAsStandardCurrency),new MyExchangeRateToStandardCurrency(1.0));

		if ( MyUtility.connectedToInternet(context) ) {
			String lastOnlineDate = MyPreference.getLastOnlineDate(context);
			if ( lastOnlineDate == null) {
				MyPreference.setLastOnlineDate(context, MyUtility.getDateFromRawFile(context));
				this.getRatesFromRaw();
			} else {
				if (MyUtility.isDataUpdated(context, lastOnlineDate)) {
					this.getRatesFromStorage();
				} else {
					if ( MyUtility.existPastRecord(context)) {
						DataProvider.this.getRatesFromStorage();
					} else DataProvider.this.getRatesFromRaw();
					context.getApplicationContext().startService(new Intent(context.getApplicationContext(),GetRatesFromInternetService.class));
				}
			}
			//			if (MyUtility.isDataUpdated(context,MyPreference.getLastOnlineDate(context))) {
			//				getRatesFromStorage();
			//			} else {
			//				getRatesFromInternet(context.getString(R.string.DataURL));
			//				updateRates();	
			//				MyPreference.setLastOnlineDate(context,MyUtility.getDateToday());
			//			}
		} else {
			if (MyUtility.existPastRecord(context)) {
				getRatesFromStorage();
			} else {
				getRatesFromRaw();
			}
		}
	}



	public String getInputDate() {
		return this.inputDate;
	}

	private void getRatesFromRaw() {
		try {
			InputStream staticIS = context.getResources().openRawResource(R.raw.rates);
			readFromInputStream(staticIS);
			staticIS.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}


	}

	private void readFromInputStream(InputStream istream) throws IOException {
		InputStreamReader input = new InputStreamReader(istream);
		BufferedReader buffreader = new BufferedReader(input);
		this.inputDate = buffreader.readLine().trim();				

		String line;	
		//		String names = "";
		//		String fullNames = "";
		while ( (line = buffreader.readLine()) != null ) {
			String[] tempWords = line.split("[ \t\n\f\r]");
			int n = tempWords.length - 1;

			//skip empty word
			while (tempWords[n].length()==0) n--;

			String rateTo = tempWords[n--].trim();

			//skip empty word and
			while (tempWords[n].length()==0) n--;

			String currencyAbbName = tempWords[n].trim();

			//			String currencyFullName = "";
			//			for(int i = 0; i<= n; i++) {
			//				currencyFullName += tempWords[i];
			//				if (tempWords[i].length() != 0 && i!=n ) currencyFullName +=" ";
			//			}
			//			names += "\""+currencyAbbName+"\",";
			//			fullNames += "\""+currencyFullName+"\",";
			mapStr2ExchangeRate.put(currencyAbbName, 
					new MyExchangeRateToStandardCurrency(Double.parseDouble(rateTo))
			);
		}

		//		if ( names.length()== 0 ) { names="null"; } //tool code

	}


	private void getRatesFromStorage() {
		try {
			InputStream in = context.openFileInput(context.getString(R.string.StorageFileName));
			readFromInputStream(in);
			in.close();
		} catch (IOException e) {
			getRatesFromRaw();
		}
	}

	/**
	 * Get the exchange rate from currency cFrom to currency cTo
	 * @param cFrom
	 * @param cTo
	 * @return
	 */
	public double getExchangeRate(String cFrom,String cTo) {

		if (cFrom.equals(cTo)) return 1.0;

		MyExchangeRateToStandardCurrency rateFrom = mapStr2ExchangeRate.get(cFrom);
		MyExchangeRateToStandardCurrency rateTo = mapStr2ExchangeRate.get(cTo);
		return rateTo.getRateTo() / rateFrom.getRateTo();
	}

	public long changeFromTo(long amount, String cFrom, String cTo) {
		double rate = getExchangeRate(cFrom, cTo);
		return Math.round(amount * rate);
	}




}
