package com.validat.validator;

import com.validat.util.DateUtil;

public class IDNFormatterValidator
{
  private static int[] locationNumber = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 };
  private static char[] locationChar = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'W', 'Z', 'I', 'O' };
  private static int[] TWID_Regulation = { 1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1 };
  private static IDNFormatterValidator instance = null;
  
  public static IDNFormatterValidator getInstance()
  {
    if (instance == null) {
      instance = new IDNFormatterValidator();
    }
    return instance;
  }
  
  public boolean isDataValid(String object)
  {
    if (object.length() == 7) {
      return checkChinaID(object);
    }
    if ((object == null) || ("".equals(object)) || (object.length() != 10)) {
      return false;
    }
    char firstChar = object.substring(0, 1).charAt(0);
    char secondChar = object.substring(1, 2).charAt(0);
    if ((Character.isUpperCase(firstChar)) && ((object.substring(1, 2).charAt(0) == '1') || (object.substring(1, 2).charAt(0) == '2') || (object.substring(1, 2).charAt(0) == '8') || (object.substring(1, 2).charAt(0) == '9'))) {
      return checkTWID(object, firstChar);
    }
    if ((Character.isUpperCase(firstChar)) && (Character.isUpperCase(secondChar)))
    {
      if ((secondChar == 'C') || (secondChar == 'D') || (secondChar == 'E') || (secondChar == 'F')) {
        return checkNovelResidentID(object, firstChar, secondChar);
      }
      if ((secondChar == 'A') || (secondChar == 'B')) {
        return checkNovelChinaID(object, firstChar, secondChar);
      }
      return false;
    }
    if ((Character.isDigit(firstChar)) && (firstChar == '9')) {
      return checkChinaID(object);
    }
    if (Character.isDigit(firstChar)) {
      return checkResidentID(object);
    }
    return false;
  }
  
  private boolean checkTWID(String ID, char firstChar)
  {
    char[] ch = ID.substring(2).toCharArray();
    String substring = ID.substring(1);
    return validWeight(ID, substring, firstChar, ch);
  }
  
  private boolean checkNovelResidentID(String ID, char firstChar, char secondChar)
  {
    char[] ch = ID.substring(2).toCharArray();
    String value = "";
    if (secondChar == 'C') {
      value = "2";
    } else if (secondChar == 'D') {
      value = "3";
    } else if (secondChar == 'E') {
      value = "4";
    } else if (secondChar == 'F') {
      value = "5";
    }
    String substring = value + ID.substring(2);
    return validWeight(ID, substring, firstChar, ch);
  }
  
  private boolean checkResidentID(String ID)
  {
    String substring = ID.substring(0, 8);
    char[] ch = ID.toCharArray();
    for (int i = 0; i < 8; i++) {
      if (!Character.isDigit(ch[i])) {
        return false;
      }
    }
    int begin_eight = Integer.parseInt(substring);
    if ((Character.isUpperCase(ch[8])) && (Character.isUpperCase(ch[9])) && (begin_eight >= 18500101)) {
      return DATEFormatterValidator.getInstance().isDataValid(substring);
    }
    return false;
  }
  
  private boolean checkNovelChinaID(String ID, char firstChar, char secondChar)
  { // 大陸有統一證號者
    char[] ch = ID.substring(2).toCharArray();
    String value = "";
    if (secondChar == 'A') {
      value = "0";
    } else if (secondChar == 'B') {
      value = "1";
    }
    String substring = value + ID.substring(2);
    return validWeight(ID, substring, firstChar, ch);
  }
  
  private boolean checkChinaID(String ID)
  { // 大陸無統一證號
    if (ID.length() == 8)
    {
      boolean isBlankOfEight = isBlank(ID.substring(7, 8));
      if (!isBlankOfEight) {
        return false;
      }
    }
    else if (ID.length() == 9)
    {
      boolean isBlankOfEight = isBlank(ID.substring(7, 8));
      boolean isBlankOfNine = isBlank(ID.substring(8, 9));
      if ((!isBlankOfEight) || (!isBlankOfNine)) {
        return false;
      }
    }
    else if (ID.length() == 10)
    {
      boolean isBlankOfEight = isBlank(ID.substring(7, 8));
      boolean isBlankOfNine = isBlank(ID.substring(8, 9));
      boolean isBlankOfTen = isBlank(ID.substring(9, 10));
      if ((!isBlankOfEight) || (!isBlankOfNine) || (!isBlankOfTen)) {
        return false;
      }
    }
    else if (ID.length() > 10)
    {
      return false;
    }
    String yy1 = "19" + ID.substring(1, 3);
    String yy2 = "20" + ID.substring(1, 3);
    String mm = ID.substring(3, 5);
    String dd = ID.substring(5, 7);
    String west1 = yy1 + "-" + mm + "-" + dd;
    String west2 = yy2 + "-" + mm + "-" + dd;
    boolean flag = DATEFormatterValidator.getInstance().isDataValid(DateUtil.toDateTWYM(west1));
    if (flag) {
      return flag;
    }
    flag = DATEFormatterValidator.getInstance().isDataValid(DateUtil.toDateTWYM(west2));
    return flag;
  }
  
  private boolean validWeight(String ID, String substring, char firstChar, char[] ch)
  {
    int en_num = 0;
    for (int i = 0; i < 8; i++) {
      if (!Character.isDigit(ch[i])) {
        return false;
      }
    }
    for (int i = 0; i < locationChar.length; i++) {
      if (firstChar == locationChar[i])
      {
        en_num = locationNumber[i];
        break;
      }
    }
    String format_ID = new Integer(en_num).toString() + substring;
    try
    {
      int sum = 0;
      int result = 0;
      for (int i = 0; i < TWID_Regulation.length; i++)
      {
        result = Integer.parseInt(format_ID.substring(i, i + 1)) * TWID_Regulation[i] % 10;
        sum += result;
      }
      return sum % 10 == 0;
    }
    catch (Throwable e) {}
    return false;
  }
  public static boolean isBlank(String str) {
    return str == null || str.trim().isEmpty();
  }
}
