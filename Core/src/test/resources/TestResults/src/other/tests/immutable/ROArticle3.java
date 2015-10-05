/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.immutable;

import java.net.URL;
import java.util.Date;
import other.tests.immutable.Article3;


public class ROArticle3 {

    public ROArticle3(Article3 data) {
        data =  Article3.copy(data,new Article3());
        this._data = data;
    }

    private final Article3 _data;

    public Date getDatePublished() { return _data.getDatePublished(); }
    public Date getDateCreated() { return _data.getDateCreated(); }
    public int getWordCount() { return _data.getWordCount(); }
    public String getAuthor() { return _data.getAuthor(); }
    public String getRating() { return _data.getRating(); }
    public String getGenre() { return _data.getGenre(); }
    public String getLanguage() { return _data.getLanguage(); }
    public String getText() { return _data.getText(); }
    public String getVersion() { return _data.getVersion(); }
    public URL getUrl() { return _data.getUrl(); }
    public String getTags() { return _data.getTags(); }
    public String getHeadLine() { return _data.getHeadLine(); }

    public Article3 copyData() {
        return Article3.copy(_data,new Article3());
    }

}
