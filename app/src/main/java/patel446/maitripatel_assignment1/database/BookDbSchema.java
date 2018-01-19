package patel446.maitripatel_assignment1.database;

/**
 * Created by maitri on 2017-12-15.
 */

public class BookDbSchema {
    public static final class BookTable {
        public static final String NAME = "books";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String AUTHOR = "author";
            public static final String GENRE = "genre";
            public static final String PUBLICATION_DATE = "publicationDate";
            public static final String IS_AVAILABLE = "isAvailable";
            public static final String COVER_TYPE = "coverType";
        }
    }
}