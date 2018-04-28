package com.mjy.customview;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */

public class ShopUtils {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
            "B", "C", "D", "E", "F"};

    //1.6.1
    public static String spliceArguments(String merchantNo, String name, String bankcardNo,
                                         String idcard) {
        Map<String, String> map = new TreeMap<>();
        map.put("merchanNo", merchantNo);
        map.put("name", name);
        map.put("bankcardNo", bankcardNo);
        map.put("idcard", idcard);
        String splice = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            splice += entry.getKey() + "=" + entry.getValue() + "&";
        }
        splice = splice.substring(0, splice.length() - 1);
        return splice;
    }

    //1.6.2
    public static String md5(String splice, String merchantKey) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((splice + merchantKey).getBytes());
            result = byteArrayToHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(byteToHexString(b[i]));
        }
        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     *  测试用例
     */
    public static void main(String[] args){
        String splice = spliceArguments("参数1", "参数2", "参数3", "参数4"); //拼接
        String result = md5(splice, "商户key");//32位加密结果
        //放入sign中
    }
}

