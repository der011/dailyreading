/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details at
 * http://www.gnu.org/copyleft/gpl.html
*/
package com.daily.resource.rss;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;



/**
 * Reads RSS feads from source.
 * @author Pavel Dergel
 */
public class RssReader {
    
    /**
     * Gets the feed.
     * 
     * @param urlToRssFeed the url to rss feed
     * @return the feed
     */
    public static List<Article> getFeed(String urlToRssFeed) {
        try {
            // setup the url
            URL url = new URL(urlToRssFeed);
            
            // create the factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // create a parser
            SAXParser parser = factory.newSAXParser();
            
            // create the reader (scanner)
            XMLReader xmlreader = parser.getXMLReader();
            // instantiate our handler
            RssHandler theRssHandler = new RssHandler();
            // assign our handler
            xmlreader.setContentHandler(theRssHandler);
            // get our data via the url class
            InputSource is = new InputSource(url.openStream());
            // perform the synchronous parse           
            xmlreader.parse(is);
            // get the results - should be a fully populated RSSFeed instance, or null on error
            //theRssHandler.updateArticles();
            return theRssHandler.getMessages();
            
        } catch (Exception ee) {
            Log.i("Error", ee.getMessage());
            System.out.println(ee);
            ee.printStackTrace();
            // if we have a problem, simply return null
            return null;
        }
    }
}
