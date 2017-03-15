package com.lsh.wms.api.service.exception;

import org.apache.log4j.MDC;

import java.util.UUID;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 17/3/13
 * Time: 17/3/13.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.exception.
 * desc:类功能描述
 */
public class SessionId {
    private static final String SESSION_ID = "session_id";

    public static String get() {
        Object value = MDC.get(SESSION_ID);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    public static void set(String value) {
        MDC.put(SESSION_ID, value);
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public static void reset() {
        set(generateShortUuid());
    }

    public static void clear() {
        MDC.remove(SESSION_ID);
    }
}
