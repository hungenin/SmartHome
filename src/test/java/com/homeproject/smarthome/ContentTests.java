package com.homeproject.smarthome;

import com.homeproject.smarthome.tvGuide.model.Content;
import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.model.Program;
import com.homeproject.smarthome.tvGuide.model.dto.ChannelDto;
import com.homeproject.smarthome.tvGuide.model.dto.ContentDto;
import com.homeproject.smarthome.tvGuide.model.dto.ProgramDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ContentTests {
    @LocalServerPort
    private int port;
    private String baseUrl;
    private static final Long NON_EXISTENT_ID = 100L;
    private static final Content TEST_CONTENT = new Content(null, "Test content", "A movie about contents in test.", null);
    private static final Content TEST_CONTENT_WITHOUT_TITLE = new Content(null, null, "A movie about contents in test.", null);
    private static final Content TEST_CONTENT_WITH_INVALID_TITLE = new Content(null, "", "A movie about contents in test.", null);
    private static final Content TEST_CONTENT_WITHOUT_DESCRIPTION = new Content(null, "Test content", null, null);
    private static final Content TEST_CONTENT_WITH_INVALID_DESCRIPTION = new Content(null, "Test content", "", null);
    private static final Content TEST_CONTENT_WITH_NON_EXISTENT_ID = new Content(NON_EXISTENT_ID, "Test content", "A movie about contents in test.", null);
    private static final Content TEST_CONTENT_2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);
    private static final Content TEST_CONTENT_3 = new Content(null, "Test content 3", "And another movie about contents in test.", null);
    private static final Channel TEST_CHANNEL = new Channel(null, "Test channel", false, null);
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/contents";
    }


    @Test
    public void addNewValidContent_noContent_shouldReturnOkHttpStatus() {
        assertEquals(HttpStatus.OK, postResponse(TEST_CONTENT, ContentDto.class).getStatusCode());
    }

    @Test
    public void addNewValidChannelWithoutTitle_noChannel_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_CONTENT_WITHOUT_TITLE, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidContentWithInvalidTitle_noContent_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_CONTENT_WITH_INVALID_TITLE, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidChannelWithoutDescription_noChannel_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_CONTENT_WITHOUT_DESCRIPTION, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidContentWithInvalidDescription_noContent_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_CONTENT_WITH_INVALID_DESCRIPTION, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidContent_noContent_shouldReturnSameContent() {
        assertTrue(equalsWithoutId(new ContentDto(TEST_CONTENT), testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class)));
    }

    @Test
    public void addNewValidContentWithoutId_noContent_shouldReturnContentWithNotNullGeneratedId() {
        assertNotEquals(null, testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class).getId());
    }

    @Test
    public void addNewValidContentWithExistentId_oneContent_shouldReturnContentWithDifferentGeneratedId() {
        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        TEST_CONTENT_2.setId(result1.getId());
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);
        TEST_CONTENT_2.setId(null);

        assertFalse(equalsById(result1, result2));
    }

    @Test
    public void addTwoNewValidContent_noContent_shouldReturnContentsWithDifferentId() {
        assertFalse(equalsById(
                testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class),
                testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class)
        ));
    }

    @Test
    public void addTwoNewValidContent_noContent_shouldSaveTwoContents() {
        testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);

        assertEquals(2, List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class)).size());
    }

    @Test
    public void addTwoNewValidContent_noContent_shouldSaveTwoContentsWithGoodData() {
        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertAll(
                () -> assertTrue(contain(contents, result1)),
                () -> assertTrue(contain(contents, result2))
        );
    }

    @Test
    public void addNewValidContentWithoutTitle_noContent_shouldNotSaveContent() {
        testRestTemplate.postForEntity(baseUrl, TEST_CONTENT_WITHOUT_TITLE, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class)).size());
    }

    @Test
    public void addNewValidContentWithInvalidTitle_noContent_shouldNotSaveContent() {
        testRestTemplate.postForEntity(baseUrl, TEST_CONTENT_WITH_INVALID_TITLE, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class)).size());
    }

    @Test
    public void addNewValidContentWithoutDescription_noContent_shouldNotSaveContent() {
        testRestTemplate.postForEntity(baseUrl, TEST_CONTENT_WITHOUT_DESCRIPTION, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class)).size());
    }

    @Test
    public void addNewValidContentWithInvalidDescription_noContent_shouldNotSaveContent() {
        testRestTemplate.postForEntity(baseUrl, TEST_CONTENT_WITH_INVALID_DESCRIPTION, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class)).size());
    }


    @Test
    public void updateValidContent_oneContent_shouldReturnOkHttpStatus() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.OK, putResponse(result.getId(), TEST_CONTENT_2, ContentDto.class).getStatusCode());
    }

    @Test
    public void updateValidContentWithoutTitle_oneContent_shouldReturnBadRequestHttpStatus() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_CONTENT_WITHOUT_TITLE, Object.class).getStatusCode());
    }

    @Test
    public void updateValidContentWithInvalidTitle_oneContent_shouldReturnBadRequestHttpStatus() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_CONTENT_WITH_INVALID_TITLE, Object.class).getStatusCode());
    }

    @Test
    public void updateValidContentWithoutDescription_oneContent_shouldReturnBadRequestHttpStatus() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_CONTENT_WITHOUT_DESCRIPTION, Object.class).getStatusCode());
    }

    @Test
    public void updateValidContentWithInvalidDescription_oneContent_shouldReturnBadRequestHttpStatus() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_CONTENT_WITH_INVALID_DESCRIPTION, Object.class).getStatusCode());
    }

    @Test
    public void updateValidContentWithNonExistentId_oneContent_shouldReturnNotFoundHttpStatus() {
        testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.NOT_FOUND, putResponse(NON_EXISTENT_ID, TEST_CONTENT_2, Object.class).getStatusCode());
    }

    @Test
    public void updateValidContentWithExistentId_oneContent_shouldReturnSameContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        copyContentToDto(TEST_CONTENT_2, result);

        assertTrue(equalsWithId(result, putResponse(result.getId(), TEST_CONTENT_2, ContentDto.class).getBody()));
    }

    @Test
    public void updateValidContentWithExistentId_oneContent_shouldUpdateContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        copyContentToDto(TEST_CONTENT_2, result);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CONTENT_2);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithExistentId_twoContent_shouldUpdateContent() {
        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);

        copyContentToDto(TEST_CONTENT_3, result1);

        testRestTemplate.put(baseUrl + "/" + result1.getId(), TEST_CONTENT_3);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result1));
    }

    @Test
    public void updateValidContentWithExistentId_twoContent_otherContentShouldNotChange() {
        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);

        copyContentToDto(TEST_CONTENT_3, result1);

        testRestTemplate.put(baseUrl + "/" + result1.getId(), TEST_CONTENT_3);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result2));
    }

    @Test
    public void updateValidContentWithoutTitle_oneContent_shouldNotUpdateContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CONTENT_WITHOUT_TITLE);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithInvalidTitle_oneContent_shouldNotUpdateContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CONTENT_WITH_INVALID_TITLE);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithoutDescription_oneContent_shouldNotUpdateContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CONTENT_WITHOUT_DESCRIPTION);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithInvalidDescription_oneContent_shouldNotUpdateContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CONTENT_WITH_INVALID_DESCRIPTION);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithNonExistentId_noContent_shouldNotAddContent() {
        testRestTemplate.put(baseUrl + "/" + NON_EXISTENT_ID, TEST_CONTENT);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(0, contents.size());
    }

    @Test
    public void updateValidContentWithNonExistentId_oneContent_shouldNotUpdateContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        testRestTemplate.put(baseUrl + "/" + NON_EXISTENT_ID, TEST_CONTENT_2);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }


    @Test
    public void getContentWithExistentId_oneContent_shouldReturnOkHttpStatus() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.OK, getResponse(result.getId(), ContentDto.class).getStatusCode());
    }

    @Test
    public void getContentWithNonExistentId_noContent_shouldReturnNotFoundHttpStatus() {
        assertEquals(HttpStatus.NOT_FOUND, getResponse(NON_EXISTENT_ID, Object.class).getStatusCode());
    }

    @Test
    public void getContentWithExistentId_oneContent_shouldReturnSameContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        final ContentDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ContentDto.class);

        assertTrue(equalsWithId(result, getResult));
    }

    @Test
    public void getContentWithExistentId_threeContent_shouldReturnGoodContent() {
        testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_CONTENT_3, ContentDto.class);

        final ContentDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ContentDto.class);

        assertTrue(equalsWithId(result, getResult));
    }


    @Test
    public void getAllContent_noContent_shouldReturnOkHttpStatus() {
        assertEquals(HttpStatus.OK, getResponse().getStatusCode());
    }

    @Test
    public void getAllContent_noContent_shouldReturnEmptyList() {
        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class)).size());
    }

    @Test
    public void getAllContent_oneContent_shouldReturnListWithContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertAll(
                () -> assertEquals(1, contents.size()),
                () -> assertTrue(contain(contents, result))
        );
    }

    @Test
    public void getAllContent_twoContent_shouldReturnListWithContents() {
        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertAll(
                () -> assertEquals(2, contents.size()),
                () -> assertTrue(contain(contents, result1)),
                () -> assertTrue(contain(contents, result2))
        );
    }


    @Test
    public void deleteContentWithExistentId_oneContent_shouldReturnOkHttpStatus() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.OK, deleteResponse(result.getId()).getStatusCode());
    }

    @Test
    public void deleteContentWithNonExistentId_oneContent_shouldReturnNotFoundHttpStatus() {
        testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse(NON_EXISTENT_ID).getStatusCode());
    }

    @Test
    public void deleteContentWithExistentId_oneContentWithDependency_shouldReturnConflictHttpStatus() {
        assertEquals(HttpStatus.CONFLICT, deleteResponse(generateAndPostProgramWithDependenciesAndGetContent().getId()).getStatusCode());
    }

    @Test
    public void deleteContent_oneContent_shouldNotRemainContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        testRestTemplate.delete(baseUrl + "/" + result.getId());

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class)).size());
    }

    @Test
    public void deleteInnerContent_threeContent_shouldRemainGoodContents() {
        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT_2, ContentDto.class);
        final ContentDto result3 = testRestTemplate.postForObject(baseUrl, TEST_CONTENT_3, ContentDto.class);

        testRestTemplate.delete(baseUrl + "/" + result2.getId());

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertAll(
                () -> assertEquals(2, contents.size()),
                () -> assertTrue(contain(contents, result1)),
                () -> assertTrue(contain(contents, result3))
        );
    }

    @Test
    public void deleteContentWithNonExistentId_oneContent_shouldNotDeleteContent() {
        final ContentDto result = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);

        testRestTemplate.delete(baseUrl + "/" + NON_EXISTENT_ID);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertAll(
                () -> assertEquals(1, contents.size()),
                () -> assertTrue(contain(contents, result))
        );
    }

    @Test
    public void deleteContentWithExistentId_oneContentWithDependency_shouldNotDeleteContent() {
        ContentDto contentResult = generateAndPostProgramWithDependenciesAndGetContent();

        testRestTemplate.delete(baseUrl + "/" + contentResult.getId());

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertAll(
                () -> assertEquals(1, contents.size()),
                () -> assertTrue(contain(contents, contentResult))
        );
    }


    private <T> ResponseEntity<T> postResponse(Content content, Class<T> classType) {
        return testRestTemplate.postForEntity(baseUrl, content, classType);
    }

    private <T> ResponseEntity<T> putResponse(Long id, Content content, Class<T> classType) {
        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(content);
        return testRestTemplate.exchange(baseUrl + "/" + id, HttpMethod.PUT, httpEntity, classType);
    }

    private <T> ResponseEntity<T> getResponse(Long id, Class<T> classType) {
        return testRestTemplate.getForEntity(baseUrl + "/" + id, classType);
    }

    private ResponseEntity<Object> getResponse() {
        return testRestTemplate.getForEntity(baseUrl, Object.class);
    }

    private ResponseEntity<Object> deleteResponse(Long id) {
        return testRestTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, null, Object.class);
    }

    private HttpEntity<Content> createHttpEntityWithMediaTypeJson(Content content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(content, headers);
    }

    private ContentDto generateAndPostProgramWithDependenciesAndGetContent() {
        final ContentDto contentResult = testRestTemplate.postForObject(baseUrl, TEST_CONTENT, ContentDto.class);
        TEST_CONTENT.setId(contentResult.getId());

        final ChannelDto channelResult = testRestTemplate.postForObject("http://localhost:" + port + "/api/channels", TEST_CHANNEL, ChannelDto.class);
        TEST_CHANNEL.setId(channelResult.getId());

        final Program testProgram = new Program(
                null,
                LocalDateTime.of(2021, 8, 11, 4, 0, 0),
                LocalDateTime.of(2021, 8, 11, 4, 50, 0),
                TEST_CONTENT,
                TEST_CHANNEL
        );
        testRestTemplate.postForObject("http://localhost:" + port + "/api/programs", testProgram, ProgramDto.class);

        TEST_CONTENT.setId(null);
        TEST_CHANNEL.setId(null);

        return contentResult;
    }

    private void copyContentToDto(Content content, ContentDto result) {
        result.setTitle(content.getTitle());
        result.setDescription(content.getDescription());
    }

    private boolean equalsById(ContentDto contentDto, Object object) {
        if (contentDto == object) return true;
        if (contentDto == null || object == null || contentDto.getClass() != object.getClass()) return false;

        ContentDto otherContentDto = (ContentDto) object;

        return Objects.equals(contentDto.getId(), otherContentDto.getId());
    }

    private boolean equalsWithId(ContentDto contentDto, Object object) {
        if (contentDto == object) return true;
        if (contentDto == null || object == null || contentDto.getClass() != object.getClass()) return false;

        ContentDto otherContentDto = (ContentDto) object;

        if (!Objects.equals(contentDto.getId(), otherContentDto.getId())) return false;
        if (!Objects.equals(contentDto.getTitle(), otherContentDto.getTitle())) return false;
        return Objects.equals(contentDto.getDescription(), otherContentDto.getDescription());
    }

    private boolean equalsWithoutId(ContentDto contentDto, Object object) {
        if (contentDto == object) return true;
        if (contentDto == null || object == null || contentDto.getClass() != object.getClass()) return false;

        ContentDto otherContentDto = (ContentDto) object;

        if (!Objects.equals(contentDto.getTitle(), otherContentDto.getTitle())) return false;
        return Objects.equals(contentDto.getDescription(), otherContentDto.getDescription());
    }

    private boolean contain(List<ContentDto> contents, ContentDto contentDto) {
        for (ContentDto otherContentDto : contents) {
            if (equalsWithId(contentDto, otherContentDto)) return true;
        }
        return false;
    }
}
