package com.shahpreetk.javaSpringBootBookLibrary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLibraryRepository extends JpaRepository<Book, Integer> {


}
