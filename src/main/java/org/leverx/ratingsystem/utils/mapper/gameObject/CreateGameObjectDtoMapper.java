package org.leverx.ratingsystem.utils.mapper.gameObject;

import org.leverx.ratingsystem.model.dto.gameObject.CreateGameObjectDto;
import org.leverx.ratingsystem.model.entity.GameObject;
import org.leverx.ratingsystem.model.entity.User;

public class CreateGameObjectDtoMapper {

    public static GameObject toGameObject(CreateGameObjectDto createGameObjectDto, User seller) {
        return GameObject.builder()
                .game(createGameObjectDto.game())
                .title(createGameObjectDto.title())
                .text(createGameObjectDto.text())
                .user(seller)
                .build();
    }
}
