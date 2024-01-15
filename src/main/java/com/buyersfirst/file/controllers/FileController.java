package com.buyersfirst.file.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.buyersfirst.file.interfaces.PathRequestBody;
import com.buyersfirst.file.service.FileStorageService;

@RestController
@RequestMapping (path = "/file")
public class FileController {

    @Autowired
    FileStorageService fileStorageService;
    
    @PostMapping (path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody String uploadFile (
        @RequestParam String path, 
        @RequestParam String update, 
        @RequestPart MultipartFile file
    ) {
        try {
            fileStorageService.save(file, path, Boolean.parseBoolean(update));
            return path;
        } catch (Exception e) {
            String ExceptionStr = e.toString();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionStr);
        }
        
    }

    @GetMapping (path = "/get-image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody byte[] getFile (@RequestBody PathRequestBody body) {
        try {
            Resource file = fileStorageService.load(body.path);
            return file.getContentAsByteArray();
        } catch (Exception e) {
            String ExceptionStr = e.toString();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionStr);
        }
    }
}
