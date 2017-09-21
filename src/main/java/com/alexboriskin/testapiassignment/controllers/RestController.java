package com.alexboriskin.testapiassignment.controllers;

import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.services.FileService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/files")
public class RestController {

    private FileService fileService;

    public RestController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(produces = { "application/xml", "application/json" })
    public HttpEntity<List<File>> getAllFiles(@RequestParam(required = false, defaultValue = "") String fileName,
                                              @RequestParam(required = false, defaultValue = "") String ID) {

        List<File> allFilesList = new ArrayList<>();
        List<File> filteredList;

        if (fileName.equals("") && ID.equals("")) {
            allFilesList = fileService.getAll();
        } else {
            if (!ID.equals("")) {
                allFilesList.add(fileService.getById(Long.parseLong(ID)));
            }
            if (!fileName.equals("")) {
                filteredList = fileService.getByName(fileName);
                allFilesList.addAll(filteredList);
            }
        }

        if (allFilesList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (File file : allFilesList) {
                Link selfLink = linkTo(methodOn(RestController.class).getFile(file.getFileId())).withSelfRel();
                file.add(selfLink);
            }
        }
        return new ResponseEntity<>(allFilesList, HttpStatus.OK);

    }

    @GetMapping(value = "/{id:[\\d]+}", produces = { "application/xml", "application/json" })
    public HttpEntity<File> getFile(@PathVariable Long id) {
        File file = fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Link selfLink = linkTo(methodOn(RestController.class).getFile(file.getFileId())).withSelfRel();
        file.add(selfLink);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PutMapping(value = "/{id:[\\d]+}", produces = { "application/xml", "application/json" },
                                        consumes = { "application/xml", "application/json" })
    public HttpEntity<File> updateFile(@PathVariable Long id, @RequestBody File file) {
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        file.setFileId(id);
        fileService.update(file);

        Link selfLink = linkTo(methodOn(RestController.class).getFile(file.getFileId())).withSelfRel();
        file.add(selfLink);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PostMapping(produces = { "application/xml", "application/json" },
                 consumes = { "application/xml", "application/json" })
    public HttpEntity<File> postFile(@RequestBody File file) {

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        fileService.saveNew(file);

        Link selfLink = linkTo(methodOn(RestController.class).getFile(file.getFileId())).withSelfRel();
        file.add(selfLink);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @DeleteMapping("/{id:[\\d]+}")
    public HttpEntity<File> deleteFile(@PathVariable Long id) {
        File file = fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fileService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
