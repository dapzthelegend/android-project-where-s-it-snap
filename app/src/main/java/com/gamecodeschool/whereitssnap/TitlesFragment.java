package com.gamecodeschool.whereitssnap;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.gamecodeschool.whereitssnap.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class TitlesFragment extends ListFragment implements AdapterView.OnItemLongClickListener {
    private Cursor mCursor ;
    private ActivityComs mActivityComs;
    private ListView listView;



    public TitlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactDbHelper contactDbHelper = new ContactDbHelper(getActivity());
        String tag = getArguments().getString("Tag");
        SQLiteDatabase db = contactDbHelper.getReadableDatabase();

        if (tag == "_NO_TAG"){
            mCursor= contactDbHelper.getTitles(db);
            Log.e("Titles", "Display Title");
        }

        else{
            mCursor = contactDbHelper.getTitlesWithTag(tag,db);
        }


        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, mCursor, new String[]{ContactContract.ContractEntry.TABLE_PHOTO_IMAGETITLE}
                , new int[] {android.R.id.text1}, 0);

        Log.e("Title", "Done");
      setListAdapter(cursorAdapter);





    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        mCursor.moveToPosition(position);
        int dbId = mCursor.getInt(mCursor.getColumnIndex(ContactContract.ContractEntry.TABLE_PHOTO_ID));


        mActivityComs.onTitlesListItemSelected(dbId);
        Log.e("Title", "Sent position");
        Log.e("Title", "" + dbId);
        listView =  l;
        listView.setLongClickable(true);


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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return true;
    }
}
