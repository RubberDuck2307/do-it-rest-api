package com.example.do_it_api.event.service;

import com.example.do_it_api.event.Event;
import com.example.do_it_api.event.EventRepo;
import com.example.do_it_api.event.dto.EventBriefGetDTO;
import com.example.do_it_api.event.dto.EventCreateDTO;
import com.example.do_it_api.event.dto.EventFullGetDTO;
import com.example.do_it_api.event.event_list.EventList;
import com.example.do_it_api.event.event_list.EventListRepo;
import com.example.do_it_api.event.event_list.EventListService;
import com.example.do_it_api.event.service.EventGetService;
import com.example.do_it_api.exception.custom_exception.InvalidRequestBodyException;
import com.example.do_it_api.security.EntityAccessHelper;
import com.example.do_it_api.task.Task;
import com.example.do_it_api.task.TaskRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {


    private final EventGetService eventGetService;
    private final EventDeleteService eventDeleteService;
    private final EventPostService eventPostService;
    private final EventPutService eventPutService;

    public ResponseEntity<EventBriefGetDTO> saveEvent(EventCreateDTO eventDTO) {
        return eventPostService.saveEvent(eventDTO);
    }

    public ResponseEntity<Void> deleteEvent(Long id) {
        return eventDeleteService.deleteEvent(id);
    }

    public ResponseEntity<EventBriefGetDTO> updateEvent(Long id, EventCreateDTO eventDTO) {
        return eventPutService.updateEvent(id, eventDTO);
    }

    public ResponseEntity<List<EventBriefGetDTO>> getAllEvents() {
        return eventGetService.getAllEvents();
    }

    public ResponseEntity<EventFullGetDTO> getEvent (Long id) {
        return eventGetService.getEvent(id);
    }

    public ResponseEntity<List<EventBriefGetDTO>> getTodayEvents() {
        return eventGetService.getTodayEvents();
    }

    public ResponseEntity<List<EventBriefGetDTO>> getThisWeekEvents() {
        return eventGetService.getThisWeekEvents();
    }

    public ResponseEntity<List<EventFullGetDTO>> getWeekEventsFull(LocalDate date) {
        return eventGetService.getWeekEventsFull(date);
    }

    public ResponseEntity<List<EventFullGetDTO>> getEventsByDayFull(LocalDate date) {
        return eventGetService.getEventsByDayFull(date);
    }

    public ResponseEntity<List<EventFullGetDTO>> getEventsInMonthFull(LocalDate date) {
        return eventGetService.getEventsInMonthFull(date);
    }

    public ResponseEntity<List<EventFullGetDTO>> getEventsInYearRange(LocalDate date) {
        return eventGetService.getEventsInYearRange(date);
    }

}

