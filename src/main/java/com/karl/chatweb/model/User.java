package com.karl.chatweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", indexes = @Index(name = "idx_users_username", columnList = "username", unique = true))
public class User {
    @Id
    @GeneratedValue
    public Long id;

    @Column(length = 100, nullable = false, unique = true)
    public String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;

    @Column(length = 50)
    public String role;

    @Column(length = 7)
    public String color;

    @ManyToMany(cascade = {
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_badges",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "badge_id")},
            indexes = {
                    @Index(name = "idx_users_badges_user_id", columnList = "user_id"),
                    @Index(name = "idx_users_badges_badge_id", columnList = "badge_id")
             }
    )
    public List<Badge> badges;

}
