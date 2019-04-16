package com.example.zhang.tgvideoconver.mp4;

import android.content.Context;

/**
 * Created by Administrator on 2019/3/13.
 */

public class VideoInfo {
    private long startTime;
    private long endTime;
    private int rotationValue;
    private int originalWidth;
    private int originalHeight;
    private int resultWidth;
    private int resultHeight;
    private int bitrate;
    private int framerate;
    private String originalPath;
    private String attachPath;

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getRotationValue() {
        return rotationValue;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public int getResultWidth() {
        return resultWidth;
    }

    public int getResultHeight() {
        return resultHeight;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getFramerate() {
        return framerate;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public static class Builder {

        private long startTime;
        private long endTime;
        private int rotationValue;
        private int originalWidth;
        private int originalHeight;
        private int resultWidth;
        private int resultHeight;
        private int bitrate;
        private int framerate;
        private String originalPath;
        private String attachPath;




        public Builder setStartTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(long endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setRotationValue(int rotationValue) {
            this.rotationValue = rotationValue;
            return this;
        }

        public Builder setOriginalWidth(int originalWidth) {
            this.originalWidth = originalWidth;
            return this;
        }

        public Builder setOriginalHeight(int originalHeight) {
            this.originalHeight = originalHeight;
            return this;
        }

        public Builder setResultWidth(int resultWidth) {
            this.resultWidth = resultWidth;
            return this;
        }

        public Builder setResultHeight(int resultHeight) {
            this.resultHeight = resultHeight;
            return this;
        }

        public Builder setBitrate(int bitrate) {
            this.bitrate = bitrate;
            return this;
        }

        public Builder setFramerate(int framerate) {
            this.framerate = framerate;
            return this;
        }

        public Builder setOriginalPath(String originalPath) {
            this.originalPath = originalPath;
            return this;
        }


        public Builder setAttachPath(String attachPath) {
            this.attachPath = attachPath;
            return this;
        }

        public VideoInfo bulid() {
            VideoInfo  videoInfo = new VideoInfo();
            videoInfo.startTime = this.startTime;
            videoInfo.endTime = this.endTime;
            videoInfo.rotationValue = this.rotationValue;
            videoInfo.originalWidth = this.originalWidth;
            videoInfo.originalHeight = this.originalHeight;
            videoInfo.resultWidth = this.resultWidth;
            videoInfo.resultHeight = this.resultHeight;
            videoInfo.bitrate = this.bitrate;
            videoInfo.framerate = this.framerate;
            videoInfo.originalPath = this.originalPath;
            videoInfo.attachPath = this.attachPath;
            return videoInfo;
        }

    }

}
