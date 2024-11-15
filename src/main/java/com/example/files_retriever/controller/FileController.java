package com.example.files_retriever.controller;

import com.example.files_retriever.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String userName, @RequestParam("file") MultipartFile file) {
        try{
            String fileUrl = fileService.uploadUserFile(userName, file);
            return ResponseEntity.ok(fileUrl);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("file upload failed!");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchFiles(@RequestParam String userName, @RequestParam String searchKey){
        List<String> files = fileService.searchUserFiles(userName, searchKey);
        return ResponseEntity.ok(files);
    }


}
