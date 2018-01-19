package patel446.maitripatel_assignment1;

import android.support.v4.app.Fragment;

public class BookListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BookListFragment();
    }
}
