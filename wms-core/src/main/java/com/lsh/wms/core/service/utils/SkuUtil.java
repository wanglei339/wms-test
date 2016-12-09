package com.lsh.wms.core.service.utils;

/**
 * Created by lixin-mac on 2016/12/9.
 */
public class SkuUtil {

    /**
     * 将传入的skucode转为18位
     * @return
     */
    public static String getSkuCode(String skuCode) {
        while (skuCode.length() < 18){
            StringBuffer sb = new StringBuffer();
            sb.append("0").append(skuCode);
            skuCode = sb.toString();
        }
        return skuCode;
    }

    public static void main(String[] args) {
        String skuCode = "234111111";
        System.out.println(getSkuCode(skuCode));
        System.out.println(getSkuCode(skuCode).length());
    }
}
