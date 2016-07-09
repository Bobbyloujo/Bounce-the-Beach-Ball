/**
 * BobEngine - 2D game engine for Android
 *
 * Copyright (C) 2014, 2015, 2016 Benjamin Blaszczak
 *
 * BobEngine is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser Public License
 * version 2.1 as published by the free software foundation.
 *
 * BobEngine is provided without warranty; without even the implied
 * warranty of merchantability or fitness for a particular
 * purpose. See the GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with BobEngine; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301 USA
 *
 */

package com.bobbyloujo.bobengine.extra;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * This helper class contains some useful functionality for your app.
 *
 * Created by Benjamin on 2/13/2015.
 */
public class BobHelper {

	// Constants
	final int VISIBILITY =                                      // The flags for immersive mode
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_FULLSCREEN |
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

	// Data
	private int screenWidth;                                    // Real width of the screen in pixels
	private int screenHeight;                                   // Real height of the screen in pixels
	private boolean useImmersive = false;                       // Flag that determines if Immersive Mode is in use
	private String save = "save";                               // Name of shared preferences file for saving data

	// Objects
	private Activity activity;
	private WindowManager wm;
	private Point size;

	/**
	 * You should initialize your BobHelper in or after onCreate() of activity.
	 * @param activity - The activity that this BobHelper is tied to.
	 */
	@SuppressLint("NewApi")
	public BobHelper(Activity activity) {
		this.activity = activity;

		wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		size = new Point();

		try {                                                   // Get screen dimensions
			wm.getDefaultDisplay().getRealSize(size);           // New method, might not work on old devices
			screenWidth = size.x;
			screenHeight = size.y;
		} catch (NoSuchMethodError er) {                        // If new method didn't work, use depreciated methods
			screenWidth = wm.getDefaultDisplay().getWidth();
			screenHeight = wm.getDefaultDisplay().getHeight();
		}

		ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		Log.i("info", Integer.toString(am.getMemoryClass()) + "MB ram available.");
	}

	public void onResume() {
		if (useImmersive) {
			useImmersiveMode();
		}
	}

	/**
	 * Uses KitKat's immersive mode. Immersive mode only works on Android 4.4.2
	 * and up. There is no need to check for version number when using this
	 * method. This method will handle older versions for you.
	 */
	@SuppressLint("NewApi")
	public void useImmersiveMode() {

		try {                                                                  // Immersive mode (Will not work on versions prior to 4.4.2)
			activity.getWindow().getDecorView().setSystemUiVisibility(VISIBILITY);      // Set the flags for immersive mode
			UIChangeListener();                                                // Add a listener to detect if we have lost immersive mode

			wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
			size = new Point();

			wm.getDefaultDisplay().getRealSize(size);                          // Get -REAL- screen size. This excludes the navbar, which isn't visible
			screenWidth = size.x;
			screenHeight = size.y;

			useImmersive = true;
		} catch (NoSuchMethodError e) {                                        // Immersive mode not supported (Android version < 4.4.2)
			// Get KitKat!

			Log.d("BobEngine", "Immersive mode not supported. (Android version < 4.4.2)");
		}
	}

	/**
	 * This method creates a listener that will detect when immersive mode is
	 * lost and get it back. Needs to be called again in onResume.
	 */
	@SuppressLint("NewApi")
	private void UIChangeListener() {
		final View decorView = activity.getWindow().getDecorView();

		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					decorView.setSystemUiVisibility(VISIBILITY);
				}
			}
		});
	}

	/**
	 * Returns the width of the screen.<br />
	 * <br />
	 * On devices using API level 13 and higher, this function will return the
	 * real width of the screen including things like the navigation bar and
	 * title bar. <br />
	 * <br />
	 * BobView includes a function to get just the width of the view.
	 *
	 * @return - Width of the screen, in pixels.
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public int getScreenWidth() {
		try {                                                   // Get screen dimensions
			wm.getDefaultDisplay().getRealSize(size);           // New method, might not work on old devices
			screenWidth = size.x;
		} catch (NoSuchMethodError er) {                        // If new method didn't work, use depreciated methods
			screenWidth = wm.getDefaultDisplay().getWidth();
		}

		return screenWidth;
	}

	/**
	 * Returns the width of the screen.<br />
	 * <br />
	 * On devices using API level 13 and higher, this function will return the
	 * real height of the screen including things like the navigation bar and
	 * title bar. <br />
	 * <br />
	 * BobView includes a function to get just the height of the view.
	 *
	 * @return - Width of the screen, in pixels.
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public int getScreenHeight() {
		try {                                                   // Get screen dimensions
			wm.getDefaultDisplay().getRealSize(size);           // New method, might not work on old devices
			screenHeight = size.y;
		} catch (NoSuchMethodError er) {                        // If new method didn't work, use depreciated methods
			screenHeight = wm.getDefaultDisplay().getHeight();
		}

		return screenHeight;
	}

	/**
	 * Set the save file to use with saveInt()... getSavedInt()... etc.
	 * @param fileName
	 */
	public void setSaveFile(String fileName) {
		save = fileName;
	}

	/**
	 * Save an integer value. It can be retrieved even after the application has quit
	 * by calling getSavedInt();
	 *
	 * @param name  - name of the saved integer. Will be used for retrieval.
	 * @param value - value to save.
	 */
	public void saveInt(String name, int value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putInt(name, value);
		edit.commit();
	}

	/**
	 * Save an boolean value. It can be retrieved even after the application has quit
	 * by calling getSavedBool();
	 *
	 * @param name  - name of the saved boolean. Will be used for retrieval.
	 * @param value - value to save.
	 */
	public void saveBool(String name, boolean value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putBoolean(name, value);
		edit.commit();
	}

	/**
	 * Save an float value. It can be retrieved even after the application has quit
	 * by calling getSavedFloat();
	 *
	 * @param name  - name of the saved float. Will be used for retrieval.
	 * @param value - value to save.
	 */
	public void saveFloat(String name, float value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putFloat(name, value);
		edit.commit();
	}

	/**
	 * Save an string value. It can be retrieved even after the application has quit
	 * by calling getSavedString();
	 *
	 * @param name  - name of the saved string. Will be used for retrieval.
	 * @param value - value to save.
	 */
	public void saveString(String name, String value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putString(name, value);
		edit.commit();
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. 0 if name doesn't exist.
	 */
	public int getSavedInt(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getInt(name, 0);
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. 0f if name doesn't exist.
	 */
	public float getSavedFloat(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getFloat(name, 0f);
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. false if name doesn't exist.
	 */
	public boolean getSavedBool(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getBoolean(name, false);
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. "" if name doesn't exist.
	 */
	public String getSavedString(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getString(name, "");
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. 0 if name doesn't exist.
	 */
	public int getSavedInt(String name, final int DEFAULT) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getInt(name, DEFAULT);
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. 0f if name doesn't exist.
	 */
	public float getSavedFloat(String name, final float DEFAULT) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getFloat(name, DEFAULT);
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. false if name doesn't exist.
	 */
	public boolean getSavedBool(String name, final boolean DEFAULT) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getBoolean(name, DEFAULT);
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. "" if name doesn't exist.
	 */
	public String getSavedString(String name, final String DEFAULT) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getString(name, DEFAULT);
	}
}
