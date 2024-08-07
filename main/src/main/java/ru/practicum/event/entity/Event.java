package ru.practicum.event.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.entity.Category;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.event.util.State;
import ru.practicum.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "annotation")
    String annotation;
    @Column(name = "title")
    String title;
    @Column(name = "description")
    String description;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "published_on", insertable = false)
    LocalDateTime publishedOn;
    @Column(name = "paid")
    Boolean paid;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "participants")
    Integer participants;
    @OneToOne
    @JoinColumn(name = "location_id")
    Location location;
    @OneToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    State state;
    @ManyToMany(mappedBy = "event")
    List<Compilation> compilations;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserEventReaction> reactions = new HashSet<>();
}
