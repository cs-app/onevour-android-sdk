package org.cise.core.utilities.commons;

public class StringUtils {

    public static boolean isNumber(String s){
        boolean result = true;
        if (s == null) {
            result = false;
        } else {
            char[] chars = s.toCharArray();
            for(char c:chars){
                if (!Character.isDigit(c)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
