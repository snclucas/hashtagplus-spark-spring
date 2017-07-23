package com.hashtagplus.model.repo;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.MessageHashtag;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageHashtagRepository extends MongoRepository<MessageHashtag, String> {

    //@Query(value = "{'message_id': ?0}")
    List<Message> findByMessage_id(ObjectId message_id);

    //@Query(value = "{'text': ?0}")
    List<Hashtag> findByHashtag_id(ObjectId hashtag_id);

    @Override
    @Query(value = "{'message': ?0}")
    void deleteAll();

    void deleteMessageHashtagsByMessage(Message message);
}