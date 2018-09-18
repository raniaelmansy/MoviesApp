package com.example.movies.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.movies.R;
import com.example.movies.utils.Constants;
import com.example.movies.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Rania on 9/18/2018.
 */

public class ArtistImageActivity extends AppCompatActivity implements View.OnClickListener {

    private String mProfileImagePath;
    private ImageView mProfileImage;
    private ProgressBar mProgressBar;
    private ImageView mSaveImage;

    public static final int PERMISSION_REQUEST_ID = 333;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_image);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.ARTIST_IMAGE_PATH_EXTRA_KEY)) {
            mProfileImagePath = bundle.getString(Constants.ARTIST_IMAGE_PATH_EXTRA_KEY);
        }

        mProfileImage = findViewById(R.id.artistImage);
        mProgressBar = findViewById(R.id.main_progress);
        mSaveImage = findViewById(R.id.saveImageView);
        mSaveImage.setOnClickListener(this);
        mSaveImage.setEnabled(false);

        if(mProfileImage != null){
            Glide.with(this)
                    .load(Constants.ImagesPath_original + mProfileImagePath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                    .centerCrop()
                    .crossFade(1000)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            mSaveImage.setEnabled(true);
                            return false;
                        }
                    })
                    .into(mProfileImage);
        }
    }

    @Override
    public void onClick(View v) {
        callDownloadImage();
    }

    private void callDownloadImage(){
         new DownloadTask().execute(stringToURL(Constants.ImagesPath_original + mProfileImagePath));
    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            mProgressBar.setVisibility(View.VISIBLE);
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();
                // Connect the http url connection
                connection.connect();
                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                if(connection != null)
                    connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            mProgressBar.setVisibility(View.GONE);
            if(result!=null){
                // Save bitmap to storage
                if(checkWritePermission(ArtistImageActivity.this) == PackageManager.PERMISSION_GRANTED) {
                    saveImageToInternalStorage(result);
                }
            }else {
                Utils.showToast(ArtistImageActivity.this,  getString(R.string.something_went_wrong));
            }
        }
    }

    // Convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Save a bitmap into storage
    protected void saveImageToInternalStorage(Bitmap bitmap){
        // Get Picture directory
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.
                DIRECTORY_PICTURES);

        if(checkWritePermission(this) == PackageManager.PERMISSION_GRANTED) {
            String folderName = "MoviesApp";
            String fileName = "Image_"+ System.currentTimeMillis() + ".jpg";
            final File path = new File(documentsDir, folderName + File.separator+ fileName);
            path.getParentFile().mkdirs();

            try {
                // Initialize a new OutputStream
                OutputStream stream = null;
                // If the output file exists, it can be replaced or appended to it
                stream = new FileOutputStream(path);
                // Compress the bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                // Flushes the stream
                stream.flush();
                // Closes the stream
                stream.close();
                Utils.showToast(ArtistImageActivity.this, getString(R.string.image_saved_successfully));

            } catch (IOException e) // Catch the exception
            {
                e.printStackTrace();
                Utils.showToast(ArtistImageActivity.this, getString(R.string.something_went_wrong));
            }
        }
    }

    private static int checkWritePermission(Context context){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_ID);
            return PackageManager.PERMISSION_DENIED;
        }else{
            return PackageManager.PERMISSION_GRANTED;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Try to download and save image after get permission
                    callDownloadImage();
                } else {
                    Utils.showToast(this, getString(R.string.storage_permission_denied));
                }
            }
        }
    }
}
