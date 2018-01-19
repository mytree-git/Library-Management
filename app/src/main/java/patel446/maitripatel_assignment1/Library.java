package patel446.maitripatel_assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import patel446.maitripatel_assignment1.database.BookCursorWrapper;
import patel446.maitripatel_assignment1.database.BookDbHelper;
import patel446.maitripatel_assignment1.database.BookDbSchema;

/**
 * Created by maitri on 2017-12-15.
 */

public class Library {
    private static Library sLibrary;
    private Context mAppContext;
    private SQLiteDatabase mDatabase;

    public static Library getInstance(Context context) {
        if (sLibrary == null) {
            sLibrary = new Library(context);
        }

        return sLibrary;
    }

    private Library(Context context) {
        mAppContext = context.getApplicationContext();
        mDatabase = new BookDbHelper(mAppContext).getWritableDatabase();
    }

    public void addBook(Book book) {
        ContentValues contentValues = getContentValues(book);
        mDatabase.insert(BookDbSchema.BookTable.NAME, null, contentValues);
    }

    public void deleteBook(Book book) {
        mDatabase.delete(BookDbSchema.BookTable.NAME,
                BookDbSchema.BookTable.Cols.UUID + " = ? ",
                new String[] { book.getUuid().toString() });
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        try (BookCursorWrapper bookCursorWrapper = queryBooks(null, null)) {
            bookCursorWrapper.moveToFirst();
            while (!bookCursorWrapper.isAfterLast()) {
                books.add(bookCursorWrapper.getBook());
                bookCursorWrapper.moveToNext();
            }
        }
        return books;
    }

    public Book getBook(UUID uuid) {
        try (BookCursorWrapper bookCursorWrapper = queryBooks(
                BookDbSchema.BookTable.Cols.UUID + " = ? ",
                new String[] { uuid.toString() })) {
            if (bookCursorWrapper.getCount() == 0
                    || !bookCursorWrapper.moveToFirst()) {
                return null;
            } else {
                return bookCursorWrapper.getBook();
            }
        }
    }

    public File getPhotoFile(Book book) {
        File filesDir = mAppContext.getFilesDir();
        return new File(filesDir, book.getPhotoFilename());
    }

    public void updateBook(Book book) {
        String uuidString = book.getUuid().toString();
        ContentValues values = getContentValues(book);
        mDatabase.update(BookDbSchema.BookTable.NAME, values,
                BookDbSchema.BookTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private BookCursorWrapper queryBooks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                BookDbSchema.BookTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new BookCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(final Book book) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookDbSchema.BookTable.Cols.UUID, book.getUuid().toString());
        contentValues.put(BookDbSchema.BookTable.Cols.TITLE, book.getTitle());
        contentValues.put(BookDbSchema.BookTable.Cols.AUTHOR, book.getAuthor());
        contentValues.put(BookDbSchema.BookTable.Cols.GENRE, book.getGenre());
        contentValues.put(BookDbSchema.BookTable.Cols.PUBLICATION_DATE, book.getPublicationDate().getTime());
        contentValues.put(BookDbSchema.BookTable.Cols.IS_AVAILABLE, book.isAvailable() ? 1 : 0);
        contentValues.put(BookDbSchema.BookTable.Cols.COVER_TYPE, book.getmCover());
        //contentValues.put(BookDbSchema.BookTable.Cols.TYPE, book.getType().toString());
        return contentValues;
    }
}
