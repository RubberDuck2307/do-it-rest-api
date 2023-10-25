package com.example.do_it_api.event.event_list.dto;

import lombok.Data;

@Data
public class EventListShortGetDTO {
    private Long id;
    private String name;
    private String color;
    private Integer amount;
}
