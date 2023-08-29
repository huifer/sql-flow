package com.github.huifer.sql_flow.utils;

import java.util.HashMap;
import java.util.Map;

public class FlowTempStorageUtils {

  private static final ThreadLocal<Map<String, Object>> queryData = new ThreadLocal<>();


  public static void setQueryData(String nodeId, Object data) {
    Map<String, Object> objectMap = queryData.get();
    if (objectMap == null) {
      objectMap = new HashMap<>();
    }
    objectMap.put(nodeId, data);
    queryData.set(objectMap);

  }

  public static Map<String, Object> getQueryData() {
    return queryData.get();
  }

}
