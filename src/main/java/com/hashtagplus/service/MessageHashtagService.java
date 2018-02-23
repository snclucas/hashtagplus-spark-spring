package com.hashtagplus.service;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.model.repo.MessageHashtagRepository;
import com.hashtagplus.model.util.HTMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageHashtagService {

  @Autowired
  private MessageHashtagRepository messageHashtagRepository;

  @Autowired
  private MessageService messageService;

  @Autowired
  private HashtagService hashtagService;

  public MessageHashtag saveMessageHashtag(Message message, Hashtag hashtag, HtplUser user) {
    MessageHashtag mh = new MessageHashtag(message, hashtag, user.getUsername());
    return messageHashtagRepository.save(mh);
  }


  public Message saveMessageWithHashtags(MessageFormData messageFormData, HtplUser user) {
    // XXX Message message = new Message(messageFormData.getTitle(), messageFormData.getText(), user.getUsername());
    Message message = new Message(messageFormData.getTitle(), messageFormData.getText(), user.getUsername());
    String messageText = messageFormData.getText();

    HTMLUtils.TextComponents textComponents = HTMLUtils.processText(messageText);

    List<MediaUrl> urls = HTMLUtils.extractMediaURLS(textComponents.urls);
    if(urls.size() > 0) {
      message.setContentType(urls.get(0).getContentType());
    }

    // Add all found media sources
    urls.stream()
            .filter(mu -> HTMLUtils.isMediaUrl(mu.getContentType()))
            .forEach(message::addMediaUrl);

    message.hasText = textComponents.hasText;

    List<Hashtag> hashtagList = new ArrayList<>();

    String topic = messageFormData.getTopic();

    List<String> hashtags = textComponents.hashtags;

    if(!topic.equals("")) {
      hashtags.add(topic);
    }

    boolean msgPrivate = hashtags.stream()
            .anyMatch(h -> h.equalsIgnoreCase("private"));

    if(msgPrivate) {
      message.setPrivacy("private");
    }

    for (String hashtag : textComponents.hashtags) {
      if (!hashtag.equals(" ")) {
        hashtag = hashtag.trim();
        if(hashtag.startsWith("#")) {
          hashtag = hashtag.substring(1,hashtag.length());
        }
        Hashtag hashtagToSave = new Hashtag(hashtag.replace(" ", " "));
        hashtagToSave = hashtagService.saveHashtag(hashtagToSave);
        hashtagList.add(hashtagToSave);
        saveMessageHashtag(message, hashtagToSave, user);
      }
    }
    message.setHashtags(hashtagList);
    return messageService.saveMessage(message);
  }

  public Page<MessageHashtag> getMessagesWithHashtag(String hashtagsText, HtplUser user, Sort sort, int pageNumber, int limit) {
    Pageable pageable =
            new PageRequest(pageNumber - 1, limit, sort);
   // hashtagsText = hashtagsText.replace(" ", "");
    List<String> hashTagsTextList = Arrays.asList(hashtagsText.split(","));

    List<Hashtag> hashtagList = hashTagsTextList.stream()
            .map(ht -> hashtagService.findHashtag(ht))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

   // Page<MessageHashtag> dd = messageHashtagRepository.findMessageHashtagsByHashtagIdIn(hashtagList, pageable);

    Page<MessageHashtag> dd = messageHashtagRepository.simon(hashtagList, user, pageable);
    return dd;

//    List<MessageHashtag> ddd = dd.getContent()
//            .stream()
//            //.filter(mh -> mh.getUser_id().equals(user.getId()))
//            .collect(Collectors.toList());
//
//    int start = pageable.getOffset();
//    int end = (start + pageable.getPageSize()) > ddd.size() ? ddd.size() : (start + pageable.getPageSize());
//    return new PageImpl<>(ddd.subList(start, end), pageable, ddd.size());
  }

  public List<MessageHashtag> getMessagesWithHashtags(List<String> hashtags) {
    return messageHashtagRepository.findByHashtagIn(hashtags);
  }

  public List<MessageHashtag> getHashtagsWithMessage(Message message) {
    return messageHashtagRepository.findByMessage(message);
  }

  public void deleteAllForMessage(Message message) {
    messageHashtagRepository.deleteMessageHashtagsByMessage(message);
  }

  public List<AggDao> aggregate(HtplUser user) {
    return messageHashtagRepository.aggregate(user);
  }


  public Page<MessageHashtag> getMessagesWithTopicAndHashtags(String topic, String hashtagsText, HtplUser user, Sort sort, int pageNumber, int limit) {
    Pageable request = new PageRequest(pageNumber - 1, limit, sort);

    hashtagsText = hashtagsText.replace(" ", "");

    Hashtag topicHashtag = hashtagService.findHashtag(topic);

    List<String> hashTagsTextList = hashtagsText.equals("") ? Collections.emptyList() : Arrays.asList(hashtagsText.split(","));

    List<Hashtag> hashtagList = hashTagsTextList.stream()
            .map(ht -> hashtagService.findHashtag(ht))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    return messageHashtagRepository.getMessagesWithTopicAndHashtags(topicHashtag, hashtagList, request);
  }

}