package com.hashtagplus.model.repo;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.MessageHashtag;

import java.util.List;


public interface  MessageHashtagRepositoryCustom {
    List<AggDao> aggregate(HtplUser user);

    List<MessageHashtag> getMessagesWithTopicAndHashtags(Hashtag topicHashtag, List<Hashtag> hashtags);
}
