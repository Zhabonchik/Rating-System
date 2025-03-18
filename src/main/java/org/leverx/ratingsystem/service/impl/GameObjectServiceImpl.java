package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.gameObject.GameObjectNotFoundException;
import org.leverx.ratingsystem.exception.gameObject.WrongSellerOfObjectException;
import org.leverx.ratingsystem.exception.user.UserNotFoundException;
import org.leverx.ratingsystem.model.dto.gameObject.CreateGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.GetGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.UpdateGameObjectDto;
import org.leverx.ratingsystem.model.entity.GameObject;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.repository.GameObjectRepository;
import org.leverx.ratingsystem.repository.UserRepository;
import org.leverx.ratingsystem.service.GameObjectService;
import org.leverx.ratingsystem.utils.mapper.gameObject.CreateGameObjectDtoMapper;
import org.leverx.ratingsystem.utils.mapper.gameObject.GetGameObjectDtoMapper;
import org.leverx.ratingsystem.utils.mapper.gameObject.UpdateGameObjectDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameObjectServiceImpl implements GameObjectService {

    private final GameObjectRepository gameObjectRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public GameObjectServiceImpl(GameObjectRepository gameObjectRepository, UserRepository userRepository,
                                 JwtService jwtService) {
        this.gameObjectRepository = gameObjectRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public List<GetGameObjectDto> getObjectsBySellerId(Integer sellerId) {

        List<GameObject> gameObjects = gameObjectRepository.findAllByUserId(sellerId);

        return GetGameObjectDtoMapper.toDto(gameObjects);
    }

    @Override
    public GetGameObjectDto getObjectByIdAndSellerId(Integer objectId, Integer sellerId) {

        GameObject gameObject = gameObjectRepository.findById(objectId)
                .orElseThrow(() -> new GameObjectNotFoundException("Game object with id = " + objectId + " not found"));

        if (!gameObject.getUser().getId().equals(sellerId)) {
            throw new WrongSellerOfObjectException("Object with id = " + objectId
                    + " doesn't belong to seller with id = " + sellerId);
        }

        return GetGameObjectDtoMapper.toDto(gameObject);

    }

    @Override
    public GetGameObjectDto createObject(CreateGameObjectDto createGameObjectDto, Integer sellerId, String jwtToken) {

        String sellerEmail = null;

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
            sellerEmail = jwtService.extractUsername(jwtToken);
        }

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id = " + sellerId + " not found"));

        if (!seller.getEmail().equals(sellerEmail)) {
            throw new WrongSellerOfObjectException("You cannot create an item for seller with id = " + sellerId);
        }

        GameObject gameObject = CreateGameObjectDtoMapper.toGameObject(createGameObjectDto, seller);

        return GetGameObjectDtoMapper.toDto(gameObjectRepository.save(gameObject));
    }

    @Override
    public GetGameObjectDto updateObject(UpdateGameObjectDto updateGameObjectDto, Integer objectId) {

        GameObject gameObject = gameObjectRepository.findById(objectId)
                .orElseThrow(() -> new GameObjectNotFoundException("Game object with id = " + objectId + " not found"));

        GameObject updatedGameObject = UpdateGameObjectDtoMapper.toGameObject(gameObject, updateGameObjectDto);

        return GetGameObjectDtoMapper.toDto(gameObjectRepository.save(updatedGameObject));
    }

    @Override
    public void deleteObject(Integer objectId) {
        gameObjectRepository.deleteById(objectId);
    }
}
