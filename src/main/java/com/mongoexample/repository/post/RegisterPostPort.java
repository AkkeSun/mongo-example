package com.mongoexample.repository.post;

import com.mongoexample.document.PostDocument;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class RegisterPostPort {

    private final MongoTemplate mongoTemplate;

    public List<PostDocument> saveAll(String collectionName, List<PostDocument> documents) {
        return (ArrayList<PostDocument>) mongoTemplate.insert(documents, collectionName);
    }

    public PostDocument save(String collectionName, PostDocument postDocument) {
        return mongoTemplate.insert(postDocument, collectionName);
    }
}
