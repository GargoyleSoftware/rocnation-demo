package com.rulych.customfont;

import java.util.*;
import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.*;
import android.widget.TextView;

/**
 * Based on Felipe Micaroni Lalli’s post on Stack Overflow
 * (http://stackoverflow.com/a/13633767/1555605).
 * 
 * @author Rubén Illodo Brea
 */
public abstract class CustomFont {
	
	public final static String DEFAULT_FONT_NORMAL = "fonts/Font-Normal.ttf";
	public final static String DEFAULT_FONT_ITALIC = "fonts/Font-Italic.ttf";
	public final static String DEFAULT_FONT_BOLD = "fonts/Font-Bold.ttf";
	public final static String DEFAULT_FONT_BOLD_ITALIC = "fonts/Font-BoldItalic.ttf";
	
	private static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();
	private static SparseArray<String> whichFontToUseForEachTypefaceStyle = new SparseArray<String>();
	
	static {
		whichFontToUseForEachTypefaceStyle.put(Typeface.NORMAL, DEFAULT_FONT_NORMAL);
		whichFontToUseForEachTypefaceStyle.put(Typeface.ITALIC, DEFAULT_FONT_ITALIC);
		whichFontToUseForEachTypefaceStyle.put(Typeface.BOLD, DEFAULT_FONT_BOLD);
		whichFontToUseForEachTypefaceStyle.put(Typeface.BOLD_ITALIC, DEFAULT_FONT_BOLD_ITALIC);
	}
	
	private static Typeface getTypeface(Context context, Typeface originalTypeface) {
		String ttfFileToUse = whichFontToUseForEachTypefaceStyle.get(Typeface.NORMAL);
		
		if (originalTypeface != null) {
			int style = originalTypeface.getStyle();
			
			String ttfFileToUseForStyle = whichFontToUseForEachTypefaceStyle.get(style);
			if (ttfFileToUseForStyle != null)
				ttfFileToUse = ttfFileToUseForStyle;
		}
		
		return getTypefaceUsingCache(context, ttfFileToUse);
	}
	
	private static Typeface getTypefaceUsingCache(Context context, String fontPath) {
		if (!typefaceCache.containsKey(fontPath))
			typefaceCache.put(fontPath, Typeface.createFromAsset(context.getAssets(), fontPath));
		
		return typefaceCache.get(fontPath);
	}
	
	/**
	 * Sets the .ttf files (inside {@code assets}) you want to use for each
	 * typeface style.
	 * <p>
	 * There’s no need to use this method if you place your .ttf files at the
	 * default location and with the default names (see
	 * {@linkplain CustomFont#DEFAULT_FONT_NORMAL},
	 * {@linkplain CustomFont#DEFAULT_FONT_ITALIC},
	 * {@linkplain CustomFont#DEFAULT_FONT_BOLD},
	 * {@linkplain CustomFont#DEFAULT_FONT_BOLD_ITALIC}).
	 * 
	 * @param forNormal
	 *            .ttf file por {@linkplain Typeface#NORMAL}.
	 * @param forItalic
	 *            .ttf file por {@linkplain Typeface#ITALIC}.
	 * @param forBold
	 *            .ttf file por {@linkplain Typeface#BOLD}.
	 * @param forBoldItalic
	 *            .ttf file por {@linkplain Typeface#BOLD_ITALIC}.
	 */
	public static void setFontsToUse(String forNormal, String forItalic, String forBold, String forBoldItalic) {
		whichFontToUseForEachTypefaceStyle.put(Typeface.NORMAL, forNormal);
		whichFontToUseForEachTypefaceStyle.put(Typeface.ITALIC, forItalic);
		whichFontToUseForEachTypefaceStyle.put(Typeface.BOLD, forBold);
		whichFontToUseForEachTypefaceStyle.put(Typeface.BOLD_ITALIC, forBoldItalic);
	}
	
	/**
	 * Walks ViewGroups, finds TextViews and applies Typefaces taking styling
	 * into consideration.
	 * 
	 * @param context
	 *            used to reach the .ttf files in {@code assets}.
	 * @param view
	 *            root view to apply typeface to.
	 */
	public static void setCustomFont(Context context, View view) {
		if (view instanceof ViewGroup)
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
				setCustomFont(context, ((ViewGroup) view).getChildAt(i));
		else if (view instanceof TextView) {
			Typeface currentTypeface = ((TextView) view).getTypeface();
			((TextView) view).setTypeface(getTypeface(context, currentTypeface));
		}
	}
	
	private CustomFont() {
	}
	
}