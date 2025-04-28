package edu3431.matiukhin.softwarequality6.repository;/*
@author sasha
@project SoftwareQuality5
@class ItemRepository
@version 1.0.0
@since 14.04.2025 - 15 - 28
*/




import edu3431.matiukhin.softwarequality6.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {
}