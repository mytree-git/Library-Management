package patel446.maitripatel_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by maitri on 2017-12-15.
 */

public class BookListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bookRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            // Restore any state necessary
        }

        updateUi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save any state here
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_book_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_book:
                Book book = new Book();
                Library.getInstance(getActivity()).addBook(book);
                Intent intent = BookPagerActivity.newIntent(getActivity(), book.getUuid());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUi() {
        Library library = Library.getInstance(getActivity());
        List<Book> books = library.getBooks();

        if (mAdapter == null) {
            mAdapter = new BookAdapter(books);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setBooks(books);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Book mBook;

        private TextView mTitle;
        private TextView mAuthor;
        private ImageView mIsAvailable;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_book, parent, false));
            itemView.setOnClickListener(this);

            mTitle = (TextView) itemView.findViewById(R.id.list_item_book_title);
            mAuthor = (TextView) itemView.findViewById(R.id.list_item_book_author);
            //mIsAvailable = itemView.findViewById(R.id.list_item_book_is_available);
        }

        public void bind(Book book) {
            mBook = book;
            mTitle.setText(mBook.getTitle());
            mAuthor.setText(mBook.getAuthor());
           // mIsAvailable.setVisibility(mBook.isAvailable() ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onClick(final View view) {
            Intent intent = BookPagerActivity.newIntent(getActivity(), mBook.getUuid());
            startActivity(intent);
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {

        private List<Book> mBooks;

        public BookAdapter(List<Book> books) {
            mBooks = books;
        }

        @Override
        public BookHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BookHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(final BookHolder holder, final int position) {
            Book book = mBooks.get(position);
            holder.bind(book);
        }

        @Override
        public int getItemCount() {
            return mBooks.size();
        }

        public void setBooks(List<Book> books) {
            mBooks = books;
        }
    }
}
