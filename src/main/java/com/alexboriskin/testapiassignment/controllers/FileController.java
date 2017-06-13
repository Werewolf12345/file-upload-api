package com.alexboriskin.testapiassignment.controllers;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.services.FileService;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/files")
    @ResponseBody
    public List<File> getAllFiles() {
        return fileService.getAll();
    }

    @GetMapping("/files/{id}")
    @ResponseBody
    public File getFile(@PathVariable Long id) {
        return fileService.getById(id);
    }
    
    @PutMapping("/files/{id}")
    public ResponseEntity<File> updateFile(@PathVariable Long id,  @RequestBody File file, @Context UriInfo uriInfo) {
        if (file == null) {
            return new ResponseEntity<File>(HttpStatus.NOT_FOUND);
        }
        
        file.setId(id);
        fileService.update(file);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        URI location = URI.create(getUriForSelf( uriInfo,  file));
        responseHeaders.setLocation(location);
        return new ResponseEntity<File>(file, responseHeaders, HttpStatus.OK);
    }
    
    @GetMapping("/files/upload")
    public String uploadFile() {
        return "FileUpload";
    }

    @PostMapping("/files/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        File uploadedFile = fileService.processUploadedFile(file);

        if (uploadedFile != null) {
            redirectAttributes.addAttribute("id", uploadedFile.getId())
                    .addFlashAttribute(
                            "message",
                            "You successfully uploaded "
                                    + file.getOriginalFilename() + "!");

            return "redirect:/files/{id}";
        } else {
            return "redirect:/";
        }
    }

    @DeleteMapping("/files/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileService.deleteById(id);
    }
    
    private String getUriForSelf(UriInfo uriInfo, File file) {
        String uri = uriInfo.getBaseUriBuilder()
                .path(FileController.class)
                .path(Long.toString(file.getId()))
                .build()
                .toString();
        return uri;
    }

}
