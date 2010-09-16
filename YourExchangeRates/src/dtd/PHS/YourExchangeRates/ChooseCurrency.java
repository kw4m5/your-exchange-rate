package dtd.PHS.YourExchangeRates;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseCurrency extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.choose_main_currency);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, MyUtility.getCurrencies()));

        final ListView listView = getListView();

        listView.setItemsCanFocus(false);
        
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        MyOnItemClickListener listener = new MyOnItemClickListener();
        listView.setOnItemClickListener(listener);
	}
	
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String currency = (String)parent.getItemAtPosition(position);
			MyPreference.setMainCurrency(ChooseCurrency.this, currency);
			ChooseCurrency.this.finish();
		}

	
	}

}
