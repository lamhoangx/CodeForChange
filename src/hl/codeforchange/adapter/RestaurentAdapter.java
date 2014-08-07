package hl.codeforchange.adapter;

import hl.codeforchange.activity.R;
import hl.codeforchange.item.ItemRestaurent;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurentAdapter extends BaseAdapter {
	private final Activity activity;
	private final ArrayList data;
	private static LayoutInflater inflater = null;
	public Resources res;
	int i = 0;
	public View vi;
	public ViewHolder holder;
	private ItemRestaurent itemRestaurent;
	
	public RestaurentAdapter(Activity a, ArrayList al, Resources resLocal) {
		activity = a;
		data = al;
		res = resLocal;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		vi = convertView;
		
		if (convertView == null) {
			vi = inflater.inflate(R.layout.item_info_restaurent, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.tvNameRestaurent);
			holder.text2 = (TextView) vi.findViewById(R.id.tvInfoRestaurent);
			holder.text3 = (TextView) vi
					.findViewById(R.id.tvPhoneNumberRestaurent);
			holder.image = (ImageView) vi.findViewById(R.id.imgRestaurent);
			
			vi.setTag(holder);
		} else {
		}
		holder = (ViewHolder) vi.getTag();
		if (data.size() <= 0) {
			// holder.text.setText("No Data");
		} else {
			itemRestaurent = null;
			itemRestaurent = (ItemRestaurent) data.get(position);
			
			holder.text.setText(itemRestaurent.getName());
			holder.text2.setText(itemRestaurent.getIntroduce());
			holder.text3.setText(itemRestaurent.getPhone_number());
			
			String uri = "android.resource://hl.codeforchange.activity"
					+ "/raw/" + itemRestaurent.getImage_res();
			
			Uri path = Uri
					.parse("android.resource://hl.codeforchange.activity/raw/"
							+ itemRestaurent.getImage_res());
			holder.image.setImageURI(path);
		}
		
		return vi;
	}
	
	@Override
	public int getCount() {
		if (data.size() <= 0) {
			return 0;
		}
		return data.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public static class ViewHolder {
		public TextView text;
		public TextView text3;
		public TextView text2;
		public ImageView image;
	}
	
}
