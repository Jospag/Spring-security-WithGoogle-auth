package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Content;
import org.example.model.repository.ContentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentRepository repository;

    public ContentController(ContentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public List<Content> findAllContent(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Content findById(@PathVariable Integer id){
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found")
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Content create (@Valid @RequestBody Content content){
        return repository.save(content);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update/{id}")
    public Content update(@Valid @RequestBody Content content, @PathVariable Integer id){
        if(!repository.existById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!");
        }
        return repository.save(content);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        repository.delete(id);
    }

}

