package hl.codeforchange.adapter;

import hl.codeforchange.activity.R;
import hl.codeforchange.item.ItemComment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private final Activity activity;
	private final ArrayList data;
	private static LayoutInflater inflater = null;
	public Resources res;
	int i = 0;
	public View vi;
	public ViewHolder holder;
	private ItemComment itemComment;
	
	public CommentAdapter(Activity a, ArrayList al, Resources resLocal) {
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
			vi = inflater.inflate(R.layout.item_comment, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.tvNameUserComment);
			holder.text2 = (TextView) vi.findViewById(R.id.tvStatusUserComment);
			
			vi.setTag(holder);
		} else {
		}
		holder = (ViewHolder) vi.getTag();
		if (data.size() <= 0) {
			// holder.text.setText("No Data");
		} else {
			itemComment = null;
			itemComment = (ItemComment) data.get(position);
			
			holder.text.setText(itemComment.getUser_name());
			holder.text2.setText(itemComment.getStatus_comment());
			
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
		public TextView text2;
	}
}
