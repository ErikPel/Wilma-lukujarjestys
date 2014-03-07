package com.school.schedule;

import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RegisterAdapter {
	static final String DATABASE_NAME = "register.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE = "create table " + "REGISTER" + "( "
			+ "ID" + " integer primary key autoincrement,"
			+ "username text,password text,wilma_url text); ";
	// Variable to hold the database instanceSTER
	public SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;

	public RegisterAdapter(Context _context) {
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public RegisterAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public void insertEntry(String username, String password, String wilmaURL) {
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("username", username);
		newValues.put("password", password);
		newValues.put("wilma_url", wilmaURL);
		db.insert("REGISTER", null, newValues);
	}

	public int deleteEntry(String username) {
		String where = "USERNAME=?";
		int numberOFEntriesDeleted = db.delete("LOGIN", where,
				new String[] { username });
		return numberOFEntriesDeleted;
	}

	public String getSinlgeEntry(String username) {
		Cursor cursor = db.query("REGISTER", null, " username=?",
				new String[] { username }, null, null, null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String password = cursor.getString(cursor.getColumnIndex("password"));
		cursor.close();
		return password;
	}
	boolean userInfoExists()
	{
		Cursor cursor = db.rawQuery("SELECT * FROM REGISTER",new String[] {});
		if(cursor.getCount() < 1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public Hashtable getUserInfo() {
		Hashtable results = new Hashtable();
		Cursor cursor = db.rawQuery("SELECT * FROM REGISTER",new String[] {});
		cursor.moveToFirst();
		results.put("username",cursor.getString(1));
		results.put("password",cursor.getString(2));
		results.put("wilma_url",cursor.getString(3));
		return results;
	}
	public void updateEntry(String username, String password,String wilmaURL) {
		ContentValues updatedValues = new ContentValues();
		updatedValues.put("username", username);
		updatedValues.put("password", password);
		updatedValues.put("wilma_url", wilmaURL);

		String where = "USERNAME = ?";
		db.update("REGISTER", updatedValues, where, new String[] { username });
	}
}
