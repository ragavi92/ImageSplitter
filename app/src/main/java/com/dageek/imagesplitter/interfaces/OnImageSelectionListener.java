package com.dageek.imagesplitter.interfaces;

import android.view.View;

import com.dageek.imagesplitter.modals.CustomImage;


public interface OnImageSelectionListener {
    void onClick(CustomImage customImage, View view, int position);
}
