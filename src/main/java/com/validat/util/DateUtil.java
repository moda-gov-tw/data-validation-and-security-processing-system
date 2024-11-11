package com.validat.util;

import com.validat.validator.DATEFormatterValidator;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil
{
  public static final int YEAR = 1;
  public static final int MONTH = 2;
  public static final int DAY = 3;
  public static final int ROC_YEAR_TO_GREGORIAN_YEAR_DIFF = 1911;
  private static DateUtil instance = null;
  private static final String[] weekDayArray = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
  public Calendar _cal;
  
  public DateUtil()
  {
    this._cal = Calendar.getInstance();
  }
  
  public static DateUtil getInstance()
  {
    if (instance == null) {
      instance = new DateUtil();
    }
    return instance;
  }
  
  public static final String getCurrentDateTime()
  {
    return formatDateAsSqlString(new java.util.Date());
  }
  
  public static final java.util.Date parseDate(String yyyymmdd)
  {
    if (yyyymmdd != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      return sdf.parse(yyyymmdd, new ParsePosition(0));
    }
    return null;
  }
  
  public static final java.util.Date parseRocDate(String yyymmdd)
  {
    if (!checkDate(yyymmdd)) {
      throw new IllegalArgumentException("Invalid roc date: " + yyymmdd);
    }
    int year = Integer.parseInt(yyymmdd.substring(0, 3));
    int month = Integer.parseInt(yyymmdd.substring(3, 5));
    int dayOfMonth = Integer.parseInt(yyymmdd.substring(5));
    Calendar calendar = new GregorianCalendar(year + 1911, month - 1, dayOfMonth);
    return calendar.getTime();
  }
  
  public static final boolean checkDate(String yyymmdd)
  {
    if ((yyymmdd == null) || (yyymmdd.length() != 7)) {
      return false;
    }
    int year = Integer.parseInt(yyymmdd.substring(0, 3)) + 1911;
    int month = Integer.parseInt(yyymmdd.substring(3, 5));
    int day = Integer.parseInt(yyymmdd.substring(5));
    if (month > 12) {
      return false;
    }
    if ((year % 4 == 0) && (year % 400 == 0))
    {
      if ((month == 2) && 
        (day > 29)) {
        return false;
      }
    }
    else if ((year % 4 == 0) && (year % 400 != 0) && (year % 100 != 0))
    {
      if ((month == 2) && 
        (day > 29)) {
        return false;
      }
    }
    else if ((month == 2) && 
      (day > 28)) {
      return false;
    }
    if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10) || (month == 12))
    {
      if (day > 31) {
        return false;
      }
    }
    else if (day > 30) {
      return false;
    }
    return true;
  }
  
  public static final java.util.Date getBeginOfTheDay(java.util.Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(11, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    
    Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
    timestamp.setNanos(0);
    return timestamp;
  }
  
  public static final java.util.Date getEndOfTheDay(java.util.Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(11, 23);
    calendar.set(12, 59);
    calendar.set(13, 59);
    calendar.set(14, 999);
    
    Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
    timestamp.setNanos(999999999);
    return timestamp;
  }
  
  public static final String formatDateAsSqlString(java.util.Date date)
  {
    return formatDateAsSqlString(date, ' ');
  }
  
  public static final String formatDateAsSqlString(java.util.Date date, char splitter)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'" + splitter + "'HH:mm:ss");
      return sdf.format(date);
    }
    return null;
  }
  
  public static String formatDate(java.util.Date date, String simpleDateFormat)
  {
    if (simpleDateFormat == null) {
      throw new IllegalArgumentException("simpleDateFormat must be specified.");
    }
    Calendar cal = Calendar.getInstance();
    if (date != null) {
      cal.setTimeInMillis(date.getTime());
    }
    int currentYear = cal.get(1);
    
    NumberFormat nf = new DecimalFormat("000");
    String twy = nf.format(currentYear - 1911);
    
    simpleDateFormat = simpleDateFormat.replaceAll("twy", twy);
    

    SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
    String formatedTime = sdf.format(cal.getTime());
    
    return formatedTime;
  }
  
  public static String getCurrentTimeInFormat(String dateFormat)
  {
    return formatDate(null, dateFormat);
  }
  
  public static final String formateRocDate(String yyymmdd)
  {
    if (!checkDate(yyymmdd)) {
      throw new IllegalArgumentException("Invalid roc date: " + yyymmdd);
    }
    String rocDateString = yyymmdd.substring(0, 3) + "/" + yyymmdd.substring(3, 5) + "/" + yyymmdd.substring(5, 7);
    return rocDateString;
  }
  
  public static final String formateRocDateTime(String yyymmddhhmmss)
  {
    if ((yyymmddhhmmss == null) || (yyymmddhhmmss.length() != 13)) {
      throw new IllegalArgumentException("Invalid roc datetime: " + yyymmddhhmmss);
    }
    String yyymmdd = formateRocDate(yyymmddhhmmss.substring(0, 7));
    String hhmmss = yyymmddhhmmss.substring(7, 13);
    if (!hhmmss.matches("([0-1]\\d|2[0-3])([0-5]\\d)([0-5]\\d)")) {
      throw new IllegalArgumentException("Invalid roc datetime: " + hhmmss);
    }
    String rocDateString = yyymmdd + " " + hhmmss.substring(0, 2) + ":" + hhmmss.substring(2, 4) + ":" + hhmmss.substring(4, 6);
    

    return rocDateString;
  }
  
  public static final String getDateByDays(String dateTWY, int days, String flag)
  {
    String dateString = "";
    
    boolean result = DATEFormatterValidator.getInstance().isDataValid(dateTWY);
    if (!result) {
      return "CH0010";
    }
    String westDate = toDateYM(dateTWY);
    String replaceWestDate = westDate.replace('-', '/');
    try
    {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
      java.util.Date date = ft.parse(replaceWestDate);
      cal.setTime(date);
      if ("-".equals(flag)) {
        days = -days;
      }
      cal.add(5, days);
      date = cal.getTime();
      ft = new SimpleDateFormat("yyyy-MM-dd");
      dateString = toDateTWYM(ft.format(date));
    }
    catch (Exception e) {}
    return dateString;
  }
  
  public static final String getCurrentTWDate()
  {
    return getCurrentTWYMD() + formTimeString(new java.util.Date(), "HHmmssSSS");
  }
  
  public static final String getCurrentDate()
  {
    return formTimeString(new java.util.Date(), "yyyyMMddHHmmssSSS");
  }
  
  public static String[] getWeekDay(String _date)
  {
    String westDate = toDateYM(_date);
    String weekDay = formTWTimeString(toDate(westDate), "EEE");
    String[] week = new String[2];
    if (weekDay != null)
    {
      for (int i = 0; i < weekDayArray.length; i++) {
        if (weekDay.equals(weekDayArray[i]))
        {
          week[0] = new Integer(i).toString();
          week[1] = weekDayArray[i].substring(2);
          return week;
        }
      }
      week[0] = "CH0010";
      week[1] = "CH0010";
      return week;
    }
    week[0] = "CH0010";
    week[1] = "CH0010";
    return week;
  }
  
  public static String toDateYM(String dateTWY)
  {
    boolean result = DATEFormatterValidator.getInstance().isDataValid(dateTWY);
    if (!result) {
      return null;
    }
    if (dateTWY.length() == 6) {
      dateTWY = 0 + dateTWY;
    }
    Integer dateYY = new Integer(dateTWY.substring(0, 3));
    Integer dateMM = new Integer(dateTWY.substring(3, 5));
    Integer dateDD = new Integer(dateTWY.substring(5, 7));
    int westYY = dateYY.intValue() + 1911;
    return westYY + "-" + dateMM + "-" + dateDD;
  }
  
  public static final String toDateTWYM(String westYMD)
  {
    String[] date = westYMD.split("-");
    Integer year = new Integer(date[0]);
    year = Integer.valueOf(year.intValue() - 1911);
    return year.toString() + date[1] + date[2];
  }
  
  public static final String getDayDiff(String StartDate, String EndDate)
  {
    boolean startDateFlag = DATEFormatterValidator.getInstance().isDataValid(StartDate);
    
    boolean endDateFlag = DATEFormatterValidator.getInstance().isDataValid(EndDate);
    if ((startDateFlag) && (endDateFlag))
    {
      String westStartDate = toDateYM(StartDate);
      String westEndDate = toDateYM(EndDate);
      String dStart = westStartDate.replace('-', '/');
      String dEnd = westEndDate.replace('-', '/');
      long days = 0L;
      try
      {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date dateStart = ft.parse(dStart);
        java.util.Date dateEnd = ft.parse(dEnd);
        days = dateStart.getTime() - dateEnd.getTime();
        days = Math.abs(days) / 1000L / 60L / 60L / 24L;
      }
      catch (Exception e) {}
      Long dateL = new Long(days);
      return dateL.toString();
    }
    return "CH0010";
  }
  
  public static final String getToday()
  {
    return formSQLDate(new java.util.Date());
  }
  
  public static final String getNowDateTime()
  {
    return formSQLDateTime(new java.util.Date());
  }
  
  public static final java.util.Date getDate()
  {
    return toDate(new java.util.Date());
  }
  
  public static final String formSQLDate(String ymd)
  {
    int ymdNo = Integer.parseInt(ymd);
    if (ymdNo < 10000000) {
      ymdNo += 19110000;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return formSQLDate(sdf.parse("" + ymdNo, new ParsePosition(0)));
  }
  
  public static final String formSQLDate(String ymd, String sp)
  {
    String[] splitymd = ymd.split(sp);
    String newYMD = "" + (Integer.parseInt(splitymd[0]) * 10000 + Integer.parseInt(splitymd[1]) * 100 + Integer.parseInt(splitymd[2]));
    

    return formSQLDate(newYMD);
  }
  
  public static final String formSQLDate(java.util.Date date)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      return sdf.format(date);
    }
    return null;
  }
  
  public static final java.sql.Date toSQLDate(java.util.Date d)
  {
    if (d != null) {
      return new java.sql.Date(d.getTime());
    }
    return null;
  }
  
  public static final Timestamp toSQLDateTime(java.util.Date d)
  {
    if (d != null) {
      return new Timestamp(d.getTime());
    }
    return null;
  }
  
  public static final java.util.Date toDate(Object sd)
  {
    if ((sd instanceof String)) {
      return toDate((String)sd);
    }
    if ((sd instanceof java.util.Date)) {
      return toDate((java.util.Date)sd);
    }
    return null;
  }
  
  public static final java.util.Date toDateOrTime(String sd, char split)
  {
    java.util.Date retDate = null;
    if (sd != null) {
      if (sd.indexOf(":") > 0) {
        retDate = toDateTime(sd, split);
      } else {
        retDate = toDate(sd);
      }
    }
    return retDate;
  }
  
  public static final java.util.Date toDate(String sd)
  {
    if (sd != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      return sdf.parse(sd, new ParsePosition(0));
    }
    return null;
  }
  
  public static final java.util.Date toDate(java.util.Date d)
  {
    if (d != null) {
      return toDate(formSQLDate(d));
    }
    return d;
  }
  
  public static int compareYM(Object date, Object base)
  {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(toDate(date));
    int d1 = tmpCal.get(1) * 100 + tmpCal.get(2);
    tmpCal.setTime(toDate(base));
    int d2 = tmpCal.get(1) * 100 + tmpCal.get(2);
    return d1 - d2;
  }
  
  public static final boolean equals(java.util.Date d1, java.util.Date d2)
  {
    long t1 = 0L;
    long t2 = 0L;
    if (d1 != null) {
      t1 = d1.getTime();
    }
    if (d2 != null) {
      t2 = d2.getTime();
    }
    return t1 == t2;
  }
  
  public static final int getYear()
  {
    return Calendar.getInstance().get(1);
  }
  
  public static final int getMonth()
  {
    return Calendar.getInstance().get(2) + 1;
  }
  
  public static final int getDay()
  {
    return Calendar.getInstance().get(5);
  }
  
  public int getCYear()
  {
    return this._cal.get(1);
  }
  
  public int getCMonth()
  {
    return this._cal.get(2) + 1;
  }
  
  public int getCDay()
  {
    return this._cal.get(5);
  }
  
  public static final int addMonthInYM(String ym, int am)
  {
    int retVal = 0;
    if (ym != null) {
      try
      {
        retVal = addMonthInYM(Integer.parseInt(ym), am);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return retVal;
  }
  
  public static final int addMonthInYM(int ym, int am)
  {
    int iy = ym / 100 + am / 12;
    int im = ym % 100 + am % 12;
    if (im < 1)
    {
      im += 12;
      iy--;
    }
    if (im > 12)
    {
      im -= 12;
      iy++;
    }
    return iy * 100 + im;
  }
  
  public static final String addMonth(java.util.Date base, int mon)
  {
    return formSQLDate(addMonthDate(base, mon));
  }
  
  public static final java.util.Date addMonthDate(java.util.Date base, int mon)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(base);
    c.add(2, mon);
    return c.getTime();
  }
  
  public static String addDate(java.util.Date nowDate, int dayType, int delta)
  {
    String retStr = null;
    switch (dayType)
    {
    case 1: 
      retStr = addYMD(nowDate, delta, 0, 0);
      break;
    case 2: 
      retStr = addYMD(nowDate, 0, delta, 0);
      break;
    case 3: 
      retStr = addYMD(nowDate, 0, 0, delta);
      break;
    }
    return retStr;
  }
  
  public static final String addYMD(java.util.Date base, int y, int m, int d)
  {
    return formSQLDate(addYMDDate(base, y, m, d));
  }
  
  public static final java.util.Date addYMDDate(java.util.Date base, int y, int m, int d)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(base);
    c.add(1, y);
    c.add(2, m);
    c.add(6, d);
    return c.getTime();
  }
  
  public static final java.util.Date addHMSDate(java.util.Date base, int h, int m, int s)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(base);
    c.add(11, h);
    c.add(12, m);
    c.add(13, s);
    return c.getTime();
  }
  
  public static final int getCurrentYM()
  {
    DateUtil du = new DateUtil();
    return du.getCYear() * 100 + du.getCMonth();
  }
  
  public static final int getCurrentTWYM()
  {
    DateUtil du = new DateUtil();
    return (du.getCYear() - 1911) * 100 + du.getCMonth();
  }
  
  public static final int getCurrentTWYMD()
  {
    DateUtil du = new DateUtil();
    return (du.getCYear() - 1911) * 10000 + du.getCMonth() * 100 + du.getCDay();
  }
  
  public static long calculateDueTime(int day)
  {
    Calendar c = Calendar.getInstance();
    c.add(5, day);
    c.set(10, 0);
    return c.getTimeInMillis();
  }
  
  public static int getMaxDate(int y, int m)
  {
    GregorianCalendar cr = new GregorianCalendar(y, m - 1, 1);
    return cr.getActualMaximum(5);
  }
  
  public static int getMaxDate(String ym)
  {
    int a = toADYM(Integer.parseInt(ym));
    return getMaxDate(a / 100, a % 100);
  }
  
  public static final java.util.Date toDateTime(String time)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return sdf.parse(time, new ParsePosition(0));
  }
  
  public static final java.util.Date toDateTime(String time, char split)
  {
    if (hasData(time))
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'" + split + "'HH:mm:ss");
      return sdf.parse(time, new ParsePosition(0));
    }
    return null;
  }
  
  public static final String toADYM(String ym)
  {
    int tmpym = Integer.parseInt(ym);
    return "" + toADYM(tmpym);
  }
  
  public static final int toADYM(int ym)
  {
    if (ym > 99900) {
      return ym;
    }
    int tmp_m = ym % 100;
    int tmp_y = ym / 100;
    if ((tmp_m > 12) || (tmp_m < 1)) {
      return ym;
    }
    if (tmp_y > 999) {
      return ym;
    }
    return ym + 191100;
  }
  
  public static final int getDateYear(java.util.Date d)
  {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(d);
    return tmpCal.get(1);
  }
  
  public static final int getDateYear(Object d)
  {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(toDate(d));
    return tmpCal.get(1);
  }
  
  public static final int getDateMonth(java.util.Date d)
  {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(d);
    return tmpCal.get(2) + 1;
  }
  
  public static final int getDateMonth(Object d)
  {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(toDate(d));
    return tmpCal.get(2) + 1;
  }
  
  public static final int getDateDay(Object d)
  {
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(toDate(d));
    return tmpCal.get(5);
  }
  
  public static final int getDateYM(Object d)
  {
    Calendar tmpCal = Calendar.getInstance();
    if ((d instanceof java.util.Date)) {
      tmpCal.setTime((java.util.Date)d);
    } else {
      tmpCal.setTime(toDate(d));
    }
    return tmpCal.get(1) * 100 + tmpCal.get(2) + 1;
  }
  
  public static final String formSQLDateTime(java.util.Date date)
  {
    return formSQLDateTime(date, ' ');
  }
  
  public static final String formSQLDateTime(java.util.Date date, char split)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'" + split + "'HH:mm:ss");
      return sdf.format(date);
    }
    return null;
  }
  
  public static final String formTimeString(java.util.Date date)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
      return sdf.format(date);
    }
    return null;
  }
  
  private static final String formTimeString(java.util.Date date, String dateFormat)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
      return sdf.format(date);
    }
    return null;
  }
  
  private static final String formTWTimeString(java.util.Date date, String dateFormat)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.TAIWAN);
      return sdf.format(date);
    }
    return null;
  }
  
  public static final String formTimeStringIn8(java.util.Date date)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      return sdf.format(date);
    }
    return null;
  }
  
  public static final java.util.Date getFirstDateTime(java.util.Date base, int mon, String hms)
  {
    return getSPDateTime(base, mon, 1, hms);
  }
  
  public static final java.util.Date getMaxDateTime(java.util.Date base, int mon, String hms)
  {
    return getSPDateTime(base, mon, 100, hms);
  }
  
  public static final java.util.Date getSPDateTime(java.util.Date base, int mon, int d, String hms)
  {
    String tmpHMS = null;
    String tmpYMD = null;
    if (hasData(hms)) {
      tmpHMS = hms;
    } else {
      tmpHMS = formTimeStringIn8(base);
    }
    if (mon != 0) {
      tmpYMD = addMonth(base, mon);
    } else {
      tmpYMD = formSQLDate(base);
    }
    Calendar tmpCal = Calendar.getInstance();
    tmpCal.setTime(toDateTime(tmpYMD + "T" + tmpHMS));
    if (d > 0)
    {
      if (d > 99) {
        d = tmpCal.getActualMaximum(5);
      }
      tmpCal.set(5, d);
    }
    return tmpCal.getTime();
  }
  
  public static boolean hasData(String s)
  {
    if ((s != null) && (s.length() > 0)) {
      return true;
    }
    return false;
  }
  
  public static String getFormattedDate(Timestamp date, String simpleDateFormat)
  {
    if (hasData(simpleDateFormat))
    {
      Calendar cal = Calendar.getInstance();
      if (date != null) {
        cal.setTimeInMillis(date.getTime());
      }
      int currentYear = cal.get(1);
      
      NumberFormat nf = new DecimalFormat("000");
      String twy = nf.format(currentYear - 1911);
      
      simpleDateFormat = simpleDateFormat.replaceAll("twy", twy);
      

      SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
      String formatedTime = sdf.format(cal.getTime());
      
      return formatedTime;
    }
    return null;
  }
  
  public static String getFormattedDate(String simpleDateFormat)
  {
    return getFormattedDate(null, simpleDateFormat);
  }
  
  public static final java.util.Date parseFormattedDate(String dateTimeStr, String simpleDateFormat)
  {
    if (dateTimeStr == null) {
      return null;
    }
    if (simpleDateFormat == null) {
      simpleDateFormat = "twy/MM/dd";
    }
    int startPos = simpleDateFormat.indexOf("twy");
    if (startPos >= 0)
    {
      String before = "";
      String twy = "";
      String after = "";
      if (startPos > 0) {
        before = dateTimeStr.substring(0, startPos);
      }
      twy = dateTimeStr.substring(startPos, startPos + 3);
      if (startPos + 3 < dateTimeStr.length()) {
        after = dateTimeStr.substring(startPos + 3, dateTimeStr.length());
      }
      NumberFormat nf = new DecimalFormat("000");
      try
      {
        int twYear = nf.parse(twy).intValue() + 1911;
        simpleDateFormat = simpleDateFormat.replaceAll("twy", "yyyy");
        dateTimeStr = before + twYear + after;
      }
      catch (ParseException e)
      {
        e.printStackTrace();
        return null;
      }
    }
    SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
    java.util.Date retDate = null;
    retDate = sdf.parse(dateTimeStr, new ParsePosition(0));
    return retDate;
  }
  
  public static final java.util.Date parseFormattedDateTime(String dateTimeStr, String simpleDateFormat)
  {
    if (dateTimeStr == null) {
      return null;
    }
    if (simpleDateFormat == null) {
      simpleDateFormat = "twy/MM/dd HH:mm:ss";
    }
    return parseFormattedDate(dateTimeStr, simpleDateFormat);
  }
  
  public static final Timestamp parseFormattedTimestamp(String dateTimeStr, String simpleDateFormat)
  {
    if (dateTimeStr == null) {
      return null;
    }
    if (simpleDateFormat == null) {
      simpleDateFormat = "twy/MM/dd HH:mm:ss";
    }
    java.util.Date resDate = parseFormattedDate(dateTimeStr, simpleDateFormat);
    if (resDate == null) {
      return null;
    }
    return dateToTimestamp(resDate);
  }
  
  public static final Timestamp dateToTimestamp(java.util.Date date)
  {
    if (date == null) {
      return null;
    }
    return new Timestamp(date.getTime());
  }
  
  public static final java.util.Date timestampToDate(Timestamp dateTime)
  {
    if (dateTime == null) {
      return null;
    }
    return new java.util.Date(dateTime.getTime());
  }
}
