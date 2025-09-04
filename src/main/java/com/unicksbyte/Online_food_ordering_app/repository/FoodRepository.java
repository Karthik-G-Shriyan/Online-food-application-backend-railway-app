package com.unicksbyte.Online_food_ordering_app.repository;

import com.unicksbyte.Online_food_ordering_app.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FoodRepository extends MongoRepository<FoodEntity,String> {
}
