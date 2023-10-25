package com.karl.chatweb.repository;

import com.karl.chatweb.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamRepository  extends JpaRepository<Stream, Long> {
    Stream findByNid(String nid);
}
