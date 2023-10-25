package com.karl.chatweb.repository;

import com.karl.chatweb.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Badge findByUrl(String url);
}
