package com.example.do_it_api.event;

import com.example.do_it_api.event.event_list.EventList;
import com.example.do_it_api.security.PrivateEntity;
import com.example.do_it_api.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(schema = "todo")
@Getter
@Setter
public class Event implements PrivateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(500)")
    private String description;
    @Column(nullable = false)
    private String color;
    private String location;
    @Column(nullable = false)
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "list")
    private EventList list;
    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JsonBackReference
    private Set<Task> relatedTasks;

    public void set(Event event){
        this.setStartTime(event.getStartTime());
        this.setEndTime(event.getEndTime());
        this.setName(event.getName());
        this.setDescription(event.getDescription());
        this.setColor(event.getColor());
        this.setLocation(event.getLocation());
    }

    static public boolean validateEvent(Event event){
        if (event.getStartTime() == null || event.getEndTime() == null || event.getName() == null || event.getColor() == null){
            return false;
        }
        return !event.getStartTime().isAfter(event.getEndTime());
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
