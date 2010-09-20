package dtd.PHS.YourExchangeRates;

import android.app.ListActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChoosePrecision extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_precision);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		String[] decPrecisions = this.getResources().getStringArray(R.array.DecimalPrecisions);
		setListAdapter(new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_single_choice,
				decPrecisions));
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
			int precision = Integer.parseInt((String)(parent.getItemAtPosition(position)));
			MyPreference.setDecimalPrecision(ChoosePrecision.this, precision);
			ChoosePrecision.this.finish();
		}


	}
}
