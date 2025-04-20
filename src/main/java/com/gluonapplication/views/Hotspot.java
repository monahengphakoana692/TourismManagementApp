package com.gluonapplication.views;

import com.gluonhq.maps.MapPoint;

public class Hotspot {
    private final String id;
    private final String title;
    private final String description;
    private final MapPoint location;
    private final String imagePath;  // Changed from imageUrl to path
    private final String audioPath;   // Changed from audioUrl to path
    private final String videoPath;   // Changed from videoUrl to path

    public Hotspot(String id, String title, String description,
                   MapPoint location, String imagePath,
                   String audioPath, String videoPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.imagePath = imagePath;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
    }

    // Getters
    public String getTitle() { return title; }
    public MapPoint getLocation() { return location; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public String getAudioPath() { return audioPath; }
    public String getVideoPath() { return videoPath; }

}