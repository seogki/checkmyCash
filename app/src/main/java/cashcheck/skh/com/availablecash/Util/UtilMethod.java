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

    public static String getCurrentMonth() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd", Locale.KOREA);
        return df.format(c);
    }

    public static String getCurrentYear() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.KOREA);
        return df.format(c);
    }


    public static String getCurrentDateToSec() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy-MM dd HH:mm:ss", Locale.KOREA);
        return df.format(c);
    }

    public static String millisToDate(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd", Locale.KOREA);
        Date date = calendar.getTime();
        return df.format(date);
    }

    public static String getWeekofDay(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd", Locale.KOREA);
        try {
            Date first = df.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(first);
            int value = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            switch (value) {
                case 0:
                    return "일요일";
                case 1:
                    return "월요일";
                case 2:
                    return "화요일";
                case 3:
                    return "수요일";
                case 4:
                    return "목요일";
                case 5:
                    return "금요일";
                case 6:
                    return "토요일";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getExcelDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss", Locale.KOREA);
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
        if (amount.isEmpty()) {
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
