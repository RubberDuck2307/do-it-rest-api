package com.example.ToDoAPI.event.event_list;

import com.example.ToDoAPI.event.Event;
import com.example.ToDoAPI.event.EventRepo;
import com.example.ToDoAPI.event.event_list.DTOs.EventListCreateDTO;
import com.example.ToDoAPI.event.event_list.DTOs.EventListDeleteDTO;
import com.example.ToDoAPI.event.event_list.DTOs.EventListShortGetDTO;
import com.example.ToDoAPI.security.EntityAccessHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventListService {
    private final ModelMapper modelMapper;
    private final EventListRepo eventListRepo;
    private final EntityAccessHelper<EventList> entityAccessHelper;
    private final EventRepo eventRepo;

    public ResponseEntity<List<EventListShortGetDTO>> getAllEventsList() {
        List<EventList> eventLists = eventListRepo.findAllByUser(entityAccessHelper.getLoggedUser());
        List<Event> events = eventRepo.findAllByUserIdAndListNotNull(entityAccessHelper.getLoggedUserId());
        List<EventListShortGetDTO> dtos = new ArrayList<>();
        Map<Long, Integer> amountOfEventsInList = getAmountOfEventsInList(events);
        eventLists.forEach(eventList -> {
            EventListShortGetDTO dto = modelMapper.map(eventList, EventListShortGetDTO.class);
            dto.setAmount(amountOfEventsInList.get(eventList.getId()));
            dtos.add(dto);
        });
        return ResponseEntity.ok(dtos);

    }

    @Transactional
    public void addEventToList(Long listId, Event event) {
        EventList eventList = eventListRepo.findById(listId).orElseThrow(NoSuchElementException::new);
        if (!entityAccessHelper.hasUserAccessTo(eventList)) {
            throw new AccessDeniedException("Access denied");
        }
        eventList.addEvent(event);
        event.setList(eventList);
    }

    public ResponseEntity<EventListShortGetDTO> saveList(EventListCreateDTO dto) {
        EventList eventList = modelMapper.map(dto, EventList.class);
        eventList.setUser(entityAccessHelper.getLoggedUser());

        eventList = eventListRepo.save(eventList);

        EventListShortGetDTO returnDTO = modelMapper.map(eventList, EventListShortGetDTO.class);
        returnDTO.setAmount(0);
        return ResponseEntity.ok(returnDTO);

    }

    public void deleteLists(EventListDeleteDTO dto) {
        List<EventList> eventLists = eventListRepo.findAllByIdIn(dto.getIds());
        eventLists.forEach(eventList -> {
            if (!entityAccessHelper.hasUserAccessTo(eventList)) {
                throw new AccessDeniedException("Access denied");
            }
        });
        eventListRepo.deleteAll(eventLists);
    }

    private Map<Long, Integer> getAmountOfEventsInList(List<Event> events) {
        HashMap<Long, Integer> map = new HashMap<>();
        events.forEach(event -> {

                if (map.containsKey(event.getList().getId())) {
                    map.put(event.getList().getId(), map.get(event.getList().getId()) + 1);
                } else {
                    map.put(event.getList().getId(), 1);

            }
        });
        return map;
    }
}
