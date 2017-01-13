package com.amphro.receptionmapper.logger.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class QueueDB {
	/** Database information **/
	private static final String DATABASE_NAME = "QueueDatabase.db";
	private static final int DATABASE_VERSION = 1;

	/** Table information **/
	private static final String DATABASE_TABLE_QUEUE = "queue_table";

	private static final String REQUEST_KEY_ID = "_id";
	private static final int REQUEST_COL_ID = 0;

	private static final String REQUEST_KEY_TEXT = "request_text";
	private static final int REQUEST_COL_TEXT = REQUEST_COL_ID + 1;

	/** Database SQL **/
	private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_QUEUE + " (" + 
			REQUEST_KEY_ID + " integer primary key autoincrement, " + 
			REQUEST_KEY_TEXT	+ " text not null);";
	
	private static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_QUEUE;

	/** Database Instance **/
	private SQLiteDatabase m_db;
		
	/** Database open/upgrade helper **/
	private QueueDBHelper m_dbHelper;
	
	/**
	 * Parameterized Constructor
	 * 
	 * @param context 
	 * 			  The application Context in which this QueueDB is being
	 * 			  used.
	 */
	public QueueDB(Context context) {
		m_dbHelper = new QueueDBHelper(context, QueueDB.DATABASE_NAME, 
				null, QueueDB.DATABASE_VERSION);
	}
	
	/**
	 * Initializes the underlying SQLiteDatabase object for Writable access.
	 */
	public void open() {
		m_db = m_dbHelper.getWritableDatabase();
	}
	
	/**
	 * Closes the underlying SQLiteDatabse object.
	 */
	public void close() {
		m_db.close();
	}
	
	/**
	 * Inserts a Joke object into the Database.
	 * 
	 * @param request
	 * 			  The http request that will be stored.
	 * 
	 * @return The row ID of the newly inserted row, or -1 if an error 
	 * 		   occurred.
	 */
	public long insertRequest(String request) {
		ContentValues cv = new ContentValues();
		cv.put(QueueDB.REQUEST_KEY_TEXT, request);
		
		long id = m_db.insert(QueueDB.DATABASE_TABLE_QUEUE, null, cv);
		
		Log.d("database", "inserting into the database!!");
		
		return id;
	}
	
	/**
	 * Returns a Cursor containing all request rows in the Database.
	 * 
	 * @return A Cursor containing all rows in the Database Queue Table.
	 */
	public Cursor getAllRequests() {
		Log.d("database", "getting all requests");
		String[] columns = {QueueDB.REQUEST_KEY_ID, 
				QueueDB.REQUEST_KEY_TEXT};
		
		Cursor c = m_db.query(QueueDB.DATABASE_TABLE_QUEUE, columns, null, 
				null, null, null, null);
		
		return c;
	}
	
	/**
	 * This method constructs a request object using the data from a row in the 
	 * Queue Database Table pointed to by the Cursor passed in.
	 * 
	 * If the cursor is null or empty, the method should return null.
	 * 
	 * @param cursor
	 * 			  A Cursor pointing to a row in the Queue Database Table.
	 * 
	 * @return A String object containing the data pointed to by the Cursor 
	 * 		   object passed in, or null if the Cursor is empty or null.
	 */
	public static Request getRequestFromCursor(Cursor cursor) {
		if(cursor == null || cursor.getCount() == 0){
			return null;
		}
		
		String request = cursor.getString(QueueDB.REQUEST_COL_TEXT);
		Long id = cursor.getLong(QueueDB.REQUEST_COL_ID);

		Log.d("database", "converting a cursor to Request");
		
		return new Request(request, id);
	}	
	
	/**
	 * Removes a request object from the Database.
	 * 
	 * @param id
	 * 			  The id of the request to remove from the Database.
	 * 
	 * @return True if a row in the database with an _id equaling 'id' was 
	 * 		   found and removed; False otherwise. 
	 */
	public boolean removeRequest(long id) {		
		String text = QueueDB.REQUEST_KEY_ID + "=" + id;
		
		Log.d("database", "removing request with id " + id);
		
		int numDeleted = m_db.delete(QueueDB.DATABASE_TABLE_QUEUE, text, null);
		
		if(numDeleted > 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes all requests from the database. 
	 */
	public boolean removeAllRequest() {
		Log.d("database", "removing all requests");
		
		int numDeleted = m_db.delete(QueueDB.DATABASE_TABLE_QUEUE, null, null);
		
		if(numDeleted > 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Helper class used to wrap the logic necessary for Creating and Upgrading
	 * the underlying SQLiteDatabase that JokeDBAdapter relies on.
	 */
	private static class QueueDBHelper extends SQLiteOpenHelper {

		/**
		 * Parameterized constructor. Should only call its super version of 
		 * this constructor, passing in all the parameters it is given.
		 */
		public QueueDBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		/**
		 * Called when the Database is created for the first time.
		 */
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(QueueDB.DATABASE_CREATE);
		}

		/**
		 * Called when the Constructor is called with a version number that 
		 * differs from the current Database version.
		 * 
		 * This method is used to perform upgrades to the existing database. 
		 * If you should have to upgrade the database, this is where you could
		 * save data currently in the database before migrating to the new 
		 * schema. 
		 */
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			_db.execSQL(QueueDB.DATABASE_DROP);
			this.onCreate(_db);
		}

	}
}
