package com.example.zhang.tgvideoconver.utils;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2019/3/14.
 */

public class Utilities {
    public native static int convertVideoFrame(ByteBuffer src, ByteBuffer dest, int destFormat, int width, int height, int padding, int swap);
}
