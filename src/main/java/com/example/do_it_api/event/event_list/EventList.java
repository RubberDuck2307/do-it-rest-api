package com.example.do_it_api.event.event_list;

import com.example.do_it_api.event.Event;
import com.example.do_it_api.security.PrivateEntity;
import com.example.do_it_api.user.DefaultUserDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(schema = "todo")
public class EventList implements PrivateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private DefaultUserDetails user;
    @OneToMany(mappedBy = "list", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Event> events;

    public void addEvent(Event event){
        events.add(event);
    }
    @Override
    public Long getUserId() {
        return user.getId();
    }
}
