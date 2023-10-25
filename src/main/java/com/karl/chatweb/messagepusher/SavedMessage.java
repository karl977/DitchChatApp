package com.karl.chatweb.messagepusher;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SavedMessage {
    @JsonProperty("time")
    public int time;

    @JsonProperty("badges")
    public ArrayList<String> badges;

    @JsonProperty("username")
    public String username;

    @JsonProperty("color")
    public String color;

    @JsonProperty("contents")
    public ArrayList<SavedMessageContent> contents;
}
