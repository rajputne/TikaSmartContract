/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tikasmartcontractor;

/**
 *
 * @author User
 */
public class RegexTester {

    /**
     * @param args the command line arguments
     */
    public static String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        String test = " 1.01.  Defined Terms 1 ";

        test = test.substring(0, test.length() - 2);
        System.out.print(test);
    }

}
