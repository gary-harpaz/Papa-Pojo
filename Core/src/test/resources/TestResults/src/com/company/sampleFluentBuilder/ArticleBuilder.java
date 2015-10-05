/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package com.company.sampleFluentBuilder;

import java.net.URL;
import java.util.Date;
import com.company.sampleFluentBuilder.Article;


public class ArticleBuilder {

    public ArticleBuilder() {
        reset();
    }

    private Article _data;


    public static ArticleBuilder createBuilder() {
        return new ArticleBuilder();
    }

    public Article reset() {
        Article result = _data;
        _data = new Article();
        return result;
    }

    public Article getData() {
        return _data;
    }
    public ArticleBuilder setDatePublished(Date datePublished) {
        _data.setDatePublished(datePublished);
        return this;
    }
    public ArticleBuilder setDateCreated(Date dateCreated) {
        _data.setDateCreated(dateCreated);
        return this;
    }
    public ArticleBuilder setWordCount(int wordCount) {
        _data.setWordCount(wordCount);
        return this;
    }
    public ArticleBuilder setAuthor(String author) {
        _data.setAuthor(author);
        return this;
    }
    public ArticleBuilder setRating(String rating) {
        _data.setRating(rating);
        return this;
    }
    public ArticleBuilder setGenre(String genre) {
        _data.setGenre(genre);
        return this;
    }
    public ArticleBuilder setLanguage(String language) {
        _data.setLanguage(language);
        return this;
    }
    public ArticleBuilder setText(String text) {
        _data.setText(text);
        return this;
    }
    public ArticleBuilder setVersion(String version) {
        _data.setVersion(version);
        return this;
    }
    public ArticleBuilder setUrl(URL url) {
        _data.setUrl(url);
        return this;
    }
    public ArticleBuilder setTags(String tags) {
        _data.setTags(tags);
        return this;
    }
    public ArticleBuilder setHeadLine(String headLine) {
        _data.setHeadLine(headLine);
        return this;
    }

}
