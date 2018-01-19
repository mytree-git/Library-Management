package patel446.maitripatel_assignment1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by maitri on 2017-12-15.
 */

public class BookPagerActivity extends AppCompatActivity
        implements BookFragment.OnBookDeletedListener {

    private static final String EXTRA_BOOK_ID = "extraBookId";

    private ViewPager mViewPager;
    private List<Book> mBooks;

    public static Intent newIntent(Context packageContext, UUID bookId) {
        Intent intent = new Intent(packageContext, BookPagerActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pager);

        UUID bookId = (UUID) getIntent().getSerializableExtra(EXTRA_BOOK_ID);

        mViewPager = (ViewPager) findViewById(R.id.book_view_pager);

        mBooks = Library.getInstance(this).getBooks();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Book book = mBooks.get(position);
                return BookFragment.newInstance(book.getUuid());
            }

            @Override
            public int getCount() {
                return mBooks.size();
            }
        });

        for (int i = 0; i < mBooks.size(); i++) {
            if (mBooks.get(i).getUuid().equals(bookId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onBookDeleted(final Book book) {
        Library.getInstance(this).deleteBook(book);
        finish();
    }
}
