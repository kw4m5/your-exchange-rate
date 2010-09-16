package dtd.PHS.YourExchangeRates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class provides functions/data ... at global level
 * @author Pham Hung Son
 *
 */
public class MyUtility {

	public static final int DATE_FORMAT_LONGER_FIRST = 0;
	public static final int DATE_FORMAT_SHORTER_FIRST = 1;
	public static final int REQ_RESTART_DST_ACTIVITY = 0;
	public static final int REQ_FINISH_SRC_ACTIVITY = 1;
	public static String[] currenciesList = {
		"USD","EUR","JPY",
		"AUD","BGN","BRL",
		"CAD","CHF","CNY",
		"CZK","DKK","EEK",
		"GBP","HKD","HRK",
		"HUF","IDR","INR",
		"KRW","LTL","LVL",
		"MXN","MYR","NZD",
		"NOK","PHP","PLN",
		"RON","RUB","SEK",
		"SGD","THB","TRY","ZAR"};
	public static HashMap<String, String> mapAbb2FullCurrencyName;
	public static HashMap<String, String> mapFull2AbbCurrencyName;
	public static HashMap<String, String> mapNameMonth2NumMonth;
	private static String[] monthNames = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	public static String[] tempCurrenciesFullNameList = {
		"United States - Dollars","Euro - Euro","Japan - Yen","Bulgaria - Leva","Czech Republic - Koruny",
		"Denmark - Kroner","Estonia - Krooni","Great Britain - Pounds","Hungary - Forint",
		"Lithuania - Litai","Latvia - Lati","Poland - Zlotych","Romania - New Lei",
		"Sweden - Kronor","Switzerland - Francs","Norway - Kroner","Croatia - Kuna",
		"Russia - Rubles","Turkey - New Lira","Australia - Dollars","Brazil - Real",
		"Canada - Dollars","China - Yuan Renminbi","Hong Kong - Dollars","Indonesia - Rupiahs",
		"India - Rupees","South Korea - Won","Mexico - Pesos","Malaysia - Ringgits",
		"New Zealand - Dollars","Philippines - Pesos","Singapore - Dollars","Thailand - Baht","South Africa - Rand"
	};
	public static String[] tempCurrenciesList = {
		"USD","EUR","JPY","BGN","CZK",
		"DKK","EEK","GBP","HUF",
		"LTL","LVL","PLN","RON",
		"SEK","CHF","NOK","HRK",
		"RUB","TRY","AUD","BRL",
		"CAD","CNY","HKD","IDR",
		"INR","KRW","MXN","MYR",
		"NZD","PHP","SGD","THB","ZAR"};
	public static String[] passData_Calculate2PreviewSMS_currencies;
	public static String[] passData_Calculate2PreviewSMS_amount;
	public static String passData_Calculate2PreviewSMS_inputDate;
	static {
		mapAbb2FullCurrencyName = new HashMap<String, String>();
		mapFull2AbbCurrencyName = new HashMap<String, String>();
		for(int i = 0; i < tempCurrenciesList.length ; i++) {
			mapAbb2FullCurrencyName.put(tempCurrenciesList[i], tempCurrenciesFullNameList[i].trim());			
			mapFull2AbbCurrencyName.put(tempCurrenciesFullNameList[i].trim(),tempCurrenciesList[i]);
		}
		
		mapNameMonth2NumMonth = new HashMap<String, String>();
		for(int i = 0; i < 12 ; i++) {
			String digit = Integer.toString(i+1);
			if ( digit.length() == 1 ) digit = "0"+digit;
			mapNameMonth2NumMonth.put(monthNames[i].toUpperCase(),digit);
		}
	}
	
	public static void broadcastSMSIntent(Context context, String content) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body", content); 
		sendIntent.setType("vnd.android-dir/mms-sms");
		context.startActivity(sendIntent);		
	}

	public static void callOnEveryActivity(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public static boolean connectedToInternet(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo info = conMgr.getActiveNetworkInfo();  
		if ( info != null && info.isConnected() ) {
			return true;
		} else	return false;
	}

	
	public static Date convertStringToDate(String dateStr) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			 date = dateFormat.parse(dateStr);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return date;
	}

	public static int countDaysBetween(String before, String after) {
		Date bDate = convertStringToDate(before);
		Date aDate = convertStringToDate(after);
		
		long diff = aDate.getTime() - bDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return (int)diffDays;
	}

	public static String createSMSBodyForRate(double rate, String currency,Context context) {
		String content = "";
		String mainCurrency = MyPreference.getMainCurrency(context);
		String strRateTo = Double.toString(rate);
		String strRateIn = Double.toString(1.0/rate);
		int precision = MyPreference.getDecimalPrecision(context);
		
		content = "1 "+currency+" = "+ formatRate(strRateTo,precision) +" "+mainCurrency+"\n";
		content += "1 "+mainCurrency+" = "+ formatRate(strRateIn,precision) +" "+currency+"\n";
		return content;
	}


	public static String createSMSContent(Context context, String currency,
			double rate, String date) {
		String content = 
			context.getString(R.string.SMS_Title)+ " " + date + "\n" +
			createSMSBodyForRate(rate,currency,context);
		
		return content;
	}

	/**
	 * Return true/false depends on existence of an archived file 
	 * stores exchange rate in the past
	 * @param context
	 * @return
	 */
	public static boolean existPastRecord(Context context) {
		if ( MyPreference.getLastOnlineDate(context) == null) 
			return false;
		return true;
	}
	
	public static void forceActivityRestart(Activity act) {
		act.startActivity(act.getIntent()); 
		act.finish();	
	}
	
	public static String formatIntCurrency(String amount) {
		amount = getRidOfComma(amount);
		int cnt = 0;
		String x = new String("");
		for(int i = amount.length()-1; i>=0 ; i--) {
			cnt++;
			x = amount.charAt(i)+x;
			if ( cnt == 3 && i != 0) {
				cnt = 0;			
				x = ','+x ;
			}
		}
		return x;
	}
	
	public static String formatRate(String stringNum, int prec) {
		double rate = Double.parseDouble(stringNum);
		int pow10 = (int)Math.pow(10, prec);
		int intPart = (int)rate;
		int fracPart = Math.round((float)((rate-intPart)*pow10));
		String strFrac = Integer.toString(fracPart);
		if (strFrac.length() < prec) {
			char[] addMore = new char[prec-strFrac.length()];
			Arrays.fill(addMore, '0');
			strFrac = new String(addMore) + strFrac;
		}
		String numStr = formatIntCurrency(Integer.toString(intPart)) + "." + strFrac;
		return numStr;
	}

	/**
	 * Return all currencies in abbreviation form (e.g: USD,EUR...)
	 * @return
	 */
	public static String[] getCurrencies() {
		return currenciesList;
	}

	/**
	 * Return all currencies in abbreviation form (e.g: USD,EUR....) except specialCurrency
	 * @param specialCurrency
	 * @return
	 */
	public static String[] getCurrenciesExcept(String specialCurrency) {
		String[] list = new String[currenciesList.length-1];
		int i = 0;	
		for(String currency : currenciesList) {
			if ( currency.equals(specialCurrency)) continue;
			list[i++] = currency;
		}
		return list;
	}

	public static String getDateToday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(calendar.getTime());
        return today;
	}
	
	static public String getRidOfComma(String numberStr) {	
		return numberStr.replaceAll(",", "");
	}

	/**
	 * Get the resource ID for the main currency, 
	 * e.g: R.id.vnd_row_item for VND
	 * @param mainCurrency
	 * @return
	 */


	public static String getURL(Context context,String standardCurrency) {
		String URL = context.getString(R.string.DataURL) +standardCurrency+"/table.html";
		//http://www.x-rates.com/d/USD/table.html
		return URL;
	}

	/**
	 * Whether data is updated (from Internet) in the past few days.
	 * The number of days is configured by preference: PREF_NumDaysToUpdate
	 * @param context
	 * @return
	 */
	public static boolean isDataUpdated(Context context,String inputDate) {
		if (inputDate == null) return false;
		String dateToday = MyUtility.formatDate(MyUtility.getDateToday(),MyUtility.DATE_FORMAT_LONGER_FIRST);
		inputDate = MyUtility.formatDate(inputDate, MyUtility.DATE_FORMAT_LONGER_FIRST);
		boolean updated = (inputDate.compareTo(dateToday) < 0 ? false:true);
		return updated;
	}

	/**
	 * Format date from form:
	 *  dd/MM/yyyy -> yyyy/MM/dd  (with FORMAT == DATE_FORMAT_SHORTER_FIRST) 
	 * 	yyyy/MM/dd -> dd/MM/yyyy  (with FORMAT == DATE_FORMAT_LONGER_FIRST)
	 * @param dateToday
	 * @param dateFormatLongerFirst
	 * @return
	 */
	private static String formatDate(String dateStr, int FORMAT) {
		String[] words = dateStr.split("/");
		String day,month,year;
		
		if ( words[0].length() == 2) {
			// format: dd/MM/yyyy
			day = words[0]; year = words[2];
		} else {
			//format yyyy/MM/dd
			day = words[2]; year = words[0];
		}
		month = words[1];
		
		if (FORMAT == DATE_FORMAT_LONGER_FIRST) {
			return year+"/"+month+"/"+day;
		} else 
			if (FORMAT == DATE_FORMAT_SHORTER_FIRST ) return day+"/"+month+"/"+year;
			else return null;
	}

	public static String[] splitToWords(String inputLine) {
		String[] temp = inputLine.split("[ \t\n\f\r, ]");
		ArrayList<String> words = new ArrayList<String>();
		for(String word : temp ) {
			if ( word.length() == 0) continue;
			words.add(word);
		}
		String[] ret = new String[words.size()];
		words.toArray(ret);
		return ret;
	}
	
	public static void switchToHomeScreen(Activity activity) {
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(setIntent); 
	}

	
	
	/**
	 * Change date format from yyyy-MM-dd to dd/MM/yyyy
	 * @param dateStr
	 * @return
	 */
	public static String changeDateFormat(String dateStr) {
		String[] words = dateStr.split("-");
		String year = words[0];
		String month = words[1];
		String day = words[2];
		return day+"/"+month+"/"+year;
	}

	/**
	 * Get 2 out of the 3 major currencies: USD,EUR,JPY </br>
	 * - If one of them is mainCurrency : take 2 others, </br>
	 * - If none of them is mainCurrency: take USD,EUR
	 * @param mainCurrency
	 * @return
	 */
	public static String[] getMajorCurrencies(String mainCurrency) {
		String[] currencies = {"USD","EUR","JPY"};
		for(String currency: currencies) {
			if ( mainCurrency.equals(currency)) {
				return getAllStringsExcept(mainCurrency,currencies);
			}
		}
		String[] ret = {"USD","EUR"};
		return ret;
	}

	private static String[] getAllStringsExcept(String specialString,
			String[] otherStrings) {
		ArrayList<String> list = new ArrayList<String>();
		for(String s : otherStrings) {
			if ( !s.equals(specialString)) {
				list.add(s);
			}
		}
		String[] ret = new String[list.size()];
		list.toArray(ret);
		return ret;
	}

	/**
	 * Return whether numberStr is a long integer
	 * @param numberStr
	 * @return
	 */
	static public boolean isValidLong(String numberStr) {
		try {
			double d = Long.parseLong(numberStr);
			if ( d >= 0 ) return true;
			return false;
		} catch (NumberFormatException ne) {
			return false;
		}	
	}

	static public int newCursorPosition(int prePos,String preS,String aftS) {			
		int aftPos = prePos;			
		if (preS.length() < aftS.length()) {
			//Insert a char	
			aftPos = prePos + 1 + ((preS.length() + 1) % 4 == 0 ? 1:0);				
		} else if (preS.length() > aftS.length()){
			//Delete a char by Backspace			
			aftPos = prePos - 1;
			if ( prePos == 1) return 0;
			aftPos = aftPos - ((preS.length() - 1) % 4 == 0 ? 1:0);				
		}				
		//Only the case insert/delete at the end
		//	prove why is this case enough
		return aftPos;
	}

	public static String[] getCurrenciesAndFullNames() {
		String[] ret = currenciesList;
		//TODO: DEBUB here to see if currenciesList is changed ? !!!!
		for(int i =0; i < ret.length ; i++) {
			ret[i] += " : "+mapAbb2FullCurrencyName.get(ret[i]);
		}
		return ret;
	}

	/**
	 * Return the first word in a string, words are seperated by space(s)
	 * @param str
	 * @return
	 */
	public static String getFirstWord(String str) {
		String firstWord;
		String[] words = str.split(" ");
		int i = 0;
		while (words[i].length() == 0 ) i++;
		firstWord = words[i];
		return firstWord;
	}
}