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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.services.FileService;

@Controller
@RequestMapping(value = "/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping
    @ResponseBody
    public HttpEntity<List<File>> getAllFiles(@RequestParam(required=false, defaultValue="") String fileName) {
        
        List<File> allFilesList = null;
        
        if(fileName.equals("")) {
        
        allFilesList = fileService.getAll();

        if (!allFilesList.isEmpty()) {
            for (File file : allFilesList) {
                Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
                file.add(selfLink);
            }
        }
        return new ResponseEntity<List<File>>(allFilesList, HttpStatus.OK);
        } else {
            allFilesList = fileService.getByName(fileName);

            if (allFilesList.isEmpty()) {
                return new ResponseEntity<List<File>>(HttpStatus.NOT_FOUND);
            } else  {
                for (File file : allFilesList) {
                    Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
                    file.add(selfLink);
                }
            }
            return new ResponseEntity<List<File>>(allFilesList, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id:[\\d]+}", produces={"application/xml", "application/json"})
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
    
    @SuppressWarnings("deprecation")
    @GetMapping(value = "/{id:[\\d]+}", produces="text/html")
    public String getFileHtml(@PathVariable Long id,  Model model) {
        File file = fileService.getById(id);
        
        if (file != null) {
            Link selfLink = linkTo(methodOn(FileController.class).getFile(file.getFileId())).withSelfRel();
            file.add(selfLink);
            model.addAttribute("fileId", file.getFileId());
            model.addAttribute("fileName", file.getFileName());
            model.addAttribute("uploaded", file.getUploaded().toGMTString());
            model.addAttribute("metaDataId", file.getMetaData().getId());
            model.addAttribute("metaData1", file.getMetaData().getMetaData1());
            model.addAttribute("metaData2", file.getMetaData().getMetaData2());
            model.addAttribute("metaData3", file.getMetaData().getMetaData3());
            model.addAttribute("selfLink", file.getId().getHref());
        }
        
        return "FileUploaded";
    }
    
    @PutMapping("/{id:[\\d]+}")
    @ResponseBody
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

    @GetMapping("/upload")
    public String uploadFile(Model model) {
        model.addAttribute("name", "Alex");
        return "FileUpload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        File uploadedFile = fileService.processUploadedFile(file);

        if (uploadedFile != null) {
            redirectAttributes.addAttribute("id", uploadedFile.getFileId())
                              .addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");

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
            return new ResponseEntity<File>(HttpStatus.NOT_FOUND);
        }
        fileService.deleteById(id);
        return new ResponseEntity<File>(HttpStatus.NO_CONTENT);
    }

}
