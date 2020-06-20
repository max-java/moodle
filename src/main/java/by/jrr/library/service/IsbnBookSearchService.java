package by.jrr.library.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class IsbnBookSearchService {
    String baseUri = "https://isbnsearch.org/isbn/";

    public void findByIsbn(String isbn) {
        Document document = new Document("");
        try{
            document = Jsoup.connect(baseUri+isbn).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String stringToParse = document.toString();
        System.out.println(stringToParse.lastIndexOf("<div class=\"bookinfo\">"));
        //int begin = stringToParse.lastIndexOf("<div class=\"bookinfo\">");
//        int end = begin;
//        while (stringToParse.charAt(end) != '/' &&
//                stringToParse.charAt(end+1) != 'd' &&
//                stringToParse.charAt(end+2) != 'i' &&
//                stringToParse.charAt(end+3) != 'v'){
//            end++;
//        }
//        System.out.println(stringToParse.substring(begin, end));
        System.out.println("document = " + document);
    }
}
