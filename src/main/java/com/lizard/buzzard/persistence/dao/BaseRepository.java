package com.lizard.buzzard.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T,ID> {

    default Stream<T> streamAll() {
        return findAll().stream();
    }

    default T matchToPredicate(Predicate<T> p) {
        Optional<T> o = streamAll().filter(p).findFirst();
        if(o.isPresent()) {
            return o.get();
        } else {
            return null;
        }
    }
}
