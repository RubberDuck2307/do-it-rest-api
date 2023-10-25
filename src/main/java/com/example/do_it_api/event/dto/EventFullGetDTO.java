package com.example.do_it_api.event.dto;

import com.example.do_it_api.task.TaskDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class EventFullGetDTO extends EventBriefGetDTO {

    private Set<TaskDTO> relatedTasks;
}
