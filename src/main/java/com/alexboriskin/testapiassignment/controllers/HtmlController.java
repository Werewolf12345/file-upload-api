package com.alexboriskin.testapiassignment.controllers;

import com.alexboriskin.testapiassignment.commands.FileForm;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

    @GetMapping(value = "/new", produces = "text/html")
    public String newFile(Model model) {

        FileForm fileForm = new FileForm();
        fileForm.setUploaded(new Date());

        model.addAttribute("fileForm", fileForm);
        return "CreateFile";
    }

    @GetMapping(value = "{id:[\\d]+}/update", produces = "text/html")
    public String updateFile(@PathVariable Long id, Model model) {
        File file = fileService.getById(id);

        model.addAttribute("file", file);
        return "UpdateFile";
    }

    @PostMapping("/new")
    public String handleFileNew(@Valid FileForm fileForm, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            return "CreateFile";
        }

        if (fileForm != null) {
            File savedFile = fileService.saveNew(fileForm);
            redirectAttributes.addAttribute("id", savedFile.getFileId()).addFlashAttribute("message",
                    "You successfully created " + savedFile.getFileName() + "!");
            return "redirect:/files/{id}";
        } else {
            return "redirect:/";
        }
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
