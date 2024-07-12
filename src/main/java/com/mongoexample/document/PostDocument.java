package com.mongoexample.document;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
public class PostDocument {

    @Id
    private ObjectId id;

    private ObjectId writerId; // FK

    private String category;

    private String title;

    private String contents;

    private long price;


    @Builder
    public PostDocument(ObjectId id, String category, String title, String contents,
        long price, ObjectId writerId) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.price = price;
        this.writerId = writerId;
    }
}
