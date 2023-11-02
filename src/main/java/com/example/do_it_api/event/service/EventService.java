package com.example.do_it_api.event.service;

import com.example.do_it_api.event.dto.EventBriefGetDTO;
import com.example.do_it_api.event.dto.EventCreateDTO;
import com.example.do_it_api.event.dto.EventFullGetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public <T> ResponseEntity<List<T>> getAllEvents(Class<T> T) {
        return eventGetService.getAllEvents(T);
    }

    public <T> ResponseEntity<T> getEvent (Long id, Class<T> T) {
        return eventGetService.getEvent(id, T);
    }


    public <T> ResponseEntity<List<T>> getEventsByWeek(LocalDate date, Class<T> T) {
        return eventGetService.getEventsByWeek(date, T);
    }

    public <T> ResponseEntity<List<T>> getEventsByDay(LocalDate date, Class<T> T) {
        return eventGetService.getEventsByDay(date, T);
    }

    public <T> ResponseEntity<List<T>> getEventsByMonth(LocalDate date, Class<T> T) {
        return eventGetService.getEventsByMonth(date, T);
    }

    public <T> ResponseEntity<List<T>> getEventsInYearRange(LocalDate date, Class<T> T) {
        return eventGetService.getEventsInYearRange(date, T);
    }

}

