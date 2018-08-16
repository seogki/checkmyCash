package cashcheck.skh.com.availablecash.Base;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

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

    @BindingAdapter("checkDay")
    public static void checkDay(final TextView textView, final String days) {
        Context context = textView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }
        if (days != null) {
            String replace = days.replace(" ", "").replace("-", "");
            String result = replace.substring(4, 6);
            String day = replace.substring(6, 9);
            textView.setText(result + "일 " + day);
        }

    }

    @BindingAdapter("checkTotal")
    public static void checkTotal(final TextView textView, final String days) {
        Context context = textView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }
        if (days != null) {
            String replace = days.replace(" ", "").replace("-", "");
            String totalMoney = replace.substring(9, replace.length());
            String format = UtilMethod.currencyFormat(totalMoney);
            textView.setText("" + format + "원");
        }

    }

    @BindingAdapter("setDate")
    public static void setDate(final TextView textView, final String date) {
        String month = "";
        String days = "";
        if (date.length() == 4) {
            month = date.substring(0, 2);
            days = date.substring(2, 4);
            textView.setText(month + "-" + days);
        } else if (date.length() == 3) {
            month = date.substring(0, 1);
            days = date.substring(1, 3);
            textView.setText(month + "-" + days);
        } else {
            textView.setText("");
        }

    }

    @BindingAdapter("checkMoney")
    public static void checkMoney(final TextView textView, final String money) {
        Context context = textView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }
        if (money != null) {
            if (money.contains(".")) {
                String data = money.replace(".", "").substring(0, money.length() - 2);
                String result = UtilMethod.currencyFormat(data);
                textView.setText(result + "원");
            } else {
                if (money.isEmpty() || money.length() == 0) {
                    textView.setText("");
                }
                String result = UtilMethod.currencyFormat(money);
                textView.setText(result + "원");
            }
        }

    }
}
