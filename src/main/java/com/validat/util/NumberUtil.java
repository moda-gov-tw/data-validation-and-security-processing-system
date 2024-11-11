package com.validat.util;

import java.math.BigDecimal;

/**
 * 程式資訊摘要：數字轉換工具<P>
 * 類別名稱　　：PuaNumberUtils.java<P>
 * 程式內容說明：數字轉換工具<P>
 * 程式修改記錄：<P>
 * 2021-12-30：建立<P>
 * 
 * @author JeremyLee
 * @version 2.0
 * @since Dec 30, 2021
 */
public class NumberUtil {

    /**
     * 整數0
     */
    public final static Integer INTEGER_ZERO = Integer.valueOf(0);
    /**
     * Long 0
     */
    public final static Long LONG_ZERO = Long.valueOf(0L);
    /**
     * BigDecimal 0
     */
    public final static BigDecimal DECIMAL_ZERO = BigDecimal.ZERO;

    /**
     * 建構子
     * 此類別不應被實體化，需要透過類別來呼叫其靜態方法。
     */
    private NumberUtil() {
    }
    
    /**
     * 將輸入的字串轉換為int並回傳，當輸入的字串為null或空值，或無法轉換成int時，則回傳預設的數值
     * <pre>
     * <i>defaultIntIfEmpty</i>(null ,99) = 99
     * <i>defaultIntIfEmpty</i>(&#34;&#34; ,99) = 99
     * <i>defaultIntIfEmpty</i>(&#34;&nbsp;&#34; ,99) = 99
     * <i>defaultIntIfEmpty</i>(&#34; 100&#34; ,99) = 99
     * <i>defaultIntIfEmpty</i>(&#34;A&#34; ,99) = 99
     * <i>defaultIntIfEmpty</i>(&#34;101&#34; ,99) = 101
     * </pre>
     * @param intString 輸入的數值字串
     * @param defaultInt 預設的數值
     * @return 轉換後的數值
     */
    public static int defaultIntIfBlank(String intString, int defaultInt) {
        if (intString == null || intString.trim().isEmpty()) {
            return defaultInt;
        }
        int result = defaultInt;
        try {
            result = Integer.parseInt(intString);
        } catch (NumberFormatException e) {
        }
        return result;
    }
    
    /**
     * 將數字物件轉換為int，因DB查詢的數字型態，使用JPQL可能為Long，或使用native sql可能為BigDecimal型態，
     * 因此針對這兩種型態分別作轉換，其餘一律呼叫物件toString再轉換為int，若物件為null或無法轉換時則回傳0
     * @param numberObject 數字物件
     * @return int 轉換後的整數
     */
    public static int zeroIntIfNull(Object numberObject) {
        int zero = INTEGER_ZERO.intValue();
        if (numberObject == null) {
            return zero;
        }
        if (numberObject instanceof Number) {
            return ((Number) numberObject).intValue();
        }
        try {
            return Integer.parseInt(numberObject.toString());
        } catch (NumberFormatException e) {
            return zero;
        }
    }

    /**
     * 將數字物件轉換為long，因DB查詢的數字型態，使用JPQL可能為Long，或使用native sql可能為BigDecimal型態，
     * 因此針對這兩種型態分別作轉換，其餘一律呼叫物件toString再轉換為long，若物件為null或無法轉換時則回傳0
     * @param numberObject 數字物件
     * @return long 轉換後的數值
     */
    public static long zeroLongIfNull(Object numberObject) {
        long zero = LONG_ZERO.longValue();
        if (numberObject == null) {
            return zero;
        }
        if (numberObject instanceof Number) {
            return ((Number) numberObject).longValue();
        }
        try {
            return Long.parseLong(numberObject.toString());
        } catch (NumberFormatException e) {
            return zero;
        }
    }
    
    /**
     * 將數字物件轉換為BigDecimal物件，說明請參考{@link #zeroIntIfNull(Object)}
     * @param numberObject 數字物件
     * @return BigDecimal 轉換後的物件
     */
    public static BigDecimal zeroBigDecimalIfNull(Object numberObject) {
        if (numberObject == null) {
            return DECIMAL_ZERO;
        }
        if (numberObject instanceof BigDecimal) {
            return (BigDecimal) numberObject;
        }
        if (numberObject instanceof Number) {
            return BigDecimal.valueOf(((Number) numberObject).doubleValue());
        }
        try {
            return new BigDecimal(numberObject.toString());
        } catch (NumberFormatException e) {
            return DECIMAL_ZERO;
        }
    }
    /**
     * 判斷傳入的物件是否為null或是0
     * @param numberObject 傳入的number物件
     * @return 當傳入的number物件為0時回傳true
     */
    public static boolean isNullOrZero(Object numberObject) {
        return zeroIntIfNull(numberObject) == 0;
    }
    /**
     * 判斷物件是否為Integer 或 Long 或 BigDecimal
     * @param wrappedValue 物件
     * @return 若為Integer 或 Long 或 BigDecimal則返回true
     */
    public static boolean isNumber(Object wrappedValue) {
        if (wrappedValue == null) {
            return false;
        }
        return DECIMAL_ZERO.getClass().equals(wrappedValue.getClass())
            || LONG_ZERO.getClass().equals(wrappedValue.getClass())
            || INTEGER_ZERO.getClass().equals(wrappedValue.getClass());
    }
    /**
     * 依據傳入的物件(Integer 或 Long 或 BigDecimal)，回傳同型態且數值為0的物件
     * @param value 物件
     * @return 數值為0的物件
     */
    public static Object toZero(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return DECIMAL_ZERO;
        }
        if (value instanceof Integer) {
            return INTEGER_ZERO;
        }
        if (value instanceof Long) {
            return LONG_ZERO;
        }
        return null;
        
    }
    /**
     * 將數字物件轉換為{@link Integer}，若輸入的數字為{@code null}或為無法轉換為{@link Integer}時，則回傳{@code null}。
     * <p >
     * <ul style="list-style-type:none; line-height:14pt;" >
     *   <li ><i >toInteger</i >(null) = null</li >
     *   <li ><i >toInteger</i >(&#34;100&#34;) = 100</li >
     *   <li ><i >toInteger</i >(&#34;0.1&#34;) = null</li >
     * </ul >
     * @param numberObject 數字物件
     * @return 轉換後的數值
     */
    public static Integer toInteger(Object numberObject) {
        if (numberObject == null) {
            return null;
        }
        if (numberObject instanceof Number) {
            return Integer.valueOf(((Number) numberObject).intValue());
        }
        try {
            return Integer.parseInt(numberObject.toString());
        } catch (NumberFormatException nfe) {
            // ignore
        }
        return null;
    }
    /**
     * 將數字物件轉換為{@link BigDecimal BigDecimal}，若輸入的數字為{@code null}或
     * 為無法轉換為{@link BigDecimal BigDecimal}時，則回傳{@code null}。
     * <p >
     * <ul style="list-style-type:none; line-height:14pt;" >
     *   <li ><i >toBigDecimal</i >(null) = null</li >
     *   <li ><i >toBigDecimal</i >(&#34;100&#34;) = 100</li >
     *   <li ><i >toBigDecimal</i >(&#34;0.1&#34;) = 0.1</li >
     *   <li ><i >toBigDecimal</i >(&#34;A&#34;) = null</li >
     * </ul >
     * @param numberObject 數字物件
     * @return 轉換後的數值
     */
    public static BigDecimal toBigDecimal(Object numberObject) {
        if (numberObject == null) {
            return null;
        }
        if (numberObject instanceof BigDecimal) {
            return (BigDecimal) numberObject;
        }
        if (numberObject instanceof Number) {
            return BigDecimal.valueOf(((Number) numberObject).doubleValue());
        }
        try {
            return new BigDecimal(numberObject.toString());
        } catch (NumberFormatException e) {
            // ignore
        }
        return null;
    }
}
