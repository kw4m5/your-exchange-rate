package dtd.PHS.YourExchangeRates;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FirstScreen extends Activity {

	/** Called when the activity is first created. */
	private ListView lvChoose;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    String firstTime = MyPreference.getFirstTimeRunning(this);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    if ( firstTime == null ) {
	    	MyPreference.setFirstTimeRunning(this,"1");
	       	
	    	setContentView(R.layout.first_screen);
	    	
	    	lvChoose = (ListView)findViewById(R.id.lvChooseMainCurrency);
	    	lvChoose.setItemsCanFocus(false);
	    	lvChoose.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,MyPreference.getCurrenciesToShow(this)));
	    	lvChoose.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    	lvChoose.setOnItemClickListener(new MyOnItemClickListener());
	    } else {
	    	//Not the first time
	    	Intent intent = new Intent(this,ListRates.class);
	    	this.startActivity(intent);	
	    	this.finish();
	    }
	}
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String currency = (String) parent.getItemAtPosition(position);
			MyPreference.setMainCurrency(FirstScreen.this,currency);
			Intent intent = new Intent(FirstScreen.this,ListRates.class);
			FirstScreen.this.startActivity(intent);
			FirstScreen.this.finish();
		}
		
	}
}
