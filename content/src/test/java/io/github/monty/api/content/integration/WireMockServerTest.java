package io.github.monty.api.content.integration;

import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.entity.Tag;
import io.github.monty.api.content.domain.repository.AlbumRepository;
import io.github.monty.api.content.domain.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * IntegrationTest 를 위한 공통 WireMockServer 세팅 클래스
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWireMock(stubs = "classpath:/stubs/**/*.json")
@ActiveProfiles("test")
public class WireMockServerTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TagRepository tagRepository;

    /**
     * DB에 AlbumData List 세팅
     *
     * @param albumList 앨범 리스트
     * @return 세팅 결과
     */
    protected List<Album> insertAlbumList(List<Album> albumList) {
        return albumRepository.saveAll(albumList);
    }

    /**
     * DB에 TagData 세팅
     *
     * @param tagList 세팅하고자 하는 TagList
     * @return 세팅 결과
     */
    protected List<Tag> insertTagList(List<Tag> tagList) {
        return tagRepository.saveAll(tagList);
    }
}
