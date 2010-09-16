package dtd.PHS.YourExchangeRates;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class PreviewSMS extends Activity {

	String[] currencies;
	String[] amount;
	TextView[] textViews;
	int numCurrencies;
	HashMap<TextView,CheckBox> mapTV2CB;
	private String inputDate;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.preview_sms);
	    
	    this.mapTV2CB = new HashMap<TextView, CheckBox>();

	    this.updateCheckBoxesFromReference();
	    this.getInfoFromListRate();
	    this.updateTextViews();
	}
	
	/**
	 * Restore the last time checkboxes state to save user time,
	 * Don't pay any attention to main currency changing 
	 */
	private void updateCheckBoxesFromReference() {
		// TODO Auto-generated method stub
		
	}

	private void updateTextViews() {
	    this.textViews = new TextView[this.numCurrencies];
	    for(int i = 0 ; i < this.numCurrencies; i++) {
	    	

	    	TextView view = this.getTextView(i);
	    	CheckBox cb = this.getCheckBox(i);

	    	this.mapTV2CB.put(view, cb);
	    	view.setText(currencies[i]+" "+amount[i]);    	
	    	
	    }
	}

	private CheckBox getCheckBox(int i) {
		i++;
    	String idCheckBox= "cbPreview0"+Integer.toString(i);
    	int resourceId = this.getResources().getIdentifier(idCheckBox, "id", this.getPackageName());
    	return (CheckBox)findViewById(resourceId);
		
	}

	private TextView getTextView(int i) {
		i++;
    	String idTextView= "tvPreview0"+Integer.toString(i);
    	int resourceId = this.getResources().getIdentifier(idTextView, "id", this.getPackageName());
    	return (TextView)findViewById(resourceId);
	}

	/**
	 * Get data passed from ListRate
	 */
	private void getInfoFromListRate() {
		this.numCurrencies = MyUtility.passData_Calculate2PreviewSMS_amount.length;
		this.currencies = MyUtility.passData_Calculate2PreviewSMS_currencies;
		this.amount = MyUtility.passData_Calculate2PreviewSMS_amount;
		this.inputDate = MyUtility.passData_Calculate2PreviewSMS_inputDate;
	}
	
	public void onSendSMSClickHandler(View view) {
		String content = this.getString(R.string.Calculate_SMSTitle) + " " + this.inputDate+ "\n  ";
		boolean passedFirstChecked= false;
		for(int i = 0; i < this.numCurrencies ; i++) {
			//TODO: debug ! Is this code work as I expected ? map[getTextView(i)] consistent
			CheckBox cb = this.mapTV2CB.get(this.getTextView(i));
			if ( cb.isChecked()) {
				if (passedFirstChecked) content +=" = "; else content+= "   ";
				content += this.currencies[i] + " " + this.amount[i] + "\n";
				passedFirstChecked = true;
			}
		}
		MyUtility.broadcastSMSIntent(this, content);
	}

}
