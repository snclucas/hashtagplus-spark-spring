package com.hashtagplus.service;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.repo.HashtagRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;


    public List<Hashtag> getAllHashtags(Sort sort, int pageNumber, int limit) {
        Pageable request =
                new PageRequest(pageNumber - 1, limit, sort);
        return hashtagRepository.findAll(request).getContent();
    }



    public Hashtag saveHashtag(Hashtag hashtag) {
        Hashtag existingHashtag = hashtagRepository.findByHashtag(hashtag.getText());
        if(existingHashtag == null) {
            hashtag.id = new ObjectId();
            return hashtagRepository.save(hashtag);
        }
        else
            return existingHashtag;
    }

}