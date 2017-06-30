package com.alexboriskin.testapiassignment.controllers;

import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/files")
public class HtmlController {

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(produces = "text/html")
    public String getAllFilesHtml(Model model) {

        List<File> allFilesList = fileService.getAll();

        if (!allFilesList.isEmpty()) {
            for (File file : allFilesList) {
                Link selfLink = linkTo(methodOn(RestController.class).getFile(file.getFileId())).withSelfRel();
                file.add(selfLink);
            }
            model.addAttribute("fileList", allFilesList);
        }

        return "ListOfFiles";

    }

    @GetMapping(value = "/{id:[\\d]+}", produces = "text/html")
    public String getFileHtml(@PathVariable Long id, Model model) {
        File file = fileService.getById(id);

        if (file != null) {
            Link selfLink = linkTo(methodOn(RestController.class).getFile(file.getFileId())).withSelfRel();
            file.add(selfLink);
            model.addAttribute("file", file);
        }

        return "File";
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
        file.setUploaded(new Date());
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
    public String handleFileUpdate(@PathVariable Long id, File file) {

        if (file != null) {
            fileService.update(file);
            return "redirect:/files/{id}";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("{id:[\\d]+}/delete")
    public String handleFileDelete(@PathVariable Long id) {

        File file = fileService.getById(id);

        if (file != null) {
            fileService.deleteById(id);
        }

        return "redirect:/files";
    }
}
