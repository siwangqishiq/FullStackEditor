package xyz.panyi.fullstackeditor.bridge;

public class NativeBridge {
    static {
        System.loadLibrary("avcodec");
        System.loadLibrary("avutil");
        System.loadLibrary("fullstackeditor");
    }


    public static native String stringFromJNI();

    public static native String ffmpegVersion();
}
