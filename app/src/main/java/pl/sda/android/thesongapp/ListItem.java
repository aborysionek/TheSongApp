package pl.sda.android.thesongapp;

import org.json.JSONObject;

/**
 * Created by Właściciel on 2017-07-20.
 */

public class ListItem {

    private String head;
    private String artist;
    private String release;
    private String imageUrl;

    public ListItem(String head, String artist, String release, String imageUrl) {
        this.head = head;
        this.artist = artist;
        this.release = release;
        this.imageUrl = imageUrl;
    }

    public ListItem(String head, String artist, String release, JSONObject imageUrl) {
        this.head = head;
        this.artist = artist;
        this.release = release;
        this.imageUrl = String.valueOf(imageUrl);
    }
    public String getHead() {
        return head;
    }

    public String getArtist() {
        return artist;
    }

    public String getRelease() {
        return release;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

