package com.karl.chatweb.messagepusher;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SavedMessageContent {
    @JsonProperty("type")
    public String type;

    @JsonProperty("content")
    public String content;
}
