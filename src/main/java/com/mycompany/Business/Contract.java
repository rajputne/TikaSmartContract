/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Business;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Contract {

    String tableOfContent;

    ArrayList<Articles> articleList;

    public Contract() {
        articleList = new ArrayList<>();
    }

    public String getTableOfContent() {
        return tableOfContent;
    }

    public void setTableOfContent(String tableOfContent) {
        this.tableOfContent = tableOfContent;
    }


    public ArrayList<Articles> getArticleList() {
        return articleList;
    }

    public void setArticleList(ArrayList<Articles> articleList) {
        this.articleList = articleList;
    }
}
