package hl.codeforchange.fragment;

import hl.codeforchange.activity.R;
import hl.codeforchange.utils.CacheVariant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
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
			ImageLoader.getInstance().displayImage(CacheVariant.PATH_IMG_AFTER, imgAfter);
		}
		if (!CacheVariant.PATH_IMG_BEFORE.equals("")) {
			ImageLoader.getInstance().displayImage(CacheVariant.PATH_IMG_BEFORE,
					imgBefore);
		}
		if (!CacheVariant.PATH_IMG_QRCODE.equals("")) {
			ImageLoader.getInstance().displayImage(CacheVariant.PATH_IMG_QRCODE,
					imgInfoQRcode);
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

		imgInfoQRcode = (ImageView) v.findViewById(R.id.imgQRCode);
		// image view
		imgAfter = (ImageView) v.findViewById(R.id.imgAfter);
		imgBefore = (ImageView) v.findViewById(R.id.imgBefore);

		imgAfter.setOnClickListener(this);
		imgBefore.setOnClickListener(this);
		imgInfoQRcode.setOnClickListener(this);
		captureType = CAPTURE_PICTURE_TYPE.NONE;
		loadImageCapture();
		return v;
	}

	@Override
	public void onResume() {

		super.onResume();
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
		}

	}

	private void capturePicture(Context context, int requestCode,
			Uri imageUriIndex) {

		if (imageUri == null) {
			imageUri = getImageFileUri();
		}

		imageUri = context.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new ContentValues());

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, requestCode);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		imgAfter.setImageBitmap(null);
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
				// Bitmap bitmap = BitmapFactory.decodeFile(photoFileName);
				// imgAfter.setImageBitmap(bitmap);
				CacheVariant.PATH_IMG_AFTER = "file://" + photoFileName;
				// ImageLoader.getInstance().displayImage(strPathImageAfter,
				// imgAfter);
				loadImageCapture();
			}
			if (captureType == CAPTURE_PICTURE_TYPE.BEFORE
					&& requestCode == CAPTURE_IMAGE_BEFORE_REQUEST_CODE) {
				// Bitmap bitmap = BitmapFactory.decodeFile(photoFileName);
				// imgBefore.setImageBitmap(bitmap);
				CacheVariant.PATH_IMG_BEFORE = "file://" + photoFileName;
				// ImageLoader.getInstance().displayImage(strPathImageBefore,
				// imgBefore);
				loadImageCapture();
			}
			if (captureType == CAPTURE_PICTURE_TYPE.QRCODE
					&& requestCode == CAPTURE_IMAGE_QRCODE_REQUEST_CODE) {
				// Bitmap bitmap = BitmapFactory.decodeFile(photoFileName);
				// imgInfoQRcode.setImageBitmap(bitmap);
				CacheVariant.PATH_IMG_QRCODE = "file://" + photoFileName;
				// ImageLoader.getInstance().displayImage(strPathImageQRCode,
				// imgInfoQRcode);
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

	private static Uri getImageFileUri() {

		// Create a storage directory for the images
		// To be safe(er), you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this

		imagePath = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Tuxuri");
		Log.d(tag, "Find " + imagePath.getAbsolutePath());
		if (!imagePath.exists()) {
			if (!imagePath.mkdirs()) {
				Log.d("CameraTestIntent", "failed to create directory");
				return null;
			} else {
				Log.d(tag, "create new Tux folder");
			}
		}

		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File image = new File(imagePath, "TUX_" + timeStamp + ".jpg");

		if (!image.exists()) {
			try {
				image.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return Uri.fromFile(image);
	}

}
