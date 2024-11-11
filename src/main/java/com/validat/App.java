package com.validat;

import com.validat.util.DateUtil;
import com.validat.validator.BANFormatterValidator;
import com.validat.validator.DATEFormatterValidator;
import com.validat.validator.IDNFormatterValidator;


/**
 * testMain
 */
public class App {
    public static void main(String[] args) {

        // 統編測試
        System.out.println("統編檢核測試");
        String[] bans = {
                "96979933", // 中華電信股份有限公司
                "22099131", // 台灣積體電路製造股份有限公司
                "04541302", // 鴻海精密工業股份有限公司
                "84149961", // 聯發科技股份有限公司
                "23638777", // 華碩電腦股份有限公司
                "29047665", // 宏達電股份有限公司
                "10458575", // 安迪亞美容坊
                "12345678", // 假公司1
                "87654321", // 假公司2
                "22334455"  // 假公司3
        };

        BANFormatterValidator instance = BANFormatterValidator.getInstance();

        for (String ban : bans) {
            System.out.println("統一編號: " + ban + " 是符合統編格式? " + instance.isDataValid(ban));
        }
        // 身分證測試 國稅務識別碼
        System.out.println("身份證檢核測試");
        String[] ids = {
                "A123456789", // 有效的台灣身份證1
                "S152892614", // 有效的台灣身份證2
                "AC94737017", // 有效的舊式新住民身份證1
                "SB40558330", // 有效的舊式新住民身份證2
                "P908661184", // 有效的新式新住民身份證1
                "A800000014", // 有效的新式新住民身份證2
                "9710511",  // 有效的無統一證號的大陸人1
                "9990412",   // 有效的無統一證號的大陸人2
                "A12345678",  // 無效（長度錯誤）
                "A12345678A", // 無效（含有非數字字符）
                "12345678",   // 無效（以數字開頭）
                "Z98765432",  // 無效（不符合規則）
                "A111111111", // 無效（檢查數字權重）
                "A1234567",   // 無效（少於9位數字）
                "C23456781",  // 無效（新住民身份證，權重檢查失敗）
                "12345678"   // 中國身份證格式錯誤
        };

        IDNFormatterValidator instance_idn = IDNFormatterValidator.getInstance();

        for (String id : ids) {
            System.out.println("身分證號碼: " + id + " 是符合身份證格式? " + instance_idn.isDataValid(id));
        }

        // 日期測試
        System.out.println("日期檢核測試");
        String[] testDates = {
                "20230101",  // 有效
                "20230229",  // 無效 (不是閏年)
                "1110101",   // 有效
                "1110132",   // 無效 (日期不合法)
                "2023-01-01", // 無效 (包含非數字字符）
                "",          // 無效 (空字符串）
                "20231",     // 無效 (太短)
                "2023010101", // 無效 (太長)
                "20240229",  // 有效 (閏年)
                DateUtil.toDateTWYM("2023-01-01"), // 日期轉換
                "20231301"   // 無效 (月份超出範圍）
        };
        DATEFormatterValidator dateValidator = DATEFormatterValidator.getInstance();

        for (String date : testDates) {
            System.out.println("日期: " + date + " 是符合日期格式? " + dateValidator.isDataValid(date));
        }
    }
}
