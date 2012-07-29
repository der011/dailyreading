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
package com.daily.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;


/**
 * The Class ReadingWebView.
 * 
 * @author Pavel Dergel
 */
public class ReadingWebView extends WebView {

    /**
     * @param context
     * @param attrs
     */
    public ReadingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new reading web view.
     * 
     * @param context the context
     */
    public ReadingWebView(Context context) {
        super(context);
    }
    
}
