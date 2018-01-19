package patel446.maitripatel_assignment1;

import java.util.Date;
import java.util.UUID;

/**
 * Created by maitri on 2017-12-15.
 */

public class Book {
    private UUID mUuid;
    private String mTitle;
    private String mAuthor;
    private String mGenre;
    private Date mPublicationDate;
    private boolean mIsAvailable;
    private int mCover;

    public Book() {
        this(UUID.randomUUID());
    }

    public Book(final UUID uuid) {
        mUuid = uuid;
        mPublicationDate = new Date();
    }

    public Book(final UUID uuid, final String title, final String genre, final Date publicationDate,
                final boolean isAvailable, final int cover_type) {
        mUuid = uuid;
        mTitle = title;
        mGenre = genre;
        mPublicationDate = publicationDate;
        mIsAvailable = isAvailable;
        mCover = cover_type;
    }

    public UUID getUuid() {
        return mUuid;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(final String author) {
        mAuthor = author;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(final String genre) {
        mGenre = genre;
    }

    public Date getPublicationDate() {
        return mPublicationDate;
    }

    public void setPublicationDate(final Date publicationDate) {
        mPublicationDate = publicationDate;
    }

    public boolean isAvailable() {
        return mIsAvailable;
    }

    public void setAvailable(final boolean available) {
        mIsAvailable = available;
    }

    public int getmCover() {
        return mCover;
    }

    public void setmCover(final int cover) {
        mCover = cover;
    }

    public String getPhotoFilename() {
        return "IMG_" + getUuid().toString() + ".jpg";
    }


}
