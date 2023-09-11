package com.example.ToDoAPI.event.DTOs;

import com.example.ToDoAPI.task.Task;
import com.example.ToDoAPI.task.TaskDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class EventFullGetDTO extends EventBriefGetDTO {

    private Set<TaskDTO> relatedTasks;
}
