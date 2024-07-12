package com.mongoexample.repository.user;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.mongoexample.document.UserInfoDocument;
import com.mongoexample.dto.UserAndPosts;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class FindUserPort {

    private final MongoTemplate mongoTemplate;

    public List<UserInfoDocument> findAll(String collectionName) {
        return mongoTemplate.findAll(UserInfoDocument.class, collectionName);
    }

    public List<UserInfoDocument> findAll(String collectionName, Pageable pageable) {
        Query query = new Query().with(pageable);
        return mongoTemplate.find(query, UserInfoDocument.class, collectionName);
    }

    public List<UserInfoDocument> findAllSortByCondition(String collectionName,
        String conditionColumn) {
        Query query = new Query().with(Sort.by(Direction.DESC, conditionColumn));
        return mongoTemplate.findAll(UserInfoDocument.class, collectionName);
    }

    public List<UserInfoDocument> findAllByName(String collectionName, String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.find(query, UserInfoDocument.class, collectionName);
    }

    public UserInfoDocument findByName(String collectionName, String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, UserInfoDocument.class, collectionName);
    }

    public UserInfoDocument findByNameAndAge(String collectionName, String name, int age) {
        Query query = new Query(Criteria.where("name").is(name)
            .and("age").is(age));
        return mongoTemplate.findOne(query, UserInfoDocument.class, collectionName);
    }

    public UserAndPosts findUserInfoAndPost(String postCollectionName, String userCollectionName,
        String name) {
        Aggregation aggregation = newAggregation(
            match(Criteria.where("name").is(name)),
            lookup(postCollectionName, "_id", "writerId", "posts")
        );
        AggregationResults<UserAndPosts> result = mongoTemplate.aggregate(
            aggregation, userCollectionName, UserAndPosts.class);
        return result.getUniqueMappedResult();
    }
}
