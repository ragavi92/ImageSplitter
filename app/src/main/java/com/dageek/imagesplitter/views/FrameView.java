package com.dageek.imagesplitter.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class FrameView extends View {
    Paint paintForFrame = new Paint();
    int frameCount = 0;
    float frameWidth = 0;
    float frameHeight = 0;
    float strokeWidth = 5;

    Paint paintForExtraSpace = new Paint();

    private void init() {
        paintForFrame.setColor(Color.RED);
        paintForFrame.setStrokeWidth(strokeWidth);
        paintForFrame.setStyle(Paint.Style.STROKE);
        paintForExtraSpace.setColor(Color.parseColor("#80000000"));
    }

    public FrameView(Context context) {
        super(context);
        init();
    }

    public FrameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    @Override
    public void onDraw(Canvas canvas) {
        frameWidth = this.getWidth() / frameCount;
        frameHeight = (frameWidth / 4) * 5;
        for (int sliceNo = 1; sliceNo <= frameCount; sliceNo++) {
            drawFrame(canvas, sliceNo);
        }
        this.grayExtraSpace(canvas);
    }

    private void drawFrame(Canvas canvas, int sliceNo) {
        float startX = (sliceNo - 1) * frameWidth;
        float startY = (this.getHeight() - frameHeight) / 2;
        float stopX = startX + frameWidth;
        float stopY = startY + frameHeight;
        RectF rect = new RectF(startX, startY, stopX, stopY);
        canvas.drawRect(rect, paintForFrame);
    }

    private void grayExtraSpace(Canvas canvas) {
        float startY = (this.getHeight() - frameHeight) / 2;
        RectF topRect = new RectF(0, 0, this.getWidth(), startY);
        RectF bottomRect = new RectF(0, startY + frameHeight, this.getWidth(), this.getHeight());
        canvas.drawRect(topRect, paintForExtraSpace);
        canvas.drawRect(bottomRect, paintForExtraSpace);
    }
}
