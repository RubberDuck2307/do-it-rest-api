package com.example.ToDoAPI.event.event_list.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class EventListDeleteDTO {

    List<Long> ids;

}
