package com.garciagiovane.dropbox.repository;

import com.garciagiovane.dropbox.model.UserFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<UserFile, String> {
    List<UserFile> findByIdOwner(String idOwner);
}
