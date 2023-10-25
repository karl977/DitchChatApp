package com.karl.chatweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "streams")
public class Stream {
    @Id
    @GeneratedValue
    public Long id;

    @Column(length = 50, nullable = false, unique = true)
    public String nid;

    @Column(length = 50)
    public String name;

    public String title;

    public String category;

    @JsonProperty("image_url")

    public String imageUrl;

    @JsonProperty("avatar_url")
    public String avatarUrl;

    @JsonProperty("video_id")
    public String videoId;

    public Long viewers;
}
