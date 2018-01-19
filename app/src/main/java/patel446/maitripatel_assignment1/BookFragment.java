package patel446.maitripatel_assignment1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by maitri on 2017-12-15.
 */

public class BookFragment extends Fragment {
    private static final String ARG_BOOK_ID = "argBookId";
    private static final String DIALOG_DATE = "dialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;

    private Book mBook;
    private OnBookDeletedListener mListener;

    private EditText mTitle;
    private EditText mAuthor;
    private Spinner mGenre;
    private Button mDate;
    private CheckBox mIsAvailable;
    private RadioGroup mCoverGroup;
    private File mPhotoFile;
    private ImageButton mCameraBtn;
    private ImageView mPhotoView;
    private Button mEmailBtn;

    public interface OnBookDeletedListener {
        void onBookDeleted(Book book);
    }

    public static BookFragment newInstance(UUID bookId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOK_ID, bookId);

        BookFragment fragment = new BookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID bookId = (UUID) getArguments().getSerializable(ARG_BOOK_ID);
        mBook = Library.getInstance(getActivity()).getBook(bookId);
        mPhotoFile = Library.getInstance(getActivity()).getPhotoFile(mBook);
    }

    private static abstract class BookSetter {
        public abstract void set(String value);
    }

    private static class BookTextWatcher implements TextWatcher {

        private BookSetter mBookSetter;

        public BookTextWatcher(BookSetter bookSetter) {
            mBookSetter = bookSetter;
        }

        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
            // Do nothing
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
            mBookSetter.set(charSequence.toString());
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            // Do nothing
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        mTitle = (EditText) view.findViewById(R.id.titleInput);
        mTitle.setText(mBook.getTitle());
        mTitle.addTextChangedListener(new BookTextWatcher(new BookSetter() {
            @Override
            public void set(final String value) {
                mBook.setTitle(value);
            }
        }));

        mAuthor = (EditText) view.findViewById(R.id.authorInput);
        mAuthor.setText(mBook.getAuthor());
        mAuthor.addTextChangedListener(new BookTextWatcher(new BookSetter() {
            @Override
            public void set(final String value) {
                mBook.setAuthor(value);
            }
        }));

        mGenre = (Spinner) view.findViewById(R.id.genreSpinner);
        int index = 0;
        for (int i = 0; i < mGenre.getCount(); ++i) {
            if (mGenre.getItemAtPosition(i).toString().equalsIgnoreCase(mBook.getGenre())) {
                index = i;
                break;
            }
        }
        mGenre.setSelection(index);
        mGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                mBook.setGenre(mGenre.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        mDate = (Button) view.findViewById(R.id.publicationDateButton);
        updateDate();
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mBook.getPublicationDate());
                dialog.setTargetFragment(BookFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mIsAvailable = (CheckBox) view.findViewById(R.id.isAvailableCheckBox);
        mIsAvailable.setChecked(mBook.isAvailable());
        mIsAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mBook.setAvailable(isChecked);
            }
        });

        mCoverGroup = (RadioGroup) view.findViewById(R.id.typeRadioGroup);
        switch (mBook.getmCover()) {
            case 1:
                mCoverGroup.check(R.id.softcoverRb);
                break;
            case 2:
                mCoverGroup.check(R.id.hardcoverRb);

        }

        mCoverGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                //checkedId = 1;
                switch(checkedId){
                    case R.id.softcoverRb:
                        mBook.setmCover(1);
                        break;
                    case R.id.hardcoverRb:
                        mBook.setmCover(2);
                        break;
                    default:
                }
            }
        });

        mEmailBtn = (Button) view.findViewById(R.id.book_email_btn);
        mEmailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getBookDetails());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.email_subject));
                i = Intent.createChooser(i, getString(R.string.send_email));
                startActivity(i);
            }
        });

        mCameraBtn = (ImageButton) view.findViewById(R.id.book_cover_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(captureImage, "Take your photo"), REQUEST_PHOTO);
            */
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "patel446.maitripatel_assignment1.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) view.findViewById(R.id.book_cover_view);
        updateCoverView();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Library.getInstance(getActivity()).updateBook(mBook);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mBook.setPublicationDate(date);
            updateDate();
        }
        if(requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "patel446.maitripatel_assignment1.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updateCoverView();
        }
    }

    private void updateDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        mDate.setText(dateFormat.format(mBook.getPublicationDate()));
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_book, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_book:
                mListener.onBookDeleted(mBook);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        try {
            mListener = (OnBookDeletedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnBookDeletedListener");
        }
    }


    private String getBookDetails() {
        String available_string = null;
        if (mBook.isAvailable()) {
            available_string = getString(R.string.book_available);
        } else {
            available_string = getString(R.string.book_not_available);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mBook.getPublicationDate()).toString();
        String author = mBook.getAuthor();
        if (author == null) {
            author = getString(R.string.no_author);
        } else {
            author = getString(R.string.book_author, author);
        }
        String details = getString(R.string.book_details,
                mBook.getTitle(), dateString, available_string, author);
        return details;
    }

    public void updateCoverView() {

        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
