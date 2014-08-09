package hl.codeforchange.adapter;

import hl.codeforchange.activity.R;
import hl.codeforchange.item.ItemComment;
import hl.codeforchange.item.ItemForum;
import hl.codeforchange.utils.CacheVariant;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ForumAdapter extends BaseAdapter implements OnClickListener {
	private final Activity activity;
	private final ArrayList data;
	private static LayoutInflater inflater = null;
	public Resources res;
	int i = 0;
	public View vi;
	public ViewHolder holder;
	private ItemForum itemPost;
	private LinearLayout llComment;
	private Button btnComment;
	private EditText edComment;
	private ArrayList<ItemComment> arrItemComment;
	private CommentAdapter adapterComment;
	private ListView lvComment;

	public ForumAdapter(Activity a, ArrayList al, Resources resLocal) {
		activity = a;
		data = al;
		res = resLocal;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		vi = convertView;
		// arrItemComment = new ArrayList<ItemComment>();

		if (convertView == null) {
			vi = inflater.inflate(R.layout.item_post_social, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.tvNameUser);
			holder.text2 = (TextView) vi.findViewById(R.id.tvPostInfo_Social);
			holder.image = (ImageView) vi.findViewById(R.id.imgPost);

			llComment = (LinearLayout) vi.findViewById(R.id.llComment);
			edComment = (EditText) vi.findViewById(R.id.edComment);
			btnComment = (Button) vi.findViewById(R.id.btnComment);
			lvComment = (ListView) vi.findViewById(R.id.lvComment);

			vi.setTag(holder);
		} else {
		}
		holder = (ViewHolder) vi.getTag();
		if (data.size() <= 0) {
			// holder.text.setText("No Data");
		} else {
			itemPost = null;
			itemPost = (ItemForum) data.get(position);

			holder.text.setText(itemPost.getUser_name());
			holder.text2.setText(itemPost.getStatus());

			ImageLoader.getInstance().displayImage(itemPost.getPic_post(), holder.image);

		}

		btnComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i("Test Button Comment press", " COMMENT   " + position);
				if (!edComment.getText().toString().equals("")) {
					ItemComment itemComm = new ItemComment();
					itemComm.setUser_name("User Name");
					itemComm.setStatus_comment(edComment.getText().toString());
					edComment.setText("");

					arrItemComment.add(itemComm);
					adapterComment.notifyDataSetChanged();
					lvComment.setAdapter(adapterComment);

				}
			}
		});
		// btnComment.setOnClickListener(this);
		holder.image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				llComment.setVisibility(NO_SELECTION);
			}
		});

		arrItemComment = new ArrayList<ItemComment>();
		adapterComment = new CommentAdapter(activity, arrItemComment, res);

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

	@Override
	public void onClick(View v) {
		// int id = v.getId();
		// if (id == R.id.btnComment) {
		// Log.i("Test Button Comment press", " COMMENT   ");
		// if (!edComment.getText().toString().equals("")) {
		// ItemComment itemComm = new ItemComment();
		// itemComm.setUser_name("User Name");
		// itemComm.setStatus_comment(edComment.getText().toString());
		// edComment.setText("");
		//
		// arrItemComment.add(itemComm);
		// adapterComment.notifyDataSetChanged();
		// lvComment.setAdapter(adapterComment);
		//
		// }

		// lvComment.setAdapter(adapterComment);
		// }
	}
}
