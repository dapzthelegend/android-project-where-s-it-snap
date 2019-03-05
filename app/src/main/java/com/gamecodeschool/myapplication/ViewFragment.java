package com.gamecodeschool.myapplication;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamecodeschool.whereitssnap.ContactContract;
import com.gamecodeschool.whereitssnap.ContactDbHelper;
import com.gamecodeschool.whereitssnap.R;

import java.net.URI;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {
    private Cursor mCursor;
    private ImageView mImageView;
    public TextView textView;
    public Button button;
    private Button btnDelete;



    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = getArguments().getInt("Position");
        Log.e("ImageView", "Done" + position);

        ContactDbHelper contactDbHelper = new ContactDbHelper(getActivity());
        SQLiteDatabase db = contactDbHelper.getReadableDatabase();
        mCursor = contactDbHelper.getPhoto(position,db);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view, container, false);
    textView = view.findViewById(R.id.textView);
    button = view.findViewById(R.id.btnShowLocation);
    Log.e("Text", mCursor.getString(mCursor.getColumnIndex(ContactContract.ContractEntry.TABLE_PHOTO_IMAGETITLE)));
    textView.setText(mCursor.getString(mCursor.getColumnIndex(ContactContract.ContractEntry.TABLE_PHOTO_IMAGETITLE)));
    mImageView = view.findViewById(R.id.imageView);
    mImageView.setImageURI(Uri.parse(mCursor.getString(mCursor.getColumnIndex(ContactContract.ContractEntry.TABLE_PHOTO_URI))));
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            double lat = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(ContactContract.ContractEntry.TABLE_ROW_LAT)));

            double lng = Double.valueOf(mCursor.getString
                    (mCursor.getColumnIndex
                            (ContactContract.ContractEntry.TABLE_ROW_LONG)));

            String uri = String.format(Locale.ENGLISH,"geo:%f,%f", lat,lng);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            getActivity().startActivity(intent);
        }
    });
    btnDelete = view.findViewById(R.id.btnDelete);
    btnDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContactDbHelper contactDbHelper = new ContactDbHelper(getActivity());
            SQLiteDatabase db = contactDbHelper.getWritableDatabase();
            int pos = getArguments().getInt("Position");
            contactDbHelper.deleteContacts(pos,db);
        }
    });



     return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BitmapDrawable bd = (BitmapDrawable) mImageView.getDrawable();
        bd.getBitmap().recycle();
        mImageView.setImageBitmap(null);
    }
}
