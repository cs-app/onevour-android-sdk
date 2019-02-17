package org.cise.core.utilities.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by user on 26/07/2017.
 */

public class PhotoHandler implements Serializable, Parcelable {

    private static final String TAG = "PhotoHandler";
    private static final String ALLOW_KEY = "ALLOWED";
    private static final String CAMERA_PREF = "camera_pref";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static int IMAGE_REQUEST = 101;

    private Activity activity;
    private String imageDirectory;
    private String imageNamePrefix = "IMG_";
    private String imageFilePath;
    private int imageSizeLimit = 10000000;
    private Uri uriOutput;
    private ImageView imageView;

    public PhotoHandler(Activity activity, String imageDirectory) {
        this.activity = activity;
        this.imageDirectory = imageDirectory;
    }

    public PhotoHandler(Activity activity, String imageDirectory, Uri uriOutput) {
        this.activity = activity;
        this.imageDirectory = imageDirectory;
        this.uriOutput = uriOutput;
    }

    public PhotoHandler(Activity activity, String imageDirectory, Uri uriOutput, ImageView imageView) {
        this.activity = activity;
        this.imageDirectory = imageDirectory;
        this.uriOutput = uriOutput;
        this.imageView = imageView;
    }

    public PhotoHandler(Activity activity, String imageDirectory, String imageNamePrefix, Uri uriOutput, ImageView imageView) {
        this.activity = activity;
        this.imageDirectory = imageDirectory;
        this.imageNamePrefix = imageNamePrefix;
        this.uriOutput = uriOutput;
        this.imageView = imageView;
    }

    public PhotoHandler(Activity activity, String imageDirectory, String imageNamePrefix, int imageSizeLimit, Uri uriOutput, ImageView imageView) {
        this.activity = activity;
        this.imageDirectory = imageDirectory;
        this.imageNamePrefix = imageNamePrefix;
        this.imageSizeLimit = imageSizeLimit;
        this.uriOutput = uriOutput;
        this.imageView = imageView;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public String getImageNamePrefix() {
        return imageNamePrefix;
    }

    public void setImageNamePrefix(String imageNamePrefix) {
        this.imageNamePrefix = imageNamePrefix;
    }

    public int getImageSizeLimit() {
        return imageSizeLimit;
    }

    public void setImageSizeLimit(int imageSizeLimit) {
        this.imageSizeLimit = imageSizeLimit;
    }

    public Uri getUriOutput() {
        return uriOutput;
    }

    public void setUriOutput(Uri uriOutput) {
        this.uriOutput = uriOutput;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getImageFilePath() {
        Log.d(TAG, "get image path : " + imageFilePath);
        return imageFilePath;
    }

    public Uri getImageFilePathUri() {

        return Uri.fromFile(new File(getImageFilePath()));
    }

    private void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public void openCamera() {
        // check permision
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(activity, ALLOW_KEY)) {
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
            // open camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uriOutput = Uri.fromFile(getOutputMediaFile());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);
            cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10000000);
            activity.startActivityForResult(cameraIntent, IMAGE_REQUEST);
        }
    }

    /**
     * privileged camera
     */
    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    /**
     * privileged camera
     */
    private Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    /**
     * privileged camera
     */
    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(activity);
                    }
                });

        alertDialog.show();
    }

    /**
     * privileged camera
     */
    private void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    /**
     * result camera compress original
     */
    public void cameraResult() {
        if (isCreatedPicture()) {
            compressImageOriginal();
            Glide.with(imageView.getContext()).load(getImageFilePathUri()).into(imageView);
        } else {
            Toast.makeText(activity, "Failed capture image, source not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * result camera compress original
     *
     * @param width
     * @param height
     */
    public void cameraResult(int width, int height) {
        Log.d(TAG, "path result : " + getImageFilePathUri());
        if (isCreatedPicture()) {
            compressImageOriginal();
            Glide.with(imageView.getContext()).load(getImageFilePathUri()).into(imageView);
        } else {
            Toast.makeText(activity, "Failed capture image, source not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * result camera compress original
     *
     * @param imageView set to ImageView
     */
    public void cameraResult(ImageView imageView) {
        if (isCreatedPicture()) {
            compressImageOriginal();
            Glide.with(imageView.getContext()).load(getImageFilePathUri()).into(imageView);
        } else {
            Toast.makeText(activity, "Failed capture image, source not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * result camera compress original
     *
     * @param imageView set to ImageView
     * @param width
     * @param height
     */
    public void cameraResult(ImageView imageView, int width, int height) {
        if (isCreatedPicture()) {
            compressImageOriginal();
            Glide.with(imageView.getContext()).load(getImageFilePathUri()).into(imageView);
        } else {
            Toast.makeText(activity, "Failed capture image, source not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * get image file
     */
    public File getImage() {
        return (null != uriOutput ? new File(uriOutput.toString()) : null);
    }

    /**
     * get image uri
     */
    public String getImageUriPath() {
        return uriOutput.getPath();
    }

    /**
     * create temporary file before open camera
     */
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageDirectory);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + imageDirectory + " directory");
                return null;
            }
        }
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(mediaStorageDir.getPath());
        imagePath.append(File.separator);
        imagePath.append(imageNamePrefix);
        imagePath.append(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()));
        imagePath.append(".jpg");
        setImageFilePath(imagePath.toString());
        Log.d(TAG, "camera file path : " + getImageFilePath());
        File mediaFile = new File(getImageFilePath());
        return mediaFile;
    }

    private boolean isCreatedPicture() {
        return (null != getImageFilePath() && new File(getImageUriPath()).exists());
    }

    /**
     * compress image result original
     */
    private void compressImageOriginal() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageDirectory + "Compress");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + imageDirectory + " directory");
                return;
            }
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Bitmap bitmap = BitmapFactory.decodeFile(getImageFilePath(), options);
            FileOutputStream out = new FileOutputStream(getImageFilePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 55, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * compress image copy to compress folder
     */
    private void compressImage() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageDirectory + "Compress");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + imageDirectory + " directory");
                return;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(getImageUriPath(), options);
            FileOutputStream out = new FileOutputStream(mediaStorageDir.getPath() + File.separator + imageNamePrefix + timeStamp + ".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageDirectory);
        dest.writeString(this.imageNamePrefix);
        dest.writeString(this.imageFilePath);
        dest.writeInt(this.imageSizeLimit);
        dest.writeParcelable(this.uriOutput, flags);
    }

    protected PhotoHandler(Parcel in) {
        this.activity = in.readParcelable(Activity.class.getClassLoader());
        this.imageDirectory = in.readString();
        this.imageNamePrefix = in.readString();
        this.imageFilePath = in.readString();
        this.imageSizeLimit = in.readInt();
        this.uriOutput = in.readParcelable(Uri.class.getClassLoader());
        this.imageView = in.readParcelable(ImageView.class.getClassLoader());
    }

    public static final Creator<PhotoHandler> CREATOR = new Creator<PhotoHandler>() {
        @Override
        public PhotoHandler createFromParcel(Parcel source) {
            return new PhotoHandler(source);
        }

        @Override
        public PhotoHandler[] newArray(int size) {
            return new PhotoHandler[size];
        }
    };
}
