package test.yuedong.com.myapplication;

import android.util.Log;

public class LogEx {
    private static boolean debugVersion = true;
    private static final String kTag = "yuedong";

    public static void initDebug(boolean debugVersion){
        LogEx.debugVersion = debugVersion;
    }

    public static void v(String msg) {
        if(debugVersion) {
            Log.v(tag(), msg);
        }
    }

    public static void d(String msg) {
        if(debugVersion) {
            Log.d(tag(), msg);
        }
    }

    public static void i(String msg) {
        if (debugVersion) {
            Log.i(tag(), msg);
        }
    }

    public static void w(String msg) {
        if(debugVersion) {
            Log.w(tag(), msg);
        } else {
            Log.w(kTag, msg);
        }
    }

    public static void e(String msg) {
        if(debugVersion) {
            Log.e(tag(), msg);
        } else {
            Log.e(kTag, msg);
        }
    }

    private static String tag() {
        StringBuilder sb = new StringBuilder();
        sb.append("yuedong ");

        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        if (null != stackTrace && stackTrace.length > 2) {
            StackTraceElement ste = stackTrace[2];
            String className = ste.getClassName();
            int index = className.lastIndexOf('.');
            if (index > 0) {
                className = className.substring(index + 1);
            }
            sb.append(className).append('.').append(ste.getMethodName());
        }

        return sb.toString();
    }
}

