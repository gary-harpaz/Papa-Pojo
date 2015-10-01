/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package com.company.sampleInterface;

import com.company.sampleNamingOptions.*;
import java.net.URL;
import java.util.Date;


public interface IArticleHeader {

Date getDatePublished();
void setDatePublished(Date datePublished);
Person2 getAuthor();
void setAuthor(Person2 author);
URL getUrl();
void setUrl(URL url);
String getHeadLine();
void setHeadLine(String headLine);

}
