package com.alexboriskin.testapiassignment.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
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
    public HttpEntity<File> getFile(@PathVariable Long id) {
        File file = fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<File>(HttpStatus.NOT_FOUND);
        }

        Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
        file.add(selfLink);

        return new ResponseEntity<File>(file, HttpStatus.OK);
    }

    @PutMapping("/files/{id}")
    public HttpEntity<File> updateFile(@PathVariable Long id, @RequestBody File file) {
        if (file == null) {
            return new ResponseEntity<File>(HttpStatus.NOT_FOUND);
        }

        file.setFileId(id);
        fileService.update(file);

        Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
        file.add(selfLink);

        return new ResponseEntity<File>(file, HttpStatus.OK);
    }

    @GetMapping("/files/upload")
    public String uploadFile() {
        return "FileUpload";
    }

    @PostMapping("/files/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        File uploadedFile = fileService.processUploadedFile(file);

        if (uploadedFile != null) {
            redirectAttributes.addAttribute("id", uploadedFile.getFileId()).addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");

            return "redirect:/files/{id}";
        } else {
            return "redirect:/";
        }
    }

    @DeleteMapping("/files/{id}")
    public HttpEntity<File> deleteFile(@PathVariable Long id) {
        File file = fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<File>(HttpStatus.NOT_FOUND);
        }
        fileService.deleteById(id);
        return new ResponseEntity<File>(HttpStatus.NO_CONTENT);
    }

}
