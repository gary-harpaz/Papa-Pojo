/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package com.company.sample06;

import com.company.sample01.*;
import java.net.URL;
import java.util.Date;


public interface IArticleHeader {

Date getDatePublished();
void setDatePublished(Date datePublished);
Person1 getAuthor();
void setAuthor(Person1 author);
URL getUrl();
void setUrl(URL url);
String getHeadLine();
void setHeadLine(String headLine);

}
