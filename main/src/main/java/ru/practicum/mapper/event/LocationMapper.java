package ru.practicum.mapper.event;

import org.springframework.stereotype.Component;
import ru.practicum.dto.event.LocationDto;
import ru.practicum.event.entity.Location;

@Component
public class LocationMapper {

    public Location fromDto(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
