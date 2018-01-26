package com.n1technology.app.exampreparationnetwork.quiz;

/**
 * Created by RAKEZ on 11/28/2017.
 */

public class ItemLevel {
    private String levelId;
    private boolean isActive;

    public ItemLevel(String levelId, boolean isActive) {
        this.levelId = levelId;
        this.isActive = isActive;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
