package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.FileResponse;
import com.example.demo.model.WebResponse;
import com.example.demo.service.FileService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;


    @Parameter(name = "user", hidden = true)
    @PostMapping(
            value = "/api/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FileResponse> uploadFile(User user, @RequestBody MultipartFile file) {
        FileResponse response = fileService.upload(file);

        return WebResponse.<FileResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping(
            value = "/api/file/{filename}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = fileService.load(filename);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
