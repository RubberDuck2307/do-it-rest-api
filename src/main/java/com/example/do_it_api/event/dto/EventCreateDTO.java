package com.example.do_it_api.event.dto;

import com.example.do_it_api.config.IdDeserializer;
import com.example.do_it_api.task.TaskDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EventCreateDTO {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @Size(max = 255, message = "{validation.name.size.too_long}")
    private String name;
    @Size(max = 500, message = "{validation.name.size.too_long}")
    private String description = "";
    private String location = "";
    private String color = "yellow";
    private Set<TaskDTO> relatedTasks = new HashSet<>();
    @JsonDeserialize(using = IdDeserializer.class)
    private Long listId;
}
