package com.rafael.nsf.restapi.people;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * JIRA documentation used:
 * https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issue-attachments/#api-rest-api-3-issue-issueidorkey-attachments-post
 */
@Service
@Slf4j
public class UploadService {

    private RestTemplate restTemplate = restTemplate();

    @Autowired
    private ApplicationContext context;

    private String serverUrl = "http://localhost:9080/bot-api/files/";
    private String filePath = "C:\\Users\\victor.vieira\\Downloads\\techSession_March2022.png";
    private String GS_URL = "gs://lifty-botty-config/images/lifty_micro.png";

    private static final String ENCODING = "UTF-8";


    public void sendSimpleFileToJira() {
        // Fill out the "form"...
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        //parameters.add("file", new FileSystemResource(filePath)); // load file into parameter
        parameters.add("file", readFileFromStorage(GS_URL));

        // Set the headers...
        HttpHeaders headers = createAuthHeaders("JIRA_USER", "JIRA_TOKEN");
        headers.set("Content-Type", "multipart/form-data"); // we are sending a form
        headers.set("Accept", "application/json"); // looks like you want a string back
        headers.set("X-Atlassian-Token", "nocheck");
        //CURL command:
        // curl -D- -u {username}:{password} -X POST -H "X-Atlassian-Token: nocheck" -F "file=@{path/to/file}"
        // https://aromeira.atlassian.net/rest/api/3/issue/{issue-key}/attachments
        // Fire!
        int statusCode = restTemplate.exchange(
                "JIRA_URL",
                HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(parameters, headers),
                String.class
        ).getStatusCodeValue();
        System.out.println("Returned status: " + statusCode);
    }

    public void sendSimpleFile() {
        // Fill out the "form"...
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        //parameters.add("file", new FileSystemResource(filePath)); // load file into parameter
        parameters.add("file", readFileFromStorage(GS_URL));

        // Set the headers...
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data"); // we are sending a form
        headers.set("Accept", "text/plain"); // looks like you want a string back

        // Fire!
        String result = restTemplate.exchange(
                serverUrl,
                HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(parameters, headers),
                String.class
        ).getBody();
    }

    public void sendUploadFiles() {
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("files", getTestFile(body, new HttpHeaders()));

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
    private HttpEntity getTestFile(MultiValueMap<String, Object> fileMap, HttpHeaders headers) {
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
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(bytes, headers);
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

    /**
     * Read from cloud storage
     * @param fileURI
     * @return
     */
    public Resource readFileFromStorage(String fileURI) {
        try {
            // resource
            Resource resource = context.getResource(fileURI);
            // prints basic info
            log.debug("Filename: " + resource.getFilename());
            log.debug("Description: " + resource.getDescription());

            return resource;
        } catch (Exception ex) {
            log.error("Error reading file from GCP bucket", ex);
        }

        // return content
        return null;
    }

    //create base64
    private HttpHeaders createAuthHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64Utils.encode(auth.getBytes(Charset.forName(ENCODING)));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

}
