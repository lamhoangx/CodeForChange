package hl.codeforchange.fragment;

import hl.codeforchange.activity.R;
import hl.codeforchange.adapter.RestaurentAdapter;
import hl.codeforchange.item.ItemRestaurent;
import hl.codeforchange.utils.Utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FragmentHome extends SherlockFragment implements OnClickListener {
	private static final String TAG = "FragmentHome";
	private PullToRefreshListView mPullRefreshListView;
	private View v;
	private ListView lvInfoRestaurents;
	private RestaurentAdapter adapter;
	private ArrayList<ItemRestaurent> iTems;
	
	public static FragmentHome newInstance(int position) {
		final FragmentHome f = new FragmentHome();
		final Bundle args = new Bundle();
		args.putInt("pos", position);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getDataRestaurent();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_home, container, false);
		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.pull_refresh_list);
		
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel("waitting ... ");
						new RefreshData().execute();
					}
				});
		
		lvInfoRestaurents = mPullRefreshListView.getRefreshableView();
		
		registerForContextMenu(lvInfoRestaurents);
		
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_home, lvInfoRestaurents,false);
		lvInfoRestaurents.addHeaderView(header);
		
		adapter = new RestaurentAdapter(getActivity(), iTems, getResources());
		
		lvInfoRestaurents.setAdapter(adapter);
		
		return v;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
	}
	
	public void getDataRestaurent() {
		iTems = new ArrayList<ItemRestaurent>();
		String strValue = Utils.readData(getResources(), R.raw.data_restaurent);
		JSONObject jsonRoot;
		try {
			jsonRoot = new JSONObject(strValue);
			Gson gson = new Gson();
			JSONArray jsonArray = jsonRoot.getJSONArray("restaurent");
			for (int i = 0; i < jsonArray.length(); i++) {
				Log.d("JSON", jsonArray.getJSONObject(i).toString());
				JSONObject objGson = jsonArray.getJSONObject(i);
				final ItemRestaurent course = gson.fromJson(objGson.toString(),
						ItemRestaurent.class);
				iTems.add(course);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public class RefreshData extends AsyncTask<Void, Void, ListView> {
		
		@Override
		protected ListView doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
				getDataRestaurent();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(ListView result) {
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
		
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
}
