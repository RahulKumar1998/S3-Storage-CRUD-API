package com.example.files_retriever.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final S3Service s3Service;

    public FileService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public List<String> searchUserFiles(String userName, String searchKey){
        return s3Service.searchFiles(userName, searchKey);
    }

    public String uploadUserFile(String userName, MultipartFile file) throws IOException {
        String filePath = userName + "/" + file.getOriginalFilename();
        return s3Service.uploadFile(filePath, file.getInputStream(), file.getSize(), file.getContentType());
    }

}
