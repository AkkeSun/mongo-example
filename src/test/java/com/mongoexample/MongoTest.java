package com.mongoexample;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongoexample.document.PostDocument;
import com.mongoexample.document.UserInfoDocument;
import com.mongoexample.dto.PostAndWriter;
import com.mongoexample.dto.PostPriceSum;
import com.mongoexample.dto.UserAndPosts;
import com.mongoexample.repository.collection.DeleteCollectionPort;
import com.mongoexample.repository.post.FindPostPort;
import com.mongoexample.repository.post.RegisterPostPort;
import com.mongoexample.repository.user.FindUserPort;
import com.mongoexample.repository.user.RegisterUserPort;
import com.mongoexample.repository.user.UpdateUserPort;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MongoTest {

    @Autowired
    RegisterUserPort registerUserPort;

    @Autowired
    FindUserPort findUserPort;

    @Autowired
    UpdateUserPort updateUserPort;

    @Autowired
    DeleteCollectionPort deleteCollectionPort;

    @Autowired
    RegisterPostPort registerPostPort;

    @Autowired
    FindPostPort findPostPort;

    String userCollectionName;

    String postCollectionName;

    @BeforeEach
    void init() {
        userCollectionName =
            "user_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        postCollectionName =
            "post_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        deleteCollectionPort.deleteCollection(userCollectionName);
        deleteCollectionPort.deleteCollection(postCollectionName);
    }

    @Nested
    @DisplayName("데이터 저장 및 조회 테스트")
    class save_and_find_test {

        @Test
        @DisplayName("하나의 데이터가 정상적으로 저장되고 조회되는지 확인한다.")
        void oneDataSaveTest() {
            // given
            UserInfoDocument document = UserInfoDocument.builder()
                .name("od")
                .age(34)
                .build();
 
            // when
            registerUserPort.save(userCollectionName, document);
            UserInfoDocument savedData = findUserPort
                .findByName(userCollectionName, document.getName());

            // then
            assertThat(savedData.getName()).isEqualTo(document.getName());
            assertThat(savedData.getAge()).isEqualTo(document.getAge());
        }

        @Test
        @DisplayName("복수의 데이터가 정상적으로 저장되고 조회되는지 확인한다.")
        void multiDataSaveTest() {
            // given
            UserInfoDocument document1 = UserInfoDocument.builder()
                .name("od")
                .age(34)
                .build();
            UserInfoDocument document2 = UserInfoDocument.builder()
                .name("exg")
                .age(31)
                .build();

            // when
            registerUserPort.saveAll(userCollectionName, Arrays.asList(document1, document2));
            List<UserInfoDocument> savedDataList = findUserPort.findAll(userCollectionName);

            // then
            assertThat(savedDataList.size()).isEqualTo(2);
            for (UserInfoDocument document : savedDataList) {
                if (document.getName().equals(document1.getName())) {
                    assertThat(document.getAge()).isEqualTo(document1.getAge());
                } else {
                    assertThat(document.getName()).isEqualTo(document2.getName());
                    assertThat(document.getAge()).isEqualTo(document2.getAge());
                }
            }
        }

        @Test
        @DisplayName("부모 객채가 자식 객차의 데이터를 함께 조회하는지 확인한다.")
        void referenceDataSaveTest() {
            // given
            UserInfoDocument userInfoDocument = UserInfoDocument.builder()
                .name("od")
                .age(34)
                .build();
            UserInfoDocument savedUser = registerUserPort.save(userCollectionName,
                userInfoDocument);
            PostDocument postDocument1 = PostDocument.builder()
                .category("1")
                .title("테스트문서 제목 1")
                .contents("테스트문서 내용 1")
                .price(10000)
                .writerId(savedUser.getId())
                .build();
            PostDocument postDocument2 = PostDocument.builder()
                .category("2")
                .title("테스트문서 제목 2")
                .contents("테스트문서 내용 2")
                .price(20000)
                .writerId(savedUser.getId())
                .build();
            registerPostPort.saveAll(postCollectionName,
                Arrays.asList(postDocument1, postDocument2));

            // when
            UserAndPosts userInfoAndPost = findUserPort.findUserInfoAndPost(
                postCollectionName, userCollectionName, savedUser.getName());

            // then
            assertThat(userInfoAndPost.getName()).isEqualTo(savedUser.getName());
            assertThat(userInfoAndPost.getAge()).isEqualTo(savedUser.getAge());
            assertThat(userInfoAndPost.getPosts().size()).isEqualTo(2);
            for (PostDocument post : userInfoAndPost.getPosts()) {
                if (post.getTitle().equals(postDocument1.getTitle())) {
                    assertThat(post.getCategory()).isEqualTo(postDocument1.getCategory());
                    assertThat(post.getContents()).isEqualTo(postDocument1.getContents());
                    assertThat(post.getPrice()).isEqualTo(postDocument1.getPrice());
                    assertThat(post.getWriterId()).isEqualTo(postDocument1.getWriterId());
                } else {
                    assertThat(post.getTitle()).isEqualTo(postDocument2.getTitle());
                    assertThat(post.getCategory()).isEqualTo(postDocument2.getCategory());
                    assertThat(post.getContents()).isEqualTo(postDocument2.getContents());
                    assertThat(post.getPrice()).isEqualTo(postDocument2.getPrice());
                    assertThat(post.getWriterId()).isEqualTo(postDocument2.getWriterId());
                }
            }
        }

        @Test
        @DisplayName("자식 객채가 부모 객차의 데이터를 함께 조회하는지 확인한다.")
        void referenceDataSaveTest2() {
            // given
            UserInfoDocument userInfoDocument = UserInfoDocument.builder()
                .name("od")
                .age(34)
                .build();
            UserInfoDocument savedUser = registerUserPort.save(userCollectionName,
                userInfoDocument);
            PostDocument postDocument = PostDocument.builder()
                .category("1")
                .title("테스트문서 제목 1")
                .contents("테스트문서 내용 1")
                .price(10000)
                .writerId(savedUser.getId())
                .build();
            registerPostPort.save(postCollectionName, postDocument);

            // when
            PostAndWriter result = findPostPort.findPostAndWriter(
                postCollectionName, userCollectionName, postDocument.getTitle());
            UserInfoDocument writer = result.getWriter();

            // then
            assertThat(result.getCategory()).isEqualTo(postDocument.getCategory());
            assertThat(result.getTitle()).isEqualTo(postDocument.getTitle());
            assertThat(result.getContents()).isEqualTo(postDocument.getContents());
            assertThat(result.getPrice()).isEqualTo(postDocument.getPrice());
            assertThat(result.getCategory()).isEqualTo(postDocument.getCategory());
            assertThat(result.getCategory()).isEqualTo(postDocument.getCategory());
            assertThat(writer.getId()).isEqualTo(savedUser.getId());
            assertThat(writer.getName()).isEqualTo(savedUser.getName());
            assertThat(writer.getAge()).isEqualTo(savedUser.getAge());
        }


        @Test
        @DisplayName("포스트 통계가 정상적으로 응답하는지 확인한다.")
        void postAggregationResultsTest() {
            // given
            PostDocument postDocument1 = PostDocument.builder()
                .category("1")
                .title("테스트문서 제목 1")
                .contents("테스트문서 내용 1")
                .price(10000)
                .build();
            PostDocument postDocument2 = PostDocument.builder()
                .category("1")
                .title("테스트문서 제목 2")
                .contents("테스트문서 내용 2")
                .price(15000)
                .build();
            registerPostPort.saveAll(postCollectionName,
                Arrays.asList(postDocument1, postDocument2));

            // when
            PostPriceSum result =
                findPostPort.findPostPriceSum(postCollectionName, "1");

            // then
            assertThat(result.getCategory()).isEqualTo(postDocument1.getCategory());
            assertThat(result.getCnt()).isEqualTo(2);
            assertThat(result.getPriceSum()).isEqualTo(25000);
            assertThat(result.getTitleList().size()).isEqualTo(2);
            assertThat(result.getTitleList().contains(postDocument1.getTitle())).isTrue();
            assertThat(result.getTitleList().contains(postDocument2.getTitle())).isTrue();

        }
    }
}