package com.forecast.weather.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Aleksei Romashkin
 * on 03/02/16.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    public static final int SPACE = 15;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = SPACE;

        if(parent.getChildPosition(view) == 0)
            outRect.top = SPACE;
    }
}
