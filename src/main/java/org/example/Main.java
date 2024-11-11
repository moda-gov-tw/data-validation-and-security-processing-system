package org.example;

import com.validat.validator.BANFormatterValidator;
import com.validat.validator.DATEFormatterValidator;
import com.validat.validator.IDNFormatterValidator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.apache.commons.lang3.StringUtils;

import utils.DigitToChineseUtils;
import utils.TextMaskUtils;

public class Main {
    public static void main(final String[] args) {
        final JFrame frame = new JFrame("資料檢核及安全處理系統");
        frame.setSize(500, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 使用 BoxLayout
        final BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(boxLayout);

        // 創建面板
        final JPanel panel = new JPanel();
        placeComponents(panel);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加邊框
        frame.add(panel);

        // 創建面板1
        final JPanel panel1 = new JPanel();
        placeComponents1(panel1);
        panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加邊框
        frame.add(panel1);

        // 創建面板2
        final JPanel panel2 = new JPanel();
        placeComponents2(panel2);
        panel2.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加邊框
        frame.add(panel2);

        // 創建面板3
        final JPanel panel3 = new JPanel();
        placeComponents3(panel3);
        panel3.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加邊框
        frame.add(panel3);

        // 創建面板4
        final JPanel panel4 = new JPanel();
        placeComponents4(panel4);
        panel4.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加邊框
        frame.add(panel4);

        // 創建面板5
        final JPanel panel5 = new JPanel();
        placeComponents5(panel5);
        panel5.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加邊框
        frame.add(panel5);

        // 顯示框架
        frame.setVisible(true);

    }

    private static void placeComponents(final JPanel panel) {
        panel.setLayout(null);

        // 創建title標籤
        final JLabel titleLabel = new JLabel("阿拉伯數字轉中文");
        titleLabel.setForeground(Color.BLUE); // 顯示藍色
        titleLabel.setBounds(10, 10, 150, 25);
        panel.add(titleLabel);

        // 創建文字標籤
        final JLabel userLabel = new JLabel("輸入文字:");
        userLabel.setBounds(10, 40, 80, 25);
        panel.add(userLabel);

        // 創建文字輸入框
        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 40, 165, 25);
        panel.add(userText);

        // 添加 DocumentFilter，限制只能輸入數字且最多 16 位
        ((AbstractDocument) userText.getDocument()).setDocumentFilter(new NumericDocumentFilter(16));
        panel.add(userText);

        // 創建按鈕
        final JButton convertButton = new JButton("執行");
        convertButton.setBounds(280, 40, 80, 25);
        panel.add(convertButton);

        // 創建結果顯示標籤
        final JTextField resultText = new JTextField("結果:");
        resultText.setBounds(10, 70, 300, 25);//(x,y,width,height)
        resultText.setEditable(false); // 設置為只讀模式
        panel.add(resultText);

        // 添加按鈕的事件監聽器
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String inputText = userText.getText();

                // 確保不為空且是有效的數字
                if (!inputText.isEmpty()) {
                    try {
                        // 轉換為中文數字
                        final String mosaicText = DigitToChineseUtils.dollar2Chinese(inputText);
                        resultText.setText("結果: " + mosaicText);
                        resultText.setForeground(Color.BLACK); // 顯示黑色
                    } catch (final Exception ex) {
                        resultText.setText("輸入錯誤，請輸入有效數字");
                        resultText.setForeground(Color.RED); // 顯示紅色
                    }
                } else {
                    resultText.setText("請輸入數字");
                    resultText.setForeground(Color.RED); // 顯示紅色
                }
            }
        });
    }

    // 自訂的 DocumentFilter，只允許數字輸入並限制最大字數
    static class NumericDocumentFilter extends DocumentFilter {
        private int maxDigits;

        // 構造方法，傳入最大字數
        public NumericDocumentFilter(final int maxDigits) {
            this.maxDigits = maxDigits;
        }

        @Override
        public void insertString(final FilterBypass fb, final int offset, final String string,
                final AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            // 如果當前字串長度和即將插入的字串長度超過限制，則忽略
            if ((fb.getDocument().getLength() + string.length()) <= maxDigits && string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(final FilterBypass fb, final int offset, final int length, final String text,
                final AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            // 如果替換後的總長度超過限制，則忽略
            if ((fb.getDocument().getLength() - length + text.length()) <= maxDigits && text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }

    private static void placeComponents1(final JPanel panel1) {
        panel1.setLayout(null);

        // 創建title標籤
        final JLabel titleLabel = new JLabel("資料遮罩");
        titleLabel.setForeground(Color.BLUE); // 顯示藍色
        titleLabel.setBounds(10, 10, 80, 25);
        panel1.add(titleLabel);

        // 創建文字標籤
        final JLabel userLabel = new JLabel("輸入文字:");
        userLabel.setBounds(10, 40, 80, 25);
        panel1.add(userLabel);

        // 創建文字輸入框
        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 40, 165, 25);
        panel1.add(userText);

        // 創建按鈕
        final JButton convertButton = new JButton("遮罩");
        convertButton.setBounds(280, 40, 80, 25);
        panel1.add(convertButton);

        // 創建結果顯示標籤
        final JTextField resultText = new JTextField("結果:");
        resultText.setBounds(10, 70, 300, 25);
        resultText.setEditable(false); // 設置為只讀模式
        panel1.add(resultText);

        // 添加按鈕的事件監聽器
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String inputText = userText.getText();
                if (StringUtils.isNotBlank(inputText)) {
                    final String mosaicText = TextMaskUtils.mast(inputText);
                    resultText.setText("結果: " + mosaicText);
                    resultText.setForeground(Color.BLACK); // 顯示黑色
                } else {
                    resultText.setText("結果: " + "請先輸入文字");
                    resultText.setForeground(Color.RED); // 顯示紅色
                }
            }
        });
    }

    private static void placeComponents2(final JPanel panel2) {
        panel2.setLayout(null);

        // 創建title標籤
        final JLabel titleLabel = new JLabel("Base 64 編解碼");
        titleLabel.setForeground(Color.BLUE); // 顯示藍色
        titleLabel.setBounds(10, 10, 180, 25);
        panel2.add(titleLabel);

        final JLabel userLabel = new JLabel("輸入文字:");
        userLabel.setBounds(10, 40, 80, 25);
        panel2.add(userLabel);

        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 40, 165, 25);
        panel2.add(userText);

        final JButton encodeButton = new JButton("編碼");
        encodeButton.setBounds(280, 40, 80, 25);
        panel2.add(encodeButton);

        final JButton decodeButton = new JButton("解碼");
        decodeButton.setBounds(370, 40, 80, 25);
        panel2.add(decodeButton);

        // 將 JLabel 改為 JTextField
        final JTextField resultText = new JTextField("結果:");
        resultText.setBounds(10, 70, 300, 25);
        resultText.setEditable(false); // 設置為只讀模式
        panel2.add(resultText);

        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String inputText = userText.getText();
                if (StringUtils.isNotBlank(inputText)) {
                    final String encodedText = Base64.getEncoder().encodeToString(inputText.getBytes(StandardCharsets.UTF_8));
                    resultText.setText("結果: " + encodedText);
                    resultText.setForeground(Color.BLACK); // 顯示黑色
                } else {
                    resultText.setText("結果: " + "請先輸入文字");
                    resultText.setForeground(Color.RED); // 顯示紅色
                }
            }
        });

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final String inputText = userText.getText();
                    if (StringUtils.isNotBlank(inputText)) {
                        final String decodedText = new String(Base64.getDecoder().decode(inputText.replaceAll(" ", "")),
                                StandardCharsets.UTF_8);
                        resultText.setText("結果: " + decodedText);
                        resultText.setForeground(Color.BLACK); // 顯示黑色
                    } else {
                        resultText.setText("結果: " + "請先輸入文字");
                        resultText.setForeground(Color.RED); // 顯示紅色
                    }
                } catch (final IllegalArgumentException ex) {
                    resultText.setText("無效的輸入! 請再試一次.");
                }
            }
        });
    }

    private static void placeComponents3(final JPanel panel3) {
        panel3.setLayout(null);

        // 創建title標籤
        final JLabel titleLabel = new JLabel("統編檢核");
        titleLabel.setForeground(Color.BLUE); // 顯示藍色
        titleLabel.setBounds(10, 10, 80, 25);
        panel3.add(titleLabel);

        // 創建文字標籤
        final JLabel userLabel = new JLabel("輸入統編:");
        userLabel.setBounds(10, 40, 80, 25);
        panel3.add(userLabel);

        // 創建文字輸入框
        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 40, 165, 25);
        panel3.add(userText);

        // 創建按鈕
        final JButton convertButton = new JButton("檢核");
        convertButton.setBounds(280, 40, 80, 25);
        panel3.add(convertButton);

        // 創建結果顯示標籤
        final JTextField resultText = new JTextField("結果:");
        resultText.setBounds(10, 70, 300, 25);
        resultText.setEditable(false); // 設置為只讀模式
        panel3.add(resultText);

        // 添加按鈕的事件監聽器
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String inputText = userText.getText();
                if (StringUtils.isNotBlank(inputText)) {
                    final BANFormatterValidator instance = BANFormatterValidator.getInstance();
                    final boolean flag = instance.isDataValid(inputText);
                    final String reslt = flag == true ? "有效" : "無效";
                    resultText.setText("結果: " + reslt);
                    resultText.setForeground(Color.BLACK); // 顯示黑色
                } else {
                    resultText.setText("結果: " + "請先輸入統編");
                    resultText.setForeground(Color.RED); // 顯示紅色
                }
            }
        });
    }

    private static void placeComponents4(final JPanel panel4) {
        panel4.setLayout(null);

        // 創建title標籤
        final JLabel titleLabel = new JLabel("身份證檢核");
        titleLabel.setForeground(Color.BLUE); // 顯示藍色
        titleLabel.setBounds(10, 10, 80, 25);
        panel4.add(titleLabel);

        // 創建文字標籤
        final JLabel userLabel = new JLabel("輸入身份證:");
        userLabel.setBounds(10, 40, 80, 25);
        panel4.add(userLabel);

        // 創建文字輸入框
        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 40, 165, 25);
        panel4.add(userText);

        // 創建按鈕
        final JButton convertButton = new JButton("檢核");
        convertButton.setBounds(280, 40, 80, 25);
        panel4.add(convertButton);

        // 創建結果顯示標籤
        final JTextField resultText = new JTextField("結果:");
        resultText.setBounds(10, 70, 300, 25);
        resultText.setEditable(false); // 設置為只讀模式
        panel4.add(resultText);

        // 添加按鈕的事件監聽器
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String inputText = userText.getText();
                if (StringUtils.isNotBlank(inputText)) {
                    final IDNFormatterValidator instance_idn = IDNFormatterValidator.getInstance();
                    final boolean flag = instance_idn.isDataValid(inputText);
                    final String reslt = flag == true ? "有效" : "無效";
                    resultText.setText("結果: " + reslt);
                    resultText.setForeground(Color.BLACK); // 顯示黑色
                } else {
                    resultText.setText("結果: " + "請先輸入身份證");
                    resultText.setForeground(Color.RED); // 顯示紅色
                }
            }
        });
    }

    private static void placeComponents5(final JPanel panel5) {
        panel5.setLayout(null);

        // 創建title標籤
        final JLabel titleLabel = new JLabel("日期檢核");
        titleLabel.setForeground(Color.BLUE); // 顯示藍色
        titleLabel.setBounds(10, 10, 80, 25);
        panel5.add(titleLabel);

        // 創建文字標籤
        final JLabel userLabel = new JLabel("輸入日期:");
        userLabel.setBounds(10, 40, 80, 25);
        panel5.add(userLabel);

        // 創建文字輸入框
        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 40, 165, 25);
        panel5.add(userText);

        // 創建按鈕
        final JButton convertButton = new JButton("檢核");
        convertButton.setBounds(280, 40, 80, 25);
        panel5.add(convertButton);

        // 創建結果顯示標籤
        final JTextField resultText = new JTextField("結果:");
        resultText.setBounds(10, 70, 300, 25);
        resultText.setEditable(false); // 設置為只讀模式
        panel5.add(resultText);

        // 添加按鈕的事件監聽器
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String inputText = userText.getText();
                if (StringUtils.isNotBlank(inputText)) {
                    final DATEFormatterValidator dateValidator = DATEFormatterValidator.getInstance();
                    final boolean flag = dateValidator.isDataValid(inputText);
                    final String reslt = flag == true ? "有效" : "無效";
                    resultText.setText("結果: " + reslt);
                    resultText.setForeground(Color.BLACK); // 顯示黑色
                } else {
                    resultText.setText("結果: " + "請先輸入日期");
                    resultText.setForeground(Color.RED); // 顯示紅色
                }
            }
        });
    }

}