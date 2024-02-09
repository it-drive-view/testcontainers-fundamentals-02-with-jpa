package dev.danvega.danson.post;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post,Integer> {

    Optional<Post> findByTitle(String title);

}
