package dtd.PHS.YourExchangeRates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class About extends Activity {

	private MyMainMenu mainMenu;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
	    MyUtility.callOnEveryActivity(this);
	    setContentView(R.layout.about);
	    mainMenu = new MyMainMenu(this);
	}
	
	public void onClickIBListRateHandler(View view) {
		Intent intent = new Intent(this,ListRates.class);
		this.startActivity(intent);
	}
	public void onClickConverterHandler(View view) {
		Intent intent = new Intent(this,CalculateRate.class);
		this.startActivity(intent);
	}
	public void onClickSMSHandler(View view) {
		Intent intent = new Intent(this,SendSMS.class);
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
}
