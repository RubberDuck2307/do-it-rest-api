package com.example.do_it_api.event.service;

import com.example.do_it_api.event.dto.EventBriefGetDTO;
import com.example.do_it_api.event.dto.EventFullGetDTO;
import com.example.do_it_api.event.Event;
import com.example.do_it_api.event.EventRepo;
import com.example.do_it_api.security.EntityAccessHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventGetService {
    private final EventRepo eventRepo;
    private final ModelMapper modelMapper;
    private final EntityAccessHelper<Event> entityAccessHelper;

    public ResponseEntity<List<EventFullGetDTO>> getEventsByDayFull(LocalDate localDate){
        List<Event> events = getEventsByDay(localDate);
        List<EventFullGetDTO> eventFullGetDTOS = events.stream().map(event -> modelMapper.map(event, EventFullGetDTO.class)).toList();
        return new ResponseEntity<>(eventFullGetDTOS, HttpStatus.OK);
    }

    public ResponseEntity<List<EventBriefGetDTO>> getEventsByDayBrief(LocalDate localDate){
        List<Event> events = getEventsByDay(localDate);
        List<EventBriefGetDTO> eventBriefGetDTOS = events.stream().map(event -> modelMapper.map(event, EventBriefGetDTO.class)).toList();
        return new ResponseEntity<>(eventBriefGetDTOS, HttpStatus.OK);
    }

    public ResponseEntity<List<EventBriefGetDTO>> getEventsInMonthBrief(LocalDate localDate){
        List<Event> events = getEventsInMonth(localDate);
        List<EventBriefGetDTO> eventBriefGetDTOS = events.stream().map(event -> modelMapper.map(event, EventBriefGetDTO.class)).toList();
        return new ResponseEntity<>(eventBriefGetDTOS, HttpStatus.OK);
    }



    public ResponseEntity<List<EventFullGetDTO>> getEventsInMonthFull(LocalDate localDate){
        List<Event>  events = getEventsInMonth(localDate);
        List<EventFullGetDTO> dtos = events.stream().map(event -> modelMapper.map(event, EventFullGetDTO.class)).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    public ResponseEntity<List<EventFullGetDTO>> getEventsInYearRange(LocalDate date){

        LocalDate start = date.minusMonths(6);
        LocalDate end = date.plusMonths(6);
        List<Event> events = getEventsInDates(start.atStartOfDay(), end.atTime(LocalTime.MAX));
        List<EventFullGetDTO> dtos = events.stream().map(event -> modelMapper.map(event, EventFullGetDTO.class)).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    public ResponseEntity<List<EventFullGetDTO>> getWeekEventsFull(LocalDate localDate){
        List<Event> events = getWeekEvents(localDate);
        List<EventFullGetDTO> eventFullGetDTOS = events.stream().map(event -> modelMapper.map(event, EventFullGetDTO.class)).toList();
        return new ResponseEntity<>(eventFullGetDTOS, HttpStatus.OK);
    }
    public ResponseEntity<List<EventBriefGetDTO>> getWeekEventsBrief (LocalDate localDate){
        List<Event> events = getWeekEvents(localDate);
        List<EventBriefGetDTO> eventBriefGetDTOS = events.stream().map(event -> modelMapper.map(event, EventBriefGetDTO.class)).toList();
        return new ResponseEntity<>(eventBriefGetDTOS, HttpStatus.OK);
    }

    public List<Event> getWeekEvents(LocalDate date){
        LocalDateTime startWeek = LocalDateTime.of(date.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endWeek = LocalDateTime.of(date.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        return getEventsInDates(startWeek, endWeek);
    }


    public ResponseEntity<List<EventBriefGetDTO>> getTodayEvents() {
        return getEventsByDayBrief(LocalDate.now());
    }

    public ResponseEntity<List<EventBriefGetDTO>> getThisWeekEvents(){
        return getWeekEventsBrief(LocalDate.now());
    }


    public ResponseEntity<List<EventBriefGetDTO>> getAllEvents() {
        List<Event> events = eventRepo.findAllByUserId(entityAccessHelper.getLoggedUserId());
        List<EventBriefGetDTO> eventBriefGetDTOS = new ArrayList<>();
        events.forEach(event -> eventBriefGetDTOS.add(modelMapper.map(event, EventBriefGetDTO.class)));
        return new ResponseEntity<>(eventBriefGetDTOS, HttpStatus.OK);
    }

    public ResponseEntity<EventFullGetDTO> getEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No such event"));
        if (!entityAccessHelper.hasUserAccessTo(event)) {
            throw new AccessDeniedException("Access denied");
        }
        EventFullGetDTO returnEvent;
        returnEvent = modelMapper.map(event, EventFullGetDTO.class);
        return new ResponseEntity<>(returnEvent, HttpStatus.OK);
    }

    private List<Event> getEventsInDates(LocalDateTime start, LocalDateTime end){
        List<Event> eventsOfTheDate = eventRepo.findByUserIdAndStartTimeBeforeAndEndTimeAfter(entityAccessHelper.getLoggedUserId(), start, end);
        List<Event> fullDayEvents = eventRepo.findByUserIdAndStartTimeOrEndTimeBetween(entityAccessHelper.getLoggedUserId(), start, end);
        eventsOfTheDate.addAll(fullDayEvents);
        return eventsOfTheDate;
    }

    private List<Event> getEventsByDay(LocalDate localDate){
        LocalDateTime startDay = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(localDate, LocalTime.MAX);
        return getEventsInDates(startDay, endDay);
    }

    private List<Event> getEventsInMonth(LocalDate localDate){
        LocalDateTime startMonth = LocalDateTime.of(localDate.withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime endMonth = LocalDateTime.of(localDate.withDayOfMonth(localDate.lengthOfMonth()), LocalTime.MAX);
        LocalDateTime fistMonday = startMonth.with(DayOfWeek.MONDAY);
        LocalDateTime lastSunday = endMonth.with(DayOfWeek.SUNDAY);
        Period period = Period.between(fistMonday.toLocalDate(), lastSunday.toLocalDate());
        if (period.getDays() <= 41)
            lastSunday = lastSunday.plusWeeks(1);
        return getEventsInDates(fistMonday, lastSunday);
    }
}
