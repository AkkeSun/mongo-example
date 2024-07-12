package com.mongoexample.dto;

import com.mongoexample.document.UserInfoDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
public class PostAndWriter {

    private ObjectId id;
    private ObjectId writerId;
    private String category;
    private String title;
    private long price;
    private String contents;
    private UserInfoDocument writer;

    @Builder
    public PostAndWriter(ObjectId id, ObjectId writerId, String category, String title,
        long price, String contents, UserInfoDocument writer) {
        this.id = id;
        this.writerId = writerId;
        this.category = category;
        this.title = title;
        this.price = price;
        this.contents = contents;
        this.writer = writer;
    }
}
