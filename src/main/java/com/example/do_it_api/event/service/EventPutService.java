package com.example.do_it_api.event.service;

import com.example.do_it_api.event.Event;
import com.example.do_it_api.event.EventRepo;
import com.example.do_it_api.event.dto.EventBriefGetDTO;
import com.example.do_it_api.event.dto.EventCreateDTO;
import com.example.do_it_api.event.event_list.EventListService;
import com.example.do_it_api.exception.custom_exception.InvalidRequestBodyException;
import com.example.do_it_api.security.EntityAccessHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventPutService {

    private final EventRepo eventRepo;
    private final EntityAccessHelper<Event> entityAccessHelper;
    private final EventListService eventListService;
    private final ModelMapper modelMapper;
    @Transactional
    public ResponseEntity<EventBriefGetDTO> updateEvent(Long id, EventCreateDTO eventDTO) {
        Event event = eventRepo.findById(id).orElseThrow(NoSuchElementException::new);
        if (!entityAccessHelper.hasUserAccessTo(event)) {
            throw new AccessDeniedException("Access denied");
        }
        Event newEvent = modelMapper.map(eventDTO, Event.class);
        if (!Event.validateEvent(newEvent)) {
            throw new InvalidRequestBodyException();
        }
        event.set(newEvent);
        if (eventDTO.getListId() != null && eventDTO.getListId() != 0) {
            eventListService.addEventToList(eventDTO.getListId(), event);
        }
        if (eventDTO.getListId() == null || eventDTO.getListId() == 0) {
            event.setList(null);
        }
        EventBriefGetDTO returnEvent = modelMapper.map(event, EventBriefGetDTO.class);
        return new ResponseEntity<>(returnEvent, HttpStatus.OK);
    }
}
