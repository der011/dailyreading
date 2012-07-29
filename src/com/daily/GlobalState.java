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
package com.daily;

import android.app.Application;

import com.daily.resource.db.FeedDB;

/**
 * @author <a href="mailto:pavel.dergel@netcom-gsm.no">Pavel Dergel</a>
 *
 */
public class GlobalState extends Application {
    
    private FeedDB feedDb;

    /**
     * Gets the feed db.
     *
     * @return the feed db
     */
    public FeedDB getFeedDb() {
        return feedDb;
    }

    
    /**
     * Sets the feed db.
     *
     * @param feedDb the new feed db
     */
    public void setFeedDb(FeedDB feedDb) {
        this.feedDb = feedDb;
    }
    
}
