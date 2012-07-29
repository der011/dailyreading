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

import static com.daily.resource.rss.BaseFeedParser.*;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Rss feed processor.  Responsible for reading and parsing the feed.
 * 
 */
public class RssHandler extends DefaultHandler {
    
    private List<Article> messages;
    private Article currentMessage;
    private StringBuilder builder;
    
    /**
     * Gets the messages.
     * 
     * @return the messages
     */
    public List<Article> getMessages() {
        return this.messages;
    }
    
    /** {@inheritDoc} */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }
    
    /** {@inheritDoc} */
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        if (this.currentMessage != null) {
            if (localName.equalsIgnoreCase(TITLE)) {
                currentMessage.setTitle(builder.toString());
            } else if (localName.equalsIgnoreCase(LINK)) {
                currentMessage.setLink(builder.toString());
            } else if (localName.equalsIgnoreCase(DESCRIPTION)) {
                currentMessage.setDescription(builder.toString());
            } else if (localName.equalsIgnoreCase(CONTENT_ENCODED)) {
                currentMessage.setContent(builder.toString().replaceAll("\\s+", " "));
            } else if (localName.equalsIgnoreCase(PUB_DATE)) {
                currentMessage.setDateFromStr(builder.toString());
            } else if (localName.equalsIgnoreCase(ITEM)) {
                messages.add(currentMessage);
            }
            builder.setLength(0);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        messages = new ArrayList<Article>();
        builder = new StringBuilder();
    }
    
    /** {@inheritDoc} */
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if (localName.equalsIgnoreCase(ITEM)) {
            this.currentMessage = new Article();
        } else {
            this.builder.setLength(0);
        }
    }
    
}
