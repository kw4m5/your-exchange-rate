package dtd.PHS.YourExchangeRates;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CalculateRate extends Activity {

	MyMainMenu mainMenu;
	TextView tvTitle,tvMainCurrency,tvMajorCurrency01,tvMajorCurrency02,tvECBLink,tvInputDate;
	EditText etMainCurrency,etMajorCurrency01,etMajorCurrency02;
	ArrayList<EditText> allEditText;
	EditText etFree01,etFree02;
	Spinner spinner01,spinner02;
	String mainCurrency;
	HashMap<EditText, String> mapET2Currency;
	DataProvider dataProvider;
	private  HashMap<Spinner, EditText> mapSpinner2ET;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate_rate);

		this.initProperties();

	}


	private void initProperties() {
		MyUtility.callOnEveryActivity(this);
		try {
			this.dataProvider = new DataProvider(this, this.mainCurrency);
			this.mainMenu = new MyMainMenu(this);	    
			this.mainCurrency = MyPreference.getMainCurrency(this);
			this.mapET2Currency = new HashMap<EditText, String>();
			this.mapSpinner2ET = new HashMap<Spinner, EditText>();
			this.allEditText = new ArrayList<EditText>();



			this.initTextViews();
			this.initSpinners();

			this.initEditTexts();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}


	private void initEditTexts() {
		this.etMainCurrency = (EditText)findViewById(R.id.etMainCurrency);
		registerEditText(etMainCurrency,(String)tvMainCurrency.getText());

		this.etMajorCurrency01 = (EditText)findViewById(R.id.etMajorCurrency01);
		registerEditText(etMajorCurrency01,(String)tvMajorCurrency01.getText());

		this.etMajorCurrency02 = (EditText)findViewById(R.id.etMajorCurrency02);
		registerEditText(etMajorCurrency02,(String)tvMajorCurrency02.getText());
		
	
	}


	private void registerEditText(EditText et, String currency) {
		this.mapET2Currency.put(et, currency);
		et.addTextChangedListener(new MyTextWatcher(et));
		allEditText.add(et);

	}


	private void initSpinners() {
		
		this.etFree01 = (EditText)findViewById(R.id.etFree01);
		this.etFree02 = (EditText)findViewById(R.id.etFree02);	
		this.spinner01 = (Spinner)findViewById(R.id.spinner01);
		this.spinner02 = (Spinner)findViewById(R.id.spinner02);
		registerEditText(this.etFree01,(String)spinner01.getSelectedItem());
		registerEditText(this.etFree02,(String)spinner02.getSelectedItem());

		
		this.mapSpinner2ET.put(spinner01, etFree01);
		this.mapSpinner2ET.put(spinner02, etFree02);
		
		
		ArrayAdapter<String > adapter = new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_spinner_item, 
				MyUtility.getCurrencies());
		this.spinner01.setAdapter(adapter);
		this.spinner02.setAdapter(adapter);
		
		MyOnItemSelectedListener listener = new MyOnItemSelectedListener();
		this.spinner01.setOnItemSelectedListener(listener);
		this.spinner02.setOnItemSelectedListener(listener);
		
	}


	private void initTextViews() {
		this.tvTitle = (TextView)findViewById(R.id.tvCalculateRateTitle);
		this.tvECBLink = (TextView)findViewById(R.id.tvCalculateECB);
		this.tvInputDate = (TextView)findViewById(R.id.tvCalculateInputDate);
		this.tvInputDate.setText(" "+this.dataProvider.getInputDate());
		
		
		this.tvMainCurrency = (TextView)findViewById(R.id.tvMainCurrencyLabel);
		this.tvMajorCurrency01 = (TextView)findViewById(R.id.tvMajorCurrency01);
		this.tvMajorCurrency02 = (TextView)findViewById(R.id.tvMajorCurrency02);


		this.tvMainCurrency.setText(this.mainCurrency);		
		String[] majorCurrencies = MyUtility.getMajorCurrencies(this.mainCurrency);
		this.tvMajorCurrency01.setText(majorCurrencies[0]);		
		this.tvMajorCurrency02.setText(majorCurrencies[1]);
	}


	public void onClearButtonClickHandler(View view) {
		for(EditText et : allEditText) {
			et.setText("");
		}
	}

	public void onSendSMSButtonClickHandler(View view) {
//		String content = 
//			this.getString(R.string.Calculate_SMSTitle) + " " + this.dataProvider.getInputDate() + "\n  "+
//			this.etMainCurrency.getText().toString()    + " " + (String) this.tvMainCurrency.getText() + "\n= " + 
//			this.etMajorCurrency01.getText().toString() + " " + (String) this.tvMajorCurrency01.getText() + "\n= "+
//			this.etMajorCurrency02.getText().toString() + " " + (String) this.tvMajorCurrency02.getText() + "\n= "+
//			this.etFree01.getText().toString()			+ " " + (String) this.spinner01.getSelectedItem() + "\n= "+
//			this.etFree02.getText().toString()			+ " " + (String) this.spinner02.getSelectedItem();
//		MyUtility.broadcastSMSIntent(this, content);
		this.saveDataForPreview();
		Intent intent = new Intent(this,PreviewSMS.class);
		this.startActivity(intent);
	}

	private void saveDataForPreview() {
		MyUtility.passData_Calculate2PreviewSMS_currencies = new String[5];
		MyUtility.passData_Calculate2PreviewSMS_amount = new String[5];
		
		MyUtility.passData_Calculate2PreviewSMS_currencies[0] = this.tvMainCurrency.getText().toString();
		MyUtility.passData_Calculate2PreviewSMS_currencies[1] = this.tvMajorCurrency01.getText().toString();
		MyUtility.passData_Calculate2PreviewSMS_currencies[2] = this.tvMajorCurrency02.getText().toString();
		MyUtility.passData_Calculate2PreviewSMS_currencies[3] = this.spinner01.getSelectedItem().toString();
		MyUtility.passData_Calculate2PreviewSMS_currencies[4] = this.spinner02.getSelectedItem().toString();
		
		MyUtility.passData_Calculate2PreviewSMS_amount[0] = this.etMainCurrency.getText().toString();
		MyUtility.passData_Calculate2PreviewSMS_amount[1] = this.etMajorCurrency01.getText().toString();
		MyUtility.passData_Calculate2PreviewSMS_amount[2] = this.etMajorCurrency02.getText().toString();
		MyUtility.passData_Calculate2PreviewSMS_amount[3] = this.etFree01.getText().toString();
		MyUtility.passData_Calculate2PreviewSMS_amount[4] = this.etFree02.getText().toString();	
		
		MyUtility.passData_Calculate2PreviewSMS_inputDate = this.dataProvider.getInputDate();
	}


	/**
	 * Clicked on bottom icon : ListRates
	 * @param view
	 */
	public void onClickListRatesHandler(View view) {
		Intent intent = new Intent(this,ListRates.class);
		this.startActivity(intent);
	}

	/**
	 * Clicked on bottom icon: SendSMS
	 * @param view
	 */
	public void onClickSMSHandler(View view) {
		Intent intent = new Intent(this,SendSMS.class);
		this.startActivity(intent);

	}
	public void onClickAboutHandler(View view) {
		Intent intent = new Intent(this,About.class);
		this.startActivity(intent);

	}

	/**
	 * Create the main menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		mainMenu.createOptionsMenu(menu);
		return true;
	}

	public void onClickECBHandler(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(this.getString(R.string.WebDataURL)) );
		startActivity(intent);
	}
	/**
	 * Some item is selected
	 */
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


	private class MyTextWatcher implements TextWatcher {

		private EditText toWatchEditText;
		private String oldText;
		private int oldPos;

		public MyTextWatcher(EditText et) {
			this.toWatchEditText = et;
		}

		@Override
		public void afterTextChanged(Editable s) {
			if ( this.toWatchEditText.findFocus() != null ){
				String amount = MyUtility.getRidOfComma(s.toString());
				if ( MyUtility.isValidLong(amount)) {
					this.updateEditTexts(this.toWatchEditText,amount);
				}
			}
		}



		private void updateEditTexts(EditText onChangeET, String amount) {
			for (EditText e : CalculateRate.this.mapET2Currency.keySet()) {
				if ( e.findFocus() == null ) {
					long convertedAmount = 
						CalculateRate.this.dataProvider.
							changeFromTo( Long.parseLong(amount),
									CalculateRate.this.mapET2Currency.get(onChangeET), 
									CalculateRate.this.mapET2Currency.get(e));
					e.setText(MyUtility.formatIntCurrency(Long.toString(convertedAmount)));
				} 
				else {
					String afterString = MyUtility.formatIntCurrency(amount);
					if ( ! afterString.equals( onChangeET.getText().toString() ) ) {			
						int newCursorPosition = MyUtility.newCursorPosition(this.oldPos,this.oldText,afterString);
						onChangeET.setText(afterString);
						Editable etext = onChangeET.getText(); 
						Selection.setSelection(etext, newCursorPosition);
					}
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			this.oldText = s.toString();
			this.oldPos = Selection.getSelectionStart(this.toWatchEditText.getText());;
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

	}
	
	private class MyOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			String currency = (String)parent.getSelectedItem();
			EditText et = CalculateRate.this.mapSpinner2ET.get((Spinner)parent);
			mapET2Currency.put(et, currency);
			CalculateRate.this.forceUpdateEditText(et);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}

	public void forceUpdateEditText(EditText et) {
		if (this.etMainCurrency.getText().toString().equals("")) return;
		String amountMainCurrencyString = MyUtility.getRidOfComma(this.etMainCurrency.getText().toString());
		long amountMainCurr = Long.parseLong(amountMainCurrencyString);
		long amountExchange = this.dataProvider.changeFromTo(
				amountMainCurr, 
				this.mainCurrency, 
				this.mapET2Currency.get(et));
 
		et.setText(MyUtility.formatIntCurrency(Long.toString(amountExchange)));
		
	}
}
