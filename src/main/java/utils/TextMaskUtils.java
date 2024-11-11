package utils;

public class TextMaskUtils {
    //================================================
    //== [Enumeration types] Block Start
    //== [Enumeration types] Block End
    //================================================
    //== [static variables] Block Start
    private final static String MASK_NOTE = "O";

    //== [static variables] Block Stop
    //================================================
    //== [instance variables] Block Start
    //== [instance variables] Block Stop
    //================================================
    //== [static Constructor] Block Start
    //== [static Constructor] Block Stop
    //================================================
    //== [Constructors] Block Start (含init method)
    //== [Constructors] Block Stop
    //================================================
    //== [Static Method] Block Start
    //== [Static Method] Block Stop
    //================================================
    //== [Accessor] Block Start
    //== [Accessor] Block Stop
    //================================================
    //== [Overrided JDK Method] Block Start (Ex. toString / equals+hashCode)
    //== [Overrided JDK Method] Block Stop
    //================================================
    //== [Method] Block Start
    //####################################################################
    //## [Method] sub-block :
    //####################################################################
    public static String mast(final String src) {
        return mast(src, MASK_NOTE);
    }

    /**
     * 目前僅姓名遮罩
     * 1個字 全遮
     * 2個字 遮後面
     * 3個字以上 王O明 台OOOOO司
     *
     * @param src
     * @param mastNote
     * @return
     */
    public static String mast(final String src, final String mastNote) {
        if (src == null) {
            return null;
        }
        final int length = src.length();

        if (length == 1) {
            return mastNote;
        }
        if (length == 2) {
            return src.charAt(0) + mastNote;
        }
        final int total = src.length();

        final StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < total - 2; i++) {
            stringBuffer.append(mastNote);
        }
        return src.charAt(0) + stringBuffer.toString() + src.substring(src.length() - 1);

    }

    //    public static void main(final String[] args) {
    //        System.out.println(MastUtils.mast("ABGHdsdsdGssxsssd"));
    //    }
    //== [Method] Block Stop
    //================================================
    //== [Inner Class] Block Start
    //== [Inner Class] Block Stop
    //================================================
}
