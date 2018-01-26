package com.n1technology.app.exampreparationnetwork.model;

/**
 * Created by RAKEZ on 12/8/2017.
 */

public class LeaderBoard {
    private String name;
    private String imageURL;
    private String score;

    public LeaderBoard() {
    }

    public LeaderBoard(String name, String imageURL, String score) {
        this.name = name;
        this.imageURL = imageURL;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
