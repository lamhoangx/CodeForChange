package hl.codeforchange.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class APIGetJson {
	private static String strResult;
	
	public static void getJson(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		
		JsonObjectRequest request = new JsonObjectRequest(url, jsonRequest,
				listener, errorListener);
		AppController.getInstance().addToRequestQueue(request, "GET JSON");
	}
	
	public static String postServer(HashMap<String, String> hMapParams) {
		strResult = "";
		String tag_json_obj = "json_obj_req";
		String url = hMapParams.get("url");
		final String key = hMapParams.get("key");
		final String param = hMapParams.get("param");
		
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url,
				null, new Response.Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						strResult = response.toString();
					}
					
				}, new Response.ErrorListener() {
					
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						
					}
				}) {
			
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(key, param);
				return headers;
			}
			
		};
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		return strResult;
	}
	
}
