package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.entity.Hit;
import ru.practicum.response.HitResponse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {


    @Query("SELECT new ru.practicum.response.HitResponse(h.app, h.uri, COUNT(h.uri)) FROM Hit h " +
            "WHERE(h.timestamp BETWEEN :start AND :end) AND(h.uri IN :uris) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.uri) DESC")
    List<HitResponse> findALlWithUris(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.response.HitResponse(h.app, h.uri, COUNT(h.uri)) FROM Hit h " +
            "WHERE(h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.uri) DESC")
    List<HitResponse> findAllWithOutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.response.HitResponse(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM Hit h " +
            "WHERE(h.timestamp BETWEEN :start AND :end) AND(h.uri IN :uris) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.uri) DESC")
    List<HitResponse> findALlWithUrisUnique(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.response.HitResponse(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM Hit h " +
            "WHERE(h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.uri) DESC")
    List<HitResponse> findAllWithOutUrisUnique(LocalDateTime start, LocalDateTime end);

}
