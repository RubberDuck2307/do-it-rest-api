package com.example.do_it_api.event;

import com.example.do_it_api.event.dto.EventBriefGetDTO;
import com.example.do_it_api.event.dto.EventCreateDTO;
import com.example.do_it_api.event.dto.EventFullGetDTO;
import com.example.do_it_api.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "${app.url}", allowCredentials = "true")
@RequestMapping("api/v1/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @GetMapping("/")
    public ResponseEntity<List<EventBriefGetDTO>> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullGetDTO> getEvent(@PathVariable Long id){
        return eventService.getEvent(id);
    }


    @GetMapping("/today")
    public ResponseEntity<List<EventBriefGetDTO>> getTodayEvents(){
        return eventService.getTodayEvents();
    }


    @PostMapping("/")
    public ResponseEntity<EventBriefGetDTO> saveEvent(@Valid @RequestBody EventCreateDTO eventDTO){
        return eventService.saveEvent(eventDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        return eventService.deleteEvent(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventBriefGetDTO> updateEvent(@PathVariable Long id, @RequestBody EventCreateDTO eventDTO){
        return eventService.updateEvent(id, eventDTO);
    }

    @GetMapping("/week")
    public ResponseEntity<List<EventBriefGetDTO>> getThisWeekEvents(){
        return eventService.getThisWeekEvents();
    }

    @GetMapping("/week/{day}/{month}/{year}")
    public ResponseEntity<List<EventFullGetDTO>> getWeekEvents(@PathVariable int day, @PathVariable int month, @PathVariable int year){
        LocalDate date = LocalDate.of(year, month, day);
        return eventService.getWeekEventsFull(date);
    }

    @GetMapping("/{day}/{month}/{year}")
    public ResponseEntity<List<EventFullGetDTO>> getDayEvents(@PathVariable int day, @PathVariable int month, @PathVariable int year){
        LocalDate date = LocalDate.of(year, month, day);
        return eventService.getEventsByDayFull(date);
    }
    @GetMapping("/month/{month}/{year}")
    public ResponseEntity<List<EventFullGetDTO>> getMonthEvents(@PathVariable int month, @PathVariable int year){
        LocalDate date = LocalDate.of(year, month, 1);
        return eventService.getEventsInMonthFull(date);
    }

    @GetMapping("/year/range/{day}/{month}/{year}")
    public ResponseEntity<List<EventFullGetDTO>> getEventsInYearRange(@PathVariable int day, @PathVariable int month, @PathVariable int year){
          LocalDate date = LocalDate.of(year, month, day);
          return eventService.getEventsInYearRange(date);
    }
}
