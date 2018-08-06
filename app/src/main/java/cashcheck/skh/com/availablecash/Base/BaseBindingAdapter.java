package cashcheck.skh.com.availablecash.Base;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;

import cashcheck.skh.com.availablecash.Util.KeyboardUtils;
import cashcheck.skh.com.availablecash.Util.UtilMethod;

/**
 * Created by Seogki on 2018. 4. 12..
 */

public class BaseBindingAdapter {

    @BindingAdapter("keyboardDetect")
    public static void keyboardDetect(@NonNull final View view, final String data) {

        Context context = view.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }


        if (UtilMethod.getActivity(view.getContext()) != null)
            KeyboardUtils.addKeyboardToggleListener(UtilMethod.getActivity(view.getContext()), new KeyboardUtils.SoftKeyboardToggleListener() {
                @Override
                public void onToggleSoftKeyboard(boolean isVisible) {
                    view.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                }
            });

    }
}
