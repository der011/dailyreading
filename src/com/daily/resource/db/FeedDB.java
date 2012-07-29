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
package com.daily.resource.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.daily.resource.rss.Article;
import com.daily.settings.Preferences.ArchiveSize;
import com.daily.utils.DateUtils;

/**
 * The Class FeedDB.
 */
public class FeedDB {
    
    private static final String COL_CONTENT = "content";
    private static final String COL_URL = "url";
    private static final String COL_TITLE = "title";
    private static final String COL_ARTICLE_ID = "article_id";
    private static final String COL_TIMESTAMP = "feed_ts";

    // Preferences table
    private static final String COL_KEY = "pref_key";
    private static final String COL_VALUE = "value";
    

//    private static final String CREATE_TABLE_FEEDS = "create table if not exists feeds (feed_id integer primary key autoincrement, "
//            + "title text not null, url text not null);";
    
//    private static final String CREATE_TABLE_ARTICLES = "create table if not exists articles (article_id integer primary key autoincrement, "
//            + "feed_id int not null, title text not null, url text not null);";
    private static final String CREATE_TABLE_ARTICLES = "create table if not exists articles (article_id integer primary key not null, "
            + "title text not null, url text not null, content text, feed_ts long not null);";
    
    private static final String CREATE_TABLE_PERSISTENT_STATE = "create table if not exists app_state (pref_key text primary key not null, "
            + "value text not null);";
    
    private static final String ARTICLES_TABLE = "articles";
    private static final String APP_STATE_TABLE = "app_state";
    private static final String DATABASE_NAME = "dailyreading.db";
    private static final int DATABASE_VERSION = 1;
    
    private SQLiteDatabase db;
    
    /**
     * Instantiates a new feed db. Automatically delete records older than specified archive size.
     * 
     * @param ctx the ctx
     */
    public FeedDB(Context ctx, ArchiveSize archiveSize) {
        try {
            //ctx.deleteDatabase(DATABASE_NAME);
            db = ctx.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            //deleteFeed(getTodayFeed().getId());
            db.execSQL(CREATE_TABLE_ARTICLES);
            db.execSQL(CREATE_TABLE_PERSISTENT_STATE);
            
            deleteOldRecords(archiveSize);
        } catch (Exception e) {
            Log.e("Error opening Daily reading DB: ", e.getMessage(), e);
        }
    }
    
    /**
     * @param archiveSize
     */
    public boolean deleteOldRecords(ArchiveSize archiveSize) {
        if (archiveSize == ArchiveSize.UNLIMITED) {
            return true; 
        }
        Calendar today = Calendar.getInstance();
        long deleteFrom = DateUtils.addToDate(today.getTime(), Calendar.DATE, -1 * archiveSize.getDays()).getTime();
        
        return (db.delete(ARTICLES_TABLE, COL_TIMESTAMP + "<" + deleteFrom, null) > 0);

    }

    /**
     * Delete feed.
     * 
     * @param feedId the feed id
     * @return true, if successful
     */
    public boolean deleteFeed(String feedId) {
        return (db.delete(ARTICLES_TABLE, COL_ARTICLE_ID + "=" + feedId, null) > 0);
    }
    
    /**
     * Insert article. 
     * 
     * @param article the article
     * @return true, if successful
     */
    public boolean insertArticle(Article article) {
        ContentValues values = new ContentValues();
        values.put(COL_ARTICLE_ID, article.getId());
        values.put(COL_TITLE, article.getTitle());
        values.put(COL_URL, article.getLink().toString());
        values.put(COL_CONTENT, article.getContent());
        values.put(COL_TIMESTAMP, article.getDate().getTime());
        long rowId = -1;
        try {
            rowId = (db.replace(ARTICLES_TABLE, null, values));
        } catch (Exception e) {
            Log.e("DB", "Error inserting article: " + article, e);
            e.printStackTrace();
        }
        
        return rowId > 0;
    }
    
    /**
     * Insert articles.
     * 
     * @param articles the articles
     * @return the number of successfully inserted articles.
     */
    public int insertArticles(List<Article> articles) {
        int okCount = 0;
        if (articles != null) {
            for (Article a: articles) {
                if (insertArticle(a)) {
                    okCount++;
                }
            }
        }
        return okCount;
    }
    
    /**
     * Delete aricles.
     * 
     * @param feedId the feed id
     * @return true, if successful
     */
    public boolean deleteAricle(Long feedId) {
        return (db.delete(ARTICLES_TABLE, "article_id=" + feedId.toString(), null) > 0);
    }
    
    /**
     * Gets the feeds.
     * 
     * @return the feeds
     */
    public List<Article> getFeeds() {
        ArrayList<Article> feeds = new ArrayList<Article>();
        try {
            Cursor c = db.query(ARTICLES_TABLE, new String[] { COL_ARTICLE_ID, COL_TITLE, COL_URL, COL_CONTENT, COL_TIMESTAMP }, null, null, null, null, COL_TIMESTAMP + " DESC");
            return mapArticles(c);
        } catch (SQLException e) {
            Log.e("DailyReading", e.toString());
        } 
        return feeds;
    }

    /**
     * Gets the today feed.
     * 
     * @return the today feed
     */
    public Article getTodayFeed() {
        return getFeed(Calendar.getInstance().getTime());
    }
    
    /**
     * Gets the feed.
     * 
     * @param date the date
     * @return the feed
     */
    public Article getFeed(Date date) {
        long startTs = DateUtils.startOfDay(date).getTime();
        long endTs = DateUtils.endOfDay(date).getTime();
        Cursor c = null;
        try {
            c = db.query(ARTICLES_TABLE, new String[] {COL_ARTICLE_ID, COL_TITLE, COL_URL, COL_CONTENT, COL_TIMESTAMP }, COL_TIMESTAMP + " > " + startTs
                    + " and " + COL_TIMESTAMP + " < " + endTs, null, null, null, null);
            if (c.getCount() == 0) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return mapArticles(c).get(0);
    }
    
    public Article getFeed(String articleId) {
        Cursor c = null;
        try {
            c = db.query(ARTICLES_TABLE, new String[] {COL_ARTICLE_ID, COL_TITLE, COL_URL, COL_CONTENT, COL_TIMESTAMP }, COL_ARTICLE_ID + " = " + articleId, null, null, null, null);
            if (c.getCount() == 0) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return mapArticles(c).get(0);
    }


    /**
     * Map articles.
     * 
     * @param c the c
     * @return the list
     */
    private List<Article> mapArticles(Cursor c) {
        ArrayList<Article> feeds = new ArrayList<Article>();
        if (c == null || c.getCount() == 0) {
            return feeds;
        }
        try {
            c.moveToFirst();
            do {
                Article feed = new Article();
                feed.setId(c.getLong(c.getColumnIndex(COL_ARTICLE_ID)));
                feed.setTitle(c.getString(c.getColumnIndex(COL_TITLE)));
                feed.setLink(c.getString(c.getColumnIndex(COL_URL)));
                feed.setContent(c.getString(c.getColumnIndex(COL_CONTENT)));
                feed.setDate(DateUtils.toDate(c.getLong(c.getColumnIndex(COL_TIMESTAMP))));
                feeds.add(feed);
            } while(c.moveToNext());
            return feeds;
        } catch (SQLException e) {
            Log.e("DailyReading", e.toString());
            return feeds;
        } 
    }
    
    public boolean savePreference(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(COL_KEY, key);
        values.put(COL_VALUE, value);
        long rowId = -1;
        try {
            rowId = (db.replace(APP_STATE_TABLE, null, values));
        } catch (Exception e) {
            Log.e("DB", "Error inserting app_state: " + key + " value: " + value, e);
            e.printStackTrace();
        }
        
        return rowId > 0;
        
    }
    
    public String getPreference(String key) {
        Cursor c = null;
        try {
            c = db.query(APP_STATE_TABLE, new String[] {COL_KEY, COL_VALUE }, COL_KEY + " = " + "\"" + key + "\"", null, null, null, null);
            c.moveToFirst();
            return c.getString(c.getColumnIndex(COL_VALUE));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
        
    }

//    public List<Article> getArticles(Long feedId) {
//        ArrayList<Article> articles = new ArrayList<Article>();
//        try {
//            Cursor c = db.query(ARTICLES_TABLE, new String[] { "article_id", COL_FEED_ID, COL_TITLE, COL_URL }, "feed_id=" + feedId.toString(), null, null, null,
//                                null);
//            
//            int numRows = c.count();
//            c.first();
//            for (int i = 0; i < numRows; ++i) {
//                Article article = new Article();
//                article.articleId = c.getLong(0);
//                article.feedId = c.getLong(1);
//                article.title = c.getString(2);
//                article.url = new URL(c.getString(3));
//                articles.add(article);
//                c.next();
//            }
//        } catch (SQLException e) {
//            Log.e("NewsDroid", e.toString());
//        } catch (MalformedURLException e) {
//            Log.e("NewsDroid", e.toString());
//        }
//        return articles;
//    }
    
}
