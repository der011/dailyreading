/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details at http://www.gnu.org/copyleft/gpl.html
 */
package com.daily.resource.rss;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The Class Message represents one Rss Feed message.
 */
public class Article implements Comparable<Article>, Serializable {
    
    private static final long serialVersionUID = -9022995326920288934L;
    static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    static SimpleDateFormat ID_FORMATTER = new SimpleDateFormat("yyyyMMdd");
    private long id;
    
    private String title;
    private URL link;
    private String description;
    private String content;
    private Date date;
    
    /**
     * Gets the id. Id is based on the date (YYYYMMDD)
     * 
     * @return the id
     */
    public String getId() {
        if (this.date == null) {
            this.date = Calendar.getInstance().getTime();
        }
        return ID_FORMATTER.format(this.date);
    }

    
    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(long id) {
        this.id = id;
    }

    
    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title.
     * 
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title.trim();
    }
    
    /**
     * Gets the link.
     * 
     * @return the link
     */
    public URL getLink() {
        return link;
    }
    
    /**
     * Sets the link.
     * 
     * @param link the new link
     */
    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description.trim();
    }
    
    /**
     * Gets the content.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }
    
    
    /**
     * Sets the content.
     * 
     * @param content the new content
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    
    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }
    
    /**
     * Sets the date.
     * 
     * @param date the new date
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Gets the formatted date.
     * 
     * @return the formatted date
     */
    public String getFormattedDate() {
        return getFormattedDate(FORMATTER);
    }
    
    /**
     * Gets the formatted date.
     *
     * @param formatter the formatter
     * @return the formatted date
     */
    public String getFormattedDate(SimpleDateFormat formatter) {  
        return formatter.format(this.date);
    }
    
    /**
     * Sets the date.
     * 
     * @param date the new date
     */
    public void setDateFromStr(String date) {
        // pad the date if necessary
        while (!date.endsWith("00")) {
            date += "0";
        }
        try {
            this.date = FORMATTER.parse(date.trim());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Copy.
     * 
     * @return the message
     */
    public Article copy() {
        Article copy = new Article();
        copy.title = title;
        copy.link = link;
        copy.description = description;
        copy.date = date;
        return copy;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Message [title=" + title + ", link=" + link + ", description=" + description + ", content=" + content + ", date=" + date + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + (int)(id ^ (id >>> 32));
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Article other = (Article)obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (id != other.id)
            return false;
        if (link == null) {
            if (other.link != null)
                return false;
        } else if (!link.equals(other.link))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }
    
    /** {@inheritDoc} */
    public int compareTo(Article another) {
        if (another == null)
            return 1;
        // sort descending, most recent first
        return another.date.compareTo(date);
    }
}
