package com.homeproject.smarthome.tvGuide;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BackendGenerator {
    private static final String PATH = "src/main/java/" + String.join("/", BackendGenerator.class.getPackageName().split("\\."));
    private static final String PACKAGE_NAME = BackendGenerator.class.getPackageName();

    public static void main(String[] args) {
        String modelName = "Content";
        try {
            createException();
            createHttpResponse();
            createModel(modelName);
            createRepository(modelName);
            createService(modelName);
            createApiController(modelName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createModel(String className) throws IOException {
        className = formatClassName(className);
        if (!Files.isDirectory(Path.of(PATH + "/model"))) {
            Files.createDirectory(Path.of(PATH + "/model"));
        }

        //TODO check if exist, ask what happen
        Files.write(Path.of(PATH + "/model/" + className + ".java"), getModelClassData(className).getBytes());
    }

    private static void createRepository(String modelName) throws IOException {
        modelName = formatClassName(modelName);
        if (!Files.isDirectory(Path.of(PATH + "/dao"))) {
            Files.createDirectory(Path.of(PATH + "/dao"));
        }

        //TODO check if exist, ask what happen
        Files.write(Path.of(PATH + "/dao/" + modelName + "Repository.java"), getCrudRepositoryInterfaceData(modelName).getBytes());
    }

    private static void createService(String modelName) throws IOException {
        modelName = formatClassName(modelName);
        if (!Files.isDirectory(Path.of(PATH + "/service"))) {
            Files.createDirectory(Path.of(PATH + "/service"));
        }

        //TODO check if exist, ask what happen
        Files.write(Path.of(PATH + "/service/" + modelName + "Service.java"), getServiceClassData(modelName).getBytes());
    }

    private static void createApiController(String modelName) throws IOException {
        if (!Files.isDirectory(Path.of(PATH + "/controller"))) {
            Files.createDirectory(Path.of(PATH + "/controller"));
        }

        //TODO check if exist, ask what happen
        modelName = formatClassName(modelName);
        Files.write(Path.of(PATH + "/controller/" + modelName + "ApiController.java"), getApiControllerClassData(modelName).getBytes());
    }

    private static void createHttpResponse() throws IOException {
        if (!Files.isDirectory(Path.of(PATH + "/response"))) {
            Files.createDirectory(Path.of(PATH + "/response"));
        }

        //TODO check if exist, ask what happen
        Files.write(Path.of(PATH + "/response/HttpResponse.java"), getHttpResponseClassData().getBytes());
    }

    private static void createException() throws IOException {
        if (!Files.isDirectory(Path.of(PATH + "/exception"))) {
            Files.createDirectory(Path.of(PATH + "/exception"));
        }

        //TODO check if exist, ask what happen
        Files.write(Path.of(PATH + "/exception/DataNotFoundException.java"), getDataNotFoundExceptionClassData().getBytes());
    }

    private static String formatClassName(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    private static String getModelClassData(String modelName) {
        return String.format(
               "package %s.model;\n" +
               "\n" +
               "import lombok.Getter;\n" +
               "import lombok.Setter;\n" +
               "import javax.persistence.Entity;\n" +
               "import javax.persistence.GeneratedValue;\n" +
               "import javax.persistence.Id;\n" +
               "\n" +
               "@Getter\n" +
               "@Setter\n" +
               "@Entity\n" +
               "public class %s {\n" +
               "    @Id\n" +
               "    @GeneratedValue\n" +
               "    private Long id;\n" +
               "}",
                PACKAGE_NAME, modelName
        );
    }

    private static String getCrudRepositoryInterfaceData(String modelName) {
        return String.format(
                "package %1$s.dao;\n" +
                "\n" +
                "import %1$s.model.%2$s;\n" +
                "import org.springframework.data.repository.CrudRepository;\n" +
                "\n" +
                "public interface %2$sRepository extends CrudRepository<%2$s, Long> {\n" +
                "}",
                PACKAGE_NAME, modelName
        );
    }

    private static String getServiceClassData(String modelName) {
        return String.format("package %1$s.service;\n" +
                "\n" +
                "import %1$s.exception.DataNotFoundException;\n" +
                "import %1$s.dao.%2$sRepository;\n" +
                "import %1$s.model.%2$s;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "@Service\n" +
                "public class %2$sService {\n" +
                "    @Autowired\n" +
                "    private %2$sRepository %3$sRepository;\n" +
                "\n" +
                "    public %2$s add(%2$s %3$s) {\n" +
                "        %3$s.setId(null);\n" +
                "        return %3$sRepository.save(%3$s);\n" +
                "    }\n" +
                "    \n" +
                "    public %2$s updateById(Long id, %2$s %3$s) {\n" +
                "        if (%3$sRepository.existsById(id)) {\n" +
                "            %3$s.setId(id);\n" +
                "            return %3$sRepository.save(%3$s);\n" +
                "        } else {\n" +
                "            throw new DataNotFoundException();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public %2$s findById(Long id) {\n" +
                "        return %3$sRepository.findById(id).orElseThrow(DataNotFoundException::new);\n" +
                "    }\n" +
                "\n" +
                "    public List<%2$s> findAll() {\n" +
                "        List<%2$s> list = new ArrayList<>();\n" +
                "        %3$sRepository.findAll().forEach(list::add);\n" +
                "        return list;\n" +
                "    }\n" +
                "\n" +
                "    public void deleteById(Long id) {\n" +
                "        if (%3$sRepository.existsById(id)) {\n" +
                "            %3$sRepository.deleteById(id);\n" +
                "        } else {\n" +
                "            throw new DataNotFoundException();\n" +
                "        }\n" +
                "    }\n" +
                "}\n",
                PACKAGE_NAME,
                modelName,
                modelName.toLowerCase()
        );
    }

    private static String getApiControllerClassData(String modelName) {
        return String.format("package %1$s.controller;\n" +
                "\n" +
                "import %1$s.exception.DataNotFoundException;\n" +
                "import %1$s.model.%2$s;\n" +
                "import %1$s.service.%2$sService;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.validation.BindingResult;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "import javax.validation.Valid;\n" +
                "\n" +
                "import static %1$s.response.HttpResponse.dataNotFoundByIdResponse;\n" +
                "import static %1$s.response.HttpResponse.invalidDataResponse;\n" +
                "\n" +
                "@RestController\n" +
                "@RequestMapping(\"/%3$ss\")\n" +
                "public class %2$sApiController {\n" +
                "    @Autowired\n" +
                "    private %2$sService %3$sService;\n" +
                "\n" +
                "    @PostMapping\n" +
                "    public ResponseEntity<?> add(@Valid @RequestBody %2$s %3$s, BindingResult bindingResult) {\n" +
                "        if (bindingResult.hasErrors()){\n" +
                "            return invalidDataResponse(bindingResult);\n" +
                "        }\n" +
                "        return ResponseEntity.ok(%3$sService.add(%3$s));\n" +
                "    }\n" +
                "\n" +
                "    @PutMapping(\"/{id}\")\n" +
                "    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody %2$s %3$s, BindingResult bindingResult) {\n" +
                "        if (bindingResult.hasErrors()){\n" +
                "            return invalidDataResponse(bindingResult);\n" +
                "        }\n" +
                "        try {\n" +
                "            return ResponseEntity.ok(%3$sService.updateById(id, %3$s));\n" +
                "        } catch (DataNotFoundException e) {\n" +
                "            return dataNotFoundByIdResponse(\"%2$s\", id);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public ResponseEntity<?> findById(@PathVariable Long id) {\n" +
                "        try {\n" +
                "            return ResponseEntity.ok(%3$sService.findById(id));\n" +
                "        } catch (DataNotFoundException e) {\n" +
                "            return dataNotFoundByIdResponse(\"%2$s\", id);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @GetMapping\n" +
                "    public ResponseEntity<?> findAll() {\n" +
                "        return ResponseEntity.ok(%3$sService.findAll());\n" +
                "    }\n" +
                "\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public ResponseEntity<?> deleteById(@PathVariable Long id) {\n" +
                "        try {\n" +
                "            %3$sService.deleteById(id);\n" +
                "            return ResponseEntity.ok().build();\n" +
                "        } catch (DataNotFoundException e) {\n" +
                "            return dataNotFoundByIdResponse(\"%2$s\", id);\n" +
                "        }\n" +
                "    }\n" +
                "}\n",
                PACKAGE_NAME,
                modelName,
                modelName.toLowerCase()
        );
    }

    private static String getHttpResponseClassData() {
        return "package " + PACKAGE_NAME + ".response;\n" +
                "\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "import org.springframework.http.MediaType;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.validation.BindingResult;\n" +
                "import org.springframework.validation.ObjectError;\n" +
                "import java.util.stream.Collectors;\n" +
                "\n" +
                "public abstract class HttpResponse {\n" +
                "    public static ResponseEntity<?> dataNotFoundByIdResponse(String name, Long id) {\n" +
                "        return ResponseEntity\n" +
                "                .status(HttpStatus.NOT_FOUND)\n" +
                "                .contentType(MediaType.APPLICATION_JSON)\n" +
                "                .body(\"[\" + jsonErrorObject(name, \"id\", String.valueOf(id), \"Not found!\") + \"]\");\n" +
                "    }\n" +
                "\n" +
                "    public static ResponseEntity<?> invalidDataResponse(BindingResult bindingResult) {\n" +
                "        return ResponseEntity\n" +
                "                .badRequest()\n" +
                "                .contentType(MediaType.APPLICATION_JSON)\n" +
                "                .body(\"[\" + bindingResult.getAllErrors()\n" +
                "                        .stream()\n" +
                "                        .map(HttpResponse::objectErrorToJsonObject)\n" +
                "                        .collect(Collectors.joining(\", \")) + \"]\");\n" +
                "    }\n" +
                "\n" +
                "    private static String objectErrorToJsonObject(ObjectError error) {\n" +
                "        return jsonErrorObject(\n" +
                "                error.toString().split(\"object '\")[1].split(\"'\")[0],\n" +
                "                error.toString().split(\"field '\")[1].split(\"'\")[0],\n" +
                "                error.toString().split(\"rejected value \\\\[\")[1].split(\"]\")[0],\n" +
                "                error.toString().split(\"default message \\\\[\")[2].split(\"]\")[0]\n" +
                "        );\n" +
                "    }\n" +
                "\n" +
                "    private static String jsonErrorObject(String objectName, String fieldName, String rejectedValue, String message) {\n" +
                "        return String.format(\n" +
                "                \"{\\\"object\\\" : \\\"%s\\\", \\\"field\\\" : \\\"%s\\\", \\\"rejected value\\\" : \\\"%s\\\", \\\"message\\\" : \\\"%s\\\"}\",\n" +
                "                objectName, fieldName, rejectedValue, message\n" +
                "        );\n" +
                "    }\n" +
                "}\n";
    }

    private static String getDataNotFoundExceptionClassData() {
        return "package " + PACKAGE_NAME + ".exception;\n" +
                "\n" +
                "public class DataNotFoundException extends RuntimeException {\n" +
                "    public DataNotFoundException() {\n" +
                "        super();\n" +
                "    }\n" +
                "\n" +
                "    public DataNotFoundException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "}";
    }
}
