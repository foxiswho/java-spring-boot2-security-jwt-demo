package com.foxwho.demo.util;

import java.security.MessageDigest;

public class Password {

    private static String charset = "UTF-8";

    /**
     * 获取MD5加密后的字符串
     *
     * @param str 明文
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String phpMd5(String str) {
        if (str.length() < 1) {
            return "";
        }
        try {
            /** 创建MD5加密对象 */
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            /** 进行加密 */
            md5.update(str.getBytes(charset));
            /** 获取加密后的字节数组 */
            byte[] md5Bytes = md5.digest();
            String res = "";
            for (int i = 0; i < md5Bytes.length; i++) {
                int temp = md5Bytes[i] & 0xFF;
                if (temp <= 0XF) {
                    // 转化成十六进制不够两位，前面加零
                    res += "0";
                }
                res += Integer.toHexString(temp);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 密码加密
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        str = str.trim();
        if (str.length() < 1) {
            return "";
        }
        return phpMd5(phpMd5(str) + phpMd5(str));
    }

//    public static void main(String[] args) {
//        System.out.println(phpMd5("A1234好")); // 2b9da2e0b4f35691d0161af912d278a1
//        System.out.println(phpMd5("ASDLF213gf234dfgdfg")); // f3a4a988ca0734087ec484ab8ac1731d
//        System.out.println(md5("ASDLF213gf234dfgdfg")); // 4fee13ad857da632dc3e87d7ce1158c7
//        String tmp = "A1234好.123~!@#$%^*&(";
//        System.out.println(tmp);
//        try {
//            System.out.println(Base64.getEncoder().encodeToString(tmp.getBytes(charset))); //QTEyMzTlpb0uMTIzfiFAIyQlXiomKA==
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        String tmp2 = "QTEyMzTlpb0uMTIzfiFAIyQlXiomKA==";
//        String result = "";
//        try {
//            result = new String(Base64.getDecoder().decode(tmp2), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        // A1234好.123~!@#$%^*&(
//        System.out.println(result);
//
//    }
}
