package com.homeproject.smarthome;

import com.homeproject.smarthome.testModel.Content;
import com.homeproject.smarthome.testModel.Channel;
import com.homeproject.smarthome.testModel.Program;
import com.homeproject.smarthome.testModel.dto.ChannelDto;
import com.homeproject.smarthome.testModel.dto.ContentDto;
import com.homeproject.smarthome.testModel.dto.ProgramDto;
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
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/contents";
    }


    @Test
    public void addNewValidContent_noContent_shouldReturnOkHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ResponseEntity<ContentDto> postResponse = testRestTemplate.postForEntity(baseUrl, testContent, ContentDto.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

    @Test
    public void addNewValidContentWithInvalidTitle_noContent_shouldReturnBadRequestHttpStatus() {
        final Content content = new Content(null, "", "description", null);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, content, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewValidContentWithInvalidDescription_noContent_shouldReturnBadRequestHttpStatus() {
        final Content content = new Content(null, "title", "", null);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, content, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewValidContent_noContent_shouldReturnSameContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        assertTrue(equalsWithoutId(new ContentDto(testContent), result));
    }

    @Test
    public void addNewValidContentWithoutId_noContent_shouldReturnContentWithNotNullId() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        assertNotEquals(null, result.getId());
    }

    @Test
    public void addNewValidContentWithNonExistentId_noContent_shouldReturnContentWithGeneratedId() {
        final Content testContent = new Content(100L, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        assertEquals(1L, result.getId());
    }

    @Test
    public void addNewValidContentWithExistentId_oneContent_shouldReturnContentWithDifferentGeneratedId() {
        final Content testContent1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, testContent1, ContentDto.class);

        final Content testContent2 = new Content(result1.getId(), "Test content 2", "Another movie about contents in test.", null);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, testContent2, ContentDto.class);

        assertFalse(equalsById(result1, result2));
    }

    @Test
    public void addTwoNewValidContent_noContent_shouldReturnContentsWithDifferentId() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);

        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        assertFalse(equalsById(result1, result2));
    }

    @Test
    public void addTwoNewValidContent_noContent_shouldSaveTwoContents() {
        final Content testContent1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final Content testContent2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);

        testRestTemplate.postForObject(baseUrl, testContent1, ContentDto.class);
        testRestTemplate.postForObject(baseUrl, testContent2, ContentDto.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));
        assertEquals(2, contents.size());
    }

    @Test
    public void addTwoNewValidContent_noContent_shouldSaveTwoContentsWithGoodData() {
        final Content testContent1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final Content testContent2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);

        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, testContent1, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, testContent2, ContentDto.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result1));
        assertTrue(contain(contents, result2));
    }

    @Test
    public void addNewValidContentWithInvalidTitle_noContent_shouldNotSaveContent() {
        final Content content = new Content(null, "", "description", null);
        testRestTemplate.postForEntity(baseUrl, content, Object.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(0, contents.size());
    }

    @Test
    public void addNewValidContentWithInvalidDescription_noContent_shouldNotSaveContent() {
        final Content content = new Content(null, "Test content", "", null);
        testRestTemplate.postForEntity(baseUrl, content, Object.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(0, contents.size());
    }


    @Test
    public void updateValidContent_oneContent_shouldReturnOkHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("New test content");
        testContent.setDescription("A new movie about contents in test.");

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<ContentDto> putResponse = testRestTemplate.exchange(baseUrl + "/" + result.getId(), HttpMethod.PUT, httpEntity, ContentDto.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
    }

    @Test
    public void updateValidContentWithInvalidTitle_oneContent_shouldReturnBadRequestHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("");
        testContent.setDescription("A new movie about contents in test.");

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<Object> putResponse = testRestTemplate.exchange(baseUrl + "/" + result.getId(), HttpMethod.PUT, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse.getStatusCode());
    }

    @Test
    public void updateValidContentWithInvalidDescription_oneContent_shouldReturnBadRequestHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("New test content");
        testContent.setDescription("");

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<Object> putResponse = testRestTemplate.exchange(baseUrl + "/" + result.getId(), HttpMethod.PUT, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse.getStatusCode());
    }

    @Test
    public void updateValidContentWithNonExistentId_oneContent_shouldReturnNotFoundHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("New test content");
        testContent.setDescription("A new movie about contents in test.");

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<Object> putResponse = testRestTemplate.exchange(baseUrl + "/100", HttpMethod.PUT, httpEntity, Object.class);

        assertEquals(HttpStatus.NOT_FOUND, putResponse.getStatusCode());
    }

    @Test
    public void updateValidContentWithExistentId_oneContent_shouldReturnSameContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("New test content");
        testContent.setDescription("A new movie about contents in test.");
        result.setTitle("New test content");
        result.setDescription("A new movie about contents in test.");

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<ContentDto> putResponse = testRestTemplate.exchange(baseUrl + "/" + result.getId(), HttpMethod.PUT, httpEntity, ContentDto.class);

        assertTrue(equalsWithId(result, putResponse.getBody()));
    }

    @Test
    public void updateValidContentWithExistentId_oneContent_shouldUpdateContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("New test content");
        testContent.setDescription("A new movie about contents in test.");
        result.setTitle("New test content");
        result.setDescription("A new movie about contents in test.");

        testRestTemplate.put(baseUrl + "/" + result.getId(), testContent);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithExistentId_twoContent_shouldUpdateContent() {
        final Content testContent1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final Content testContent2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);

        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, testContent1, ContentDto.class);
        testRestTemplate.postForObject(baseUrl, testContent2, ContentDto.class);

        testContent1.setTitle("New test content");
        testContent1.setDescription("A new movie about contents in test.");
        result1.setTitle("New test content");
        result1.setDescription("A new movie about contents in test.");

        testRestTemplate.put(baseUrl + "/" + result1.getId(), testContent1);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result1));
    }

    @Test
    public void updateValidContentWithExistentId_twoContent_otherContentShouldNotChange() {
        final Content testContent1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final Content testContent2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);

        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, testContent1, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, testContent2, ContentDto.class);

        testContent1.setTitle("New test content");
        testContent1.setDescription("A new movie about contents in test.");
        result1.setTitle("New test content");
        result1.setDescription("A new movie about contents in test.");

        testRestTemplate.put(baseUrl + "/" + result1.getId(), testContent1);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result2));
    }

    @Test
    public void updateValidContentWithInvalidTitle_oneContent_shouldNotUpdateContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("");
        testContent.setDescription("A new movie about contents in test.");

        testRestTemplate.put(baseUrl + "/" + result.getId(), testContent);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithInvalidDescription_oneContent_shouldNotUpdateContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("New test content");
        testContent.setDescription("");

        testRestTemplate.put(baseUrl + "/" + result.getId(), testContent);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }

    @Test
    public void updateValidContentWithNonExistentId_noContent_shouldNotAddContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);

        testRestTemplate.put(baseUrl + "/100", testContent);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(0, contents.size());
    }

    @Test
    public void updateValidContentWithNonExistentId_oneContent_shouldNotUpdateContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testContent.setTitle("New test content");
        testContent.setDescription("A new movie about contents in test.");

        testRestTemplate.put(baseUrl + "/100", testContent);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertTrue(contain(contents, result));
    }


    @Test
    public void getContentWithExistentId_oneContent_shouldReturnOkHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        final ResponseEntity<ContentDto> getResponse = testRestTemplate.getForEntity(baseUrl + "/" + result.getId(), ContentDto.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    public void getContentWithNonExistentId_noContent_shouldReturnNotFoundHttpStatus() {
        final ResponseEntity<Object> getResponse = testRestTemplate.getForEntity(baseUrl + "/100", Object.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void getContentWithExistentId_oneContent_shouldReturnSameContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        final ContentDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ContentDto.class);

        assertTrue(equalsWithId(result, getResult));
    }

    @Test
    public void getContentWithExistentId_threeContent_shouldReturnGoodContent() {
        final Content testContent1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final Content testContent2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);
        final Content testContent3 = new Content(null, "Test content 3", "And another movie about contents in test.", null);

        testRestTemplate.postForObject(baseUrl, testContent1, ContentDto.class);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent2, ContentDto.class);
        testRestTemplate.postForObject(baseUrl, testContent3, ContentDto.class);

        final ContentDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ContentDto.class);

        assertTrue(equalsWithId(result, getResult));
    }


    @Test
    public void getAllContent_noContent_shouldReturnOkHttpStatus() {
        final ResponseEntity<ContentDto[]> getResponse = testRestTemplate.getForEntity(baseUrl, ContentDto[].class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    public void getAllContent_noContent_shouldReturnEmptyList() {
        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(0, contents.size());
    }

    @Test
    public void getAllContent_oneContent_shouldReturnListWithContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(1, contents.size());
        assertTrue(contain(contents, result));
    }

    @Test
    public void getAllContent_twoContent_shouldReturnListWithContents() {
        final Content content1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final Content content2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);

        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, content1, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, content2, ContentDto.class);

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(2, contents.size());
        assertTrue(contain(contents, result1));
        assertTrue(contain(contents, result2));
    }


    @Test
    public void deleteContentWithExistentId_oneContent_shouldReturnOkHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<ContentDto> deleteResponse = testRestTemplate.exchange(baseUrl + "/" + result.getId(), HttpMethod.DELETE, httpEntity, ContentDto.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }

    @Test
    public void deleteContentWithNonExistentId_oneContent_shouldReturnNotFoundHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<Object> deleteResponse = testRestTemplate.exchange(baseUrl + "/100", HttpMethod.DELETE, httpEntity, Object.class);

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
    }

    @Test
    public void deleteContentWithExistentId_oneContentWithDependency_shouldReturnConflictHttpStatus() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto contentResult = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);
        testContent.setId(contentResult.getId());

        final Channel testChannel = new Channel(null, "Test channel", false, null);
        final ChannelDto channelResult = testRestTemplate.postForObject("http://localhost:" + port + "/api/channels", testChannel, ChannelDto.class);
        testChannel.setId(channelResult.getId());

        final Program testProgram = new Program(
                null,
                LocalDateTime.of(2021, 8, 11, 4, 0, 0),
                LocalDateTime.of(2021, 8, 11, 4, 50, 0),
                testContent,
                testChannel
        );
        testRestTemplate.postForObject("http://localhost:" + port + "/api/programs", testProgram, ProgramDto.class);

        final HttpEntity<Content> httpEntity = createHttpEntityWithMediaTypeJson(testContent);
        final ResponseEntity<Object> deleteResponse = testRestTemplate.exchange(baseUrl + "/" + contentResult.getId(), HttpMethod.DELETE, httpEntity, Object.class);

        assertEquals(HttpStatus.CONFLICT, deleteResponse.getStatusCode());
    }

    @Test
    public void deleteContent_oneContent_shouldNotRemainContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testRestTemplate.delete(baseUrl + "/" + result.getId());

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(0, contents.size());
    }

    @Test
    public void deleteInnerContent_threeContent_shouldRemainGoodContents() {
        final Content testContent1 = new Content(null, "Test content", "A movie about contents in test.", null);
        final Content testContent2 = new Content(null, "Test content 2", "Another movie about contents in test.", null);
        final Content testContent3 = new Content(null, "Test content 3", "And another movie about contents in test.", null);

        final ContentDto result1 = testRestTemplate.postForObject(baseUrl, testContent1, ContentDto.class);
        final ContentDto result2 = testRestTemplate.postForObject(baseUrl, testContent2, ContentDto.class);
        final ContentDto result3 = testRestTemplate.postForObject(baseUrl, testContent3, ContentDto.class);

        testRestTemplate.delete(baseUrl + "/" + result2.getId());

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(2, contents.size());
        assertTrue(contain(contents, result1));
        assertTrue(contain(contents, result3));
    }

    @Test
    public void deleteContentWithNonExistentId_oneContent_shouldNotDeleteContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto result = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);

        testRestTemplate.delete(baseUrl + "/100");

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(1, contents.size());
        assertTrue(contain(contents, result));
    }

    @Test
    public void deleteContentWithExistentId_oneContentWithDependency_shouldNotDeleteContent() {
        final Content testContent = new Content(null, "Test content", "A movie about contents in test.", null);
        final ContentDto contentResult = testRestTemplate.postForObject(baseUrl, testContent, ContentDto.class);
        testContent.setId(contentResult.getId());

        final Channel testChannel = new Channel(null, "Test channel", false, null);
        final ChannelDto channelResult = testRestTemplate.postForObject("http://localhost:" + port + "/api/channels", testChannel, ChannelDto.class);
        testChannel.setId(channelResult.getId());

        final Program testProgram = new Program(
                null,
                LocalDateTime.of(2021, 8, 11, 4, 0, 0),
                LocalDateTime.of(2021, 8, 11, 4, 50, 0),
                testContent,
                testChannel
        );
        testRestTemplate.postForObject("http://localhost:" + port + "/api/programs", testProgram, ProgramDto.class);

        testRestTemplate.delete(baseUrl + "/" + contentResult.getId());

        final List<ContentDto> contents = List.of(testRestTemplate.getForObject(baseUrl, ContentDto[].class));

        assertEquals(1, contents.size());
        assertTrue(contain(contents, contentResult));
    }


    private HttpEntity<Content> createHttpEntityWithMediaTypeJson(Content content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(content, headers);
    }

    private boolean equalsById(ContentDto contentDto, Object object) {
        if (contentDto == object) return true;
        if (contentDto == null || object == null || contentDto.getClass() != object.getClass()) return false;

        ContentDto otherContentDto = (ContentDto) object;

        if (!Objects.equals(contentDto.getId(), otherContentDto.getId())) return false;

        return true;
    }

    private boolean equalsWithId(ContentDto contentDto, Object object) {
        if (contentDto == object) return true;
        if (contentDto == null || object == null || contentDto.getClass() != object.getClass()) return false;

        ContentDto otherContentDto = (ContentDto) object;

        if (!Objects.equals(contentDto.getId(), otherContentDto.getId())) return false;
        if (!Objects.equals(contentDto.getTitle(), otherContentDto.getTitle())) return false;
        if (!Objects.equals(contentDto.getDescription(), otherContentDto.getDescription())) return false;

        return true;
    }

    private boolean equalsWithoutId(ContentDto contentDto, Object object) {
        if (contentDto == object) return true;
        if (contentDto == null || object == null || contentDto.getClass() != object.getClass()) return false;

        ContentDto otherContentDto = (ContentDto) object;

        if (!Objects.equals(contentDto.getTitle(), otherContentDto.getTitle())) return false;
        if (!Objects.equals(contentDto.getDescription(), otherContentDto.getDescription())) return false;

        return true;
    }

    private boolean contain(List<ContentDto> contents, ContentDto contentDto) {
        for (ContentDto otherContentDto : contents) {
            if (equalsWithId(contentDto, otherContentDto)) return true;
        }
        return false;
    }
}
