package org.leverx.ratingsystem.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.leverx.ratingsystem.model.entity.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, Integer> {

    @Query("SELECT o FROM GameObject o WHERE o.user.id = :sellerId")
    List<GameObject> findAllByUserId(@Param("sellerId") Integer sellerId);
}
