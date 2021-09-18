package org.cise.core.utilities.commons;

import android.content.res.Resources;
import android.util.TypedValue;

public class DimensionValue {

    public static int dpToPx(int value) {
        Resources r = ContextHelper.getApplication().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics()));
    }
}
