package com.example.do_it_api.event.service;

import com.example.do_it_api.event.Event;
import com.example.do_it_api.event.EventRepo;
import com.example.do_it_api.security.EntityAccessHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventDeleteService {

    private final EntityAccessHelper<Event> entityAccessHelper;
    private final EventRepo eventRepo;

    public ResponseEntity<Void> deleteEvent(Long id) {
        if (!entityAccessHelper.hasUserAccessTo(eventRepo.findById(id).orElseThrow(NoSuchElementException::new))) {
            throw new AccessDeniedException("Access denied");
        }
        eventRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
