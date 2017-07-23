package com.hashtagplus.model.repo;

import com.hashtagplus.model.HtplUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserDetailsRepository extends MongoRepository<HtplUserDetails, String> {

    //@Query(value = "{'username': ?0}")
    HtplUserDetails findByUsername(String userName);

}