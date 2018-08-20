package cashcheck.skh.com.availablecash.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Seogki on 2018. 6. 18..
 */

public class UtilMethod {

    @Nullable
    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy-MM", Locale.KOREA);
        return df.format(c);
    }

    public static String getCurrentDateToSec() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy-MM dd HH:mm:ss", Locale.KOREA);
        return df.format(c);
    }

    @SuppressLint("DefaultLocale")
    public static String getMemoryUsage(int i) {
        String heapSize = String.format("%.3f", (float) (Runtime.getRuntime().totalMemory() / 1024.00 / 1024.00));
        String freeMemory = String.format("%.3f", (float) (Runtime.getRuntime().freeMemory() / 1024.00 / 1024.00));

        String allocatedMemory = String
                .format("%.3f", (float) ((Runtime.getRuntime()
                        .totalMemory() - Runtime.getRuntime()
                        .freeMemory()) / 1024.00 / 1024.00));
        String heapSizeLimit = String.format("%.3f", (float) (Runtime.getRuntime().maxMemory() / 1024.00 / 1024.00));

        String nObjects = "Objects Allocated: " + i;

        return "Current Heap Size: " + heapSize
                + "\n Free memory: "
                + freeMemory
                + "\n Allocated Memory: "
                + allocatedMemory
                + "\n Heap Size Limit:  "
                + heapSizeLimit
                + "\n" + nObjects;


    }

    public static String currencyFormat(String amount) {
        if (amount.isEmpty() || amount.length() == 0) {
            return "";
        }
        if (amount.contains("첫번째")) {
            String result = amount.replace("첫번째", "");
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            return formatter.format(Double.parseDouble(result));
        } else {
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            return formatter.format(Double.parseDouble(amount));
        }

    }
}
