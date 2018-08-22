package cashcheck.skh.com.availablecash.Base;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

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
            textView.setText(result);
        }

    }

    @BindingAdapter("checkDays")
    public static void checkDays(final TextView textView, final String days) {
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
            String day = replace.substring(6, 9);
            textView.setText(day);
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

    @BindingAdapter("setMonth")
    public static void setMonth(final TextView textView, final String date) {
        String month = "";
        String days = "";
        String result = date.replace("-", "").replace(" ", "");
        if (result.length() == 4) {
            month = result.substring(0, 2);
            days = result.substring(2, 4);
            textView.setText("20" + month + "년 " + days + "월");
        } else if (result.length() == 3) {
            month = result.substring(0, 1);
            days = result.substring(1, 3);
            textView.setText("20" + month + "년 " + days + "월");
        } else {
            textView.setText("");
        }

    }

    @BindingAdapter("setYear")
    public static void setYear(final TextView textView, final String date) {
        String month;
        String days;
        String result = date.replace("-", "").replace(" ", "");
        if (!Objects.equals(result, "")) {
            month = result.substring(2, 4);
            days = result.substring(4, 6);
            textView.setText(month + "월 " + days + "일");
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
