/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tikasmartcontractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONObject;

import org.xml.sax.SAXException;

/**
 *
 * @author User
 */
public class PDFReader {

    /**
     * @param args the command line arguments
     */
    public static String outputArray[];

    public static String parseString(String documentText) throws FileNotFoundException {
        System.out.println("----INDEXOF----");
        String documentText1[] = documentText.split("SCHEDULES");
        documentText = documentText1[1];
        int definedTermsStart = documentText.indexOf("ARTICLE 1");
        int definedTermsEnd = documentText.indexOf("ARTICLE 2");

        int start = documentText.indexOf('“', definedTermsStart);
        int end = documentText.indexOf('”', start);
        int delimiter = -1;
        int count = 0;
        JSONObject obj = new JSONObject();
        if (start != -1 && end < definedTermsEnd) {
            while (start != -1 && end < definedTermsEnd) {

                System.out.println(documentText.substring(start + 1, end));

                delimiter = documentText.indexOf(".", end + 1);

                System.out.println(documentText.substring(end + 1, delimiter));

                System.out.println();

                obj.put(documentText.substring(start + 1, end), documentText.substring(end + 1, delimiter).replaceAll("\n", " "));
                String term = documentText.substring(start + 1, end);
                count++;

                start = documentText.indexOf('“', end + 1);
                end = documentText.indexOf('”', start);

            }
        } else {
            definedTermsStart = documentText.indexOf("SECTION 1");
            definedTermsEnd = documentText.indexOf("SECTION 2");

            start = documentText.indexOf('“', definedTermsStart);
            end = documentText.indexOf('”', start);
            delimiter = -1;
            count = 0;
            while (start != -1 && end < definedTermsEnd) {

                System.out.println(documentText.substring(start + 1, end));

                delimiter = documentText.indexOf(".", end + 1);

                System.out.println(documentText.substring(end + 1, delimiter));

                System.out.println();

                obj.put(documentText.substring(start + 1, end), documentText.substring(end + 1, delimiter).replaceAll("\n", " "));
                String term = documentText.substring(start + 1, end);
                count++;

                start = documentText.indexOf('“', end + 1);
                end = documentText.indexOf('”', start);

            }

        }
        PrintWriter out = new PrintWriter("DefinedTerms4.json");
        out.println(obj.toString());

        System.out.println("stop");
        // node.close();
        return obj.toString();
    }

    public static String extractTableOfContents(String documentText) {
        int tableOfContentsStart = 0;

        tableOfContentsStart = documentText.indexOf("TABLE OF CONTENTS");

        if (tableOfContentsStart == -1) {
            tableOfContentsStart = documentText.indexOf("T A B L E   O F   C O N T E N T S");
        }
        int tableOfContentsEnd = documentText.indexOf("SCHEDULES");
        String tableOfContents = documentText.substring(tableOfContentsStart, tableOfContentsEnd);

        return tableOfContents;
    }

    public static void main(String[] args) throws IOException, SAXException, TikaException {

        FileInputStream inputstream = null;
        try {
            // TODO code application logic here

            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            inputstream = new FileInputStream(new File("C:\\Users\\User\\Documents\\NetBeansProjects\\TikaSmartContractor\\Contracts\\Contract3"
                    + ".pdf"));
            ParseContext pcontext = new ParseContext();
            //parsing the document using PDF parser
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputstream, handler, metadata, pcontext);
            //getting the content of the document
            System.out.println("Contents of the PDF :" + handler.toString());
            //getting metadata of the document
            String pdfContent = handler.toString();
            pdfContent = handler.toString().replaceAll("(/[^\\da-zA-Z.]/)", "");

            //Extract Table of Contents
            String toc = extractTableOfContents(pdfContent);
            System.out.println(toc);
            ArrayList<String> keys = new ArrayList<>();
            Boolean containArticle = toc.contains("Article");
            Boolean containARTICLE = toc.contains("ARTICLE");
            //For Contract1
            if (containArticle) {
                String articleSplit[] = toc.split("Article");
                for (String article : articleSplit) {
                    String sectionSplit[] = article.split("Section");
                    System.out.println(sectionSplit.length);
                    for (String section : sectionSplit) {
                        System.out.println(section);
                        keys.add(section);
                    }

                }
                System.out.println(articleSplit.length);
            }

            //For Contract 2
            if (containARTICLE) {
                String articleSplit[] = toc.split("ARTICLE");
                
                for (String article : articleSplit) {
                    
                    String sectionSplit[] = article.split("Section");
                    System.out.println(sectionSplit.length);
                    for (String section : sectionSplit) {
                        System.out.println(section);
                        keys.add(section);
                    }
                }
                System.out.println(articleSplit.length);
            }

            String documentText1[] = pdfContent.split("SCHEDULES");
            JSONObject myObj = new JSONObject();
            String fullDocument = documentText1[1];
            fullDocument = fullDocument.replaceAll(" +", " ");
            for (int i = 0; i < keys.size(); i++) {
                try {
                    String prevKeys = keys.get(i);
                    String afterKeys = keys.get(i + 1);

                    String prevKeysList[] = prevKeys.split("  ");
                    String my = prevKeysList[1].replaceAll("[^a-zA-Z]", "");
                    String finalPrevKeys = prevKeysList[0] + "  " + my;

                    String afterKeysList[] = afterKeys.split("  ");
                    my = afterKeysList[1].replaceAll("[^a-zA-Z]", "");
                    String finalafterKeys = afterKeysList[0] + "  " + my;

                    // prevKeys = prevKeys.replaceAll("\n", "");
                    //afterKeys = afterKeys.replaceAll("\n", "");
                    // prevKeys = prevKeys.replaceAll("[^a-zA-Z]+", " ");
                    // afterKeys = afterKeys.replaceAll("[^a-zA-Z]+", " ");
                    prevKeys = prevKeys.trim().replaceAll(" +", " ");
                    afterKeys = afterKeys.trim().replaceAll(" +", " ");
                    prevKeys = prevKeys.substring(0, prevKeys.length() - 3);
                    afterKeys = afterKeys.substring(0, afterKeys.length() - 3);

                    //For Contract 1 
                    int prevKeysIndex = fullDocument.indexOf(prevKeys);
                    int afterKeysIndex = fullDocument.indexOf(afterKeys);

                    //Search the Numbers not the text works but accuracy will not be great can generate false positive
                    //int prevKeysIndex = fullDocument.indexOf(prevKeysList[0]);
                    //int afterKeysIndex = fullDocument.indexOf(afterKeysList[0]);
                    if (prevKeysIndex < afterKeysIndex) {
                        String value = fullDocument.substring(prevKeysIndex, afterKeysIndex);
                        value = value.replaceAll("\\[", "").replaceAll("\\]", "");
                        value = value.substring(prevKeys.length(), value.length());
                        if (prevKeys.contains("Increased Cost and Reduced Return; Capital Adequacy; Reserves on Eurodollar Rate Loans")) {
                            System.out.println("Problem");
                        }
                        myObj.put(prevKeys, value);

                    }
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
            PrintWriter out = new PrintWriter("MyIndexex1Sample.json");

            out.print(myObj.toString());
            out.close();
            pdfContent = parseString(pdfContent);

            FileWriter fw11 = new FileWriter("MyContractText.txt");
            BufferedWriter bw11 = new BufferedWriter(fw11);
            bw11.write(pdfContent);

            bw11.close();
            fw11.close();
            System.out.println("Metadata of the PDF:");
            String[] metadataNames = metadata.names();
            for (String name : metadataNames) {
                System.out.println(name + " : " + metadata.get(name));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputstream.close();
            } catch (IOException ex) {
                Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
