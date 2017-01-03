package hcfs.test.utils;

import org.json.JSONException;
import org.json.JSONObject;

import hcfs.test.spec.JsonFields;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class JsonUtils {

	public static boolean getResult(String jsonString) throws JSONException {
		return new JSONObject(jsonString).getBoolean(JsonFields.EXE_RESULT);
	}

	public static int getCode(String jsonString) throws JSONException {
		return new JSONObject(jsonString).getInt(JsonFields.RETURN_CODE);
	}

	public static JSONObject getDataObject(String jsonString) throws JSONException {
		return new JSONObject(jsonString).getJSONObject(JsonFields.RETURN_DATA);
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<>();
		Iterator<String> keysItr = object.keys();
		while(keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if(value instanceof JSONArray)
				value = toList((JSONArray) value);
			else if(value instanceof JSONObject)
				value = toMap((JSONObject) value);
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<>();
		for(int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if(value instanceof JSONArray)
				value = toList((JSONArray) value);
			else if(value instanceof JSONObject)
				value = toMap((JSONObject) value);
			list.add(value);
		}
		return list;
	}

	public static int[] getHCFSEvents(String jsonString) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonString);
		int[] events = new int[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			 JSONObject jsonObj = jsonArray.getJSONObject(i);
			events[i] =  jsonObj.getInt(JsonFields.EVENT_ID);
		}
		return events;
	}
}
