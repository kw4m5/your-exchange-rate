package dtd.PHS.YourExchangeRates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;

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
			if (MyUtility.isDataUpdated(context,MyPreference.getLastOnlineDate(context))) {
				getRatesFromStorage();
			} else {
				getRatesFromInternet(context.getString(R.string.DataURL));
				updateRates();	
				MyPreference.setLastOnlineDate(context,MyUtility.getDateToday());
			}
		} else {
			if (MyUtility.existPastRecord(context)) {
				getRatesFromStorage();
			} else {
				getRatesFromRaw();

			}
		}
	}

	/**
	 * Store the exchange rate table (of USD-Chosen as standard) locally to access next time
	 * @param mainCurrency
	 */
	private void updateRates() {
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					context.openFileOutput(context.getString(R.string.StorageFileName),Context.MODE_PRIVATE));
			out.write(this.getInputDate()+"\n");
			for(String currency: MyUtility.getCurrenciesExcept(context.getString(R.string.ChoosenAsStandardCurrency))) {
				out.write(currency+"\t");
				MyExchangeRateToStandardCurrency rate = mapStr2ExchangeRate.get(currency);
				out.write(Double.toString(rate.getRateTo()) + "\n");				
			}
			out.flush();
			out.close();			

		} catch (IOException e) {

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
	private void getRatesFromInternet(String URLString) throws Exception {
		URL internetURL = new URL(URLString);
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
		this.inputDate = MyUtility.changeDateFormat(inputLine.substring(inputLine.indexOf(markChar)+1, inputLine.lastIndexOf(markChar)));

		//		String nameCurrencies = "";
		while (inputLine != null) {
			inputLine = in.readLine();

			//No more info needed
			if (inputLine.contains("</Cube")) break;

			int bpos = inputLine.indexOf(markChar);
			int epos = inputLine.indexOf(markChar,bpos+1);
			String currency = inputLine.substring(bpos+1, epos);

			//			nameCurrencies +=currency+",";
			bpos = inputLine.indexOf(markChar, epos+1);
			epos = inputLine.indexOf(markChar,bpos+1);
			String rateStr = inputLine.substring(bpos+1, epos);

			mapStr2ExchangeRate.put(currency, new MyExchangeRateToStandardCurrency(Double.parseDouble(rateStr)));
		}

		in.close();

	}

	//		//Get the connection, throws MalformURLException
	//		URL internetURL = new URL(URLString);
	//		BufferedReader in = new BufferedReader(new InputStreamReader(internetURL.openStream()));
	//		String inputLine="ABC";
	//		String previousLine;
	//
	//		//Skip the content until the beginning of rates table
	//		String regBeginTableRates = context.getString(R.string.BeginRecogTableRates);
	//		while ( ! (inputLine.trim()).equals(regBeginTableRates) ) {
	//			inputLine = in.readLine();
	//		}
	//
	//		//Begin reading the rates
	//		while (inputLine != null) {
	//			previousLine = inputLine;
	//			inputLine = in.readLine();
	//			if ( this.containCurrencyInfo(inputLine) ) {
	//				String nextLine = in.readLine();
	//				this.extractCurrencyInfo(previousLine,inputLine,nextLine);
	//
	//				//skip the next 2 lines for performance reason
	//				nextLine = in.readLine();
	//				nextLine = in.readLine();
	//				inputLine = in.readLine();
	//
	//			} else 
	//				if ( this.containInputDate(previousLine)) {
	//					this.extractInputDate(previousLine);
	//					in.close();
	//					break; //We don't need anymore information
	//				}
	//
	//
	//
	//		}
	//		in.close();

	//	}
	//
	//	private void extractCurrencyInfo(String previousLine, String currentLine,
	//			String nextLine) {
	//		String HTMLSpace = context.getString(R.string.HTMLSpace);
	//
	//		//previousLine: contains the currency name
	//		int beginPos = previousLine.indexOf(HTMLSpace) + HTMLSpace.length();
	//		int endPos = previousLine.lastIndexOf(HTMLSpace);
	//		String currencyFullName = previousLine.substring(beginPos, endPos);
	//
	//		//currentLine: contains the "To" exchange rate
	//		String recogExchangeRate = context.getString(R.string.RecogCurrencyExchange);
	//		beginPos = currentLine.indexOf(recogExchangeRate)+recogExchangeRate.length();
	//		endPos = currentLine.lastIndexOf("</a>");
	//		String toRateStr = currentLine.substring(beginPos,endPos);
	//		String abbCurrencyName = MyUtility.mapFull2AbbCurrencyName.get(currencyFullName);
	//		this.mapStr2ExchangeRate.put(
	//				abbCurrencyName, 
	//				new MyExchangeRate(Double.parseDouble(toRateStr))
	//		);
	//
	//	}
	//
	//
	//	private boolean containCurrencyInfo(String inputLine) {
	//		String regCurr = context.getString(R.string.RecogCurrencyExchange);
	//		return inputLine.contains(regCurr);		
	//	}
	//
	//
	//	private void extractInputDate(String inputLine) {
	//		String regStringBegin = context.getString(R.string.BeginRegcoStringOfDate);
	//		String regStringEnd = context.getString(R.string.EndRegcoStringOfDate);
	//		String rawStringDate = inputLine.substring(
	//				inputLine.indexOf(",", inputLine.indexOf(regStringBegin))+1,
	//				inputLine.indexOf(regStringEnd,inputLine.indexOf(regStringBegin))).trim();
	//
	//		String[] words = MyUtility.splitToWords(rawStringDate);
	//
	//		String month = MyUtility.mapNameMonth2NumMonth.get(words[0].toUpperCase());
	//
	//		String day = words[1];
	//		if (day.length() == 1 ) day = "0" + day;
	//
	//		String year = words[2];
	//
	//		this.inputDate=day+"/"+month+"/"+year;
	//	}
	//
	//
	//	private boolean containInputDate(String aline) {
	//		return aline.contains(context.getString(R.string.BeginRegcoStringOfDate));
	//	}
	//

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
