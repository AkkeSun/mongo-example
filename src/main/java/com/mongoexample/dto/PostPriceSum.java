package com.mongoexample.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPriceSum {

    private int priceSum;
    private int cnt;
    private String category;
    private List<String> titleList;

    @Builder
    public PostPriceSum(int priceSum, int cnt, String category, List<String> titleList) {
        this.priceSum = priceSum;
        this.cnt = cnt;
        this.category = category;
        this.titleList = titleList;
    }
}
