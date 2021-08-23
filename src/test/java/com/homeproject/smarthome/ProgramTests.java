package com.homeproject.smarthome;

import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.model.Content;
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
public class ProgramTests {
    @LocalServerPort
    private int port;
    private String baseUrl;
    private static final Long NON_EXISTENT_ID = 100L;
    private static final LocalDateTime START_TIME = LocalDateTime.of(2021, 8, 11, 4, 0, 0);
    private static final LocalDateTime START_TIME_2 = LocalDateTime.of(2021, 8, 11, 6, 30, 0);
    private static final LocalDateTime START_TIME_3 = LocalDateTime.of(2021, 8, 11, 20, 0, 0);
    private static final LocalDateTime END_TIME = LocalDateTime.of(2021, 8, 11, 4, 50, 0);
    private static final LocalDateTime END_TIME_2 = LocalDateTime.of(2021, 8, 11, 8, 20, 0);
    private static final LocalDateTime END_TIME_3 = LocalDateTime.of(2021, 8, 11, 21, 50, 0);

    private static final Content TEST_CONTENT = new Content(null, "Test content", "A movie about contents in test.", null);
    private static final Content TEST_CONTENT_WITH_NON_EXISTENT_ID = new Content(NON_EXISTENT_ID, "Test content", "A movie about contents in test.", null);
    private static final Channel TEST_CHANNEL = new Channel(null, "Test channel", false, null);
    private static final Channel TEST_CHANNEL_WITH_NON_EXISTENT_ID = new Channel(NON_EXISTENT_ID, "Test channel", false, null);

    private static final Program TEST_PROGRAM = new Program(null, START_TIME, END_TIME, TEST_CONTENT, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_WITHOUT_START_TIME = new Program(null, null, END_TIME, TEST_CONTENT, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_WITHOUT_END_TIME = new Program(null, START_TIME, null, TEST_CONTENT, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_WITH_INVALID_TIME = new Program(null, END_TIME, START_TIME, TEST_CONTENT, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_WITHOUT_CONTENT = new Program(null, START_TIME, END_TIME, null, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_WITHOUT_CHANNEL = new Program(null, START_TIME, END_TIME, TEST_CONTENT, null);
    private static final Program TEST_PROGRAM_WITH_NON_EXISTENT_CONTENT = new Program(null, START_TIME, END_TIME, TEST_CONTENT_WITH_NON_EXISTENT_ID, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_WITH_NON_EXISTENT_CHANNEL = new Program(null, START_TIME, END_TIME, TEST_CONTENT, TEST_CHANNEL_WITH_NON_EXISTENT_ID);
    private static final Program TEST_PROGRAM_WITH_NON_EXISTENT_ID = new Program(NON_EXISTENT_ID, START_TIME, END_TIME, TEST_CONTENT, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_2 = new Program(null, START_TIME_2, END_TIME_2, TEST_CONTENT, TEST_CHANNEL);
    private static final Program TEST_PROGRAM_3 = new Program(null, START_TIME_3, END_TIME_3, TEST_CONTENT, TEST_CHANNEL);
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/programs";

        TEST_CONTENT.setId(testRestTemplate.postForObject("http://localhost:" + port + "/api/contents", TEST_CONTENT, ContentDto.class).getId());
        TEST_CHANNEL.setId(testRestTemplate.postForObject("http://localhost:" + port + "/api/channels", TEST_CHANNEL, ChannelDto.class).getId());
    }

    @Test
    public void addNewValidProgram_noProgram_shouldReturnOkHttpStatus() {
        assertEquals(HttpStatus.OK, postResponse(TEST_PROGRAM, ProgramDto.class).getStatusCode());
    }

    @Test
    public void addNewValidProgramWithoutStartTime_noProgram_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_PROGRAM_WITHOUT_START_TIME, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidProgramWithoutEndTime_noProgram_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_PROGRAM_WITHOUT_END_TIME, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidProgramWithInvalidTime_noProgram_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_PROGRAM_WITH_INVALID_TIME, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidProgramWithoutContent_noProgram_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_PROGRAM_WITHOUT_CONTENT, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidProgramWithNonExistentContent_noProgram_shouldReturnNotFoundHttpStatus() {
        assertEquals(HttpStatus.NOT_FOUND, postResponse(TEST_PROGRAM_WITH_NON_EXISTENT_CONTENT, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidProgramWithoutChannel_noProgram_shouldReturnBadRequestHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, postResponse(TEST_PROGRAM_WITHOUT_CHANNEL, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidProgramWithNonExistentChannel_noProgram_shouldReturnNotFoundHttpStatus() {
        assertEquals(HttpStatus.NOT_FOUND, postResponse(TEST_PROGRAM_WITH_NON_EXISTENT_CHANNEL, Object.class).getStatusCode());
    }

    @Test
    public void addNewValidProgram_noProgram_shouldReturnSameProgram() {
        assertTrue(equalsWithoutId(new ProgramDto(TEST_PROGRAM), testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class)));
    }

    @Test
    public void addNewValidProgramWithoutId_noProgram_shouldReturnProgramWithNotNullGeneratedId() {
        assertNotEquals(null, testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class).getId());
    }

    @Test
    public void addNewValidProgramWithNonExistentId_noProgram_shouldReturnProgramWithNotNullGeneratedId() {
        assertNotEquals(null, testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_WITH_NON_EXISTENT_ID, ProgramDto.class).getId());
    }

    @Test
    public void addNewValidProgramWithExistentId_oneProgram_shouldReturnProgramWithDifferentGeneratedId() {
        final ProgramDto result1 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        TEST_PROGRAM_2.setId(result1.getId());
        final ProgramDto result2 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);
        TEST_PROGRAM_2.setId(null);

        assertFalse(equalsById(result1, result2));
    }

    @Test
    public void addTwoNewValidProgram_noProgram_shouldReturnProgramsWithDifferentId() {
        assertFalse(equalsById(
                testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class),
                testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class)
        ));
    }

    @Test
    public void addTwoNewValidProgram_noProgram_shouldSaveTwoPrograms() {
        testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);

        assertEquals(2, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void addTwoNewValidProgram_noProgram_shouldSaveTwoProgramsWithGoodData() {
        final ProgramDto result1 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);
        final ProgramDto result2 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertAll(
                () -> assertTrue(contain(programs, result1)),
                () -> assertTrue(contain(programs, result2))
        );
    }

    @Test
    public void addNewValidProgramWithoutStartTime_noProgram_shouldNotSaveProgram() {
        testRestTemplate.postForEntity(baseUrl, TEST_PROGRAM_WITHOUT_START_TIME, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void addNewValidProgramWithoutEndTime_noProgram_shouldNotSaveProgram() {
        testRestTemplate.postForEntity(baseUrl, TEST_PROGRAM_WITHOUT_END_TIME, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void addNewValidProgramWithInvalidTime_noProgram_shouldNotSaveProgram() {
        testRestTemplate.postForEntity(baseUrl, TEST_PROGRAM_WITH_INVALID_TIME, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void addNewValidProgramWithoutContent_noProgram_shouldNotSaveProgram() {
        testRestTemplate.postForEntity(baseUrl, TEST_PROGRAM_WITHOUT_CONTENT, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void addNewValidProgramWithNonExistentContent_noProgram_shouldNotSaveProgram() {
        testRestTemplate.postForEntity(baseUrl, TEST_PROGRAM_WITH_NON_EXISTENT_CONTENT, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void addNewValidProgramWithoutChannel_noProgram_shouldNotSaveProgram() {
        testRestTemplate.postForEntity(baseUrl, TEST_PROGRAM_WITHOUT_CHANNEL, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void addNewValidProgramWithNonExistentChannel_noProgram_shouldNotSaveProgram() {
        testRestTemplate.postForEntity(baseUrl, TEST_PROGRAM_WITH_NON_EXISTENT_CHANNEL, Object.class);

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }


    @Test
    public void updateValidProgram_oneProgram_shouldReturnOkHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.OK, putResponse(result.getId(), TEST_PROGRAM_2, ProgramDto.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithoutStartTime_oneProgram_shouldReturnBadRequestHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_PROGRAM_WITHOUT_START_TIME, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithoutEndTime_oneProgram_shouldReturnBadRequestHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_PROGRAM_WITHOUT_END_TIME, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithInvalidTime_oneProgram_shouldReturnBadRequestHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_PROGRAM_WITH_INVALID_TIME, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithoutContent_oneProgram_shouldReturnBadRequestHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_PROGRAM_WITHOUT_CONTENT, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithNonExistentContent_oneProgram_shouldReturnNotFoundHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.NOT_FOUND, putResponse(result.getId(), TEST_PROGRAM_WITH_NON_EXISTENT_CONTENT, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithoutChannel_oneProgram_shouldReturnBadRequestHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, putResponse(result.getId(), TEST_PROGRAM_WITHOUT_CHANNEL, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithNonExistentChannel_oneProgram_shouldReturnNotFoundHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.NOT_FOUND, putResponse(result.getId(), TEST_PROGRAM_WITH_NON_EXISTENT_CHANNEL, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithNonExistentId_oneProgram_shouldReturnNotFoundHttpStatus() {
        testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.NOT_FOUND, putResponse(NON_EXISTENT_ID, TEST_PROGRAM_2, Object.class).getStatusCode());
    }

    @Test
    public void updateValidProgramWithExistentId_oneProgram_shouldReturnSameProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        copyProgramToDto(TEST_PROGRAM_2, result);

        assertTrue(equalsWithId(result, putResponse(result.getId(), TEST_PROGRAM_2, ProgramDto.class).getBody()));
    }

    @Test
    public void updateValidProgramWithExistentId_oneProgram_shouldUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        copyProgramToDto(TEST_PROGRAM_2, result);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_2);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithExistentId_twoProgram_shouldUpdateProgram() {
        final ProgramDto result1 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);

        copyProgramToDto(TEST_PROGRAM_3, result1);

        testRestTemplate.put(baseUrl + "/" + result1.getId(), TEST_PROGRAM_3);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result1));
    }

    @Test
    public void updateValidProgramWithExistentId_twoProgram_otherProgramShouldNotChange() {
        final ProgramDto result1 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);
        final ProgramDto result2 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);

        copyProgramToDto(TEST_PROGRAM_3, result1);

        testRestTemplate.put(baseUrl + "/" + result1.getId(), TEST_PROGRAM_3);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result2));
    }

    @Test
    public void updateValidProgramWithoutStartTime_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_WITHOUT_START_TIME);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithoutEndTime_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_WITHOUT_END_TIME);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithInvalidTime_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_WITH_INVALID_TIME);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithoutContent_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_WITHOUT_CONTENT);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithNonExistentContent_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_WITH_NON_EXISTENT_CONTENT);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithoutChannel_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_WITHOUT_CHANNEL);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithNonExistentChannel_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + result.getId(), TEST_PROGRAM_WITH_NON_EXISTENT_CHANNEL);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }

    @Test
    public void updateValidProgramWithNonExistentId_noProgram_shouldNotAddProgram() {
        testRestTemplate.put(baseUrl + "/" + NON_EXISTENT_ID, TEST_PROGRAM);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertEquals(0, programs.size());
    }

    @Test
    public void updateValidProgramWithNonExistentId_oneProgram_shouldNotUpdateProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.put(baseUrl + "/" + NON_EXISTENT_ID, TEST_PROGRAM_2);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertTrue(contain(programs, result));
    }


    @Test
    public void getProgramWithExistentId_oneProgram_shouldReturnOkHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.OK, getResponse(result.getId(), ProgramDto.class).getStatusCode());
    }

    @Test
    public void getProgramWithNonExistentId_noProgram_shouldReturnNotFoundHttpStatus() {
        assertEquals(HttpStatus.NOT_FOUND, getResponse(NON_EXISTENT_ID, Object.class).getStatusCode());
    }

    @Test
    public void getProgramWithExistentId_oneProgram_shouldReturnSameProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        final ProgramDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ProgramDto.class);

        assertTrue(equalsWithId(result, getResult));
    }

    @Test
    public void getProgramWithExistentId_threeProgram_shouldReturnGoodProgram() {
        testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);
        testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_3, ProgramDto.class);

        final ProgramDto getResult = testRestTemplate.getForObject(baseUrl + "/" + result.getId(), ProgramDto.class);

        assertTrue(equalsWithId(result, getResult));
    }


    @Test
    public void getAllProgram_noProgram_shouldReturnOkHttpStatus() {
        assertEquals(HttpStatus.OK, getResponse().getStatusCode());
    }

    @Test
    public void getAllProgram_noProgram_shouldReturnEmptyList() {
        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void getAllProgram_oneProgram_shouldReturnListWithProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertAll(
                () -> assertEquals(1, programs.size()),
                () -> assertTrue(contain(programs, result))
        );
    }

    @Test
    public void getAllProgram_twoProgram_shouldReturnListWithPrograms() {
        final ProgramDto result1 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);
        final ProgramDto result2 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertAll(
                () -> assertEquals(2, programs.size()),
                () -> assertTrue(contain(programs, result1)),
                () -> assertTrue(contain(programs, result2))
        );
    }


    @Test
    public void deleteProgramWithExistentId_oneProgram_shouldReturnOkHttpStatus() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.OK, deleteResponse(result.getId()).getStatusCode());
    }

    @Test
    public void deleteProgramWithNonExistentId_oneProgram_shouldReturnNotFoundHttpStatus() {
        testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse(NON_EXISTENT_ID).getStatusCode());
    }

    @Test
    public void deleteProgram_oneProgram_shouldNotRemainProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.delete(baseUrl + "/" + result.getId());

        assertEquals(0, List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class)).size());
    }

    @Test
    public void deleteInnerProgram_threeProgram_shouldRemainGoodPrograms() {
        final ProgramDto result1 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);
        final ProgramDto result2 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_2, ProgramDto.class);
        final ProgramDto result3 = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM_3, ProgramDto.class);

        testRestTemplate.delete(baseUrl + "/" + result2.getId());

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertAll(
                () -> assertEquals(2, programs.size()),
                () -> assertTrue(contain(programs, result1)),
                () -> assertTrue(contain(programs, result3))
        );
    }

    @Test
    public void deleteProgramWithNonExistentId_oneProgram_shouldNotDeleteProgram() {
        final ProgramDto result = testRestTemplate.postForObject(baseUrl, TEST_PROGRAM, ProgramDto.class);

        testRestTemplate.delete(baseUrl + "/" + NON_EXISTENT_ID);

        final List<ProgramDto> programs = List.of(testRestTemplate.getForObject(baseUrl, ProgramDto[].class));

        assertAll(
                () -> assertEquals(1, programs.size()),
                () -> assertTrue(contain(programs, result))
        );
    }


    private <T> ResponseEntity<T> postResponse(Program program, Class<T> classType) {
        return testRestTemplate.postForEntity(baseUrl, program, classType);
    }

    private <T> ResponseEntity<T> putResponse(Long id, Program program, Class<T> classType) {
        final HttpEntity<Program> httpEntity = createHttpEntityWithMediaTypeJson(program);
        return testRestTemplate.exchange(baseUrl + "/" + id, HttpMethod.PUT, httpEntity, classType);
    }

    private <T> ResponseEntity<T> getResponse(Long id, Class<T> classType) {
        return testRestTemplate.getForEntity(baseUrl + "/" + id, classType);
    }

    private ResponseEntity<ProgramDto[]> getResponse() {
        return testRestTemplate.getForEntity(baseUrl, ProgramDto[].class);
    }

    private ResponseEntity<Object> deleteResponse(Long id) {
        return testRestTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, null, Object.class);
    }

    private HttpEntity<Program> createHttpEntityWithMediaTypeJson(Program program) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(program, headers);
    }

    private void copyProgramToDto(Program program, ProgramDto programDto) {
        programDto.setStart(program.getStart());
        programDto.setEnd(program.getEnd());
        programDto.setContent(program.getContent());
    }

    private boolean equalsById(ProgramDto programDto, Object object) {
        if (programDto == object) return true;
        if (programDto == null || object == null || programDto.getClass() != object.getClass()) return false;

        ProgramDto otherProgramDto = (ProgramDto) object;

        return Objects.equals(programDto.getId(), otherProgramDto.getId());
    }

    private boolean equalsWithId(ProgramDto programDto, Object object) {
        return  equalsById(programDto, object) && equalsWithoutId(programDto, object);
    }

    private boolean equalsWithoutId(ProgramDto programDto, Object object) {
        if (programDto == object) return true;
        if (programDto == null || object == null || programDto.getClass() != object.getClass()) return false;

        ProgramDto otherProgramDto = (ProgramDto) object;

        if (!Objects.equals(programDto.getStart(), otherProgramDto.getStart())) return false;
        if (!Objects.equals(programDto.getEnd(), otherProgramDto.getEnd())) return false;
        return Objects.equals(programDto.getContent(), otherProgramDto.getContent());
    }

    private boolean contain(List<ProgramDto> programs, ProgramDto programDto) {
        for (ProgramDto otherProgramDto : programs) {
            if (equalsWithId(programDto, otherProgramDto)) return true;
        }
        return false;
    }
}
