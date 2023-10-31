package com.example.do_it_api.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    List<Event> findAllByUser_Id(Long userId);
    List<Event> findByUser_IdAndStartTimeBeforeAndEndTimeAfter(Long id, LocalDateTime startTime, LocalDateTime endTime);

    List<Event> findAllByUser_IdAndListNotNull(Long userId);
    @Query("SELECT e FROM Event e " +
            "WHERE e.user.id = :id " +
            "AND (e.startTime BETWEEN :startTime AND :endTime OR e.endTime BETWEEN :startTime AND :endTime)")
    List<Event> findByUserIdAndStartTimeOrEndTimeBetween(Long id, LocalDateTime startTime, LocalDateTime endTime);
}
