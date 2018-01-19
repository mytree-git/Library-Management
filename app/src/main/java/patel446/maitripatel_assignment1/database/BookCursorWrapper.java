package patel446.maitripatel_assignment1.database;

/**
 * Created by maitri on 2017-12-15.
 */

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import patel446.maitripatel_assignment1.Book;

public class BookCursorWrapper extends CursorWrapper {

    public BookCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Book getBook() {
        String uuidString = getString(getColumnIndex(BookDbSchema.BookTable.Cols.UUID));
        String title = getString(getColumnIndex(BookDbSchema.BookTable.Cols.TITLE));
        String author = getString(getColumnIndex(BookDbSchema.BookTable.Cols.AUTHOR));
        String genre = getString(getColumnIndex(BookDbSchema.BookTable.Cols.GENRE));
        long publicationDate = getLong(getColumnIndex(BookDbSchema.BookTable.Cols.PUBLICATION_DATE));
        int isAvailable = getInt(getColumnIndex(BookDbSchema.BookTable.Cols.IS_AVAILABLE));
        int cover_Type = getInt(getColumnIndex(BookDbSchema.BookTable.Cols.COVER_TYPE));

        Book book = new Book(UUID.fromString(uuidString));
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPublicationDate(new Date(publicationDate));
        book.setAvailable(isAvailable != 0);
        book.setmCover(cover_Type);

        return book;
    }
}
