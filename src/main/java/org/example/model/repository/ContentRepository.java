package org.example.model.repository;

import jakarta.annotation.PostConstruct;
import org.example.model.Content;
import org.example.model.Enum.Status;
import org.example.model.Enum.Type;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ContentRepository {
    private final List<Content> contentList = new ArrayList<>();

    public ContentRepository(){
    }

    public List<Content> findAll(){
        return contentList;
    }
    public Optional<Content> findById(Integer id){
        return contentList.stream().filter(c -> c.id().equals(id)).findFirst();
    }

    public Content save(Content content){
        contentList.removeIf(c -> c.id().equals(content.id()));
        contentList.add(content);
        return content;
    }

    @PostConstruct
    public void init(){
        Content content1 = new Content(1, "My first post",
                "The thing about the first time is that it is not always the best",
                Status.IDEA,
                Type.ARTICLE,
                LocalDateTime.now(),
                null,
                "");

        contentList.add(content1);
    }

    public boolean existById(Integer id) {
        return contentList.stream().filter(c -> c.id().equals(id)).count() == 1;
    }

    public void delete(Integer id) {
        contentList.removeIf(c -> c.id().equals(id));
    }
}
