package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;


public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.isApproved = true")
    Page<EventHistory> findAllMyApprovedHistories(User user, Pageable pageable);

    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.name = :name and e.isApproved = true")
    Page<EventHistory> searchAllByUserAndName(User user, String name, Pageable pageable);

    interface EventHistory {
        Event getEvent();
        LocalDate getCreatedAt();
        static EventRepository.EventHistory invoke(Event event, LocalDate createdAt){
            return new EventHistory() {

                @Override
                public Event getEvent() {
                    return event;
                }

                @Override
                public LocalDate getCreatedAt() {
                    return createdAt;
                }
            };
        }
    }

}
