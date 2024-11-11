package com.validat.validator;

public class DATEFormatterValidator
{
  private static DATEFormatterValidator instance = null;
  
  public static DATEFormatterValidator getInstance()
  {
    if (instance == null) {
      instance = new DATEFormatterValidator();
    }
    return instance;
  }
  
  public boolean isDataValid(String object)
  {
    char[] charArray = object.toCharArray();
    boolean flag = true;
    for (int i = 0; i < charArray.length; i++) {
      if (!Character.isDigit(charArray[i])) {
        flag = false;
      }
    }
    if (flag)
    {
      if ((object != null) && (!"".equals(object)) && (object.length() <= 7) && (object.length() > 0)) {
        return CheckTWDateFormate(object);
      }
      if ((object != null) && (!"".equals(object)) && (object.length() == 8) && (object.length() > 0)) {
        return CheckWestDateFormate(object);
      }
      return false;
    }
    return false;
  }
  
  private boolean CheckWestDateFormate(String object)
  {
    try
    {
      Integer dateYY = new Integer(object.substring(0, 4));
      Integer dateMM = new Integer(object.substring(4, 6));
      Integer dateDD = new Integer(object.substring(6));
      return checkLeapYear(dateYY.intValue(), dateMM.intValue(), dateDD.intValue());
    }
    catch (Exception e) {}
    return false;
  }
  
  private boolean CheckTWDateFormate(String object)
  {
    try
    {
      if (object.length() == 6) {
        object = 0 + object;
      }
      Integer dateYY = new Integer(object.substring(0, 3));
      Integer dateMM = new Integer(object.substring(3, 5));
      Integer dateDD = new Integer(object.substring(5, 7));
      int westYY = dateYY.intValue() + 1911;
      return checkLeapYear(westYY, dateMM.intValue(), dateDD.intValue());
    }
    catch (Exception e) {}
    return false;
  }
  
  private boolean checkLeapYear(int dateYY, int dateMM, int dateDD)
  {
    int numDays = 0;
    if ((dateMM > 12) || (dateMM <= 0)) {
      return false;
    }
    switch (dateMM)
    {
    case 1: 
    case 3: 
    case 5: 
    case 7: 
    case 8: 
    case 10: 
    case 12: 
      numDays = 31;
      if (dateDD > numDays) {
        return false;
      }
      break;
    case 4: 
    case 6: 
    case 9: 
    case 11: 
      numDays = 30;
      if (dateDD > numDays) {
        return false;
      }
      break;
    case 2: 
      if (((dateYY % 4 == 0) && (dateYY % 100 != 0)) || (dateYY % 400 == 0))
      {
        numDays = 29;
        if (dateDD > numDays) {
          return false;
        }
      }
      else
      {
        numDays = 28;
        if (dateDD > numDays) {
          return false;
        }
      }
      break;
    }
    return true;
  }
}
