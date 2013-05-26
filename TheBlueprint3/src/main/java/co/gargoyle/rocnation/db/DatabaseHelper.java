package co.gargoyle.rocnation.db;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import javax.inject.Inject;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.net.Uri;
//import co.gargoyle.rocnation.R;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//	// The Android's default system path of your application database.
//	private static final String DB_NAME = "rocnation";
//	private static final int DB_VERSION = 1;
//
//	private SQLiteDatabase myDatabase;
//	private final Context mContext;
//
//	////////////////////////////////////////////////////////////
//	// Constructor
//	////////////////////////////////////////////////////////////
//
//	/**
//	 * Constructor Takes and keeps a reference of the passed context in order to
//	 * access to the application assets and resources.
//	 *
//	 * @param context
//	 */
//	@Inject
//	public DatabaseHelper(Context context) {
//		super(context, DB_NAME, null, DB_VERSION);
//		this.mContext = context;
//	}
//
//	////////////////////////////////////////////////////////////
//	// Core Plumbing
//	////////////////////////////////////////////////////////////
//
//	/**
//	 * Creates a empty database on the system and rewrites it with your own
//	 * database.
//	 * */
//	public void createDatabase() throws IOException {
//		boolean dbExist = checkDatabase();
//
//		if (dbExist) {
//			// do nothing - database already exist
//		} else {
//			try {
//				copyDatabase();
//			} catch (IOException e) {
//				e.printStackTrace();
//				throw new Error("Error copying database");
//			}
//		}
//	}
//
//	/**
//	 * Check if the database already exist to avoid re-copying the file each
//	 * time you open the application.
//	 *
//	 * @return true if it exists, false if it doesn't
//	 */
//	private boolean checkDatabase() {
//		SQLiteDatabase checkDB = null;
//		try {
//			String myPath = getDbPath();
//			checkDB = SQLiteDatabase.openDatabase(myPath, null,
//					SQLiteDatabase.OPEN_READONLY);
//		} catch (SQLiteException e) {
//			// database does't exist yet.
//		}
//
//		if (checkDB != null) {
//			checkDB.close();
//		}
//		return checkDB != null ? true : false;
//	}
//
//	/**
//	 * Copies your database from your local assets-folder to the just created
//	 * empty database in the system folder, from where it can be accessed and
//	 * handled. This is done by transfering bytestream.
//	 * */
//	private void copyDatabase() throws IOException {
//		//createBlankDbAtDestination();
//		this.getReadableDatabase();
//
//		InputStream in = mContext.getResources().openRawResource(R.raw.db_seed);
//		String outFileName = getDbPath();
//		OutputStream out = new FileOutputStream(outFileName);
//
//		byte[] buff = new byte[1024];
//		int read = 0;
//
//		try {
//			while ((read = in.read(buff)) > 0) {
//				out.write(buff, 0, read);
//			}
//		} finally {
//			in.close();
//			out.close();
//		}
//	}
//
//	public void openDatabase() throws SQLException {
//		// Open the database
//		String myPath = getDbPath();
//		myDatabase = SQLiteDatabase.openDatabase(myPath, null,
//				SQLiteDatabase.OPEN_READONLY);
//	}
//
//	private void createBlankDbAtDestination() throws IOException {
//		String path = getDbDirectoryString();
//		//(use relative path for Unix systems)
//		File directory = new File(path);
//		//(works for both Windows and Linux)
//		directory.mkdirs();
//
//		File file = new File(getDbPath());
//		file.createNewFile();
//
//		//write the bytes in file
//		if (file.exists()) {
//			byte[] data1 = {1,1,0,0};
//			OutputStream fo = new FileOutputStream(file);
//			fo.write(data1);
//			fo.close();
//		}
//
//		//f.createNewFile();
//	}
//
//	////////////////////////////////////////////////////////////
//	// SQLiteOpenHelper
//	////////////////////////////////////////////////////////////
//
//	@Override
//	public synchronized void close() {
//		if (myDatabase != null) {
//			myDatabase.close();
//		}
//		super.close();
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		try {
//			createDatabase();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//	}
//
//	private Uri getDbDirectory() {
////		File sdCard = Environment.getExternalStorageDirectory();
//
//		//String rootPath = sdCard.getAbsolutePath();
//		//String rootPath = "/data/data/";
//		String rootPath = mContext.getFilesDir().getPath();
//
//		Uri.Builder builder = new Uri.Builder();
//		builder.path(rootPath);
//		builder.appendPath("co.gargoyle.rocnation");
//		builder.appendPath("databases");
//		return builder.build();
//	}
//
//	private String getDbDirectoryString() {
//		return getDbDirectory().toString();
//	}
//
//	private String getDbPath() {
//		Uri dbDirectory = getDbDirectory();
//		Uri.Builder builder = dbDirectory.buildUpon();
//		builder.appendPath(DB_NAME);
//		return builder.build().getPath();
//	}
//
//	// Add your public helper methods to access and get content from the
//	// database.
//	// You could return cursors by doing "return myDatabase.query(....)" so it'd
//	// be easy
//	// to you to create adapters for your views.
//
//	////////////////////////////////////////////////////////////
//	// Public methods
//	////////////////////////////////////////////////////////////
//
//	public Cursor getAllSongs() {
//		SQLiteDatabase db = getReadableDatabase();
//
//		return db.query("songs", null, null, null, null, null, "track");
//	}
//
//	public Cursor getAllVideos() {
//		SQLiteDatabase db = getReadableDatabase();
//
//		return db.query("videos", null, null, null, null, null, null);
//	}
//}
