package dtd.PHS.YourExchangeRates;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RowItem extends TableRow{
	TextView tvCountryName,tvCurrrencyName;
	TextView tvToRate,tvInRate;
	TextView tvToLabel,tvInLabel;
	ImageButton ibFlag;
	Context context;
	TableLayout tmp01;
	TableRow tmp02,tmp03,tmp04;
	public RowItem(Context context) {
		super(context);
		this.context = context;
		String service = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
		li.inflate(R.layout.row_item, this, true);
		
		this.setLongClickable(true);
		
		tvCountryName = (TextView)findViewById(R.id.tvCountryName);
		this.tvCountryName.setLongClickable(true);
		this.tvCurrrencyName = (TextView)findViewById(R.id.tvCurrrencyName);
		this.tvCurrrencyName.setLongClickable(true);
		
		tvToLabel = (TextView)findViewById(R.id.tvToLabel);
		tvToRate = (TextView)findViewById(R.id.tvToRate);
		this.tvToLabel.setLongClickable(true);
		this.tvToRate.setLongClickable(true);
		
		tvInLabel = (TextView)findViewById(R.id.tvInLabel);
		tvInRate = (TextView)findViewById(R.id.tvInRate);
		this.tvInRate.setLongClickable(true);
		this.tvInLabel.setLongClickable(true);
		
		ibFlag = (ImageButton)findViewById(R.id.ibFlag);
		this.ibFlag.setLongClickable(true);
		
		
		tmp01 = (TableLayout)findViewById(R.id.RowItem_tmp01);
		tmp01.setLongClickable(true);
		
		tmp02 = (TableRow)findViewById(R.id.RowItem_tmp02);
		tmp02.setLongClickable(true);

		tmp03 = (TableRow)findViewById(R.id.RowItem_tmp03);
		tmp03.setLongClickable(true);
		
		tmp04 = (TableRow)findViewById(R.id.RowItem_tmp04);
		tmp04.setLongClickable(true);
		
	
	}

	public void setInfo(String currency,String mainCurrency) {
		this.setTag(currency);
		this.tvCountryName.setText(MyUtility.mapAbb2FullCurrencyName.get(currency));
		this.tvToLabel.setText(context.getString(R.string.LRTo)+" "+mainCurrency);
		this.tvInLabel.setText(context.getString(R.string.LRIn)+" "+mainCurrency);
		
		int flagID = getResources().getIdentifier(currency.toLowerCase()+"_flag","drawable", "dtd.PHS.YourExchangeRates");
		this.ibFlag.setImageResource(flagID);
		this.setTag(currency);
		this.tvCurrrencyName.setText(currency);
	}

	/**
	 * Set exchange rate for the view
	 * @param rate
	 */
	public void setRates(double rate) {
		int prec = MyPreference.getDecimalPrecision(context);
		tvToRate.setText(MyUtility.formatRate( Double.toString(rate), prec));
		tvInRate.setText(MyUtility.formatRate( Double.toString(1.0/rate), prec));
	}
	
}
