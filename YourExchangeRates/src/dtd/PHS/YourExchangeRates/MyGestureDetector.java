package dtd.PHS.YourExchangeRates;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MyGestureDetector extends SimpleOnGestureListener {
	private static final int SWIPE_MIN_DISTANCE = 80;
	private static final int SWIPE_MAX_OFF_PATH = 150;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;

	Activity parentAct;
	Intent leftIntent,rightIntent;
	public MyGestureDetector(Activity act,Intent leftIntent,Intent rightIntent) {
		this.parentAct = act;
		this.leftIntent = leftIntent;
		this.rightIntent = rightIntent;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) return false;
		if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {				 
			this.parentAct.startActivity(this.leftIntent);
		} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			this.parentAct.startActivity(this.rightIntent);
		}
		return true;
	}
}
	