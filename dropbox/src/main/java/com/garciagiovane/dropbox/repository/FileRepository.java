package com.garciagiovane.dropbox.repository;

import com.garciagiovane.dropbox.model.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<UserFile, String> {
    Page<UserFile> findByOriginalNameContaining(String fileName, Pageable pageable);
}
