package com.mongoexample.repository.collection;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class DeleteCollectionPort {

    private final MongoTemplate mongoTemplate;

    public void deleteCollection(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }
}
