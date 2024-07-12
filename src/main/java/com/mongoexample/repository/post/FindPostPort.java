package com.mongoexample.repository.post;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.mongoexample.dto.PostAndWriter;
import com.mongoexample.dto.PostPriceSum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class FindPostPort {

    private final MongoTemplate mongoTemplate;

    public PostAndWriter findPostAndWriter(String postCollectionName, String userCollectionName,
        String title) {
        Aggregation aggregation = newAggregation(
            match(Criteria.where("title").is(title)),
            lookup(userCollectionName, "writerId", "_id", "writer"),
            unwind("writer")
        );
        AggregationResults<PostAndWriter> result = mongoTemplate.aggregate(
            aggregation, postCollectionName, PostAndWriter.class);
        return result.getUniqueMappedResult();
    }


    public PostPriceSum findPostPriceSum(String postCollectionName, String category) {
        Aggregation aggregation = newAggregation(
            match(Criteria.where("category").is(category)),
            group()
                .count().as("cnt")
                .sum("price").as("priceSum")
                .push("title").as("titleList")
                .first("category").as("category"),
            project()
                .andExclude("_id")
                .andInclude("cnt", "priceSum", "titleList", "category")
        );

        AggregationResults<PostPriceSum> results =
            mongoTemplate.aggregate(aggregation, postCollectionName, PostPriceSum.class);

        return results.getUniqueMappedResult();
    }
}
