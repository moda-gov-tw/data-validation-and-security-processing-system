/**
 *
 */
package utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * @author
 *
 */
public class DigitToChineseUtils {

    /**
     * 傳入數字的字串後回傳相對應的中文，如傳入1234後將回傳壹仟倆百參拾肆
     */
    public static String dollar2Chinese(final String st_orgNm) {
        if ("null".equals(st_orgNm) || null == st_orgNm || StringUtils.equals("0", st_orgNm))
            return "零";

        Long orgNm = Long.valueOf(st_orgNm);

        String result = "";

        int length = 1;
        while ((orgNm /= 10) != 0) {
            length++;
        }

        orgNm = Long.valueOf(st_orgNm);

        String s1 = "";
        String theString = "";//theString : 正在轉換中文的數字
        boolean t = false; //t => 確定是否需要補 "億"、"萬"
        for (int index = length - 1; index >= 0; index--) {
            theString = change_1(orgNm, index);
            if (theString.equals("零")) {
                final String tail = StringUtils.substring(result, StringUtils.length(result) - 1, StringUtils.length(result));
                if (!StringUtils.equals("兆", tail) && !StringUtils.equals("億", tail)) {
                    if (t && (index & 3) == 0) {
                        result += wei_unit(index); //補 "億"、"萬"
                        s1 = "";
                        t = false;
                    } else {
                        s1 = "零"; //記錄 s2 之前是否有 "零" 出現
                    }
                } else {
                    s1 = "零"; //記錄 s2 之前是否有 "零" 出現
                }
            } else {
                result += s1 + theString;
                s1 = "";
                t = true; //有 theString !="零" 要留意補 "億"、"萬"
            }
        }

        return result;
    }

    private static String change_1(Long orgNm, final int index) {
        //orgNm右至左，index=0開始算
        for (int i = 0; i < index; i++) {
            orgNm /= 10;
        }
        orgNm %= 10;

        final String st = wei_chinese(orgNm);

        if (st.equals("零")) {
            return "零";
        } else {
            return st + wei_unit(index);
        }
    }

    private static String wei_chinese(final Long num) {
        final Integer numInt = Integer.valueOf(num == null ? "0" : num.toString());

        switch (numInt) {
            case 0:
                return "零";
            case 1:
                return "壹";
            case 2:
                return "貳";
            case 3:
                return "參";
            case 4:
                return "肆";
            case 5:
                return "伍";
            case 6:
                return "陸";
            case 7:
                return "柒";
            case 8:
                return "捌";
            case 9:
                return "玖";
            default:
                return "";
        }
    }

    private static String wei_unit(final int wei) {
        switch (wei) {
            case 1:
                return "拾";
            case 2:
                return "佰";
            case 3:
                return "仟";
            case 4:
                return "萬";
            case 5:
                return "拾";
            case 6:
                return "佰";
            case 7:
                return "仟";
            case 8:
                return "億";
            case 9:
                return "拾";
            case 10:
                return "佰";
            case 11:
                return "仟";
            case 12:
                return "兆";
            case 13:
                return "拾";
            case 14:
                return "佰";
            case 15:
                return "仟";
            default:
                return "";
        }

    }

    /**
     * 繳費方式轉換
     */
    public static ArrayList<String> casePtyp2Chinese(final String casePtyp, final String cntrType) {
        final ArrayList<String> list = new ArrayList<String>();
        switch (casePtyp) {
            case "1":
                list.add("一");
                list.add("每");
                return list;
            case "2":
                list.add("二");
                list.add("二、四、六、八、十、十二");
                return list;
            case "3":
                list.add("三");
                list.add("三、六、九、十二");
                return list;
            case "6":
                list.add("六");
                list.add("六、十二");
                return list;
            case "12":
                if ("06".equals(cntrType) || "08".equals(cntrType)) {
                    list.add("十二");
                    list.add("一");
                } else {
                    list.add("十二");
                    list.add("十二");
                }
                return list;
            default:
                return list;
        }
    }

    /**
     * 阿拉伯數字轉國字
     */
    public static String numToBig(final int number) {
        String result = String.valueOf(number);
        if (result.length() == 2) {
            final String str1 = result.substring(0, 1);
            final String str2 = result.substring(1, 2);
            if ("1".equals(str1)) {
                result = "十" + str2;
            } else if ("2".equals(str1)) {
                result = "二十" + str2;
            } else if ("3".equals(str1)) {
                result = "三十" + str2;
            } else if ("4".equals(str1)) {
                result = "四十" + str2;
            } else if ("5".equals(str1)) {
                result = "五十" + str2;
            } else if ("6".equals(str1)) {
                result = "六十" + str2;
            } else if ("7".equals(str1)) {
                result = "七十" + str2;
            } else if ("8".equals(str1)) {
                result = "八十" + str2;
            } else if ("9".equals(str1)) {
                result = "九十" + str2;
            }
            result = result.replace("0", "");
            result = result.replace("1", "一");
            result = result.replace("2", "二");
            result = result.replace("3", "三");
            result = result.replace("4", "四");
            result = result.replace("5", "五");
            result = result.replace("6", "六");
            result = result.replace("7", "七");
            result = result.replace("8", "八");
            result = result.replace("9", "九");
        } else {
            result = result.replace("0", "０");
            result = result.replace("1", "一");
            result = result.replace("2", "二");
            result = result.replace("3", "三");
            result = result.replace("4", "四");
            result = result.replace("5", "五");
            result = result.replace("6", "六");
            result = result.replace("7", "七");
            result = result.replace("8", "八");
            result = result.replace("9", "九");
        }
        return result;
    }

}
