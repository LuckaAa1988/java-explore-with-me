package ru.practicum.request.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.entity.Event;
import ru.practicum.request.util.Status;
import ru.practicum.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "created")
    LocalDateTime created;
    @OneToOne
    @JoinColumn(name = "event_id")
    Event event;
    @OneToOne
    @JoinColumn(name = "requester")
    User requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;
}
