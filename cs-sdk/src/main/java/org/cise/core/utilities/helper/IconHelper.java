package org.cise.core.utilities.helper;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.widget.ImageView;

/**
 * Created by Zuliadin on 08/01/2017.
 */

public class IconHelper {

    public static void changeColor(Context context, Drawable drawable, int colorResource) {
        int color = ContextCompat.getColor(context, colorResource);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public static void changeColor(Context context, ImageView imageView, int colorResource) {
        int color = ContextCompat.getColor(context, colorResource);
        imageView.setColorFilter(color);
    }
}
