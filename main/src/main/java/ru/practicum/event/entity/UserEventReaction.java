package ru.practicum.event.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_event_reactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEventReaction {
    @EmbeddedId
    private UserEventReactionId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    Event event;
    Integer reaction = 0;
}
