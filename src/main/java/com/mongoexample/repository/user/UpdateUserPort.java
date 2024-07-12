package com.mongoexample.repository.user;

import com.mongodb.client.result.UpdateResult;
import com.mongoexample.document.UserInfoDocument;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class UpdateUserPort {

    private final MongoTemplate mongoTemplate;

    public UpdateResult updateAge(String name, int newAge) {
        String collectionName = "user-info-" + new Date();
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.updateFirst(query, Update.update("age", newAge),
            UserInfoDocument.class);
    }

    public UpdateResult updateAllAge(String name, int newAge) {
        String collectionName = "user-info-" + new Date();
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.updateMulti(query, Update.update("age", newAge),
            UserInfoDocument.class);
    }
}
