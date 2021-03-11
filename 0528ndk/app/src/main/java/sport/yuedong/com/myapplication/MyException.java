package sport.yuedong.com.myapplication;

public class MyException extends Exception {


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void nativeInvokeJavaException();


    public native void nativeThrowException() throws IllegalArgumentException;

    private int operation() {
        return 2 / 0;
    }


}
