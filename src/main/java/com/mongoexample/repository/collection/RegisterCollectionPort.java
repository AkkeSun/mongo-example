package com.mongoexample.repository.collection;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class RegisterCollectionPort {

    private final MongoTemplate mongoTemplate;

    public void createIndex(String collectionName, Index index) {
        /*
        Index idx = new Index()
            .on("name", Direction.DESC)
            .on("age", Direction.ASC);
        */
        mongoTemplate.indexOps(collectionName).ensureIndex(index);
    }
 
    public void createCollection(String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }
}
