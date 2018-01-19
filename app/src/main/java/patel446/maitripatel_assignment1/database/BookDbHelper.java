package patel446.maitripatel_assignment1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maitri on 2017-12-15.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "library.db";

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + BookDbSchema.BookTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                BookDbSchema.BookTable.Cols.UUID + ", " +
                BookDbSchema.BookTable.Cols.TITLE + ", " +
                BookDbSchema.BookTable.Cols.AUTHOR + ", " +
                BookDbSchema.BookTable.Cols.GENRE + ", " +
                BookDbSchema.BookTable.Cols.PUBLICATION_DATE + ", " +
                BookDbSchema.BookTable.Cols.IS_AVAILABLE + ", " +
                BookDbSchema.BookTable.Cols.COVER_TYPE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
