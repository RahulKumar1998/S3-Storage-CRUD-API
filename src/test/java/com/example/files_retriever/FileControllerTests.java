package com.example.files_retriever;

import com.example.files_retriever.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testSearchFiles() throws Exception {
        when(fileService.searchUserFiles("sandy", "logistics")).thenReturn(List.of("logistics_01.txt"));
        mockMvc.perform(get("/api/files/search")
                .param("userName", "sandy")
                .param("searchKey", "logistics"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("logistics_01.txt"));
    }

    @Test
    void testUploadFile_Success() throws Exception {


        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "testfile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is a test file".getBytes()
        );
        String filePath = "sandy" + "/" + "file";


        mockMvc.perform(multipart("/api/files/upload")
                        .file(mockFile)
                        .param("userName", "sandy"))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadFile_EmptyFile() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "emptyfile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "".getBytes()
        );

        mockMvc.perform(multipart("/upload")
                        .file(mockFile)
                        .param("userName", "sandy"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUploadFile_MissingFile() throws Exception {

        mockMvc.perform(multipart("/upload")
                        .param("username", "sandy"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUploadFile_MissingUsername() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "testfile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is a test file".getBytes()
        );

        mockMvc.perform(multipart("/upload")
                        .file(mockFile))
                .andExpect(status().isNotFound());
    }
}


