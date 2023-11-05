package org.vivecraft.utils;

public class VLoader {
    static {
        System.loadLibrary("openvr_api");
    }

    public static native long createVKImage(int width, int height, boolean isLeft);
    public static native int getDMABuf(boolean isLeft);
    public static native long getInstance();
    public static native long getDevice();
    public static native long getPhysicalDevice();
    public static native long getQueue();
    public static native int getQueueIndex();
}
