package org.cise.core.utilities.ui.imageview;
//
// Created by  on 2019-12-02.
//

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.cise.core.R;

@SuppressLint("AppCompatCustomView")
public class ImageViewRound extends ImageView {

    private float radius = 24.0f;
    private Path path;
    private RectF rect;

    public ImageViewRound(Context context) {
        super(context);
        init();
    }

    public ImageViewRound(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageViewRound, 0, 0);
        try {
            radius = a.getDimension(R.styleable.ImageViewRound_round, radius);
        } finally {
            a.recycle();
        }
        init();
    }

    public ImageViewRound(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
