package com.validat.validator;

public class BANFormatterValidator
{
  private static BANFormatterValidator instance = null;
  private static int[] COMPANY_ID_Regulation = { 1, 2, 1, 2, 1, 2, 4, 1 };
  
  public static BANFormatterValidator getInstance()
  {
    if (instance == null) {
      instance = new BANFormatterValidator();
    }
    return instance;
  }
  
  public boolean isDataValid(String object)
  {
    if ((object == null) || (object.length() != 8)) {
      return false;
    }
    try
    {
      int sum = 0;
      for (int i = 0; i < COMPANY_ID_Regulation.length; i++)
      {
        int aMultiply = Integer.parseInt(object.substring(i, i + 1)) * COMPANY_ID_Regulation[i];
        
        int aAddition = aMultiply / 10 + aMultiply % 10;
        


        sum += aAddition;
      }
      String num = object.substring(6, 7);
      
      int mode = sum % 10;
      if (("7".equals(num)) && (mode == 9)) {
        return true;
      }
      return mode == 0;
    }
    catch (Throwable e) {}
    return false;
  }
}
