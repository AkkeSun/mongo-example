package com.mongoexample.document;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
public class UserInfoDocument {
 
    @Id // 값을 부여하지 않으면 mongodb 가 직접 값을 부여합니다.
    private ObjectId id;

    private String name;

    private int age;

    @Builder
    public UserInfoDocument(ObjectId id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
