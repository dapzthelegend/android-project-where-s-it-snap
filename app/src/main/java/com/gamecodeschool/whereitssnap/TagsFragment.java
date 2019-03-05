package com.gamecodeschool.whereitssnap;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TagsFragment extends ListFragment {
   // private Cursor mCursor;
    private ActivityComs mActivityComs;


    public TagsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactDbHelper contactDbHelper = new ContactDbHelper(getActivity());
        SQLiteDatabase db = contactDbHelper.getReadableDatabase();
       Cursor mCursor = contactDbHelper.getTags(db);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1, mCursor,
                new String[]{ContactContract.ContractEntry.TABLE_TAGS_TAG},
                new int[] {android.R.id.text1},0
        );

        setListAdapter(cursorAdapter);


    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = ((SimpleCursorAdapter) l.getAdapter()).getCursor();
        c.moveToPosition(position);
        String clickedTag = c.getString(1);
        mActivityComs.onTagsListItemSelected(clickedTag);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) getActivity();
        mActivityComs = (ActivityComs) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityComs = null;
    }
}
