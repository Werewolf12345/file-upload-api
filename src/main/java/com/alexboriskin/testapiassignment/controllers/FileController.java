package com.alexboriskin.testapiassignment.controllers;

import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/files")
public class FileController {


    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(produces = { "application/xml", "application/json" })
    @ResponseBody
    public HttpEntity<List<File>> getAllFiles(@RequestParam(required = false, defaultValue = "") 
                                                            String fileName) {

        List<File> allFilesList;

        if (fileName.equals("")) {
            allFilesList = fileService.getAll();
        } else {
            allFilesList = fileService.getByName(fileName);
        }

        if (allFilesList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (File file : allFilesList) {
                Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
                file.add(selfLink);
            }
        }
        return new ResponseEntity<>(allFilesList, HttpStatus.OK);
    }

    @GetMapping(produces = "text/html")
    public String getAllFilesHtml(Model model) {

        List<File> allFilesList = fileService.getAll();

        if (!allFilesList.isEmpty()) {
            for (File file : allFilesList) {
                Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
                file.add(selfLink);
            }

            model.addAttribute("fileList", allFilesList);
        }

        return "ListOfFiles";

    }

    @GetMapping(value = "/{id:[\\d]+}", produces = { "application/xml", "application/json" })
    @ResponseBody
    public HttpEntity<File> getFile(@PathVariable Long id) {
        File file = fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
        file.add(selfLink);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @GetMapping(value = "/{id:[\\d]+}", produces = "text/html")
    public String getFileHtml(@PathVariable Long id, Model model) {
        File file = fileService.getById(id);

        if (file != null) {
            Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
            file.add(selfLink);
            model.addAttribute("fileId", file.getFileId());
            model.addAttribute("fileName", file.getFileName());
            model.addAttribute("uploaded", file.getUploaded());
            model.addAttribute("metaDataId", file.getMetaData().getId());
            model.addAttribute("metaData1", file.getMetaData().getMetaData1());
            model.addAttribute("metaData2", file.getMetaData().getMetaData2());
            model.addAttribute("metaData3", file.getMetaData().getMetaData3());
            model.addAttribute("selfLink", file.getId().getHref());
        }

        return "File";
    }

    @PutMapping("/{id:[\\d]+}")
    @ResponseBody
    public HttpEntity<File> updateFile(@PathVariable Long id, @RequestBody File file) {
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        file.setFileId(id);
        fileService.update(file);

        Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
        file.add(selfLink);

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @GetMapping(value = "/upload", produces = "text/html")
    public String uploadFile() {
        return "FileUpload";
    }

    @PostMapping("/upload")
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

    @GetMapping(value = "/new", produces = "text/html")
    public String newFile(Model model) {
        MetaData metaData = new MetaData();
        File file = new File();
        file.setMetaData(metaData);

        model.addAttribute("file", file);
        return "CreateFile";
    }

    @PostMapping("/new")
    public String handleFileNew(File file, RedirectAttributes redirectAttributes) {

        if (file != null) {
            fileService.saveNew(file);
            redirectAttributes.addAttribute("id", file.getFileId()).addFlashAttribute("message",
                    "You successfully created " + file.getFileName() + "!");
            return "redirect:/files/{id}";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping(value = "{id:[\\d]+}/update", produces = "text/html")
    public String updateFile(@PathVariable Long id, Model model) {
        File file = fileService.getById(id);

        model.addAttribute("file", file);
        return "UpdateFile";
    }

    @PostMapping("{id:[\\d]+}/update")
    public String handleFileUpdate(@PathVariable Long id, File file, RedirectAttributes redirectAttributes) {

        if (file != null) {
            fileService.update(file);
            redirectAttributes.addAttribute("id", file.getFileId()).addFlashAttribute("message",
                    "You successfully updated " + file.getFileName() + "!");
            return "redirect:/files/{id}";
        } else {
            return "redirect:/";
        }
    }


    @DeleteMapping("/{id:[\\d]+}")
    @ResponseBody
    public HttpEntity<File> deleteFile(@PathVariable Long id) {
        File file = fileService.getById(id);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fileService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
