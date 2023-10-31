package com.example.do_it_api.event.service;

import com.example.do_it_api.event.Event;
import com.example.do_it_api.event.EventRepo;
import com.example.do_it_api.event.dto.EventBriefGetDTO;
import com.example.do_it_api.event.dto.EventCreateDTO;
import com.example.do_it_api.event.event_list.EventListService;
import com.example.do_it_api.security.EntityAccessHelper;
import com.example.do_it_api.task.Task;
import com.example.do_it_api.task.TaskRepo;
import com.example.do_it_api.user.DefaultUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventPostService {

    private final ModelMapper modelMapper;
    private final EntityAccessHelper<Event> entityAccessHelper;
    private final EventRepo eventRepo;
    private final EventListService eventListService;

    public ResponseEntity<EventBriefGetDTO> saveEvent(EventCreateDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        DefaultUserDetails loggedUser = entityAccessHelper.getLoggedUser();
        event.setUser(loggedUser);
        event.getRelatedTasks().forEach(task ->
        {
            task.setUser(loggedUser);
            task.setEvent(event);
        });
        Event savedEvent = eventRepo.save(event);
        if (eventDTO.getListId() != null && eventDTO.getListId() != 0) {
            eventListService.addEventToList(eventDTO.getListId(), event);
        }
        EventBriefGetDTO returnEvent;
        returnEvent = modelMapper.map(savedEvent, EventBriefGetDTO.class);
        return new ResponseEntity<>(returnEvent, HttpStatus.OK);
    }
}
