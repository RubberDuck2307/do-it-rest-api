package com.example.ToDoAPI.event;

import com.example.ToDoAPI.event.DTOs.EventBriefGetDTO;
import com.example.ToDoAPI.event.DTOs.EventCreateDTO;
import com.example.ToDoAPI.event.DTOs.EventFullGetDTO;
import com.example.ToDoAPI.event.event_list.EventList;
import com.example.ToDoAPI.event.event_list.EventListRepo;
import com.example.ToDoAPI.event.event_list.EventListService;
import com.example.ToDoAPI.event.services.EventGetService;
import com.example.ToDoAPI.exception.customException.InvalidRequestBodyException;
import com.example.ToDoAPI.security.EntityAccessHelper;
import com.example.ToDoAPI.task.Task;
import com.example.ToDoAPI.task.TaskRepo;
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
    private final EventRepo eventRepo;
    private final ModelMapper modelMapper;
    private final TaskRepo taskRepo;
    private final EntityAccessHelper<Event> entityAccessHelper;
    private final EventListRepo eventListRepo;
    private final EventListService eventListService;
    private final EntityAccessHelper<EventList> listEntityAccessHelper;

    @Transactional
    public ResponseEntity<EventBriefGetDTO> saveEvent(EventCreateDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        event.setUserId(entityAccessHelper.getLoggedUserId());
        event = eventRepo.save(event);

        if (eventDTO.getListId() != null && eventDTO.getListId() != 0) {
            eventListService.addEventToList(eventDTO.getListId(), event);
        }

        createTasksFromEvent(eventDTO, event);
        EventBriefGetDTO returnEvent;
        returnEvent = modelMapper.map(event, EventBriefGetDTO.class);
        return new ResponseEntity<>(returnEvent, HttpStatus.OK);
    }


    public ResponseEntity<Void> deleteEvent(Long id) {
        if (!entityAccessHelper.hasUserAccessTo(eventRepo.findById(id).orElseThrow(NoSuchElementException::new))) {
            throw new AccessDeniedException("Access denied");
        }
        eventRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
        createTasksFromEvent(eventDTO, event);
        EventBriefGetDTO returnEvent = modelMapper.map(event, EventBriefGetDTO.class);
        return new ResponseEntity<>(returnEvent, HttpStatus.OK);
    }


    private void createTasksFromEvent(EventCreateDTO eventDTO, Event event) {
        Set<Task> taskList = new HashSet<>();
        eventDTO.getRelatedTasks().forEach(taskDTO -> {
            Task task = modelMapper.map(taskDTO, Task.class);
            task.setUserId(entityAccessHelper.getLoggedUserId());
            task.setEvent(event);
            if (task.getId() == null) taskList.add(task);
        });
        taskRepo.saveAll(taskList);
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

