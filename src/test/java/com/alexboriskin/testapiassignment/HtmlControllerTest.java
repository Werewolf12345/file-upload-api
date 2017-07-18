package com.alexboriskin.testapiassignment;

import com.alexboriskin.testapiassignment.commands.FileForm;
import com.alexboriskin.testapiassignment.controllers.HtmlController;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HtmlController.class)
public class HtmlControllerTest {
    
    private final long EXISTING_ID = 1L;
    private MetaData metaData1 = new MetaData("metaData11", "metaData12", "metaData13");
    private File file = new File("file1.properties", new Date(), metaData1);
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testGetAllFilesHtml() throws Exception {
        mvc.perform(get("/files").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void testGetFileHtml() throws Exception {
        mvc.perform(get("/files/1").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void testUploadFileHtml() throws Exception {
        mvc.perform(get("/files/upload").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void testNewFile() throws Exception {

        given(fileService.saveNew(any(FileForm.class))).willReturn(file);

        mvc.perform(get("/files/new").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        mvc.perform(
                post("/files/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fileName", "file1.new")
                        .param("uploaded", "13-Jun-2017")
                        .param("metaData1", "metaData1new")
                        .param("metaData2", "metaData2new")
                        .param("metaData3", "metaData3new")
                        .accept(MediaType.TEXT_HTML)
        )

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/files/0"))
                .andExpect(flash().attribute("message",
                        "You successfully created file1.properties!"))
                .andExpect(model().hasNoErrors());

        verify(fileService, times(1)).saveNew(any(FileForm.class));
        verifyNoMoreInteractions(fileService);
    }

    @Test
    public void testUpdateFile() throws Exception {
        
        file.setFileId(EXISTING_ID);
        file.getMetaData().setId(EXISTING_ID);
        file.getUploaded().setTime(1L);
       
        doNothing().when(fileService).update(file);
        given(fileService.getById(EXISTING_ID)).willReturn(file);

        mvc.perform(
                get("/files/{id}/update", EXISTING_ID).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(fileService, times(1)).getById(anyLong());
        verifyNoMoreInteractions(fileService);

        mvc.perform(
                post("/files/{id}/update", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fileName", "file1.updated")
                        .param("uploaded", "13-Jun-2017")
                        .param("metaData.metaData1", "metaData1Updated")
                        .param("metaData.metaData2", "metaData2Updated")
                        .param("metaData.metaData3", "metaData3Updated")
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/files/" + EXISTING_ID));

        verify(fileService, times(1)).update(any(File.class));
        verifyNoMoreInteractions(fileService);
    }

    @Test
    public void testDeleteFile() throws Exception {

        file.setFileId(EXISTING_ID);
        file.getMetaData().setId(EXISTING_ID);
        file.getUploaded().setTime(1L);

        doNothing().when(fileService).deleteById(EXISTING_ID);
        given(fileService.getById(EXISTING_ID)).willReturn(file);

        mvc.perform(
                post("/files/{id}/delete", EXISTING_ID).accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/files"));

        verify(fileService, times(1)).getById(EXISTING_ID);
        verify(fileService, times(1)).deleteById(EXISTING_ID);
        verifyNoMoreInteractions(fileService);

    }

    @Test
    public void testUploadFile() throws Exception {
      
        mvc.perform(
                get("/files/upload").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        
    }

    @Test
    public void testHandleFileUpload() throws Exception {
        
        file.setFileId(EXISTING_ID);
        file.getMetaData().setId(EXISTING_ID);
        file.getUploaded().setTime(1L);
        
        MockMultipartFile mockFile = new MockMultipartFile("file", "file.name", "text/plain", "bytes".getBytes());
       
        given(fileService.processUploadedFile(any(MultipartFile.class))).willReturn(file);
        
        mvc.perform(
                fileUpload("/files/upload").file(mockFile))

                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("id", "1"))
                .andExpect(redirectedUrl("/files/" + EXISTING_ID))
                .andExpect(flash().attribute("message",
                        "You successfully uploaded " + "file.name" + "!"))
                .andExpect(model().hasNoErrors());
        
        verify(fileService, times(1)).processUploadedFile(any(MultipartFile.class));
        verifyNoMoreInteractions(fileService);
    }

}
