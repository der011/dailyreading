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
package com.daily.resource;

import java.util.List;

import com.daily.resource.DailyReadingFetcher;
import com.daily.resource.rss.Article;
import com.daily.resource.rss.RssReader;


/**
 * The Class SPDKFetcher. Reads feed from spkd.cz.
 * 
 */
public class SPDKFetcher implements DailyReadingFetcher {
    
    private static final String BASE_URL = "http://spkd.cz/feed";
    
    /** {@inheritDoc} */
    public List<Article> fetch() {
        List<Article> feeds = RssReader.getFeed(BASE_URL);
        return feeds;
    }
}
