package com.example.bookapi;

import com.google.gson.annotations.SerializedName;

public class OwnerPrefs {
    @SerializedName("title")
    public String title;
    @SerializedName("oCoverImg")
    public String oCoverImg;
    public String getTitle() {
        return title;
    }

    public String getoCoverImg() {
        return oCoverImg;
    }
}
