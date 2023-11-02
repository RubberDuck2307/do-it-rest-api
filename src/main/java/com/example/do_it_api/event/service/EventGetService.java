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

    public <T> ResponseEntity<List<T>> getEventsInYearRange(LocalDate date, Class<T> T){
        LocalDate start = date.minusMonths(6);
        LocalDate end = date.plusMonths(6);
        List<Event> events = getEventsInDates(start.atStartOfDay(), end.atTime(LocalTime.MAX));
        List<T> dtos = map(T, events);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    public <T> ResponseEntity<List<T>>getEventsByWeek(LocalDate date, Class<T> T){
        LocalDateTime startWeek = LocalDateTime.of(date.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endWeek = LocalDateTime.of(date.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        List<Event> events = getEventsInDates(startWeek, endWeek);
        List<T> dtos = map(T, events);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }


    public <T> ResponseEntity<List<T>> getAllEvents(Class<T> T) {
        List<Event> events = eventRepo.findAllByUser_Id(entityAccessHelper.getLoggedUserId());
        List<T> dtos = map(T, events);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    public <T> ResponseEntity<T> getEvent(Long id, Class<T> T) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No such event"));
        if (!entityAccessHelper.hasUserAccessTo(event)) {
            throw new AccessDeniedException("Access denied");
        }
        T returnEvent = modelMapper.map(event, T);
        return new ResponseEntity<>(returnEvent, HttpStatus.FOUND);
    }

    public <T> ResponseEntity<List<T>> getEventsByDay(LocalDate localDate, Class<T> T){
        LocalDateTime startDay = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(localDate, LocalTime.MAX);
        List<Event> events = getEventsInDates(startDay, endDay);
        List<T> dtos = map(T, events);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    public <T> ResponseEntity<List<T>> getEventsByMonth(LocalDate localDate, Class<T> T){
        LocalDateTime startMonth = LocalDateTime.of(localDate.withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime endMonth = LocalDateTime.of(localDate.withDayOfMonth(localDate.lengthOfMonth()), LocalTime.MAX);
        LocalDateTime fistMonday = startMonth.with(DayOfWeek.MONDAY);
        LocalDateTime lastSunday = endMonth.with(DayOfWeek.SUNDAY);
        Period period = Period.between(fistMonday.toLocalDate(), lastSunday.toLocalDate());
        if (period.getDays() <= 41)
            lastSunday = lastSunday.plusWeeks(1);
        List<Event> events = getEventsInDates(fistMonday, lastSunday);
        List<T> dtos = map(T, events);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    private <T> List<T> map(Class<T> T, List<Event> events) {
        return events.stream().map(event -> modelMapper.map(event, T)).toList();
    }

    private List<Event> getEventsInDates(LocalDateTime start, LocalDateTime end){
        return eventRepo.findByUserIdAndStartTimeAndEndTime(entityAccessHelper
                .getLoggedUserId(), start, end);
    }

}
