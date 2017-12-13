package com.example.xin.meetup.event.as_user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.SingleFragmentActivity;

public class SearchResultActivity extends SingleFragmentActivity {

    private int userId;
    private String category;
    private int dateRange;

    public static Intent createIntent(final Context context, final int userId, final String category, final int dateRange) {
        final Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(Constants.USER_ID_ARG, userId);
        intent.putExtra(Constants.CATEGORY_DEFAULT_STR, category);
        intent.putExtra(Constants.DATE_RANGE_ARG, dateRange);
        return intent;
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
        userId = intentArgs.getInt(Constants.USER_ID_ARG);
        category = intentArgs.getString(Constants.CATEGORY_DEFAULT_STR);
        dateRange = intentArgs.getInt(Constants.DATE_RANGE_ARG);
    }

    @Override
    protected Fragment createFragment() {
        return SearchResultListFragment.newInstance(userId, category, dateRange);
    }
}
