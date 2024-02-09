package dev.danvega.danson.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostRepository repository;

    public PostController(PostRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    List<Post> findAll() {
        return (List<Post>) repository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Post> findById(@PathVariable Integer id) {
        return Optional.ofNullable(repository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    Post save(@RequestBody @Valid Post post) {
        return repository.save(post);
    }

    @PutMapping("/{id}")
    Post update(@PathVariable Integer id, @RequestBody Post post) {
        Optional<Post> existing = repository.findById(id);
        if(existing.isPresent()) {
            Post updatedPost = new Post(existing.get().getId(),
                    existing.get().getUserId(),
                    post.getTitle(),
                    post.getBody(),
                    existing.get().getVersion());

            return repository.save(updatedPost);
        } else {
            throw new PostNotFoundException();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }

}
