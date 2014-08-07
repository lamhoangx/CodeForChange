package hl.codeforchange.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.res.Resources;
import android.util.Log;

public class Utils {
	
	public static String readData(Resources res, int id) {
		String result = "";
		String data;
		InputStream in = res.openRawResource(id);
		InputStreamReader inreader = new InputStreamReader(in);
		BufferedReader bufreader = new BufferedReader(inreader);
		StringBuilder builder = new StringBuilder();
		if (in != null) {
			try {
				while ((data = bufreader.readLine()) != null) {
					builder.append(data);
					builder.append("\n");
				}
				in.close();
				// editdata.setText(builder.toString());
				result = builder.toString();
			} catch (IOException ex) {
				Log.e("ERROR", ex.getMessage());
			}
		}
		return result;
	}
}
