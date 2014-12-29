package com.geoc.app.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.geoc.app.model.Message;
import com.geoc.app.repository.GCMessageRepository;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository
public class MessageRepositoryImpl implements GCMessageRepository{

	  @Autowired
	  private MongoTemplate mongoTemplate;

	public Map<String,List<Message>> findByToGroupByFrom(String to) {
	   HashMap<String, List<Message>> result = new HashMap<String, List<Message>>();

	   List<DBObject> ops = new ArrayList<DBObject>();
	   
	   
	   //{ $group : { _id : "$from", messages: { $push: "$$ROOT" } } }


		Map<String, Object> documentMapPush = new HashMap<String, Object>();
		documentMapPush.put("$push","$$ROOT" );

		Map<String, Object> documentMapId = new HashMap<String, Object>();
		documentMapId.put("_id", "$from");
		documentMapId.put("messages", documentMapPush);
	   Map<String, Object> documentMap = new HashMap<String, Object>();
		documentMap.put("$group",documentMapId );
	   
	   ops.add(new BasicDBObject(documentMap));
	  AggregationOutput output = mongoTemplate.getCollection(mongoTemplate.getCollectionName(Message.class)).aggregate(ops);
	   /**
	    * db.g_conversations.aggregate([{ $group : { _id : "$from", messages: { $push: "$$ROOT" } } }])
	    */
	   for(DBObject msgs : output.results()){
		   
		   List<Message> msgList = new ArrayList<Message>();
		   BasicBSONList all = (BasicBSONList) msgs.get("messages");
		   for(int i=0;i<all.size();i++){
			   DBObject list = (DBObject) all.get(i);
			  msgList.add(new Message(list.get("message").toString(),(Date)list.get("at")));
		   }
			result.put(msgs.get("_id").toString(),msgList);
	   }
	   
	   return result;
	}


}
