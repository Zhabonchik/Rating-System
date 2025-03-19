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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameObjectServiceImpl implements GameObjectService {

    private static final Logger logger = LoggerFactory.getLogger(GameObjectServiceImpl.class);
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
        logger.info("Fetching game objects of user with ID: {}", sellerId);
        List<GameObject> gameObjects = gameObjectRepository.findAllByUserId(sellerId);

        logger.info("Mapping game objects to Dto and returning them");
        return GetGameObjectDtoMapper.toDto(gameObjects);
    }

    @Override
    public GetGameObjectDto getObjectByIdAndSellerId(Integer objectId, Integer sellerId) {

        logger.info("Fetching game object with ID: {}", objectId);
        GameObject gameObject = gameObjectRepository.findById(objectId)
                .orElseThrow(() -> new GameObjectNotFoundException("Game object with id = " + objectId + " not found"));

        if (!gameObject.getUser().getId().equals(sellerId)) {
            logger.warn("Object with ID = {} doesn't belong to seller with ID = {}", objectId, sellerId);
            throw new WrongSellerOfObjectException("Object with id = " + objectId
                    + " doesn't belong to seller with id = " + sellerId);
        }

        logger.info("Mapping game object to Dto and returning it");
        return GetGameObjectDtoMapper.toDto(gameObject);

    }

    @Override
    public GetGameObjectDto createObject(CreateGameObjectDto createGameObjectDto, Integer sellerId, String jwtToken) {

        String sellerEmail = null;

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            logger.info("Extracting token");
            jwtToken = jwtToken.substring(7);
            logger.info("Extracting email from token");
            sellerEmail = jwtService.extractUsername(jwtToken);
        }

        logger.info("Fetching user with ID: {}", sellerId);
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id = " + sellerId + " not found"));

        if (!seller.getEmail().equals(sellerEmail)) {
            logger.warn("Seller email and user email, who wants to add a game object, don't coincide");
            throw new WrongSellerOfObjectException("You cannot create an item for seller with id = " + sellerId);
        }

        logger.info("Mapping game object to Dto");
        GameObject gameObject = CreateGameObjectDtoMapper.toGameObject(createGameObjectDto, seller);

        logger.info("Saving game object and returning it's Dto");
        return GetGameObjectDtoMapper.toDto(gameObjectRepository.save(gameObject));
    }

    @Override
    public GetGameObjectDto updateObject(UpdateGameObjectDto updateGameObjectDto, Integer objectId) {

        logger.info("Fetching game object with ID: {} to update it", objectId);
        GameObject gameObject = gameObjectRepository.findById(objectId)
                .orElseThrow(() -> new GameObjectNotFoundException("Game object with id = " + objectId + " not found"));

        logger.info("Updating game object with ID: {}", gameObject.getId());
        GameObject updatedGameObject = UpdateGameObjectDtoMapper.toGameObject(gameObject, updateGameObjectDto);

        logger.info("Saving updated game object and returning it's Dto");
        return GetGameObjectDtoMapper.toDto(gameObjectRepository.save(updatedGameObject));
    }

    @Override
    public void deleteObject(Integer objectId) {
        logger.info("Deleting game object with ID: {}", objectId);
        gameObjectRepository.deleteById(objectId);
    }
}
