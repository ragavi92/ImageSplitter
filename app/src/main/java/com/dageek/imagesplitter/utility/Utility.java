package com.dageek.imagesplitter.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utility {

    public static Cursor getCursor(Context context) {
        return context.getContentResolver().query(Constants.URI, Constants.PROJECTION,
                null, null, Constants.ORDER_BY);
    }

    public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // Create a Matrix for the Manipulation
        Matrix matrix = new Matrix();
        // Resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the New Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static void saveToGallery(Context context, Bitmap bitmap, String name) {
        // Getting the bitmap to the user required size
        bitmap = Utility.resizeBitmap(bitmap, 1080, 1350);
        ContextWrapper cw = new ContextWrapper(context);
        String root = Environment.getExternalStorageDirectory().toString();
        File directory = new File(root + "/ImageSplitter");
        directory.mkdirs();
        // Create imageDir
        File imageFile = new File(directory, name + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Log.e("IMAGE STORE ERROR", e.getMessage());
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                Log.e("STREAM CLOSE ERROR", e.getMessage());
            }
        }

        // To link to Gallery
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
