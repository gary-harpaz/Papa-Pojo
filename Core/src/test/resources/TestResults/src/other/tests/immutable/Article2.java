/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.immutable;

import java.net.URL;
import java.util.Date;


public class Article2 {

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

    public Article2 copy() {
        Article2 target = new Article2();
        target._datePublished = this.getDatePublished();
        target._dateCreated = this.getDateCreated();
        target._wordCount = this.getWordCount();
        target._author = this.getAuthor();
        target._rating = this.getRating();
        target._genre = this.getGenre();
        target._language = this.getLanguage();
        target._text = this.getText();
        target._version = this.getVersion();
        target._url = this.getUrl();
        target._tags = this.getTags();
        target._headLine = this.getHeadLine();
        return target;
    }

}
