package by.jrr.common;

import javax.servlet.http.HttpServletRequest;

public class MyHeaders {

    public static String cameFrom(HttpServletRequest request) {
        String origin = request.getHeader("origin");
        String referer = request.getHeader("referer");
        return referer.substring(origin.length());
    }
}
