package com.homeproject.smarthome;

import com.homeproject.smarthome.tvGuide.model.Content;
import com.homeproject.smarthome.tvGuide.model.Program;
import com.homeproject.smarthome.tvGuide.model.dto.ChannelDto;
import com.homeproject.smarthome.tvGuide.model.dto.ContentDto;
import com.homeproject.smarthome.tvGuide.model.dto.ProgramDto;
import com.homeproject.smarthome.tvGuide.model.Channel;
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
public class ChannelTests {
    @LocalServerPort
    private int port;
    private String baseUrl;
    private static final Long NON_EXISTENT_ID = 100L;
    private static final Channel TEST_CHANNEL = new Channel(null, "Test channel", false, null);
    private static final Channel TEST_CHANNEL_WITHOUT_NAME = new Channel(null, null, false, null);
    private static final Channel TEST_CHANNEL_WITH_INVALID_NAME = new Channel(null, "", false, null);
    private static final Channel TEST_CHANNEL_WITHOUT_FOLLOW = new Channel(null, "Test channel", null, null);
    private static final Channel TEST_CHANNEL_WITH_NON_EXISTENT_ID = new Channel(NON_EXISTENT_ID, "Test channel", false, null);
    private static final Channel TEST_CHANNEL_2 = new Channel(null, "Test channel 2", true, null);
    private static final Channel TEST_CHANNEL_3 = new Channel(null, "Test channel 3", false, null);
    private static final Content TEST_CONTENT = new Content(null, "Test content", "A movie about contents in test.", null);
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/channels";
    }

    @Test
    public void addNewValidChannel_noChannel_shouldReturnOkHttpStatus() {
        assertEquals(HttpStatus.OK, postResponse(TEST_CHANNEL, ChannelDto.class).getStatusCode());
    }

    @Test
    public void addNewValidChannelWithoutName_noChannel_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_CHANNEL_WITHOUT_NAME, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidChannelWithInvalidName_noChannel_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_CHANNEL_WITH_INVALID_NAME, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidChannelWithoutFollow_noChannel_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_CHANNEL_WITHOUT_FOLLOW, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidChannel_noChannel_shouldReturnSameChannel() {
        assertTrue(equalsWithoutId(new ChannelDto(TEST_CHANNEL), testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class)));
    }

    @Test
    public void addNewValidChannelWithoutId_noChannel_shouldReturnChannelWithNotNullGeneratedId() {
        assertNotEquals(null, testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class).getId());
    }

    @Test
    public void addNewValidChannelWithNonExistentId_noChannel_shouldReturnChannelWithNotNullGeneratedId() {
        assertNotEquals(null, testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_WITH_NON_EXISTENT_ID, ChannelDto.class).getId());
    }

    @Test
    public void addNewValidChannelWithExistentId_oneChannel_shouldReturnChannelWithDifferentGeneratedId() {
        final ChannelDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        TEST_CHANNEL_2.setId(result1.getId());
        final ChannelDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);
        TEST_CHANNEL_2.setId(null);

        assertFalse(equalsById(result1, result2));
    }

    @Test
    public void addTwoNewValidChannel_noChannel_shouldReturnChannelsWithDifferentId() {
        assertFalse(equalsById(
                testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class),
                testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class)
        ));
    }

    @Test
    public void addTwoNewValidChannel_noChannel_shouldSaveTwoChannels() {
        testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);

        assertEquals(2, List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class)).size());
    }

    @Test
    public void addTwoNewValidChannel_noChannel_shouldSaveTwoChannelsWithGoodData() {
        final ChannelDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
        final ChannelDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertAll(
                () -> assertTrue(contain(channels, result1)),
                () -> assertTrue(contain(channels, result2))
        );
    }

    @Test
    public void addNewValidChannelWithoutName_noChannel_shouldNotSaveChannel() {
        testRestTemplate.postForEntity(baseUrl, TEST_CHANNEL_WITHOUT_NAME, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class)).size());
    }

    @Test
    public void addNewValidChannelWithInvalidName_noChannel_shouldNotSaveChannel() {
        testRestTemplate.postForEntity(baseUrl, TEST_CHANNEL_WITH_INVALID_NAME, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class)).size());
    }

    @Test
    public void addNewValidChannelWithoutFollow_noChannel_shouldNotSaveChannel() {
        testRestTemplate.postForEntity(baseUrl, TEST_CHANNEL_WITHOUT_FOLLOW, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class)).size());
    }


    @Test
    public void updateValidChannel_oneChannel_shouldReturnOkHttpStatus() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.OK, putResponse(result.getId(), TEST_CHANNEL_2, ChannelDto.class).getStatusCode());
    }

    @Test
    public void updateValidChannelWithoutName_oneChannel_shouldReturnBadRequestHttpStatus() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_CHANNEL_WITHOUT_NAME, Object.class).getStatusCode());
    }

    @Test
    public void updateValidChannelWithInvalidName_oneChannel_shouldReturnBadRequestHttpStatus() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_CHANNEL_WITH_INVALID_NAME, Object.class).getStatusCode());
    }

    @Test
    public void updateValidChannelWithoutFollow_oneChannel_shouldReturnBadRequestHttpStatus() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_CHANNEL_WITHOUT_FOLLOW, Object.class).getStatusCode());
    }

    @Test
    public void updateValidChannelWithNonExistentId_oneChannel_shouldReturnNotFoundHttpStatus() {
        testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.NOT_FOUND, putResponse(NON_EXISTENT_ID, TEST_CHANNEL_2, Object.class).getStatusCode());
    }

    @Test
    public void updateValidChannelWithExistentId_oneChannel_shouldReturnSameChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        copyChannelToDto(TEST_CHANNEL_2, result);

        assertTrue(equalsWithId(result, putResponse(result.getId(), TEST_CHANNEL_2, ChannelDto.class).getBody()));
    }

    @Test
    public void updateValidChannelWithExistentId_oneChannel_shouldUpdateChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        copyChannelToDto(TEST_CHANNEL_2, result);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CHANNEL_2);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertTrue(contain(channels, result));
    }

    @Test
    public void updateValidChannelWithExistentId_twoChannel_shouldUpdateChannel() {
        final ChannelDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);

        copyChannelToDto(TEST_CHANNEL_3, result1);

        testRestTemplate.put(baseUrl + "/" + result1.getId(), TEST_CHANNEL_3);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertTrue(contain(channels, result1));
    }

    @Test
    public void updateValidChannelWithExistentId_twoChannel_otherChannelShouldNotChange() {
        final ChannelDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
        final ChannelDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);

        copyChannelToDto(TEST_CHANNEL_3, result1);

        testRestTemplate.put(baseUrl + "/" + result1.getId(), TEST_CHANNEL_3);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertTrue(contain(channels, result2));
    }

    @Test
    public void updateValidChannelWithoutName_oneChannel_shouldNotUpdateChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CHANNEL_WITHOUT_NAME);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertTrue(contain(channels, result));
    }

    @Test
    public void updateValidChannelWithInvalidName_oneChannel_shouldNotUpdateChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CHANNEL_WITH_INVALID_NAME);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertTrue(contain(channels, result));
    }

    @Test
    public void updateValidChannelWithoutFollow_oneChannel_shouldNotUpdateChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_CHANNEL_WITHOUT_FOLLOW);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertTrue(contain(channels, result));
    }

    @Test
    public void updateValidChannelWithNonExistentId_noChannel_shouldNotAddChannel() {
        testRestTemplate.put(baseUrl + "/" + NON_EXISTENT_ID, TEST_CHANNEL);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertEquals(0, channels.size());
    }

    @Test
    public void updateValidChannelWithNonExistentId_oneChannel_shouldNotUpdateChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        testRestTemplate.put(baseUrl + "/" + NON_EXISTENT_ID, TEST_CHANNEL_2);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertTrue(contain(channels, result));
    }


    @Test
    public void getChannelWithExistentId_oneChannel_shouldReturnOkHttpStatus() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.OK, getResponse(result.getId(), ChannelDto.class).getStatusCode());
    }

    @Test
    public void getChannelWithNonExistentId_noChannel_shouldReturnNotFoundHttpStatus() {
        assertEquals(HttpStatus.NOT_FOUND, getResponse(NON_EXISTENT_ID, Object.class).getStatusCode());
    }

    @Test
    public void getChannelWithExistentId_oneChannel_shouldReturnSameChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        final ChannelDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ChannelDto.class);

        assertTrue(equalsWithId(result, getResult));
    }

    @Test
    public void getChannelWithExistentId_threeChannel_shouldReturnGoodChannel() {
        testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_3, ChannelDto.class);

        final ChannelDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ChannelDto.class);

        assertTrue(equalsWithId(result, getResult));
    }


    @Test
    public void getAllChannel_noChannel_shouldReturnOkHttpStatus() {
        assertEquals(HttpStatus.OK, getResponse().getStatusCode());
    }

    @Test
    public void getAllChannel_noChannel_shouldReturnEmptyList() {
        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class)).size());
    }

    @Test
    public void getAllChannel_oneChannel_shouldReturnListWithChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertAll(
                () -> assertEquals(1, channels.size()),
                () -> assertTrue(contain(channels, result))
        );
    }

    @Test
    public void getAllChannel_twoChannel_shouldReturnListWithChannels() {
        final ChannelDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
        final ChannelDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertAll(
                () -> assertEquals(2, channels.size()),
                () -> assertTrue(contain(channels, result1)),
                () -> assertTrue(contain(channels, result2))
        );
    }


    @Test
    public void deleteChannelWithExistentId_oneChannel_shouldReturnOkHttpStatus() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.OK, deleteResponse(result.getId()).getStatusCode());
    }

    @Test
    public void deleteChannelWithNonExistentId_oneChannel_shouldReturnNotFoundHttpStatus() {
        testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse(NON_EXISTENT_ID).getStatusCode());
    }

    @Test
    public void deleteChannelWithExistentId_oneChannelWithDependency_shouldReturnConflictHttpStatus() {
        assertEquals(HttpStatus.CONFLICT, deleteResponse(generateAndPostProgramWithDependenciesAndGetChannel().getId()).getStatusCode());
    }

    @Test
    public void deleteChannel_oneChannel_shouldNotRemainChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        testRestTemplate.delete(baseUrl + "/" + result.getId());

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class)).size());
    }

    @Test
    public void deleteInnerChannel_threeChannel_shouldRemainGoodChannels() {
        final ChannelDto result1 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
        final ChannelDto result2 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_2, ChannelDto.class);
        final ChannelDto result3 = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL_3, ChannelDto.class);

        testRestTemplate.delete(baseUrl + "/" + result2.getId());

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertAll(
                () -> assertEquals(2, channels.size()),
                () -> assertTrue(contain(channels, result1)),
                () -> assertTrue(contain(channels, result3))
        );
    }

    @Test
    public void deleteChannelWithNonExistentId_oneChannel_shouldNotDeleteChannel() {
        final ChannelDto result = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);

        testRestTemplate.delete(baseUrl + "/" + NON_EXISTENT_ID);

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertAll(
                () -> assertEquals(1, channels.size()),
                () -> assertTrue(contain(channels, result))
        );
    }

    @Test
    public void deleteChannelWithExistentId_oneChannelWithDependency_shouldNotDeleteChannel() {
        ChannelDto channelResult = generateAndPostProgramWithDependenciesAndGetChannel();

        testRestTemplate.delete(baseUrl + "/" + channelResult.getId());

        final List<ChannelDto> channels = List.of(testRestTemplate.getForObject(baseUrl, ChannelDto[].class));

        assertAll(
                () -> assertEquals(1, channels.size()),
                () -> assertTrue(contain(channels, channelResult))
        );
    }


    private <T> ResponseEntity<T> postResponse(Channel channel, Class<T> classType) {
        return testRestTemplate.postForEntity(baseUrl, channel, classType);
    }

    private <T> ResponseEntity<T> putResponse(Long id, Channel channel, Class<T> classType) {
        final HttpEntity<Channel> httpEntity = createHttpEntityWithMediaTypeJson(channel);
        return testRestTemplate.exchange(baseUrl + "/" + id, HttpMethod.PUT, httpEntity, classType);
    }

    private <T> ResponseEntity<T> getResponse(Long id, Class<T> classType) {
        return testRestTemplate.getForEntity(baseUrl + "/" + id, classType);
    }

    private ResponseEntity<ChannelDto[]> getResponse() {
        return testRestTemplate.getForEntity(baseUrl, ChannelDto[].class);
    }

    private ResponseEntity<Object> deleteResponse(Long id) {
        return testRestTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, null, Object.class);
    }

    private HttpEntity<Channel> createHttpEntityWithMediaTypeJson(Channel channel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(channel, headers);
    }

    private ChannelDto generateAndPostProgramWithDependenciesAndGetChannel() {
        final ContentDto contentResult = testRestTemplate.postForObject("http://localhost:" + port + "/api/contents", TEST_CONTENT, ContentDto.class);
        TEST_CONTENT.setId(contentResult.getId());

        final ChannelDto channelResult = testRestTemplate.postForObject(baseUrl, TEST_CHANNEL, ChannelDto.class);
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

        return channelResult;
    }

    private void copyChannelToDto(Channel channel, ChannelDto result) {
        result.setName(channel.getName());
        result.setFollow(channel.getFollow());
    }

    private boolean equalsById(ChannelDto channelDto, Object object) {
        if (channelDto == object) return true;
        if (channelDto == null || object == null || channelDto.getClass() != object.getClass()) return false;

        ChannelDto otherChannelDto = (ChannelDto) object;

        return Objects.equals(channelDto.getId(), otherChannelDto.getId());
    }

    private boolean equalsWithId(ChannelDto channelDto, Object object) {
        if (channelDto == object) return true;
        if (channelDto == null || object == null || channelDto.getClass() != object.getClass()) return false;

        ChannelDto otherChannelDto = (ChannelDto) object;

        if (!Objects.equals(channelDto.getId(), otherChannelDto.getId())) return false;
        if (!Objects.equals(channelDto.getName(), otherChannelDto.getName())) return false;
        return Objects.equals(channelDto.getFollow(), otherChannelDto.getFollow());
    }

    private boolean equalsWithoutId(ChannelDto channelDto, Object object) {
        if (channelDto == object) return true;
        if (channelDto == null || object == null || channelDto.getClass() != object.getClass()) return false;

        ChannelDto otherChannelDto = (ChannelDto) object;

        if (!Objects.equals(channelDto.getName(), otherChannelDto.getName())) return false;
        return Objects.equals(channelDto.getFollow(), otherChannelDto.getFollow());
    }

    private boolean contain(List<ChannelDto> channels, ChannelDto channelDto) {
        for (ChannelDto otherChannelDto : channels) {
            if (equalsWithId(channelDto, otherChannelDto)) return true;
        }
        return false;
    }
}
