package hl.codeforchange.fragment;

import hl.codeforchange.activity.R;
import hl.codeforchange.postftp.FTPUploadFile;
import hl.codeforchange.utils.CacheVariant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentCapture extends SherlockFragment implements
		OnClickListener {
	private static final String TAG = "FragmentHome";
	private static int CAPTURE_IMAGE_AFTER_REQUEST_CODE = 1;
	private static int CAPTURE_IMAGE_BEFORE_REQUEST_CODE = 2;
	private static int CAPTURE_IMAGE_QRCODE_REQUEST_CODE = 3;
	private View v;
	private ImageView imgBefore, imgAfter, imgInfoQRcode;
	Uri imageUri = null, imageUriAfter = null, imageUriBefore = null,
			imageUriQrCode = null;
	public static ImageView showImg = null;
	public static Context activity;
	private Button btnPostData;

	// User
	private String user = "u488574653";
	private String pass = "code4change";

	//Post
	String LINK_POST = "http://swf.letsgeekaround.com/insert.php";
	
	private enum CAPTURE_PICTURE_TYPE {
		AFTER, BEFORE, QRCODE, NONE
	}

	private CAPTURE_PICTURE_TYPE captureType;

	public static FragmentCapture newInstance(int position, Context ac) {
		final FragmentCapture f = new FragmentCapture();
		final Bundle args = new Bundle();
		args.putInt("pos", position);
		f.setArguments(args);
		activity = ac;
		return f;
	}

	public void loadImageCapture() {
		if (!CacheVariant.PATH_IMG_AFTER.equals("")) {
			ImageLoader.getInstance().displayImage(
					"file://" + CacheVariant.PATH_IMG_AFTER, imgAfter);
		}
		if (!CacheVariant.PATH_IMG_BEFORE.equals("")) {
			ImageLoader.getInstance().displayImage(
					"file://" + CacheVariant.PATH_IMG_BEFORE, imgBefore);
		}
		if (!CacheVariant.PATH_IMG_QRCODE.equals("")) {
			ImageLoader.getInstance().displayImage(
					"file://" + CacheVariant.PATH_IMG_QRCODE, imgInfoQRcode);
		}
		captureType = CAPTURE_PICTURE_TYPE.NONE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_capture_picture, container,
				false);

		btnPostData = (Button) v.findViewById(R.id.btnPostToServer);
		imgInfoQRcode = (ImageView) v.findViewById(R.id.imgQRCode);
		// image view
		imgAfter = (ImageView) v.findViewById(R.id.imgAfter);
		imgBefore = (ImageView) v.findViewById(R.id.imgBefore);

		imgAfter.setOnClickListener(this);
		imgBefore.setOnClickListener(this);
		imgInfoQRcode.setOnClickListener(this);
		btnPostData.setOnClickListener(this);
		captureType = CAPTURE_PICTURE_TYPE.NONE;
		loadImageCapture();
		return v;
	}

	public void postDataToServer() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		Log.i("POST TO SERVER ", timeStamp);
		if (!CacheVariant.PATH_IMG_AFTER.equals("")
				&& !CacheVariant.PATH_IMG_BEFORE.equals("")
				&& !CacheVariant.PATH_IMG_QRCODE.equals("")) {
			FTPUploadFile.uploadFileData(CacheVariant.PATH_IMG_AFTER,
					"KFC_After_" + timeStamp + ".jpg", user, pass);
			FTPUploadFile.uploadFileData(CacheVariant.PATH_IMG_BEFORE,
					"KFC_Before_" + timeStamp + ".jpg", user, pass);
			FTPUploadFile.uploadFileData(CacheVariant.PATH_IMG_QRCODE,
					"KFC_QRCode_" + timeStamp + ".jpg", user, pass);

			// post to mysql
			try {
				
				new PostData().execute("hlam","10010110","100000",
								"KFC_After_" + timeStamp + ".jpg",
								"KFC_Before_" + timeStamp + ".jpg",
								"KFC_QRCode_" + timeStamp + ".jpg");
				
			} catch (Exception e) {
				Log.i("MySQL ", new String("Exception: " + e.getMessage()));
			}

		}
	}

	

	public class PostData extends AsyncTask<String, Integer, String> {
		HttpClient httpclient;
		HttpPost httppost;
		HttpResponse response;
		BufferedReader rd = null;
		
		@Override
		protected String doInBackground(String... args) {
			postData(args[0], args[1], args[2], args[3], args[4],args[5]);
			return null;
		}
		
		@Override
		protected void onPostExecute(String resultData) {
			Toast.makeText(activity,"Upload MySQL finish ", Toast.LENGTH_SHORT).show();
			
		}
		
		@Override
		protected void onPreExecute() {
			
			
			super.onPreExecute();
		}

		//
		public void postData(String username, String billcode,String price,String pic1, String pic2, String pic3 ) {
			
			// Create a new HttpClient and Post Header
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(LINK_POST);
			
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username",
						username));
				nameValuePairs.add(new BasicNameValuePair("billcode",
						billcode));
				nameValuePairs.add(new BasicNameValuePair("price",
						price));
				nameValuePairs.add(new BasicNameValuePair("pic1",
						pic1));
				nameValuePairs.add(new BasicNameValuePair("pic2",
						pic2));
				nameValuePairs.add(new BasicNameValuePair("pic3",
						pic3));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

	}
	public void postData(String username, String billcode,String price,String pic1, String pic2, String pic3 ) {
		HttpClient httpclient;
		HttpPost httppost;
		HttpResponse response;
		// Create a new HttpClient and Post Header
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost(LINK_POST);
		
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("username",
					username));
			nameValuePairs.add(new BasicNameValuePair("billcode",
					billcode));
			nameValuePairs.add(new BasicNameValuePair("price",
					price));
			nameValuePairs.add(new BasicNameValuePair("pic1",
					pic1));
			nameValuePairs.add(new BasicNameValuePair("pic2",
					pic2));
			nameValuePairs.add(new BasicNameValuePair("pic3",
					pic3));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// Execute HTTP Post Request
			response = httpclient.execute(httppost);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgAfter:
			capturePicture(getActivity().getBaseContext(),
					CAPTURE_IMAGE_AFTER_REQUEST_CODE, imageUriAfter);
			captureType = CAPTURE_PICTURE_TYPE.AFTER;
			break;
		case R.id.imgBefore:
			capturePicture(getActivity().getBaseContext(),
					CAPTURE_IMAGE_BEFORE_REQUEST_CODE, imageUriBefore);
			captureType = CAPTURE_PICTURE_TYPE.BEFORE;
			break;
		case R.id.imgQRCode:
			capturePicture(getActivity().getBaseContext(),
					CAPTURE_IMAGE_QRCODE_REQUEST_CODE, imageUriQrCode);
			captureType = CAPTURE_PICTURE_TYPE.QRCODE;
			break;
		case R.id.btnPostToServer:
			// postDataToServer();
			new UpLoadData().execute();
			break;
		}

	}

	private void capturePicture(Context context, int requestCode,
			Uri imageUriIndex) {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, timeStamp);
		imageUri = context.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		// imageUri = getImageFileUri();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == FragmentActivity.RESULT_OK) {
			Cursor c = null;
			String[] projection = { MediaStore.MediaColumns._ID,
					MediaStore.Images.ImageColumns.ORIENTATION,
					MediaStore.Images.Media.DATA };

			c = activity.getContentResolver().query(imageUri, projection, null,
					null, null);
			int columnIndex = c
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			c.moveToFirst();
			Log.i("PATH FILE  ", c.getString(columnIndex));

			String photoFileName = c.getString(c
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

			if (captureType == CAPTURE_PICTURE_TYPE.AFTER
					&& requestCode == CAPTURE_IMAGE_AFTER_REQUEST_CODE) {
				CacheVariant.PATH_IMG_AFTER = photoFileName;
				loadImageCapture();
			}
			if (captureType == CAPTURE_PICTURE_TYPE.BEFORE
					&& requestCode == CAPTURE_IMAGE_BEFORE_REQUEST_CODE) {
				CacheVariant.PATH_IMG_BEFORE = photoFileName;
				loadImageCapture();
			}
			if (captureType == CAPTURE_PICTURE_TYPE.QRCODE
					&& requestCode == CAPTURE_IMAGE_QRCODE_REQUEST_CODE) {
				CacheVariant.PATH_IMG_QRCODE = photoFileName;
				loadImageCapture();
			}
			imageUri = null;
			c.close();

		} else if (resultCode == FragmentActivity.RESULT_CANCELED) {
			// User cancelled the image capture
		} else {
			// Image capture failed, advise user
		}

	}

	String currentImagePath;
	final static String tag = " CAPTURE DM";
	static File imagePath;

	private class UpLoadData extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;
		
		@Override
		protected Void doInBackground(Void... params) {

			postDataToServer();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			if(dialog.isShowing()){
				dialog.dismiss();
			}
			Toast.makeText(activity, "Upload Finish", Toast.LENGTH_SHORT)
					.show();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Uploadding ... ");
			dialog.setCancelable(false);
			dialog.show();
			
			super.onPreExecute();
		}

	}

}
