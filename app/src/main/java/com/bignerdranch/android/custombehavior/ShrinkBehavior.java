package com.bignerdranch.android.custombehavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

// TODO: 5/22/16 3 làm class con, và override 2 method sau
public class ShrinkBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public ShrinkBehavior() { }

    public ShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // TODO: 5/22/16 4 method này thì FAB sẽ bị View nào thay đổi, ta chọn thanh Snackbar
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    // TODO: 5/22/16 5 method này được gọi thì View dc FAB theo dõi thay đổi -> khi thanh snackbar từ từ nhú lên thì method này dc gọi thường xuyên làm FAB thu nhỏ lại
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = getFabTranslationYForSnackbar(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        float scaleFactor = 1 - percentComplete;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);
        return false;
    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}
