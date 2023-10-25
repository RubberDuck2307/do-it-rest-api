package com.example.do_it_api.event.event_list;

import com.example.do_it_api.user.DefaultUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventListRepo extends JpaRepository<EventList, Long> {


    List<EventList> findAllByUser(DefaultUserDetails user);
    List<EventList> findAllByIdIn(List<Long> ids);
}
