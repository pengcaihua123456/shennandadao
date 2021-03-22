package test.yuedong.com.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StrUtil {
    public static String getPhoneNum(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNum = manager.getLine1Number();
        if (null != phoneNum && phoneNum.length() > 11) {
            int length = phoneNum.length();
            phoneNum = phoneNum.substring(length - 11, length);
        }
        return checkPhoneNum(phoneNum) ? phoneNum : null;
    }

    public static int calcTextWidth(Paint paint, String demoText) {
        return (int)paint.measureText(demoText);
    }

    public static int calcTextHeight(Paint paint, String demoText) {
        Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }

    public static CharSequence srtCutOff(CharSequence string, int length) {
        if(TextUtils.isEmpty(string)) {
            return string;
        }
        if(string.length() > length) {
            return string.subSequence(0, length-1) + "...";
        }
        return string;
    }

    public static String formatPhoneNumber(String phoneNum) {
        if (null == phoneNum) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNum.length(); ++i) {
            char ch = phoneNum.charAt(i);
            if (ch >= '0' && ch <= '9') {
                sb.append(ch);
            }
        }
        phoneNum = sb.toString();
        if (phoneNum.length() > 11) {
            return phoneNum.substring(phoneNum.length() - 11, phoneNum.length());
        }

        return phoneNum;
    }

    public static String formatPercent(int numerator, int denominator, int decimalsSize) {

        float percent = (float) numerator * 100 / denominator;

        String format = "%." + decimalsSize + "f";
        LogEx.v("percent: " + percent);
        try {
            return String.format(format, percent) + "%";
        } catch (Exception e) {
            return "error";
        }
    }

    public static String getDateTimeStr(long time_ms) {
        return DateFormat.format("yyyy-MM-dd kk:mm", time_ms).toString();
    }

    public static long getTimeMillis(SimpleDateFormat format, String timeString) {
        if (null == format || null == timeString) {
            return 0;
        }

        try {
            return format.parse(timeString).getTime();
        } catch (Exception e) {
            LogEx.w(e.toString());
            return 0;
        }
    }

    public static String getTimeIntervalUpToNow(long time_ms) {
        StringBuffer sb = new StringBuffer();
        long now = new Date().getTime();
        long interval = now - time_ms;
        if (interval > 31536000000l) {
            sb.append(interval / 31536000000l);
            sb.append("年前");
        } else if (interval > 2592000000l) {
            sb.append(interval / 2592000000l);
            sb.append("月前");
        } else if (interval > 604800000l) {
            sb.append(interval / 604800000l);
            sb.append("周前");
        } else if (interval > 86400000l) {
            sb.append(interval / 86400000l);
            sb.append("天前");
        } else if (interval > 3600000l) {
            sb.append(interval / 3600000l);
            sb.append("小时前");
        } else {
            long min = interval / 60000l;
            if (min == 0) {
                min = 1;
            }
            sb.append(min);
            sb.append("分钟前");
        }

        return sb.toString();
    }

    private static String kEmailReg = "^[a-zA-Z0-9][a-zA-Z0-9_.]*@[\\w]+(\\.[\\w])*(\\.[\\w]{2,3})$";

    public static boolean checkEmail(String email) {
        if (null == email)
            return false;
        return email.matches(kEmailReg);
    }

    private static String kPasswordReg = "^[\\w!@#$%^&*()-= _+\\[\\]{}\\\\|;':\",./<>?]{6,20}+$";

    public static boolean checkPassword(String password) {
        if (null == password) {
            return false;
        }
        return password.matches(kPasswordReg);
    }

//    private static String kPhoneNumReg = "^[0-9]{8,14}$";
    private static String kPhoneNumReg = "^[1]([0-9]{2})[0-9]{8}$";

    public static boolean checkPhoneNum(String phoneNum) {
        if (null == phoneNum) {
            return false;
        }
        return phoneNum.matches(kPhoneNumReg);
    }

    private static final String HEX_STR = "0123456789abcdef";

    public static String toHex(byte[] bytes) {
        StringBuilder buider = new StringBuilder(bytes.length << 1);

        for (int i = 0; i < bytes.length; ++i) {
            buider.append(HEX_STR.charAt((bytes[i] & 0xf0) >> 4));
            buider.append(HEX_STR.charAt(bytes[i] & 0x0f));
        }

        return buider.toString();
    }

    public static String toMD5Hex(String str) {
        return toHex(toMD5(str.getBytes()));
    }

	public static String toSHA1Hex(String str) {
		return toHex(toSHA1(str.getBytes()));
	}

    public static byte[] toMD5(byte[] bytes) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        md5.update(bytes);
        return md5.digest();
    }

	public static byte[] toSHA1(byte[] bytes) {
		MessageDigest SHA1 = null;
		try {
			SHA1 = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
		}

		SHA1.update(bytes);
		return SHA1.digest();
	}

    public static boolean empty(String str) {
        return null == str || 0 == str.length();
    }

    public static String encodePassword(String password) {
        return toMD5Hex(password).substring(0, 10);
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    public static String linkObjects(Object... args) {
        StringBuilder sSb = new StringBuilder();
        try{
            for(Object obj: args) {
                sSb.append(obj);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return sSb.toString();
    }

    public static String linkStringArray(List<String> strings, String splitString) {
        if (strings == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < strings.size(); i++) {
            if (i < strings.size() - 1) {
                stringBuilder.append(strings.get(i));
                if (splitString != null) {
                    stringBuilder.append(splitString);
                }
            } else {
                stringBuilder.append(strings.get(i));
            }
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> intArrayToStrArray(List<Integer> integers){
        ArrayList<String> strings = new ArrayList<>();
        if(integers == null){
            return strings;
        }
        for(int index = 0;index<integers.size();index++){
            strings.add(Integer.toString(integers.get(index)));
        }
        return strings;
    }
}
