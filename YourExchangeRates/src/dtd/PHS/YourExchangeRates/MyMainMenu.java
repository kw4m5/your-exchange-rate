package dtd.PHS.YourExchangeRates;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MyMainMenu {
	
	Activity activity;
	public MyMainMenu(Activity act) {
		this.activity = act;
	}

	public void createOptionsMenu(Menu menu) {
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);		
	}
	
	public boolean onItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
			
//		case R.id.menu_about_us:
//			intent = new Intent(this.context,About.class);
//			context.startActivity(intent);
//			return true;
		case R.id.menu_refresh:
			activity.startActivityForResult(activity.getIntent(), MyUtility.REQ_RESTART_DST_ACTIVITY);
			return true;
		case R.id.menu_exit:
			MyUtility.switchToHomeScreen((Activity)activity);
			return true;
		case R.id.menu_set_main_currency:
			intent = new Intent(this.activity,ChooseMainCurrency.class);
			activity.startActivityForResult(intent,MyUtility.REQ_RESTART_DST_ACTIVITY);
			return true;
		case R.id.menu_set_precision:
			intent = new Intent(this.activity,ChoosePrecision.class);
			activity.startActivityForResult(intent,MyUtility.REQ_RESTART_DST_ACTIVITY);
			return true;
		case R.id.menu_add_remove_currencies:
			intent = new Intent(this.activity,ChooseCurrenciesToShow.class);
			activity.startActivityForResult(intent,MyUtility.REQ_RESTART_DST_ACTIVITY);
			return true;
		default: return false;
		}
	}
}
