package com.karl.chatweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue
    public Long id;

    @Column(length = 255)
    public String url;

    @ManyToMany(mappedBy = "badges", cascade = {
            CascadeType.MERGE
    })
    public List<User> users;

}
