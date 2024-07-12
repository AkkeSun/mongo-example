package com.mongoexample.repository.user;

import com.mongoexample.document.UserInfoDocument;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class RegisterUserPort {

    private final MongoTemplate mongoTemplate;

    public UserInfoDocument save(String collectionName, UserInfoDocument document) {
        return mongoTemplate.insert(document, collectionName);
    }

    public List<UserInfoDocument> saveAll(String collectionName, List<UserInfoDocument> documents) {
        return (ArrayList<UserInfoDocument>) mongoTemplate.insert(documents, collectionName);
    }
}
