package org.leverx.ratingsystem.utils.mapper.gameObject;

import org.leverx.ratingsystem.model.dto.gameObject.GetGameObjectDto;
import org.leverx.ratingsystem.model.entity.GameObject;

import java.util.List;

public class GetGameObjectDtoMapper {

    public static GetGameObjectDto toDto(GameObject gameObject) {
        return new GetGameObjectDto(
                gameObject.getId(),
                gameObject.getGame(),
                gameObject.getTitle(),
                gameObject.getText(),
                gameObject.getCreatedAt(),
                gameObject.getUpdatedAt(),
                gameObject.getUser().getId());
    }

    public static List<GetGameObjectDto> toDto(List<GameObject> gameObjects) {
        return gameObjects.stream()
                .map(GetGameObjectDtoMapper::toDto)
                .toList();
    }

}
