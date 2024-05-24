package org.example.model;

import java.util.ArrayList;

public class Article {
    String title;
    String keywords;
    String abstract_;
    String typeOfPublication;
    String year;
    int numberOfDownloads;
    int numOfViewPerDay;
    int numOfLikePerDay;
    ArrayList<Author> listOfAuthors;
    ArrayList<Article> listOfReferences;
}
