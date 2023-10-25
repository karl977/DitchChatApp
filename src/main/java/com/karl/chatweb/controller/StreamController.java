package com.karl.chatweb.controller;

import com.karl.chatweb.model.Stream;
import com.karl.chatweb.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class StreamController {

    @Autowired
    private StreamRepository streamRepository;
    @GetMapping("/api/user/streams")
    public ResponseEntity<List<Stream>> getStreams(){
        return ResponseEntity.ok(streamRepository.findAll());
    }

    @GetMapping("/api/user/streams/{nid}")
    public ResponseEntity<Stream> getStream(@PathVariable(value="nid") String nid){
        Stream stream = streamRepository.findByNid(nid);
        if(stream == null){
            return ResponseEntity.status(404).body(null);
        }else{
            return ResponseEntity.ok(stream);
        }
    }
}
