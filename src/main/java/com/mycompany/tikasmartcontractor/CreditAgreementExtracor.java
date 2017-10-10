/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tikasmartcontractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author User
 */
public class CreditAgreementExtracor {

    /**
     * @param args the command line arguments
     */
    static String FILENAME = "C:\\Users\\User\\Pictures\\NLP_SmartContracts\\urls.txt";

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }

        final int length = searchStr.length();
        if (length == 0) {
            return true;
        }

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);
        } catch (Exception e) {
        }
        String sCurrentLine;

        br = new BufferedReader(new FileReader(FILENAME));
        int i = 0;
        String sbu = "";
        while ((sCurrentLine = br.readLine()) != null) {
            Document document = Jsoup.connect(sCurrentLine)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true).get();

            String safe = Jsoup.clean(document.toString(), Whitelist.relaxed());

            String linesWithoutHtml = Jsoup.parse(safe).text();

            Document doc = Jsoup.parse(safe);
            Elements parts = document.getAllElements();

            if (parts.size() > 7) {
                document.select("document").first().children().first().before("<hr></hr>");

                String linesWithoutHtml1 = "";
                Elements elements = document.select("hr");

                Element elementsTitle = document.select("title").first();
                if (containsIgnoreCase(elementsTitle.toString(), "Credit Agreement")) {
                    System.out.println("Credit Agreement");
                    for (Element element : elements) {
                        StringBuilder sb = new StringBuilder(element.toString());
                        Element next = element.nextElementSibling();
                        while (next != null && !next.tagName().startsWith("hr")) {
                            sb.append(next.toString()).append("\n");
                            next = next.nextElementSibling();
                        }
                        //System.out.println(sb);
                        linesWithoutHtml = Jsoup.parse(sb.toString()).text();

                        linesWithoutHtml1 = linesWithoutHtml1+ linesWithoutHtml;

                    }
                    String myContracts = "SentencesDouble" + i + ".txt";
                    FileWriter fw11 = new FileWriter(myContracts);
                    BufferedWriter bw11 = new BufferedWriter(fw11);
                    bw11.write(linesWithoutHtml1);
                    sbu = "";
                    bw11.close();
                    fw11.close();

                    System.out.println("Done");

                }

            }

        }
    }

}
