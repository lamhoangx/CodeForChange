package hl.codeforchange.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class SplashScreenActivity extends Activity {
	
	private static final String TAG = "SplashScreen";
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 1000;
	private final boolean mSigned = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int wwidth = metrics.widthPixels;
		
		Log.d(TAG, "View " + height + " " + wwidth);
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.empty_photo)
				// EDIT HERE
				.bitmapConfig(Config.ARGB_8888)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.handler(new Handler()).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(options).threadPoolSize(7)
				.threadPriority(Thread.NORM_PRIORITY - 1).build();
		ImageLoader.getInstance().init(config);
		
		Thread logoTimer = new Thread() {
			@Override
			public void run() {
				try {
					sleep(SPLASH_TIME_OUT);
					
					Intent intent = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(intent);
					
					finish();
				}
				
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finally {
					finish();
				}
			}
		};
		
		logoTimer.start();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
}