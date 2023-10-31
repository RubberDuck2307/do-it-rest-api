package com.example.do_it_api.task;

import com.example.do_it_api.event.Event;

import com.example.do_it_api.security.PrivateEntity;

import com.example.do_it_api.user.DefaultUserDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Table(schema = "todo")
@Entity
@Getter
@Setter
public class Task implements PrivateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column(nullable = false)
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DefaultUserDetails user;
    private boolean isImportant;
    private boolean isFinished;

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
