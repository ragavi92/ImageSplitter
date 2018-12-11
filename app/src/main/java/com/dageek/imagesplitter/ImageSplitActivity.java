package com.dageek.imagesplitter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dageek.imagesplitter.adapters.CustomImageAdapter;
import com.dageek.imagesplitter.interfaces.OnImageSelectionListener;
import com.dageek.imagesplitter.modals.CustomImage;
import com.dageek.imagesplitter.utility.Utility;
import com.dageek.imagesplitter.views.FrameView;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class ImageSplitActivity extends AppCompatActivity {

    private RecyclerView galleryRecyclerView;
    private CustomImageAdapter customImageAdapter;
    private ZoomageView previewImage;
    private float frameWidth = 0;
    private float frameHeight = 0;
    private int numberOfFrames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_split);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Getting value through Intent from previous activity
        Intent mIntent = getIntent();
        numberOfFrames = mIntent.getIntExtra("numberOfFrames", 2);

        FrameView frameView = findViewById(R.id.frameView);
        frameView.setFrameCount(numberOfFrames);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        galleryRecyclerView = findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.setLayoutManager(linearLayoutManager);

        customImageAdapter = new CustomImageAdapter(this);
        customImageAdapter.addOnSelectionListener(onImageSelectionListener);
        galleryRecyclerView.setAdapter(customImageAdapter);
        previewImage = findViewById(R.id.previewImage);

        loadImages();
    }

    private void loadImages() {
        Cursor cursor = Utility.getCursor(ImageSplitActivity.this);
        ArrayList<CustomImage> imageList = new ArrayList<>();
        int limit = 100;
        if (cursor.getCount() < 100) {
            limit = cursor.getCount();
        }
        int data = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int contentUrl = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        for (int i = 0; i < limit; i++) {
            cursor.moveToNext();
            Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + cursor.getInt(contentUrl));
            imageList.add(new CustomImage("" + path, cursor.getString(data)));
        }
        cursor.close();
        customImageAdapter.clearList();
        customImageAdapter.addImageList(imageList);
        customImageAdapter.notifyDataSetChanged();
        // To select the first image by default
        setImageForPreview(imageList.get(0).getUrl(), 0);
    }

    private OnImageSelectionListener onImageSelectionListener = new OnImageSelectionListener() {
        @Override
        public void onClick(CustomImage customImage, View view, int position) {
            setImageForPreview(customImage.getUrl(), position);
        }
    };

    public void setImageForPreview(String imageURL, int position) {
        customImageAdapter.select(position);
        File f = new File(imageURL);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
        previewImage.setImageBitmap(bitmap);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.export, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.export:
                splitAndExportImages();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void splitAndExportImages() {
        previewImage.setDrawingCacheEnabled(true);
        Bitmap sourceBitmap = Bitmap.createBitmap(previewImage.getDrawingCache());
        previewImage.setDrawingCacheEnabled(false);
        Bitmap[] images = splitBitmap(sourceBitmap);
        for (Bitmap image : images) {
            String fileName = (new Random().nextInt(61) + 20) + "";
            Utility.saveToGallery(getApplicationContext(), image, fileName);
        }
        Toast.makeText(getApplicationContext(), "Exported Successfully", Toast.LENGTH_SHORT).show();
    }

    public Bitmap[] splitBitmap(Bitmap bitmap) {
        int count = numberOfFrames;
        frameWidth = bitmap.getWidth() / count;
        frameHeight = (frameWidth / 4) * 5;

        Bitmap[] images = new Bitmap[count];
        for (int sliceNo = 1; sliceNo <= count; sliceNo++) {
            images[sliceNo - 1] = sliceBitmap(sliceNo, bitmap);
        }
        return images;
    }

    private Bitmap sliceBitmap(int sliceNo, Bitmap bitmap) {
        float startX = (sliceNo - 1) * frameWidth;
        float startY = (bitmap.getHeight() - frameHeight) / 2;
        return Bitmap.createBitmap(bitmap, Math.round(startX), Math.round(startY), Math.round(frameWidth), Math.round(frameHeight));
    }

}
