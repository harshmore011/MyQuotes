package com.example.myquotes;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Quote {

    public Quote(String quote, String author, String date) {
        this.quote = quote;
        this.author = author;
        this.date = date;
    }

    @Id
    public long id;
    public String quote, author, date;

    public Quote() {}

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
