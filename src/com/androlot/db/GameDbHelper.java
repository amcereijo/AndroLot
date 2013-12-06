package com.androlot.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.androlot.dto.TicketDto;

public class GameDbHelper extends SQLiteOpenHelper {

	private static final String TAG = "GameDbHelper";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "lotodroid";
	private static final String NUMBERS_TABLE = "numbers";
	private static final String NUMBER_FIELD = "ticket_number";
	private static final String AMMOUNT_FIELD = "ammount";
	private static final String PRICE_FIELD = "price";
	
	private static final String CREATE_NUMBERS_TABLE = "create table "+
		NUMBERS_TABLE + " ("+NUMBER_FIELD+" integer , "+AMMOUNT_FIELD+" real ,"+
		PRICE_FIELD+" real default 0 ,"+
		"primary key ("+NUMBER_FIELD+"))";
	
	public GameDbHelper(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public GameDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_NUMBERS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
	
	/**
	 * Add new ticket
	 * @param ticket
	 */
	public void addNumber(TicketDto ticket){
		SQLiteDatabase db = null;
		try{
			db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(NUMBER_FIELD, ticket.getNumber());
			values.put(AMMOUNT_FIELD, ticket.getAmmount());
			values.put(PRICE_FIELD, ticket.getPrice());
			long id = db.insert(NUMBERS_TABLE, null, values);
			if(id<0){
				Log.e(TAG, "Error inserting number");
			}
		}finally{
			closeDb(db);
		}
	}

	protected void closeDb(SQLiteDatabase db) {
		if(db!=null){db.close();}
	}
	
	/**
	 * Get all saved tickets
	 * @return
	 */
	public List<TicketDto> getTickets(){
		SQLiteDatabase db = null;
		try{
			db = getReadableDatabase();
			String[] fields = new String[]{NUMBER_FIELD,AMMOUNT_FIELD,PRICE_FIELD};
			Cursor cursor = db.query(NUMBERS_TABLE, fields, null, null, null, null, null);
			List<TicketDto> tickets = new ArrayList<TicketDto>(cursor.getCount());
			while(cursor.moveToNext()){
				TicketDto ticket = new TicketDto();
					ticket.setNumber(cursor.getInt(0));
					ticket.setAmmount(cursor.getFloat(1));
					ticket.setPrice(cursor.getFloat(2));
				tickets.add(ticket);
			}
			return tickets;
		}finally{
			closeDb(db);
		}
	}

	/**
	 * Remove a ticket
	 * @param id
	 */
	public void removeTicket(String number){
		SQLiteDatabase db = null;
		try{
			db = getWritableDatabase();
			int affected = db.delete(NUMBERS_TABLE, "number=?", new String[]{number});
			if(affected==0){
				Log.e(TAG, "Error deleting ticket :"+number);
			}
		}finally{
			closeDb(db);
		}
	}
	
	/**
	 * Update a ticket
	 * @param ticket
	 */
	public void updateTicket(TicketDto ticket){
		SQLiteDatabase db = null;
		try{
			db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(AMMOUNT_FIELD, ticket.getAmmount());
			values.put(PRICE_FIELD, ticket.getPrice());
			int affected = db.update(NUMBERS_TABLE, values, "number=?", new String[]{String.valueOf(ticket.getNumber())});
			if(affected==0){
				Log.e(TAG, "Error updating ticket :"+ticket.getNumber());
			}
		}finally{
			closeDb(db);
		}
	}
}
