package dtd.PHS.YourExchangeRates;
import android.content.Context;
import android.content.SharedPreferences;

abstract public class MyPreference {

	/**
	 *	Provides main currency (e.g: VND,EUR,USD ...), which is set 
	 *	by the first time start-up or in the Menu > Preferences 
	 * @return
	 */
	static public String getMainCurrency(Context context ) {
		String mainCurrency = getPreference(context, context.getString(R.string.PREF_MainCurrency));
		if (mainCurrency == null) mainCurrency = context.getString(R.string.DefaultMainCurrency);
		return mainCurrency;
	}

	/**
	 * Set preference prefName with data 
	 * @param prefName preference name
	 * @param data data to be stored
	 */
	public static void setPreference(Context context,String prefName, String data) {
		SharedPreferences settings = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(prefName, data);
		editor.commit();
	}

	/**
	 * Get the preference data
	 * @param context
	 * @param prefName preference name
	 * @return data stored in preference, or null if data is not found
	 */
	public static String getPreference(Context context,String prefName) {
		SharedPreferences settings = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		String prefData = settings.getString(prefName, null);	
		return prefData;		
	}

	/**
	 * Store the last online date
	 * @param context
	 * @param aDate
	 */
	public static void setLastOnlineDate(Context context, String aDate) {
		setPreference(
				context,
				context.getString(R.string.PREF_LastOnlineDate),
				aDate);
	}
	
	/**
	 * Return the last online date with the format dd/MM/yyyy - if no date is stored, return null
	 * @param context
	 * @return
	 */
	public static String getLastOnlineDate(Context context) {
		return getPreference(context, context.getString(R.string.PREF_LastOnlineDate));
	}

	/**
	 * Get the decimal precision of the rate shown in activity ListRates
	 * @return
	 */
	public static int getDecimalPrecision(Context context) {
		String strPrec = getPreference(context, context.getString(R.string.PREF_DecimalPrecesion));
		if ( strPrec == null ) {
			strPrec = context.getString(R.string.DefaultPrecision);
			setPreference(context, context.getString(R.string.PREF_DecimalPrecesion), strPrec);
		}
		return Integer.parseInt(strPrec);

	}
	
	public static void setDecimalPrecision(Context context,int prec) {
		setPreference(context, context.getString(R.string.PREF_DecimalPrecesion), Integer.toString(prec));
	}

	public static void setFirstTimeRunning(Context context,
			String data) {
		setPreference(context, context.getString(R.string.PREF_FirstTimeRunning), data);
	}

	public static String getFirstTimeRunning(Context context) {
		return getPreference(context, context.getString(R.string.PREF_FirstTimeRunning));			
	}

	public static void setMainCurrency(
			Context context,String mainCurrency) {
		setPreference(context, context.getString(R.string.PREF_MainCurrency), mainCurrency);
	}

	public static long getNumDaysToUpdate(Context context) {
		String daysStr = getPreference(context, context.getString(R.string.PREF_NumDaysToUpdate));
		if ( daysStr == null ) daysStr = context.getString(R.string.Default_NumDaysToUpdate);
		return Integer.parseInt(daysStr);
	}
}
