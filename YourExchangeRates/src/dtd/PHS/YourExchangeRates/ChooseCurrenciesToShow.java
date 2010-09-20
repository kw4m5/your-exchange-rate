package dtd.PHS.YourExchangeRates;

import android.app.ListActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseCurrenciesToShow extends ListActivity {

	private ListView listView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.choose_currencies_to_show);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,MyUtility.currenciesList));
        listView = getListView();
        //TODO: set checked states = true for all displaying currencies 
        
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        this.setCheckboxStates();
	}
	
	private void setCheckboxStates() {
		String[] currenciesDisplaying = MyPreference.getCurrenciesToShow(this);
		
		boolean[] currenciesMask = MyUtility.creatCurrenciesMask(currenciesDisplaying);
		
		
		for(int i = 0 ; i < currenciesMask.length ; i++) {
			if (currenciesMask[i] == true) 
				this.listView.setItemChecked(i, true);
			else this.listView.setItemChecked(i, false);
		}
	
	}

	public void onSaveButtonClickHandler(View view) {
		SparseBooleanArray checkedMask = listView.getCheckedItemPositions();
		String chosenCurrencies = "";
		for(int i =0; i < MyUtility.currenciesList.length ; i++) {
			if (checkedMask.get(i) == true) {
				chosenCurrencies += MyUtility.currenciesList[i] + ",";
			}
		}
//		//DONE: DEBUG code - to remove !
//		int num = 1;
//		int num2 = num + 1;
		
		String[] currenciesToShow = MyUtility.splitToWords(chosenCurrencies,",");
		MyPreference.setCurrenciesToShow(this, currenciesToShow);
		ChooseCurrenciesToShow.this.finish();
		
	}

}
