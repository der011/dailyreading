/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details at http://www.gnu.org/copyleft/gpl.html
 */
package com.daily.resource;

import java.text.SimpleDateFormat;

import com.daily.resource.rss.Article;

/**
 * Html formatter for the reading page. 
 * 
 * @author Pavel Dergel
 */
public class ReadingHtmlFormatter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d.M.yyyy");
    
    /**
     * Formats the reading text into HTML.
     * 
     * @param title the title
     * @param message the message
     * @return the string
     */
    public static String format(Article message) {
        StringBuilder htmlReading = new StringBuilder();
        
        htmlReading.append("<html>");
        htmlReading.append("<head>");
        htmlReading.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        htmlReading.append("</head>");
        htmlReading.append("<body>");
        htmlReading.append("<b>" + message.getTitle() + "</b>");
        htmlReading.append("<br/><b>" + DATE_FORMAT.format(message.getDate()) + "</b>");
        htmlReading.append(message.getContent());
        htmlReading.append("</body>");
        htmlReading.append("</html>");
        
        return htmlReading.toString();
        
    }
    
}
