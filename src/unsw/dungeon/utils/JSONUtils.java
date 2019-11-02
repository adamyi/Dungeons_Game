// this file is adapted from
// https://github.com/MessageOnTap/MessageOnTap_API/blob/master/api/src/main/java/edu/cmu/chimps/messageontap_api/JSONUtils.java
//
// which I, Adam (Xuan) Yi <i@adamyi.com, xuan@yiad.am, z5231521@cse.unsw.edu.au>, wrote back in
// 2017. I authorize myself to use it here.
//
// (btw I miss using gson)

/*
 Copyright 2017 CHIMPS Lab, Carnegie Mellon University

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package unsw.dungeon.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused", "SameParameterValue"})
public class JSONUtils {
  /**
   * Convert a JSONObject to a HashMap
   *
   * @param json the JSONObject
   * @return the HashMap converted
   * @throws JSONException when json is wrong
   */
  public static HashMap<String, Object> jsonToMap(JSONObject json) throws JSONException {
    HashMap<String, Object> retMap = new HashMap<>();

    if (json != JSONObject.NULL) {
      retMap = toMap(json);
    }
    return retMap;
  }

  /**
   * Convert a JSON string to a HashMap
   *
   * @param text the JSON string
   * @return the HashMap converted
   * @throws JSONException when json is wrong
   */
  public static HashMap<String, Object> toMap(String text) throws JSONException {
    if (TextUtils.isEmpty(text)) return new HashMap<>();
    else return toMap(new JSONObject(text));
  }

  /**
   * Convert a JSONObject to a HashMap
   *
   * @param object the JSON Object
   * @return the HashMap converted
   * @throws JSONException when json is wrong
   */
  public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
    HashMap<String, Object> map = new HashMap<>();

    Iterator<String> keysItr = object.keys();
    while (keysItr.hasNext()) {
      String key = keysItr.next();
      Object value = object.get(key);

      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toMap((JSONObject) value);
      }
      map.put(key, value);
    }
    return map;
  }

  /**
   * Convert a JSONArray to a list
   *
   * @param array the JSON Array
   * @return the List converted
   * @throws JSONException when json is wrong
   */
  public static List<Object> toList(JSONArray array) throws JSONException {
    List<Object> list = new ArrayList<>();
    for (int i = 0, len = array.length(); i < len; ++i) {
      Object value = array.get(i);
      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toMap((JSONObject) value);
      }
      list.add(value);
    }
    return list;
  }

  public static HashMap<String, Object> refactorHashMap(HashMap<String, Object> mMap) {
    return refactorHashMap(mMap, 0);
  }

  /**
   * Check all double and float values in a hash map. If they are integers, cast them to the data
   * type that they should be.
   *
   * @param mMap the hash map to be refactored
   * @param mode -1: Integer only Mode; 0: Smart Mode; 1: Long only Mode
   * @return the hash map after refactoring
   * @author Adam Yi &lt;xuan@yiad.am&gt;
   */
  public static HashMap<String, Object> refactorHashMap(HashMap<String, Object> mMap, int mode) {
    for (String key : mMap.keySet()) {
      if (mMap.get(key) != null
          && (mMap.get(key) instanceof Double || mMap.get(key) instanceof Float)) {
        Double mDouble = (Double) mMap.get(key);
        long mLong = mDouble.longValue();
        if (mDouble == mLong) {
          if (mode == 1) {
            mMap.put(key, mLong);
            continue;
          }
          int mInt = (int) mLong;
          if (mode == -1) {
            mMap.put(key, mInt);
            continue;
          }
          if (mInt == mLong) mMap.put(key, mInt);
          else mMap.put(key, mLong);
        }
      }
    }
    return mMap;
  }

  /**
   * Cast a number Object to Long
   *
   * @param num the object to be casted
   * @return the casted Long
   * @throws UnsupportedOperationException when num is not a number
   * @author Adam Yi &lt;xuan@yiad.am&gt;
   */
  public static Long longValue(Object num) throws UnsupportedOperationException {
    if (num instanceof Long) return (Long) num;
    if (num instanceof Integer) return (long) num;
    if (num instanceof Double) return (long) (double) num;
    if (num instanceof Float) return (long) (float) num;
    throw new UnsupportedOperationException();
  }

  /**
   * Cast a number Object to Integer
   *
   * @param num the object to be casted
   * @return the casted Integer
   * @throws UnsupportedOperationException when num is not a number
   * @author Adam Yi &lt;xuan@yiad.am&gt;
   */
  public static Integer intValue(Object num) throws UnsupportedOperationException {
    if (num instanceof Integer) return (Integer) num;
    if (num instanceof Long) return (int) (long) num;
    if (num instanceof Double) return (int) (double) num;
    if (num instanceof Float) return (int) (float) num;
    throw new UnsupportedOperationException();
  }
}
