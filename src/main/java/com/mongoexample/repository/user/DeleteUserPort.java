package com.mongoexample.repository.user;

import com.mongodb.client.result.DeleteResult;
import com.mongoexample.document.UserInfoDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class DeleteUserPort {

    private final MongoTemplate mongoTemplate;

    public DeleteResult delete(String collectionName, String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.remove(query, UserInfoDocument.class, collectionName);
    }
}
