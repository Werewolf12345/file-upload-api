package com.alexboriskin.testapiassignment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.services.FileService;

@RestController
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

    @PostMapping("/files/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        fileService.saveNew(null);
        redirectAttributes
                .addFlashAttribute("message", "You successfully uploaded "
                        + file.getOriginalFilename() + "!");

        return "Uploaded!";
    }

}
