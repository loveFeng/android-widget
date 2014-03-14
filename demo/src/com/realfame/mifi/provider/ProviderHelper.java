package com.realfame.mifi.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ProviderHelper extends SQLiteOpenHelper {

	public static final int VERSION = 1;
	
	public ProviderHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public ProviderHelper(Context context, String name,int version){
		this(context, name, null, version);
	}
	public ProviderHelper(Context context, String name){
		this(context, name, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists WifiAp(_id INTEGER PRIMARY KEY, ssid varchar(10), security varchar(10), password varchar(10), maxconnections INTEGER)");
		db.execSQL("create table if not exists Apn(simId INTEGER PRIMARY KEY, _id INTEGER, name text, numeric text, mcc text, mnc text, apn text, user text, server text, password text, proxy text, port text, mmsproxy text, mmsport text, mmsc text, authtype text, type text, current text)");
		db.execSQL("create table if not exists Flow(simId INTEGER PRIMARY KEY, lastflow text, allflow text, pin text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MiFiContentProvider.TABLE_NAME_WIFIAP);
		db.execSQL("DROP TABLE IF EXISTS " + MiFiContentProvider.TABLE_NAME_APN);
		db.execSQL("DROP TABLE IF EXISTS " + MiFiContentProvider.TABLE_NAME_FLOW);
		onCreate(db);
	}

}
