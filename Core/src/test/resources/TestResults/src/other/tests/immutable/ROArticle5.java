/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.immutable;

import java.net.URL;
import java.util.Date;
import other.tests.immutable.Article5;


public class ROArticle5 {

    public ROArticle5(Article5 data) {
        data =  data.copyTo(new Article5());
        this._data = data;
    }

    private final Article5 _data;

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

    public Article5 copyData() {
        return _data.copyTo(new Article5());
    }

}
