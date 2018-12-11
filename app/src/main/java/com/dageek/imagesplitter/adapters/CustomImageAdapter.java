package com.dageek.imagesplitter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.dageek.imagesplitter.R;
import com.dageek.imagesplitter.interfaces.OnImageSelectionListener;
import com.dageek.imagesplitter.modals.CustomImage;
import com.dageek.imagesplitter.utility.Utility;

import java.util.ArrayList;


public class CustomImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CustomImage> list;
    private OnImageSelectionListener onImageSelectionListener;
    private RequestManager glide;
    private RequestOptions options;
    private float size;
    private int margin = 2;
    private int padding;

    public CustomImageAdapter(Context context) {
        this.list = new ArrayList<>();
        size = Utility.convertDpToPixel(92, context) - 2;
        padding = (int) (size / 3.5);
        glide = Glide.with(context);
        options = new RequestOptions().override(256).transform(new CenterCrop()).transform(new FitCenter());
    }

    public void addOnSelectionListener(OnImageSelectionListener onImageSelectionListener) {
        this.onImageSelectionListener = onImageSelectionListener;
    }

    public CustomImageAdapter addImage(CustomImage image) {
        list.add(image);
        notifyDataSetChanged();
        return this;
    }

    public ArrayList<CustomImage> getItemList() {
        return list;
    }

    public void addImageList(ArrayList<CustomImage> images) {
        list.addAll(images);
        notifyDataSetChanged();
    }


    public void clearList() {
        list.clear();
    }

    public void select(boolean selection, int pos) {
        if (pos < 100) {
            list.get(pos).setSelected(selection);
            notifyItemChanged(pos);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.view_gallary_image, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomImage image = list.get(position);
        if (holder instanceof Holder) {
            Holder imageHolder = (Holder) holder;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) size, (int) size);
            layoutParams.setMargins(margin, margin, margin, margin);
            imageHolder.itemView.setLayoutParams(layoutParams);

            imageHolder.selection.setPadding(padding, padding, padding, padding);
            imageHolder.preview.setLayoutParams(layoutParams);

            glide.load(image.getContentUrl()).apply(options).into(imageHolder.preview);

            imageHolder.selection.setVisibility(image.getSelected() ? View.VISIBLE : View.GONE);
        } else {
            HolderNone noneHolder = (HolderNone) holder;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0, 0);
            noneHolder.itemView.setLayoutParams(layoutParams);
            noneHolder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView preview;
        private ImageView selection;


        Holder(View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            selection = itemView.findViewById(R.id.selection);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = this.getLayoutPosition();
            onImageSelectionListener.onClick(list.get(id), view, id);
        }
    }

    public class HolderNone extends RecyclerView.ViewHolder {
        HolderNone(View itemView) {
            super(itemView);
        }
    }
}
