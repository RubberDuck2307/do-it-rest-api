package com.example.do_it_api.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    @NotNull
    @Size(max = 255, message = "{validation.name.size.too_long}")
    private String name;
    private String description;
    private Long id;
    private Long userId;

    @JsonProperty(value = "isImportant")
    private boolean isImportant;

    @JsonProperty(value = "isFinished")
    private boolean isFinished;
    @NotNull(message = "Date may not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Long ListId;

    @Override
    public String toString() {
        return "TaskDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", userId=" + userId +
                ", isImportant=" + isImportant +
                ", isFinished=" + isFinished +
                ", date=" + date +
                ", ListId=" + ListId +
                '}';
    }
}
