package com.lsh.wms.core.service.utils;

import java.math.BigDecimal;

/**
 * Created by zengwenjun on 16/9/11.
 */
public class PackUtil {
    public static String PackUnit2Uom(BigDecimal packUit, String eachUnitName){
        if(eachUnitName.compareTo("EA") == 0){
            return String.format("H%02d", packUit.intValue());
        }else if (eachUnitName.compareTo("KG") == 0){
            return String.format("H%.4fK", packUit.floatValue());
        }else if (eachUnitName.compareTo("G") == 0){
            return String.format("H%.4fG", packUit.floatValue());
        }else{
            return String.format("H%.4fN", packUit.floatValue());
        }
    }

    public static BigDecimal Uom2PackUnit(String uom){
        // byte[] bytes = uom.getBytes();
        if(uom.equals("EA")){
            return new BigDecimal("1");
        }

        if(!uom.substring(0,1).equals("H")){
            return null;
        }
        if(uom.substring(uom.length()-1).equals("K") || uom.substring(uom.length()-1).equals("G") || uom.substring(uom.length()-1).equals("N")){
            return new BigDecimal(uom.substring(1, uom.length()-1));
        }else{
            return new BigDecimal(uom.substring(1));
        }
    }

    public static String Uom2EachUnitName(String uom){
        // byte[] bytes = uom.getBytes();
        if(uom.length()==0){
            return "UNKNOWN";
        }
        char[] uomChar = uom.toCharArray();
        switch (uomChar[uomChar.length-1]){
            case 'K':
                return "KG";
            case 'G':
                return "G";
            case 'N':
                return "UNKNOWN";
            default:
                return "EA";
        }
    }

    public static BigDecimal EAQty2UomQty(BigDecimal qty, BigDecimal packUnit){
        return qty.divide(packUnit, 4, BigDecimal.ROUND_DOWN);
    }

    public static BigDecimal EAQty2UomQty(BigDecimal qty, String uom){
        BigDecimal packUint = PackUtil.Uom2PackUnit(uom);
        return PackUtil.EAQty2UomQty(qty, packUint);
    }

    public static BigDecimal UomQty2EAQty(BigDecimal uomQty, BigDecimal packUint){
        return uomQty.multiply(packUint);
    }

    public static BigDecimal UomQty2EAQty(BigDecimal uomQty, String uom){
        BigDecimal packUint = PackUtil.Uom2PackUnit(uom);
        return PackUtil.UomQty2EAQty(uomQty, packUint);
    }

    public static boolean isFullPack(BigDecimal qty, String uom){
        BigDecimal uomQty = PackUtil.EAQty2UomQty(qty, uom);
        uomQty = uomQty.setScale(0, BigDecimal.ROUND_DOWN);
        BigDecimal eaQty = PackUtil.UomQty2EAQty(uomQty, uom);
        if(eaQty.compareTo(qty)==0){
            return true;
        }else{
            return false;
        }
    }
}
