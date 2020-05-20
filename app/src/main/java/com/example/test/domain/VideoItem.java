package com.example.test.domain;

public class VideoItem {

    private String path;

    private long date;

    private int videoLength;


    @Override
    public String toString() {
        return "VideoItem{" +
                "path='" + path + '\'' +
                ", date=" + date +
                ", videoLength=" + videoLength +
                '}';
    }

    public VideoItem(String path, long date, int videoLength) {
        this.path = path;
        this.date = date;
        this.videoLength = videoLength;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }
}
