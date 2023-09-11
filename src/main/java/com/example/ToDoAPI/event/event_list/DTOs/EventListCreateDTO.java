package com.example.ToDoAPI.event.event_list.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventListCreateDTO {
    @NotNull
    private String name;
    @NotNull
    private String color;
}
