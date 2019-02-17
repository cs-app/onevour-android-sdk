package org.cise.core.utilities.http;

/**
 * Created by user on 10/06/2017.
 */

public class HttpCommons {

    public static String getMessage(String message) {
        if (null != message) {
            if (message.contains("ECONNRESET (Connection reset by peer)")) {
                return "ECONNRESET : Komunikasi ke server gagal.";
            } else if (message.contains("ETIMEDOUT (Connection time out)")) {
                return "TIME OUT : Server tidak meresponse";
            } else if (message.contains("ECONNREFUSED (Connection refused)")) {
                return "REFUSED : komunikasi ke server ditolak.";
            } else if (message.contains("Unable to resolve host")) {
                return "Komunikasi ke server gagal.";
            } else if(message.isEmpty()){
                return "Unknown error message, lakukan sinkronisasi ulang!";
            } else {
                return message;
            }
        } else {
            return "Unknown error message, lakukan sinkronisasi ulang!";
        }
    }

}
