package by.jrr.library.service;

import by.jrr.library.bean.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class IsbnBookSearchService {
    String baseUri = "https://isbnsearch.org/isbn/";
    String html = "<!doctype html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "    <title>ISBN 9781617294945 - Spring in Action</title>\n" +
            "    <meta name=\"description\" content=\"Spring in Action - Information and prices for ISBN 9781617294945, ISBN 1617294942\">\n" +
            "    <meta name=\"canonical\" content=\"https://isbnsearch.org/isbn/9781617294945\">\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/style.min.css\">\n" +
            "    <link rel=\"icon\" type=\"image/x-icon\" href=\"/images/favicon.png\">\n" +
            "    <script src=\"https://www.google.com/recaptcha/api.js\" async defer></script>\n" +
            "    <script>\n" +
            "        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
            "            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
            "            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
            "        })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');\n" +
            "        ga('create', 'UA-23560130-1', 'auto');\n" +
            "        ga('send', 'pageview');\n" +
            "    </script>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"page\">\n" +
            "    <header>\n" +
            "        <p><a href=\"/\" title=\"ISBN Search\">ISBN Search</a></p>\n" +
            "    </header>\n" +
            "    <form id=\"search\" action=\"/search\" method=\"post\">\n" +
            "        <div>\n" +
            "            <label id=\"find\" for=\"searchQuery\">Find a book:</label>\n" +
            "            <input id=\"searchQuery\" name=\"searchQuery\" type=\"text\" class=\"start\" onfocus=\"if(this.value=='Enter the ISBN, Title, or Author') { this.value=''; this.className=''; }\" onblur=\"if(this.value=='') { this.value='Enter the ISBN, Title, or Author'; this.className='start'; }\" value=\"Enter the ISBN, Title, or Author\">\n" +
            "        </div>\n" +
            "        <button type=\"submit\" id=\"searchSubmit\" name=\"searchSubmit\" class=\"btn\">Search</button>\n" +
            "    </form>\n" +
            "    <div id=\"book\">\n" +
            "        <div class=\"image\">\n" +
            "            <img src=\"https://m.media-amazon.com/images/I/51xkEqwHOxL._SL200_.jpg\" alt=\"Spring in Action\">\n" +
            "        </div>\n" +
            "        <div class=\"bookinfo\">\n" +
            "            <h1>Spring in Action</h1>\n" +
            "            <p><strong>ISBN-13:</strong> <a href=\"/isbn/9781617294945\">9781617294945</a></p>\n" +
            "            <p><strong>ISBN-10:</strong> <a href=\"/isbn/1617294942\">1617294942</a></p>\n" +
            "            <p><strong>Author:</strong> Walls, Craig</p>\n" +
            "            <p><strong>Edition:</strong> 5th</p>\n" +
            "            <p><strong>Binding:</strong> Paperback</p>\n" +
            "            <p><strong>Publisher:</strong> Manning Publications</p>\n" +
            "            <p><strong>Published:</strong> November 2018</p>\n" +
            "        </div>\n" +
            "        <p class=\"clear\"></p>\n" +
            "    </div>\n" +
            "    <div class=\"prices\">\n" +
            "        <h2>Best Prices for this Book in New Condition</h2>\n" +
            "        <ul>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=-L3OyYvtIyMGcM_vUZd4ra4GxIveUvRc9QJwltPKG-5E4Wmpt0sMy2dhGcRr2c1Q5-eYac1eiBv04nUQqwxMSV_HLQf1zCKV4vJKRLWOFYeMoulDpuL7pqrzTU1Y6EM3_jX-AkUH24VIPMkXtXKTrlE1HujpqUgd2P1e6lr2dHc-5AljpvFX0GveZULs0HMfNZVg8O7FXBeSZixwsYMmz3natmP2czZPvOV2zghqMEtcFwGPthLWVDMT8PiB0IP6VJVEwqME_qYPQTNqbS6vYiuXaFJIh9mf-iySfRLRPFX6vztvOC1tyky1omuXKnF4IXwk6H8O0ccRu6A3L3CTbfi7S0LBtQagRYwy3SKCSK7qoZ7NIQMrOo0-YpJ8xJGQ\"><img src=\"/images/merchants/2022.png\" alt=\"AbeBooks.com\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=-L3OyYvtIyMGcM_vUZd4ra4GxIveUvRc9QJwltPKG-5E4Wmpt0sMy2dhGcRr2c1Q5-eYac1eiBv04nUQqwxMSV_HLQf1zCKV4vJKRLWOFYeMoulDpuL7pqrzTU1Y6EM3_jX-AkUH24VIPMkXtXKTrlE1HujpqUgd2P1e6lr2dHc-5AljpvFX0GveZULs0HMfNZVg8O7FXBeSZixwsYMmz3natmP2czZPvOV2zghqMEtcFwGPthLWVDMT8PiB0IP6VJVEwqME_qYPQTNqbS6vYiuXaFJIh9mf-iySfRLRPFX6vztvOC1tyky1omuXKnF4IXwk6H8O0ccRu6A3L3CTbfi7S0LBtQagRYwy3SKCSK7qoZ7NIQMrOo0-YpJ8xJGQ\">$35.76</a></p>\n" +
            "                        <p class=\"shipping\">Free shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"New Book. Shipped from UK. Established seller since 2000.\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=-L3OyYvtIyMGcM_vUZd4ra4GxIveUvRc9QJwltPKG-5E4Wmpt0sMy2dhGcRr2c1Q5-eYac1eiBv04nUQqwxMSV_HLQf1zCKV4vJKRLWOFYeMoulDpuL7pqrzTU1Y6EM3_jX-AkUH24VIPMkXtXKTrlE1HujpqUgd2P1e6lr2dHc-5AljpvFX0GveZULs0HMfNZVg8O7FXBeSZixwsYMmz3natmP2czZPvOV2zghqMEtcFwGPthLWVDMT8PiB0IP6VJVEwqME_qYPQTNqbS6vYiuXaFJIh9mf-iySfRLRPFX6vztvOC1tyky1omuXKnF4IXwk6H8O0ccRu6A3L3CTbfi7S0LBtQagRYwy3SKCSK7qoZ7NIQMrOo0-YpJ8xJGQ\">Buy this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=0_X1U9CDHNzLR_ILcDqW3Y1iP6jNNa6g8o86uR-6QuoOjspFVj68KGDErHouvyrmSUTO2ykGfZdYF1tQ36VFEh0Iy0MTD4OBhhMWjW3uWLrcx9Sd8CZPEl3sGtbuTkeiPTOZOYQ3osWCbjQIJvfNfp_isVoSNnd2hIKGAL6HJ54I_U_RIgAlOvGz6OWuKtd-xrZq60pBPc54eWL-MvW-lQ\"><img src=\"/images/merchants/3.png\" alt=\"Amazon.com\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=0_X1U9CDHNzLR_ILcDqW3Y1iP6jNNa6g8o86uR-6QuoOjspFVj68KGDErHouvyrmSUTO2ykGfZdYF1tQ36VFEh0Iy0MTD4OBhhMWjW3uWLrcx9Sd8CZPEl3sGtbuTkeiPTOZOYQ3osWCbjQIJvfNfp_isVoSNnd2hIKGAL6HJ54I_U_RIgAlOvGz6OWuKtd-xrZq60pBPc54eWL-MvW-lQ\">$44.99</a></p>\n" +
            "                        <p class=\"shipping\">Free shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"None\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=0_X1U9CDHNzLR_ILcDqW3Y1iP6jNNa6g8o86uR-6QuoOjspFVj68KGDErHouvyrmSUTO2ykGfZdYF1tQ36VFEh0Iy0MTD4OBhhMWjW3uWLrcx9Sd8CZPEl3sGtbuTkeiPTOZOYQ3osWCbjQIJvfNfp_isVoSNnd2hIKGAL6HJ54I_U_RIgAlOvGz6OWuKtd-xrZq60pBPc54eWL-MvW-lQ\">Buy this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=ciiF6r5K0_KS-vYvq_ZnC79vMzrkpEZR_VWFcLUW7ZmHmcNPvcLrLlUu4wOk3tF1YpDICRCzlTAl1LnipNuvGo-cB6ycdNHiXa-FLLvCg9o7qtYxylGBIAEcxnHW_MA_gNSTipX5j4cdW0fhgM8Gf2_JgSLfN_CVpQ0MMtUUyUI1Yus4yeZDoQeoVBDYUK6lNZjd5nGyywBoqvTvYO64dQ\"><img src=\"/images/merchants/17.png\" alt=\"eCampus.com\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=ciiF6r5K0_KS-vYvq_ZnC79vMzrkpEZR_VWFcLUW7ZmHmcNPvcLrLlUu4wOk3tF1YpDICRCzlTAl1LnipNuvGo-cB6ycdNHiXa-FLLvCg9o7qtYxylGBIAEcxnHW_MA_gNSTipX5j4cdW0fhgM8Gf2_JgSLfN_CVpQ0MMtUUyUI1Yus4yeZDoQeoVBDYUK6lNZjd5nGyywBoqvTvYO64dQ\">$49.49</a></p>\n" +
            "                        <p class=\"shipping\">Free shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"Brand New Book\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=ciiF6r5K0_KS-vYvq_ZnC79vMzrkpEZR_VWFcLUW7ZmHmcNPvcLrLlUu4wOk3tF1YpDICRCzlTAl1LnipNuvGo-cB6ycdNHiXa-FLLvCg9o7qtYxylGBIAEcxnHW_MA_gNSTipX5j4cdW0fhgM8Gf2_JgSLfN_CVpQ0MMtUUyUI1Yus4yeZDoQeoVBDYUK6lNZjd5nGyywBoqvTvYO64dQ\">Buy this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=L0GZrvIy5pz6wT91Zuz5Az3qK10mFSX_nbX9ls8-aRDrz8YirlpduoxaW087EVA-cx6YYD-YbM_2iKpHcO4K_hkBxJAsRYpm21g0NcOTbfip7Jbists7rqsAVXgt2kjrIiK8UaeBlrE5aekBliCZZcZa7GUcaAAjHEZn4Tlw0W94BPcdbZWf5AOj4kYXF0WwWQ4Uosft6UjXSrs6VT3JVw\"><img src=\"/images/merchants/43.png\" alt=\"BiggerBooks.com\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=L0GZrvIy5pz6wT91Zuz5Az3qK10mFSX_nbX9ls8-aRDrz8YirlpduoxaW087EVA-cx6YYD-YbM_2iKpHcO4K_hkBxJAsRYpm21g0NcOTbfip7Jbists7rqsAVXgt2kjrIiK8UaeBlrE5aekBliCZZcZa7GUcaAAjHEZn4Tlw0W94BPcdbZWf5AOj4kYXF0WwWQ4Uosft6UjXSrs6VT3JVw\">$48.50</a></p>\n" +
            "                        <p class=\"shipping\">+3.99 shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"None\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=L0GZrvIy5pz6wT91Zuz5Az3qK10mFSX_nbX9ls8-aRDrz8YirlpduoxaW087EVA-cx6YYD-YbM_2iKpHcO4K_hkBxJAsRYpm21g0NcOTbfip7Jbists7rqsAVXgt2kjrIiK8UaeBlrE5aekBliCZZcZa7GUcaAAjHEZn4Tlw0W94BPcdbZWf5AOj4kYXF0WwWQ4Uosft6UjXSrs6VT3JVw\">Buy this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "        </ul>\n" +
            "    </div>\n" +
            "    <div class=\"prices\">\n" +
            "        <h2>Best Used Prices</h2>\n" +
            "        <ul>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=0_X1U9CDHNzLR_ILcDqW3Y1iP6jNNa6g8o86uR-6QurHtFlQsBMj8nPptLLXJnHQKerjU2wKHW0i8mfIHHKjt1cqqGjo0k6XA9XCjuJ9NTluLuaUsytSx0XfN-VNe2AmpJZFlO64vl7D11y9bLHdKixqF2VX9_xAPpycXNz1ypnaVxE7e5zrqKmm7WmC47hDXt5AXAjuGYg2Phxm-LdVnvUcoJ8YKYxbAiUsCWw9jacBo6tvm-jV1CuB68X6lrvh\"><img src=\"/images/merchants/24.png\" alt=\"Amazon Mkt Used\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=0_X1U9CDHNzLR_ILcDqW3Y1iP6jNNa6g8o86uR-6QurHtFlQsBMj8nPptLLXJnHQKerjU2wKHW0i8mfIHHKjt1cqqGjo0k6XA9XCjuJ9NTluLuaUsytSx0XfN-VNe2AmpJZFlO64vl7D11y9bLHdKixqF2VX9_xAPpycXNz1ypnaVxE7e5zrqKmm7WmC47hDXt5AXAjuGYg2Phxm-LdVnvUcoJ8YKYxbAiUsCWw9jacBo6tvm-jV1CuB68X6lrvh\">$37.29</a></p>\n" +
            "                        <p class=\"shipping\">+3.99 shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"None\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=0_X1U9CDHNzLR_ILcDqW3Y1iP6jNNa6g8o86uR-6QurHtFlQsBMj8nPptLLXJnHQKerjU2wKHW0i8mfIHHKjt1cqqGjo0k6XA9XCjuJ9NTluLuaUsytSx0XfN-VNe2AmpJZFlO64vl7D11y9bLHdKixqF2VX9_xAPpycXNz1ypnaVxE7e5zrqKmm7WmC47hDXt5AXAjuGYg2Phxm-LdVnvUcoJ8YKYxbAiUsCWw9jacBo6tvm-jV1CuB68X6lrvh\">Buy this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=-L3OyYvtIyMGcM_vUZd4ra4GxIveUvRc9QJwltPKG-5E4Wmpt0sMy2dhGcRr2c1Q5-eYac1eiBv04nUQqwxMSV_HLQf1zCKV4vJKRLWOFYeMoulDpuL7pqrzTU1Y6EM3PycdsNH-uTSb7WTVarAIy_IPTvj4wDYbP09sPezCRkg-5AljpvFX0GveZULs0HMfNZVg8O7FXBeSZixwsYMmz3natmP2czZPvOV2zghqMEtcFwGPthLWVDMT8PiB0IP6aJoldnDNajoPeInAns0RMYPgQ5GOGtCEepfl7_tqAOYG7O1hZE_PBf-dfTVazPQDSCFOwolAPLYBMChyTouNJNr6aRvl0JSn8f4Q0uD5yn-tblI45Q0OncuCwfsE904F\"><img src=\"/images/merchants/2023.png\" alt=\"AbeBooks.com Used\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=-L3OyYvtIyMGcM_vUZd4ra4GxIveUvRc9QJwltPKG-5E4Wmpt0sMy2dhGcRr2c1Q5-eYac1eiBv04nUQqwxMSV_HLQf1zCKV4vJKRLWOFYeMoulDpuL7pqrzTU1Y6EM3PycdsNH-uTSb7WTVarAIy_IPTvj4wDYbP09sPezCRkg-5AljpvFX0GveZULs0HMfNZVg8O7FXBeSZixwsYMmz3natmP2czZPvOV2zghqMEtcFwGPthLWVDMT8PiB0IP6aJoldnDNajoPeInAns0RMYPgQ5GOGtCEepfl7_tqAOYG7O1hZE_PBf-dfTVazPQDSCFOwolAPLYBMChyTouNJNr6aRvl0JSn8f4Q0uD5yn-tblI45Q0OncuCwfsE904F\">$43.26</a></p>\n" +
            "                        <p class=\"shipping\">Free shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"A copy that has been read, but remains in clean condition. All pages are intact, and the cover is intact. The spine may show signs of wear. Pages can include limited notes and highlighting, and the copy can include previous owner inscriptions. At ThriftBooks, our motto is: Read More, Spend Less.\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=-L3OyYvtIyMGcM_vUZd4ra4GxIveUvRc9QJwltPKG-5E4Wmpt0sMy2dhGcRr2c1Q5-eYac1eiBv04nUQqwxMSV_HLQf1zCKV4vJKRLWOFYeMoulDpuL7pqrzTU1Y6EM3PycdsNH-uTSb7WTVarAIy_IPTvj4wDYbP09sPezCRkg-5AljpvFX0GveZULs0HMfNZVg8O7FXBeSZixwsYMmz3natmP2czZPvOV2zghqMEtcFwGPthLWVDMT8PiB0IP6aJoldnDNajoPeInAns0RMYPgQ5GOGtCEepfl7_tqAOYG7O1hZE_PBf-dfTVazPQDSCFOwolAPLYBMChyTouNJNr6aRvl0JSn8f4Q0uD5yn-tblI45Q0OncuCwfsE904F\">Buy this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "        </ul>\n" +
            "    </div>\n" +
            "    <div class=\"prices\">\n" +
            "        <h2>Best Rental Prices</h2>\n" +
            "        <ul>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=131O_34NIKXkAo0o5Xl9NwoAP3jRUPeYmNRHuazYQXjUOv4LVheTDBjpWf2uyw0zLMEbQQCeiVp15GNrenDaVD8riPXZTNw1tYHOEJRRQUhgSKxXgYDYHYnzdyg6b5YRv2UlT5b5Xue-f3HZrxTLfh4D-glpazvj8oqVZ6zS8xK90RVhLUmZ_Q-4WSuupmi3fxko6o2gC3rDKed0tXWByA\"><img src=\"/images/merchants/317.png\" alt=\"eCampus Rental\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=131O_34NIKXkAo0o5Xl9NwoAP3jRUPeYmNRHuazYQXjUOv4LVheTDBjpWf2uyw0zLMEbQQCeiVp15GNrenDaVD8riPXZTNw1tYHOEJRRQUhgSKxXgYDYHYnzdyg6b5YRv2UlT5b5Xue-f3HZrxTLfh4D-glpazvj8oqVZ6zS8xK90RVhLUmZ_Q-4WSuupmi3fxko6o2gC3rDKed0tXWByA\">$39.99</a></p>\n" +
            "                        <p class=\"shipping\">Free shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"eCampus Rental\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=131O_34NIKXkAo0o5Xl9NwoAP3jRUPeYmNRHuazYQXjUOv4LVheTDBjpWf2uyw0zLMEbQQCeiVp15GNrenDaVD8riPXZTNw1tYHOEJRRQUhgSKxXgYDYHYnzdyg6b5YRv2UlT5b5Xue-f3HZrxTLfh4D-glpazvj8oqVZ6zS8xK90RVhLUmZ_Q-4WSuupmi3fxko6o2gC3rDKed0tXWByA\">Rent this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=WEBQMEfB6L1NfcrTMJScywQbzn6tIimzoOVHrQuyNA1vfToUKXTEINRt9-o3HoFDL6-YAqPg1-gRiJrci6-hABbfOH8_xV9PtOmFNDkr_1yQYXfdiVNKrp1M5aX99veK1k9-z5TqzSo0ChKhlMkghysjVn685BEt0M85TRViX_daIoYU_mHD3zOT0yJviNPnRwMw-AZ2FUsOB0Q_zZS4WiWSHdosn5pH6-R59QGZQy04JCo0j2TlDG8UGe2AJl69zvUYVYlSJXS6cbSijlYAyZ6RUFc6Di6xWMdwWQREdNqiFSjzSQq3yCOyXlIdUEjlwFJjsspdkIa0Ipq4AzbBQXZBkDbVNdCfaoRRA8JujnR3B3ccNtBEnO_5Wn89HTag\"><img src=\"/images/merchants/309.png\" alt=\"KnetBooks.com\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=WEBQMEfB6L1NfcrTMJScywQbzn6tIimzoOVHrQuyNA1vfToUKXTEINRt9-o3HoFDL6-YAqPg1-gRiJrci6-hABbfOH8_xV9PtOmFNDkr_1yQYXfdiVNKrp1M5aX99veK1k9-z5TqzSo0ChKhlMkghysjVn685BEt0M85TRViX_daIoYU_mHD3zOT0yJviNPnRwMw-AZ2FUsOB0Q_zZS4WiWSHdosn5pH6-R59QGZQy04JCo0j2TlDG8UGe2AJl69zvUYVYlSJXS6cbSijlYAyZ6RUFc6Di6xWMdwWQREdNqiFSjzSQq3yCOyXlIdUEjlwFJjsspdkIa0Ipq4AzbBQXZBkDbVNdCfaoRRA8JujnR3B3ccNtBEnO_5Wn89HTag\">$44.54</a></p>\n" +
            "                        <p class=\"shipping\">Free shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"Books may be in new or used condition. CD's, Access Codes, etc may not be included with the rentals. Books are required to be returned at the end of the rental period.\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=WEBQMEfB6L1NfcrTMJScywQbzn6tIimzoOVHrQuyNA1vfToUKXTEINRt9-o3HoFDL6-YAqPg1-gRiJrci6-hABbfOH8_xV9PtOmFNDkr_1yQYXfdiVNKrp1M5aX99veK1k9-z5TqzSo0ChKhlMkghysjVn685BEt0M85TRViX_daIoYU_mHD3zOT0yJviNPnRwMw-AZ2FUsOB0Q_zZS4WiWSHdosn5pH6-R59QGZQy04JCo0j2TlDG8UGe2AJl69zvUYVYlSJXS6cbSijlYAyZ6RUFc6Di6xWMdwWQREdNqiFSjzSQq3yCOyXlIdUEjlwFJjsspdkIa0Ipq4AzbBQXZBkDbVNdCfaoRRA8JujnR3B3ccNtBEnO_5Wn89HTag\">Rent this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=WEBQMEfB6L1NfcrTMJScywQbzn6tIimzoOVHrQuyNA3Q_0GuPK8gfiZRQw1LAz-S2cfK6vuqwfZFDZa7BpBkcljOpwJFFwbpHNn-c7GKpRBjt6HnUOjt_uCFxwnOGtl1ekKDdM31IsEKsORo25AKHpJWt9RBs9x4GjB-lu0rYwXo14pifwULtrTEacN_WQafAsKXCDPXhQBCaftc1LGCmLR492ZN5jrt8W4FcCSh_P1M3klB4tl4qtjbU5YNrnHZsoNxMq87JrtsLKi7g7C-aSeGbnBTlMyKGM2-cEPhoJedH1JpcyYyrNwV7x6Gb5XLr61R3hoocZ7IvjtPyiU3IoRLy3z7IZYEQziDoE5YYj7IdBAXmsM28KFRA_ocHvxEtRih6zZivNw1jWoWJRZBgIAUf21fLKgC-ya9zdnIVlE\"><img src=\"/images/merchants/2117.png\" alt=\"TextbookSolutions.com Rental\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=WEBQMEfB6L1NfcrTMJScywQbzn6tIimzoOVHrQuyNA3Q_0GuPK8gfiZRQw1LAz-S2cfK6vuqwfZFDZa7BpBkcljOpwJFFwbpHNn-c7GKpRBjt6HnUOjt_uCFxwnOGtl1ekKDdM31IsEKsORo25AKHpJWt9RBs9x4GjB-lu0rYwXo14pifwULtrTEacN_WQafAsKXCDPXhQBCaftc1LGCmLR492ZN5jrt8W4FcCSh_P1M3klB4tl4qtjbU5YNrnHZsoNxMq87JrtsLKi7g7C-aSeGbnBTlMyKGM2-cEPhoJedH1JpcyYyrNwV7x6Gb5XLr61R3hoocZ7IvjtPyiU3IoRLy3z7IZYEQziDoE5YYj7IdBAXmsM28KFRA_ocHvxEtRih6zZivNw1jWoWJRZBgIAUf21fLKgC-ya9zdnIVlE\">$42.42</a></p>\n" +
            "                        <p class=\"shipping\">+3.99 shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"None\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=WEBQMEfB6L1NfcrTMJScywQbzn6tIimzoOVHrQuyNA3Q_0GuPK8gfiZRQw1LAz-S2cfK6vuqwfZFDZa7BpBkcljOpwJFFwbpHNn-c7GKpRBjt6HnUOjt_uCFxwnOGtl1ekKDdM31IsEKsORo25AKHpJWt9RBs9x4GjB-lu0rYwXo14pifwULtrTEacN_WQafAsKXCDPXhQBCaftc1LGCmLR492ZN5jrt8W4FcCSh_P1M3klB4tl4qtjbU5YNrnHZsoNxMq87JrtsLKi7g7C-aSeGbnBTlMyKGM2-cEPhoJedH1JpcyYyrNwV7x6Gb5XLr61R3hoocZ7IvjtPyiU3IoRLy3z7IZYEQziDoE5YYj7IdBAXmsM28KFRA_ocHvxEtRih6zZivNw1jWoWJRZBgIAUf21fLKgC-ya9zdnIVlE\">Rent this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=L0GZrvIy5pz6wT91Zuz5Az3qK10mFSX_nbX9ls8-aRDrz8YirlpduoxaW087EVA-cx6YYD-YbM_2iKpHcO4K_hkBxJAsRYpm21g0NcOTbfip7Jbists7rqsAVXgt2kjrIiK8UaeBlrE5aekBliCZZcZa7GUcaAAjHEZn4Tlw0W9m07cGedbhrfo8GtQJOqBL3dWYPIDFrB2-m-GB6IAPkg\"><img src=\"/images/merchants/43.png\" alt=\"BiggerBooks.com\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=L0GZrvIy5pz6wT91Zuz5Az3qK10mFSX_nbX9ls8-aRDrz8YirlpduoxaW087EVA-cx6YYD-YbM_2iKpHcO4K_hkBxJAsRYpm21g0NcOTbfip7Jbists7rqsAVXgt2kjrIiK8UaeBlrE5aekBliCZZcZa7GUcaAAjHEZn4Tlw0W9m07cGedbhrfo8GtQJOqBL3dWYPIDFrB2-m-GB6IAPkg\">$46.34</a></p>\n" +
            "                        <p class=\"shipping\">+3.99 shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"None\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=L0GZrvIy5pz6wT91Zuz5Az3qK10mFSX_nbX9ls8-aRDrz8YirlpduoxaW087EVA-cx6YYD-YbM_2iKpHcO4K_hkBxJAsRYpm21g0NcOTbfip7Jbists7rqsAVXgt2kjrIiK8UaeBlrE5aekBliCZZcZa7GUcaAAjHEZn4Tlw0W9m07cGedbhrfo8GtQJOqBL3dWYPIDFrB2-m-GB6IAPkg\">Rent this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "            <li>\n" +
            "                <div class=\"merchant\">\n" +
            "                    <a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=H0WJBKKifLiqRmMr6byXFAbCbEdcGMb-eh__sR9TjlIgG8SNRJhm6UlsZbcA_LodryP0UYDYrpRDjBpoMv441vahwghNzU-BvnFhQ0bBcX36d0eBb3wOB6SGhWfZXl5ekhld5Zw0haxq11EISM-naAudzjaQ1VU1-UXY9tGyzXYB3FaxP3xJJtGlbvm7IbwpINsJJ68ukUz2-vt6W9Wa2ZnMIgvZu0Dbq0zKgnL7X4RvPJG1ttlq_iMiqi8fDadN\"><img src=\"/images/merchants/332.png\" alt=\"ValoreBooks Rental\"></a>\n" +
            "                </div>\n" +
            "                <div class=\"info\">\n" +
            "                    <div class=\"price\">\n" +
            "                        <p class=\"pricelink\"><a rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=H0WJBKKifLiqRmMr6byXFAbCbEdcGMb-eh__sR9TjlIgG8SNRJhm6UlsZbcA_LodryP0UYDYrpRDjBpoMv441vahwghNzU-BvnFhQ0bBcX36d0eBb3wOB6SGhWfZXl5ekhld5Zw0haxq11EISM-naAudzjaQ1VU1-UXY9tGyzXYB3FaxP3xJJtGlbvm7IbwpINsJJ68ukUz2-vt6W9Wa2ZnMIgvZu0Dbq0zKgnL7X4RvPJG1ttlq_iMiqi8fDadN\">$65.44</a></p>\n" +
            "                        <p class=\"shipping\">+3.95 shipping</p>\n" +
            "                        <p class=\"status\"></p>\n" +
            "                    </div>\n" +
            "                    <div class=\"notes\">\n" +
            "                        <p class=\"tip\" title=\"Items are in very good condition\" rel=\"tooltip\">Notes</p>\n" +
            "                    </div>\n" +
            "                    <div class=\"buylink\">\n" +
            "                        <a class=\"btn view\" rel=\"nofollow\" target=\"_blank\" href=\"/book?loc=H0WJBKKifLiqRmMr6byXFAbCbEdcGMb-eh__sR9TjlIgG8SNRJhm6UlsZbcA_LodryP0UYDYrpRDjBpoMv441vahwghNzU-BvnFhQ0bBcX36d0eBb3wOB6SGhWfZXl5ekhld5Zw0haxq11EISM-naAudzjaQ1VU1-UXY9tGyzXYB3FaxP3xJJtGlbvm7IbwpINsJJ68ukUz2-vt6W9Wa2ZnMIgvZu0Dbq0zKgnL7X4RvPJG1ttlq_iMiqi8fDadN\">Rent this book ›</a>\n" +
            "                    </div>\n" +
            "                </div> </li>\n" +
            "        </ul>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<!--page-->\n" +
            "<footer>\n" +
            "    <p>Copyright © 2008-2020 <a href=\"/\">ISBNsearch.org</a></p>\n" +
            "    <p class=\"note\">This website is an independent service and is not affiliated with The International ISBN Agency Limited or any other national ISBN registration agency.</p>\n" +
            "</footer>\n" +
            "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" +
            "<script type=\"text/javascript\" src=\"/js/tips.js\"></script>\n" +
            "</body>\n" +
            "</html>\n";
    public Book findByIsbn(String isbn) {
//        Document document = new Document("");
//        try{
//            document = Jsoup.connect(baseUri+isbn).get();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        Document document = Jsoup.parse(html);
        Book book = createBook(document.select("div.bookinfo").select("p"));
        book.setImg(findImgLink(document.select("div.image").select("img").first()));
        book.setName(findName(document.select("div.bookinfo").select("h1").first()));

        book.setDescription(findDescriptionOnAbeBooks(isbn));

        System.out.println(book);
        return book;
    }

    private String findDescriptionOnAbeBooks(String isbn) {
        String link = findLinkToBook(isbn);
        Document document = new Document("");
        try{
            document = Jsoup.connect(link).get();
        } catch (Exception ex){

        }
        Element element = document.select("div.ms-toggle").first();
        return element.html();
    }

    private String findLinkToBook(String isbn) {
        String uri = "https://www.abebooks.com/servlet/SearchResults?sts=t&isbn=" + isbn;
        Document document = new Document("");
        try{
            document = Jsoup.connect(uri).get();
        } catch (Exception ex){

        }
        Element element = document.select("div.result-detail.col-xs-8").select("a").first();
        return "https://www.abebooks.com/" + element.attr("href");
    }

    private String findName(Element element) {
        return element.text();
    }

    private String findImgLink(Element element) {
        return element.attr("src");
    }

    private Book createBook(Elements elements){
        Book book = new Book();
        for (Element el : elements){
            String line = el.text();
            String[] props = line.split(":");
            switch (props[0]){
                case "ISBN-13":
                    book.setIsbn(props[1]);
                    break;
                case "Author":
                    book.setAuthor(props[1]);
                    break;
                case "Edition":
                    book.setEdition(props[1]);
                    break;
                case "Publisher":
                    book.setPublisher(props[1]);
                    break;
                case "Published":
                    book.setPublished(props[1]);
                    break;
            }
        }

        return book;
    }
}
