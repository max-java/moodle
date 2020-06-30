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
        System.out.println("document = " + document);
    }
}
