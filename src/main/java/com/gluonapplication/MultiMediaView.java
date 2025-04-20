package com.gluonapplication;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MultiMediaView
{
    private Image image;
    private ImageView imageView;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public MultiMediaView()
    {
        image = null;
        imageView=null;
        media = null;
        mediaPlayer = null;
        mediaView = null;
    }

    public void setVideoUrl(String url)
    {
        media = new Media(url.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
    }

    public MediaView getMediaView()
    {
        return mediaView;
    }

    public void setImageUrl(String url)
    {
        image = new Image(url.toString());
        imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        //imageView.setPreserveRatio(true);
    }
    public ImageView getImageView()
    {
        return imageView;
    }


}
