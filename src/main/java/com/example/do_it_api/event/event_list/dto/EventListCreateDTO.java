package com.example.do_it_api.event.event_list.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventListCreateDTO {
    @NotNull
    private String name;
    @NotNull
    private String color;
}
