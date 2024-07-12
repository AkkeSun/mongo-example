package com.mongoexample.dto;

import com.mongoexample.document.PostDocument;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAndPosts {

    private String id;

    private String name;

    private int age;

    private List<PostDocument> posts;

    @Builder
    public UserAndPosts(String id, String name, int age, List<PostDocument> posts) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.posts = posts;
    }
}
