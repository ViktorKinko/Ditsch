package com.bytepace.ditsch.utils.transitions;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;

public final class TransitionUtils {

    /**
     * Returns a modified enter transition that excludes the navigation bar and status
     * bar as targets during the animation. This ensures that the navigation bar and
     * status bar won't appear to "blink" as they fade in/out during the transition.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    /**
     * Returns a transition that will (1) move the shared element to its correct size
     * and location on screen, (2) gradually increase/decrease the shared element's
     * text size, and (3) gradually alters the shared element's text color through out
     * the transition.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Transition makeSharedElementEnterTransition(int target_id, String anim_id) {
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);

        Transition recolor = new Recolor();
        recolor.addTarget(target_id);
        recolor.addTarget(anim_id);
        set.addTransition(recolor);

        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(target_id);
        changeBounds.addTarget(anim_id);
        set.addTransition(changeBounds);

        Transition textSize = new TextSizeTransition();
        textSize.addTarget(target_id);
        textSize.addTarget(anim_id);
        set.addTransition(textSize);

        return set;
    }

    private TransitionUtils() {
    }
}
