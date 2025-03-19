package org.leverx.ratingsystem.service;

import org.leverx.ratingsystem.model.dto.gameObject.CreateGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.GetGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.UpdateGameObjectDto;

import java.util.List;

public interface GameObjectService {

    List<GetGameObjectDto> getObjectsBySellerId(Integer sellerId);

    GetGameObjectDto getObjectByIdAndSellerId(Integer objectId, Integer sellerId);

    GetGameObjectDto createObject(CreateGameObjectDto createGameObjectDto, Integer sellerId, String token);

    GetGameObjectDto updateObject(UpdateGameObjectDto updateGameObjectDto, Integer objectId);

    void deleteObject(Integer objectId);

}
