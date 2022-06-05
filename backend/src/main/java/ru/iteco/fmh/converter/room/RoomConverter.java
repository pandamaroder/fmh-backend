package ru.iteco.fmh.converter.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.iteco.fmh.dao.repository.BlockRepository;
import ru.iteco.fmh.dao.repository.NurseStationRepository;
import ru.iteco.fmh.dto.room.RoomDto;
import ru.iteco.fmh.dto.room.RoomDtoRq;
import ru.iteco.fmh.dto.room.RoomDtoRs;
import ru.iteco.fmh.model.Room;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class RoomConverter {

    private final BlockRepository blockRepository;
    private final NurseStationRepository nurseStationRepository;

    public RoomDto roomEntityToRoomDto(@NotNull Room entity) {
        return RoomDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .blockId(entity.getBlock().getId())
                .nurseStationId(entity.getNurseStation().getId())
                .maxOccupancy(entity.getMaxOccupancy())
                .comment(entity.getComment())
                .build();
    }

    public RoomDtoRs roomEntityToRoomDtoRs(@NotNull Room entity) {
        return RoomDtoRs.builder()
                .id(entity.getId())
                .name(entity.getName())
                .blockId(entity.getBlock().getId())
                .nurseStationId(entity.getNurseStation().getId())
                .maxOccupancy(entity.getMaxOccupancy())
                .comment(entity.getComment())
                .build();
    }

    public Room roomDtoRqToRoomEntity(RoomDtoRq roomDto) {
        return Room.builder()
                .id(roomDto.getId())
                .name(roomDto.getName())
                .block(blockRepository
                        .findById(roomDto.getBlockId())
                        .orElseThrow(() -> new IllegalArgumentException("Блок с таким ID не существует")))
                .nurseStation(nurseStationRepository
                        .findById(roomDto.getNurseStationId())
                        .orElseThrow(() -> new IllegalArgumentException("Пост с таким ID не существует")))
                .maxOccupancy(roomDto.getMaxOccupancy())
                .comment(roomDto.getComment())
                .build();
    }

}
