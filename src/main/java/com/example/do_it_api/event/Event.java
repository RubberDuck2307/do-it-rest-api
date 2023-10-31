package com.example.do_it_api.event;

import com.example.do_it_api.event.event_list.EventList;
import com.example.do_it_api.security.PrivateEntity;
import com.example.do_it_api.task.Task;
import com.example.do_it_api.user.DefaultUserDetails;
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
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(500)")
    private String description;
    @Column(nullable = false)
    private String color;
    private String location;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DefaultUserDetails user;
    @ManyToOne
    @JoinColumn(name = "list")
    private EventList list;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
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
    public Long getUserId() {
        return user.getId();
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
