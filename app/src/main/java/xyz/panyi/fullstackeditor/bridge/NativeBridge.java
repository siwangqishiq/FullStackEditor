package xyz.panyi.fullstackeditor.bridge;

public class NativeBridge {

    static {
        System.loadLibrary("avcodec");
        System.loadLibrary("avfilter");
        System.loadLibrary("avformat");
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");
        System.loadLibrary("fullstackeditor");
    }

    public static native String ffmpegVersion();
}
