package com.geoc.app.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.BasicBSONList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.geoc.app.model.Conversation;
import com.geoc.app.model.Message;
import com.geoc.app.model.Topic;
import com.geoc.app.repository.GCTopicRepository;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository
public class GCTopicRepositoryImpl implements GCTopicRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	private Log logger = LogFactory.getLog(this.getClass());

	public List<Topic> getTopicsParticipated(String maskedEmail) {
		Query query = new Query(Criteria.where("participants").is(maskedEmail));
		// logger.info(query.getQueryObject().toString());
		return mongoTemplate.find(query, Topic.class);
	}

	public Conversation addTopicInConversations(Message msg) {
		ArrayList<String> participants = new ArrayList<String>();
		participants.add(msg.getFrom());
		participants.add(msg.getTo());
		Query query = new Query(Criteria.where("title").is(msg.getTopic())
				.andOperator(Criteria.where("participants").all(participants)));
		Topic existing = mongoTemplate.findOne(query, Topic.class);
		// logger.info(query.getQueryObject().toString());
		Conversation toAdd = new Conversation(msg.getText(), msg.getPic(),
				msg.getAt());
		if (existing == null) {
			existing = new Topic(msg.getTopic(), msg.getTo());
			existing.setParticipants(new String[] { msg.getFrom(), msg.getTo() });
			ArrayList<Conversation> converse = new ArrayList<Conversation>();
			converse.add(toAdd);
			existing.setMessages(converse);
		} else {
			if (msg.getFrom().equals(existing.getOwner()) || isAllowedToMessage(msg.getFrom(), existing.getMessages()))
				existing.getMessages().add(toAdd);
			else
				return null;
		}
		mongoTemplate.save(existing);
		return toAdd;
	}

	private boolean isAllowedToMessage(String from, List<Conversation> msgs) {
		if (msgs == null || msgs.size() == 0) {
			return true;
		} else if (msgs.size() == 1) {
			return false;
		}
		return true;
	}

	@Deprecated
	public Map<String, List<Message>> findByToGroupByFrom(String to) {
		HashMap<String, List<Message>> result = new HashMap<String, List<Message>>();

		List<DBObject> ops = new ArrayList<DBObject>();

		// { $group : { _id : "$from", messages: { $push: "$$ROOT" } } }

		Map<String, Object> documentMapPush = new HashMap<String, Object>();
		documentMapPush.put("$push", "$$ROOT");

		Map<String, Object> documentMapId = new HashMap<String, Object>();
		documentMapId.put("_id", "$from");
		documentMapId.put("messages", documentMapPush);
		Map<String, Object> documentMap = new HashMap<String, Object>();
		documentMap.put("$group", documentMapId);

		ops.add(new BasicDBObject(documentMap));
		AggregationOutput output = mongoTemplate.getCollection(
				mongoTemplate.getCollectionName(Message.class)).aggregate(ops);
		/**
		 * db.g_conversations.aggregate([{ $group : { _id : "$from", messages: {
		 * $push: "$$ROOT" } } }])
		 */
		for (DBObject msgs : output.results()) {

			List<Message> msgList = new ArrayList<Message>();
			BasicBSONList all = (BasicBSONList) msgs.get("messages");
			for (int i = 0; i < all.size(); i++) {
				DBObject list = (DBObject) all.get(i);
				msgList.add(new Message(list.get("message").toString(),
						(Date) list.get("at")));
			}
			result.put(msgs.get("_id").toString(), msgList);
		}

		return result;
	}

}
