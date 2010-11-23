package dtd.PHS.YourExchangeRates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import dtd.PHS.YourExchangeRates.SimpleGestureFilter.SimpleGestureListener;

public class About extends Activity implements SimpleGestureListener{

	private MyMainMenu mainMenu;
	private SimpleGestureFilter detector;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
	    MyUtility.callOnEveryActivity(this);
	    setContentView(R.layout.about);
	    mainMenu = new MyMainMenu(this);
	    this.detector = new SimpleGestureFilter(this, this);
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
	
	@Override 
	public boolean dispatchTouchEvent(MotionEvent me){ 
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me); 
	}
	
	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_LEFT:
			this.startActivity(new Intent(this,ListRates.class));
			break;
		case SimpleGestureFilter.SWIPE_RIGHT:
			this.startActivity(new Intent(this,CalculateRate.class));
			break;
		default:
			break;
		}
		
	}
}
