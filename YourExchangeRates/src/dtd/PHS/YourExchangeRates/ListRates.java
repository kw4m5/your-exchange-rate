package dtd.PHS.YourExchangeRates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListRates extends Activity {

	private static final int CONTEXTMENU_SENDSMS = 0;
	ListView mainList;
	DataProvider dataProvider;
	MyMainMenu mainMenu;
	MyPreference preference;
	String mainCurrency;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_rates);
		try {      	
			this.mainCurrency = MyPreference.getMainCurrency(this);
			this.dataProvider =new DataProvider(this,mainCurrency);
			this.mainMenu = new MyMainMenu(this); 
			MyUtility.callOnEveryActivity(this);

			this.initTextViews();

			this.mainList = (ListView)findViewById(R.id.lvMainList);
			RowAdapter adapter = 
				new RowAdapter( this, 
						R.layout.row_item,
						MyUtility.getCurrenciesExcept(mainCurrency));

			this.mainList.setAdapter(adapter);
			this.mainList.setOnCreateContextMenuListener(new MyOnCreateContextMenuListener());
		} catch (Exception e) {
			//TODO: handle exception !
			String eMess = e.getMessage();
			System.out.println(eMess);
		}

	}

	private void initTextViews() {
		TextView tvInputDate = (TextView)findViewById(R.id.tvLRInputDate);
		tvInputDate.setText(" "+dataProvider.getInputDate()+":");
	}

	public void onClickECBHandler(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(this.getString(R.string.WebDataURL)) );
		startActivity(intent);
	}
	
	
	//On click bottom icon handlers
	public void onClickConverterHandler(View view) {
		Intent intent = new Intent(this,CalculateRate.class);
		this.startActivity(intent);
	}
	public void onClickSMSHandler(View view) {
		Intent intent = new Intent(this,SendSMS.class);
		this.startActivity(intent);
		
	}
	public void onClickAboutHandler(View view) {
		Intent intent = new Intent(this,About.class);
		this.startActivity(intent);

	}
	

	
	public boolean onCreateOptionsMenu(Menu menu) {
		mainMenu.createOptionsMenu(menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		return mainMenu.onItemSelected(item);	
	}
	
	/**
	 * At the moment, this method only force activity to restart (after some preference changed in menu)
	 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	switch (requestCode) {
    	case MyUtility.REQ_RESTART_DST_ACTIVITY:
    		MyUtility.forceActivityRestart(this);
    		break;
    	default: break;
    	}
    }

	private class RowAdapter extends ArrayAdapter<String> {

		public RowAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RowItem v = (RowItem)convertView;
			if ( v == null ) {				
				v = new RowItem(getContext());
			}
			String currency = getItem(position);
			//			if (currency.equals("JPY")) {
			//				currency = getItem(position);
			//			}
			v.setInfo(currency,ListRates.this.mainCurrency);
			double rate = 
				ListRates.this.
				dataProvider.getExchangeRate(ListRates.this.mainCurrency,currency);
			v.setRates(rate);
			return v;

		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem aItem) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
		switch (aItem.getItemId()) {
		case CONTEXTMENU_SENDSMS:
			String currency = (String)(this.mainList.getAdapter().getItem(menuInfo.position));
			String content = MyUtility.createSMSContent(
					this, 
					currency, 
					this.dataProvider.getExchangeRate(currency,this.mainCurrency), 
					this.dataProvider.getInputDate());
			MyUtility.broadcastSMSIntent(this, content);
			return true;
		}
		return false;
	}
	private class MyOnCreateContextMenuListener implements OnCreateContextMenuListener {



		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			String currency = (String)(info.targetView.getTag());
			menu.setHeaderTitle(v.getContext().getString(R.string.ListRate_ContextMenuTitle) + " " + currency);
			menu.add(
					0, CONTEXTMENU_SENDSMS, 
					0 , v.getContext().getString(R.string.ShareSMS));
		}

	}
}