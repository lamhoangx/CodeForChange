package hl.codeforchange.activity;

import hl.codeforchange.fragment.FragmentCapture;
import hl.codeforchange.fragment.FragmentForum;
import hl.codeforchange.fragment.FragmentHome;
import hl.codeforchange.fragment.SuperAwesomeCardFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

public class MainActivity extends SherlockFragmentActivity {
	private static final String TAG = "MainActivity";
	
	private static final String[] CONTENT = new String[] { "Nhà Hàng", "Nhận quà",
			"Chia Sẻ", "Cá Nhân" };
	private static final int[] ICONS = new int[] { R.drawable.icon_home,
			R.drawable.icon_bell, R.drawable.icon_learn,
			R.drawable.icon_settings, };
	
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private Drawable oldBackground = null;
	private int currentColor = 0xFF666666;
	private final Handler handler = new Handler();
	private Context activity = getBaseContext();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager_main);
		
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		
		pager.setAdapter(adapter);
		
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		
		tabs.setViewPager(pager);
		getSupportActionBar().setTitle("StopwastingFood");
		getSupportActionBar().setIcon(ICONS[0]);
		pager.setCurrentItem(0);
		tabs.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				getSupportActionBar()
						.setTitle("StopwastingFood");
				getSupportActionBar().setIcon(ICONS[arg0]);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void changeColor(int newColor) {
		
		tabs.setIndicatorColor(newColor);
		
		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			
			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(
					R.drawable.ic_launcher);
			LayerDrawable ld = new LayerDrawable(new Drawable[] {
					colorDrawable, bottomDrawable });
			
			if (oldBackground == null) {
				
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					// getSupportActionBar().setBackgroundDrawable(ld);
				}
				
			} else {
				
				TransitionDrawable td = new TransitionDrawable(new Drawable[] {
						oldBackground, ld });
				
				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					// getSupportActionBar().setBackgroundDrawable(td);
				}
				
				td.startTransition(200);
				
			}
			
			oldBackground = ld;
			
			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			// getSupportActionBar().setDisplayShowTitleEnabled(false);
			// getSupportActionBar().setDisplayShowTitleEnabled(true);
			
		}
		
		currentColor = newColor;
		
	}
	
	public void onColorClicked(View v) {
		int color = Color.parseColor(v.getTag().toString());
		changeColor(color);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}
	
	private final Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getSupportActionBar().setBackgroundDrawable(who);
		}
		
		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}
		
		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};
	
	public class MyPagerAdapter extends FragmentPagerAdapter {
		
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			Log.w(TAG, "Position " + position);
			getSupportActionBar().setTitle(CONTENT[position]);
			return CONTENT[position];
		}
		
		@Override
		public int getCount() {
			return CONTENT.length;
		}
		
		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return FragmentHome.newInstance(position);
			}
			if (position == 1) {
				return FragmentCapture.newInstance(position,getBaseContext());
			}
			if (position == 2) {
				return FragmentForum.newInstance(position);
			} else {
				return SuperAwesomeCardFragment.newInstance(position);
			}
			
		}
	}
	
}
