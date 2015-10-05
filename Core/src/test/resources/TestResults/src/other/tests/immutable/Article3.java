/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.immutable;

import java.net.URL;
import java.util.Date;


public class Article3 {

    private Date _datePublished;
    private Date _dateCreated;
    private int _wordCount;
    private String _author;
    private String _rating;
    private String _genre;
    private String _language;
    private String _text;
    private String _version;
    private URL _url;
    private String _tags;
    private String _headLine;

    public Date getDatePublished() { return _datePublished; }
    public void setDatePublished(Date datePublished) { this._datePublished = datePublished; }
    public Date getDateCreated() { return _dateCreated; }
    public void setDateCreated(Date dateCreated) { this._dateCreated = dateCreated; }
    public int getWordCount() { return _wordCount; }
    public void setWordCount(int wordCount) { this._wordCount = wordCount; }
    public String getAuthor() { return _author; }
    public void setAuthor(String author) { this._author = author; }
    public String getRating() { return _rating; }
    public void setRating(String rating) { this._rating = rating; }
    public String getGenre() { return _genre; }
    public void setGenre(String genre) { this._genre = genre; }
    public String getLanguage() { return _language; }
    public void setLanguage(String language) { this._language = language; }
    public String getText() { return _text; }
    public void setText(String text) { this._text = text; }
    public String getVersion() { return _version; }
    public void setVersion(String version) { this._version = version; }
    public URL getUrl() { return _url; }
    public void setUrl(URL url) { this._url = url; }
    public String getTags() { return _tags; }
    public void setTags(String tags) { this._tags = tags; }
    public String getHeadLine() { return _headLine; }
    public void setHeadLine(String headLine) { this._headLine = headLine; }

    public static Article3 copy(Article3 source,Article3 target) {
        target._datePublished = source.getDatePublished();
        target._dateCreated = source.getDateCreated();
        target._wordCount = source.getWordCount();
        target._author = source.getAuthor();
        target._rating = source.getRating();
        target._genre = source.getGenre();
        target._language = source.getLanguage();
        target._text = source.getText();
        target._version = source.getVersion();
        target._url = source.getUrl();
        target._tags = source.getTags();
        target._headLine = source.getHeadLine();
        return target;
    }

}
