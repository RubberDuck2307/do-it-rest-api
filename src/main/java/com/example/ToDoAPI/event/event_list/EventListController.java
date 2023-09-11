package com.example.ToDoAPI.event.event_list;

import com.example.ToDoAPI.event.event_list.DTOs.EventListCreateDTO;
import com.example.ToDoAPI.event.event_list.DTOs.EventListDeleteDTO;
import com.example.ToDoAPI.event.event_list.DTOs.EventListShortGetDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "${app.url}", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@RequestMapping("api/v1/eventList")
@RequiredArgsConstructor
public class EventListController {

    private final EventListService eventListService;

    @GetMapping("/")
    public ResponseEntity<List<EventListShortGetDTO>> getAllLists() {
        return eventListService.getAllEventsList();
    }

    @PostMapping("/")
    public ResponseEntity<EventListShortGetDTO> saveList(@Valid @RequestBody EventListCreateDTO dto) {
        return eventListService.saveList(dto);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteLists(@RequestBody EventListDeleteDTO dto) {
        eventListService.deleteLists(dto);
        return ResponseEntity.ok().build();
    }

}
