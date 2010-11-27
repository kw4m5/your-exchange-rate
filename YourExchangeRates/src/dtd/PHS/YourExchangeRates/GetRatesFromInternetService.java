package dtd.PHS.YourExchangeRates;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class GetRatesFromInternetService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() 
	{
		new GetRatesFromInternetTask().execute(getApplicationContext().getString(R.string.DataURL));
	}
	
	private class GetRatesFromInternetTask extends AsyncTask<String, Void, Void> {
		private String inputDate;
		private ArrayList<String> currencies;
		private ArrayList<Double> rates;
		private Context context;
		

		public GetRatesFromInternetTask() {
			this.context = GetRatesFromInternetService.this.getApplicationContext();
			
		}
		protected Void doInBackground(String... URLString) {
			try {
				this.getRatesFromInternet(URLString[0]);
				this.updateRates();
				MyPreference.setLastOnlineDate(context,MyUtility.getDateToday());
			} catch (Exception e) {			
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Store the exchange rate table (of USD-Chosen as standard) locally to access next time
		 * @param mainCurrency
		 */
		private void updateRates() {
			try {
				OutputStreamWriter out = new OutputStreamWriter(
						context.openFileOutput(context.getString(R.string.StorageFileName),Context.MODE_PRIVATE));
				out.write(this.inputDate+"\n");
				for(int i = 0 ; i < this.rates.size() ; i++) {
					out.write(this.currencies.get(i) + "\t");
					out.write(Double.toString(this.rates.get(i)) + "\n");
				}
				out.flush();
				out.close();
			} catch (IOException e) {

			}

		}
		private void getRatesFromInternet(String URLString) throws Exception {

			ExchangeRatesCollector collector = new ExchangeRatesXMLParser(URLString);
			this.inputDate = collector.getDate();
			this.currencies = collector.getCurrencies();
			this.rates = collector.getRates();		

		}

		@Override
		protected void onPostExecute (Void para) {
			MyUtility.showToast(this.context, context.getString(R.string.UpdateComplete_PleaseRefresh));
			GetRatesFromInternetService.this.stopSelf();
		}
	}

}
