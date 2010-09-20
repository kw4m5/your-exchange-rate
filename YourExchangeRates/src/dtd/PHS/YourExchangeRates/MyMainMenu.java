package dtd.PHS.YourExchangeRates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MyMainMenu {
	
	Context context;
	public MyMainMenu(Context context) {
		this.context = context;
	}

	public void createOptionsMenu(Menu menu) {
		MenuInflater inflater = ((Activity)context).getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);		
	}
	
	public boolean onItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
//		case R.id.menu_about_us:
//			intent = new Intent(this.context,About.class);
//			context.startActivity(intent);
//			return true;
		case R.id.menu_exit:
			MyUtility.switchToHomeScreen((Activity)context);
			return true;
		case R.id.menu_set_main_currency:
			intent = new Intent(this.context,ChooseMainCurrency.class);
			((Activity)context).startActivityForResult(intent,MyUtility.REQ_RESTART_DST_ACTIVITY);
			return true;
		case R.id.menu_set_precision:
			intent = new Intent(this.context,ChoosePrecision.class);
			((Activity)context).startActivityForResult(intent,MyUtility.REQ_RESTART_DST_ACTIVITY);
			return true;
		case R.id.menu_add_remove_currencies:
			intent = new Intent(this.context,ChooseCurrenciesToShow.class);
			((Activity)context).startActivityForResult(intent,MyUtility.REQ_RESTART_DST_ACTIVITY);
			return true;
		default: return false;
		}
	}
}
