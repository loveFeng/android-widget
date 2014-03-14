package com.realfame.mifi.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MiFiContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.realfame.provider.MifiProvider";
	public static final String TABLE_NAME_WIFIAP = "WifiAp";
	public static final String TABLE_NAME_APN = "Apn";
	public static final String TABLE_NAME_FLOW = "Flow";
	private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	
	private ProviderHelper mHelper = null;
	private ContentResolver mContentResolver = null;
	
    private static final int WIFIAP = 1;
    
    private static final int APN = 2;
    
    private static final int FLOW = 3;
    
    static
    {
        mUriMatcher.addURI(AUTHORITY, TABLE_NAME_WIFIAP, WIFIAP);
        mUriMatcher.addURI(AUTHORITY, TABLE_NAME_APN, APN);
        mUriMatcher.addURI(AUTHORITY, TABLE_NAME_FLOW, FLOW);
    }
	
	@Override
	public boolean onCreate() {
		mHelper = new ProviderHelper(getContext(),"mifi.db");
		mContentResolver = getContext().getContentResolver();
		return true;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		String table = null;
		switch (mUriMatcher.match(uri)) {
		case WIFIAP:
			table = TABLE_NAME_WIFIAP;
			break;
		case APN:
			table = TABLE_NAME_APN;
			break;
		case FLOW:
			table = TABLE_NAME_FLOW;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		long rowId = db.insert(table, null, values);
		if (rowId > 0) {
			Uri newUri = ContentUris.withAppendedId(uri, rowId);
			mContentResolver.notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		String table = null;
		switch (mUriMatcher.match(uri)) {
		case WIFIAP:
			table = TABLE_NAME_WIFIAP;
			break;
		case APN:
			table = TABLE_NAME_APN;
			break;
		case FLOW:
			table = TABLE_NAME_FLOW;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		int lines = db.delete(table, selection, selectionArgs);
		mContentResolver.notifyChange(uri, null);
		return lines;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		String table = null;
		switch (mUriMatcher.match(uri)) {
		case WIFIAP:
			table = TABLE_NAME_WIFIAP;
			break;
		case APN:
			table = TABLE_NAME_APN;
			break;
		case FLOW:
			table = TABLE_NAME_FLOW;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		Cursor cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
		mContentResolver.notifyChange(uri, null);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		String table = null;
		switch (mUriMatcher.match(uri)) {
		case WIFIAP:
			table = TABLE_NAME_WIFIAP;
			break;
		case APN:
			table = TABLE_NAME_APN;
			break;
		case FLOW:
			table = TABLE_NAME_FLOW;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		int lines = db.update(table, values, selection, selectionArgs);
		return lines;
	}
	@Override
	public String getType(Uri uri) {
		return null;
	}

}
