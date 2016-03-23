package com.humane.util.charset;

/**
 * <a>https://en.wikipedia.org/wiki/Byte_order_mark</a>
 */
public class CharsetUtil {
    public static String getCharset(byte[] BOM) {
        String charset = null;
        if ((BOM[0] & 0xFF) == 0xEF && (BOM[1] & 0xFF) == 0xBB && (BOM[2] & 0xFF) == 0xBF)
            charset = "UTF-8";
        else if ((BOM[0] & 0xFF) == 0xFE && (BOM[1] & 0xFF) == 0xFF)
            charset = "UTF-16BE";
        else if ((BOM[0] & 0xFF) == 0xFF && (BOM[1] & 0xFF) == 0xFE)
            charset = "UTF-16LE";
        else if ((BOM[0] & 0xFF) == 0x00 && (BOM[1] & 0xFF) == 0x00 && (BOM[0] & 0xFF) == 0xFE && (BOM[1] & 0xFF) == 0xFF)
            charset = "UTF-32BE";
        else if ((BOM[0] & 0xFF) == 0xFF && (BOM[1] & 0xFF) == 0xFE && (BOM[0] & 0xFF) == 0x00 && (BOM[1] & 0xFF) == 0x00)
            charset = "UTF-32LE";
        return charset;
    }
}
