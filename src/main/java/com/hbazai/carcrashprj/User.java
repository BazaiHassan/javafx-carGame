package com.hbazai.carcrashprj;

public class User {
    private int place;
    private String username;
    private int score;

    public User(int place, String username, int score) {
        this.place = place;
        this.username = username;
        this.score = score;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

