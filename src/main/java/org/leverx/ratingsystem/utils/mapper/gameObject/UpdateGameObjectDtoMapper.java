package org.leverx.ratingsystem.utils.mapper.gameObject;

import org.leverx.ratingsystem.model.dto.gameObject.UpdateGameObjectDto;
import org.leverx.ratingsystem.model.entity.GameObject;

public class UpdateGameObjectDtoMapper {

    public static GameObject toGameObject(GameObject gameObject, UpdateGameObjectDto updateGameObjectDto) {

        gameObject.setTitle(updateGameObjectDto.title());
        gameObject.setText(updateGameObjectDto.text());

        return gameObject;
    }

}
