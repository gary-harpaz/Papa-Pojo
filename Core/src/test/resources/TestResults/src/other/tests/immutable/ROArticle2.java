/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.immutable;

import java.net.URL;
import java.util.Date;
import other.tests.immutable.Article2;


public class ROArticle2 {

    public ROArticle2(Article2 data) {
        data =  data.copy();
        this._data = data;
    }

    private final Article2 _data;

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

    public Article2 copyData() {
        return _data.copy();
    }

}
