/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details at http://www.gnu.org/copyleft/gpl.html
 */
package com.daily.resource.rss;

import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

/**
 * The Class AndroidSaxFeedParser.
 */
public class AndroidSaxFeedParser extends BaseFeedParser {
    
    static final String RSS = "rss";
    
    /**
     * Instantiates a new android sax feed parser.
     * 
     * @param feedUrl the feed url
     */
    public AndroidSaxFeedParser(String feedUrl) {
        super(feedUrl);
    }
    
    public List<Article> parse() {
        final Article currentMessage = new Article();
        RootElement root = new RootElement(RSS);
        final List<Article> messages = new ArrayList<Article>();
        Element channel = root.getChild(CHANNEL);
        Element item = channel.getChild(ITEM);
        item.setEndElementListener(new EndElementListener() {
            
            /** {@inheritDoc} */
            public void end() {
                messages.add(currentMessage.copy());
            }
        });
        item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
            
            public void end(String body) {
                currentMessage.setTitle(body);
            }
        });
        item.getChild(LINK).setEndTextElementListener(new EndTextElementListener() {
            
            public void end(String body) {
                currentMessage.setLink(body);
            }
        });
        item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
            
            public void end(String body) {
                currentMessage.setDescription(body);
            }
        });
        item.getChild(CONTENT_ENCODED).setEndTextElementListener(new EndTextElementListener() {
            
            public void end(String body) {
                currentMessage.setContent(body);
            }
        });
        item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
            
            public void end(String body) {
                currentMessage.setDateFromStr(body);
            }
        });
        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messages;
    }
}
