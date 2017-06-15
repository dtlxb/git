package com.hply.alilayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

public interface LayoutViewFactory {

    View generateLayoutView(@NonNull final Context context);
}
