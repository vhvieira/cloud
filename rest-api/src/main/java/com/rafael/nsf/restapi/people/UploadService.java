package com.rafael.nsf.restapi.people;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UploadService {

    private RestTemplate restTemplate = restTemplate();

    private String serverUrl = "http://localhost:9080/bot-api/files/";
    private String filePath = "C:\\Users\\victor.vieira\\Downloads\\techSession_March2022.png";

    public void sendUploadFiles() {
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("files", getTestFile(body));
//        body.add("files", getTestFile(body));
//        body.add("files", getTestFile(body));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, new HttpHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .postForEntity(serverUrl, requestEntity, String.class);
    }

    public void sendSingleFile() {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new    HttpEntity<LinkedMultiValueMap<String, Object>>(
                map, headers);
        ResponseEntity<String> result = restTemplate.exchange(
                serverUrl, HttpMethod.POST, requestEntity, String.class);
    }


    /**
     * Get a test file
     * @param fileMap
     * @return
     */
    private HttpEntity getTestFile(MultiValueMap<String, Object> fileMap) {
        try {
            ContentDisposition contentDisposition = ContentDisposition
                    .builder("form-data")
                    .name("file")
                    .filename("teste.jpg")
                    .build();
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            InputStreamSource inputStreamSource = new ClassPathResource("image.png");
            byte[] bytes = inputStreamSource.getInputStream().readAllBytes();
            log.info("Bytes read: {}", bytes);
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(bytes);
            return fileEntity;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
