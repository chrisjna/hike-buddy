package com.example.hikebuddy;

/**
 * Data model for each row of the RecyclerView
 */
public class Hike {

    // Member variables representing the title and information about the sport.
    private String title;
    private String info;
    private int imageResource;
    private int diff;
    private String gear;
    private boolean favStatus;
    private String distance;
    private String elevation;
    private String terrain;

    public Hike(String title, String info, int imageResource, int diff, String gear, String distance, String elevation, String terrain) {
        this.title = title;
        this.info = info;
        this.imageResource = imageResource;
        this.diff = diff;
        this.gear = gear;
        this.distance = distance;
        this.elevation = elevation;
        this.terrain = terrain;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getDiff() {
        return diff;
    }

    public String getGear() {return gear;}

    public boolean getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(boolean favStatus) {
        this.favStatus = favStatus;
    }

    public String getDistance(){return distance;}

    public String getElevation(){return elevation;}

    public String getTerrain(){return terrain;}
}