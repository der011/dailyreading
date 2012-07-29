/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details at http://www.gnu.org/copyleft/gpl.html
 */
package com.daily;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daily.resource.db.FeedDB;
import com.daily.resource.rss.Article;

/**
 * The Class ArticlesListActivity.
 */
public class ArticlesListActivity extends ListActivity {
    
    public static final String SELECTED_ARTICLE_ID = "ARTICLE_ID";
    public static final int SUCCESS_RETURN_CODE = 1;
    

    private EfficientAdapter adap;
    
    private SharedPreferences prefs;

    private FeedDB readingDB;
    private List<Article> articles;

    
    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.readingDB = ((GlobalState)getApplication()).getFeedDb();
        this.articles = this.readingDB.getFeeds();
        showArticles(this.articles);
    }

    private void showArticles(List<Article> articles) {
        adap = new EfficientAdapter(this, articles);
        setListAdapter(adap);
        adap.notifyDataSetChanged();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Article selected = this.articles.get(position);
        
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString(SELECTED_ARTICLE_ID, selected.getId());
        intent.putExtras(b);
        setResult(SUCCESS_RETURN_CODE, intent);
        finish();
    }
    
    
    public static class EfficientAdapter extends BaseAdapter {
        
        private LayoutInflater mInflater;
        private Bitmap mIcon1;
        private Context context;
        private List<Article> articles;
        
        public EfficientAdapter(Context context, List<Article> articles) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
            this.context = context;
            this.articles = articles;
        }
        
        /**
         * Make a view to hold each row.
         * 
         * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.articles_content, null);
                holder = new ViewHolder();
                holder.textLine = (TextView)convertView.findViewById(R.id.textLine);
                holder.iconLine = (ImageView)convertView.findViewById(R.id.iconLine);
                holder.dateLine = (TextView)convertView.findViewById(R.id.dateLine);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            
            holder.iconLine.setImageBitmap(mIcon1);
            Article article = (Article)getItem(position);
            holder.textLine.setText(article.getTitle());
            holder.dateLine.setText(article.getFormattedDate(new SimpleDateFormat("d.MM.yyyy")));
            return convertView;
        }
        
        static class ViewHolder {
            TextView dateLine;
            TextView textLine;
            ImageView iconLine;
        }
        
//        public Filter getFilter() {
//            return null;
//        }
        
        @Override
        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public int getCount() {
            return articles.size();
        }
        
        @Override
        public Object getItem(int position) {
            return articles.get(position);
        }
    }
    
}
