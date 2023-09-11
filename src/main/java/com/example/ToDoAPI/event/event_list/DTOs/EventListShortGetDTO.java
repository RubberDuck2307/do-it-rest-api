package com.example.ToDoAPI.event.event_list.DTOs;

import lombok.Data;

@Data
public class EventListShortGetDTO {
    private Long id;
    private String name;
    private String color;
    private Integer amount;
}
