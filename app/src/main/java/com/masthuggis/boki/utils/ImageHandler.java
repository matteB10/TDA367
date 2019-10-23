package com.masthuggis.boki.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Handles image processing logic. For example when taking a picture, downscaling and rotating it.
 */
public class ImageHandler {
    private Activity activity;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private File currentImageFile;
    private ImageView imageViewDisplay;

    public ImageHandler(Activity activity, ImageView imageViewDisplay) {
        this.activity = activity;
        this.imageViewDisplay = imageViewDisplay;
        imageViewDisplay.setOnClickListener(view -> dispatchTakePictureIntent());
    }

    /**
     * Method that is run after android OS has completed taking a picture
     */
    public boolean onActivityResult(int requestCode, int resultCode) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //When image has been taken
            compressAndRotateImage();
            return true;
        }
        return false;
    }

    public File getCurrentImageFile() {
        return currentImageFile;
    }

    public void onImageUpdated(String url) {
        Glide.with(activity).load(url).into(imageViewDisplay);
    }

    /**
     * Sends a request to the operating system for the application's use of the device's camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                currentImageFile = createImageFile();
            } catch (Exception i) {
                System.out.println("error creating file");
                i.printStackTrace();
            }
            if (currentImageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(activity.getApplicationContext(), "com.masthuggis.boki.fileprovider",
                        currentImageFile);
                System.out.println(imageURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Compresses the size of the image taken by the user, checks if rotation is necessary and, if so, rotates the image to portrait mode
     * Also sets the image held by the ImageView shown to the user to the compressed and rotated image
     */
    private void compressAndRotateImage() {
        try {
            Bitmap initialBitmap = BitmapFactory.decodeFile(currentImageFile.getPath());
            Bitmap rotatedImage = rotateImageIfRequired(initialBitmap, currentImageFile.getAbsolutePath());
            Bitmap finalBitmap = compressBitmap(rotatedImage);
            setImageView(finalBitmap); //Set compressed and scaled image so user sees actual result
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param imageBitmap the Bitmap to be rotated
     * @param imagePath the path to the image taken, used in order to determine how much rotation is needed
     * @return a bitmap of the rotated image
     * @throws IOException if the supplied image path is invalid
     */
    private Bitmap rotateImageIfRequired(Bitmap imageBitmap, String imagePath) throws IOException {
        int rotationDegrees = getRotationDegrees(imagePath);
        return rotateImage(imageBitmap, rotationDegrees);
    }

    /**
     *
     * @param imagePath path to the image taken by the user, holds metadata about its orientation
     * @return the degrees of rotation needed for picture to be oriented in portrait mode
     * @throws IOException if supplied image path is invalid
     */
    private int getRotationDegrees(String imagePath) throws IOException {
        ExifInterface exifInterface = new ExifInterface(imagePath);
        int orientationTag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientationTag) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;

        }
    }

    /**
     * @param image the bitmap to be rotated
     * @param degrees the degrees of rotation
     * @return a rotated bitmap of the same image as was given as input
     */
    private Bitmap rotateImage(Bitmap image, int degrees) {
        if (degrees == 0) {
            return image;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    /**
     * @param input the bitmap that should be compressed
     * @return a bitmap of the same image, but compressed
     * @throws IOException if the path used to create an OutputStream isn't valid
     */
    private Bitmap compressBitmap(Bitmap input) throws IOException {
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        Bitmap scaled = Bitmap.createScaledBitmap(input, 1024, 1024, false);
        OutputStream out = new FileOutputStream(currentImageFile.getPath());
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, out); //Compresses bitmap and writes result to currentImage's path
        return BitmapFactory.decodeFile(currentImageFile.getPath());
    }

    /**
     * Creates an empty file and specifies unique file name.
     *
     * @throws IOException if image creation fails
     */
    private File createImageFile() throws IOException {
        String photoFileName = "IMG_" + UniqueIdCreator.getUniqueID();
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(photoFileName, ".jpg", storageDir);
        return image;
    }

    private void setImageView(Bitmap bitmap) {
        imageViewDisplay.setImageBitmap(bitmap);
    }
}
