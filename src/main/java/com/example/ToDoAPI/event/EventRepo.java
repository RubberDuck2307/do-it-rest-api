package com.example.ToDoAPI.event;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    List<Event> findAllByUserId(Long userId);
    List<Event> findByUserIdAndStartTimeBetweenOrEndTimeBetween(Long id, LocalDateTime startTime, LocalDateTime startTime2, LocalDateTime endTime, LocalDateTime endTime2);
    List<Event> findByUserIdAndStartTimeBeforeAndEndTimeAfter(Long id, LocalDateTime startTime, LocalDateTime endTime);

    List<Event> findAllByUserIdAndListNotNull(Long userId);
    @Query("SELECT e FROM Event e " +
            "WHERE e.userId = :id " +
            "AND (e.startTime BETWEEN :startTime AND :endTime OR e.endTime BETWEEN :startTime AND :endTime)")
    List<Event> findByUserIdAndStartTimeOrEndTimeBetween(Long id, LocalDateTime startTime, LocalDateTime endTime);
}
