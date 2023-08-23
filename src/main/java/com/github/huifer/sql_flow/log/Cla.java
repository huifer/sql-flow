package com.github.huifer.sql_flow.log;

import java.util.*;

public class Cla {

  public static void main(String[] args) {

    List<Info> infos1 = processString("1你好a");
    List<Info> infos2 = processString("2");

    Map<String, List<Info>> map = new HashMap<>();
    map.put("2你好a", infos1);
    map.put("1你好b", infos2);

    List<Map.Entry<String, List<Info>>> entryList = new ArrayList<>(map.entrySet());

    Collections.sort(entryList, new Comparator<Map.Entry<String, List<Info>>>() {
      @Override
      public int compare(Map.Entry<String, List<Info>> entry1, Map.Entry<String, List<Info>> entry2) {
        List<Info> infos1 = entry1.getValue();
        List<Info> infos2 = entry2.getValue();

        boolean hasNumber1 = hasNumber(infos1);
        boolean hasNumber2 = hasNumber(infos2);

        if (hasNumber1 && hasNumber2) {
          return compareByNumber(infos1, infos2);
        } else if (!hasNumber1 && !hasNumber2) {
          return compareByLetter(infos1, infos2);
        } else {
          return hasNumber1 ? -1 : 1;
        }
      }
    });

    for (Map.Entry<String, List<Info>> entry : entryList) {
      System.out.println(entry.getKey());
    }


  }

  public static boolean hasNumber(List<Info> infoList) {
    for (Info info : infoList) {
      if (info.isNumber) {
        return true;
      }
    }
    return false;
  }

  public static int compareByNumber(List<Info> infos1, List<Info> infos2) {

    return 1;
  }

  public static int compareByLetter(List<Info> infos1, List<Info> infos2) {
    return -1;
  }



  public static List<Info> processString(String input) {
    List<Info> infoList = new ArrayList<>();

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      Info info = new Info(c);



      info.isNumber = Character.isDigit(c);
      info.isCn = Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
      info.isEn = Character.isLetter(c) && !info.isCn;

      infoList.add(info);
    }

    return infoList;
  }

  public static class Info {

    public Info(char e) {
      this.e = e;
    }

    public boolean isNumber;
    public boolean isCn;
    public boolean isEn;
    private char e;

  }

}
