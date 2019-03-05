package com.gamecodeschool.whereitssnap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

public class AddBackgroundTask extends AsyncTask<Photo, Void, Void> {
    private Context context;
    public AddBackgroundTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(Photo... photos) {
        if (photos[0].getStorageLocation() != null ){

            ContactDbHelper contactDbHelper = new ContactDbHelper(context);
            SQLiteDatabase db = contactDbHelper.getWritableDatabase();
            contactDbHelper.addPhoto(photos[0],db);







            contactDbHelper.close();

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context,"Image Saved",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
