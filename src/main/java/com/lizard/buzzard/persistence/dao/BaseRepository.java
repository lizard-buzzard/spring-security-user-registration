package com.lizard.buzzard.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Spring Data JPA contains some built-in Repository implemented some common functions to work with database: findOne, findAll, save,...
 * NOTE: should be annotated win @NoRepositoryBean, otherwise gives an BeanCreationException (Error creating bean with name 'baseRepository')
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
        return o.isPresent() ? o.get() : null;
    }
}
