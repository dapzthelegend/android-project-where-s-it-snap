package com.gamecodeschool.whereitssnap;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureFragment extends Fragment implements LocationListener {
    //variables for the camera
    private static final int CAMERA_REQUEST = 123;
    private ImageView mImageView;
    private Uri mImageUri;
    private  EditText mEditTextTitle;
    private  EditText mEditTextTag1;
    private  EditText mEditTextTag2;
    private EditText mEditTextTag3;

    //for the location
    private Location mLocation;
    private LocationManager mLocationMangager;
    private String mProvider = "gps";

    //filepath for the photo
    String mCurrentPhotoPath;

    //where the captured image is stored


    private String pathFile;
    Context context;


    public CaptureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capture, container, false);

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        Button btnCapture = (Button) view.findViewById(R.id.btnCapture);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        if(Build.VERSION.SDK_INT>=23) {
            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

        if(Build.VERSION.SDK_INT>=23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        mEditTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
        mEditTextTag1 = (EditText) view.findViewById(R.id.editTextTag1);
         mEditTextTag2 = (EditText) view.findViewById(R.id.editTextTag2);
         mEditTextTag3 = (EditText) view.findViewById(R.id.editTextTag3);


        // Listen for capture clicks

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent
                        (MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;

                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("error", "error creating file");
                }

                if (photoFile != null) {
                    pathFile = photoFile.getAbsolutePath();
                    mImageUri = FileProvider.getUriForFile(getActivity(), "com.gamecodeschool.whereitssnap.fileprovider" ,photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //location manager
        mLocationMangager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mProvider = mLocationMangager.getBestProvider(criteria, false);
        String m = " 1 " + mProvider;
        Log.e("Provider", m);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mLocationMangager.requestLocationUpdates(mProvider, 500, 1, this);
        }catch (SecurityException e){
            Log.e("Permission", "not given");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationMangager.removeUpdates(this);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,
                ".jpg", storageDir);

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(pathFile);
                mImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("Error","Uri not set" );
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //to make sure we don't run out of memory
        BitmapDrawable bd = (BitmapDrawable) mImageView.getDrawable();
        bd.getBitmap().recycle();
        mImageView.setImageBitmap(null);
    }

    private void addPhoto(){
        Photo photo = new Photo();
        photo.setTitle(mEditTextTitle.getText().toString());
        photo.setStorageLocation(mImageUri);

        String tag1 = mEditTextTag1.getText().toString();
        String tag2 = mEditTextTag2.getText().toString();
        String tag3 = mEditTextTag3.getText().toString();

        photo.setTag1(tag1);
        photo.setTag2(tag2);
        photo.setTag3(tag3);
        photo.setGpsLocation(mLocation);

        AddBackgroundTask addBackgroundTask = new AddBackgroundTask(getActivity());
        addBackgroundTask.execute(photo);
        mEditTextTag1.setText("");
        mEditTextTag2.setText("");
        mEditTextTag3.setText("");
        mEditTextTitle.setText("");
        mImageView.setImageBitmap(null);


    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
