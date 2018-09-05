package com.mongodb.stitch.starter_app.model.objects;

/*
 * Copyright 2018-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import org.bson.BSON;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

/**
 * Friend represents a friend that would exist in a FriendList. MongoDB POJO annotations are included
 * so Friend objects can be directly inserted and retrieved from a MongoDB collection instance.
 * See http://mongodb.github.io/mongo-java-driver/3.6/driver/getting-started/quick-start-pojo/
 */

public class Friend {
  // ADAM: This key is no longer neceessary since the @BsonId annotation is used to specify the _id
  //       field of an object.
//  public static final String ID_KEY = "_id";

  public static final String OWNER_KEY = "owner_id";
  public static final String FRIEND_NAME = "name";
  public static final String DATE_ADDED = "date_added";

  @NonNull
  private ObjectId id;

  @Nullable
  private ObjectId ownerId;
  private String friendName;
  private String dateAdded;

  // ADAM: It might be worth adding a comment here that the @BsonCreator annotation tells the codec
  //       generator that this is the constructor that the remote MongoDB and local MongoDB
  //       services use to construct a Friend object from BSON that it gets from a database
  //       instance
  @BsonCreator
  public Friend(
          @NonNull @BsonId final ObjectId id,
          @BsonProperty(OWNER_KEY) final ObjectId ownerId,
          @BsonProperty(FRIEND_NAME) final String friendName,
          @BsonProperty(DATE_ADDED) final String dateAdded) {

    this.id = id;
    this.ownerId = ownerId;
    this.dateAdded = dateAdded;
    this.friendName = friendName;
  }

  // ADAM: Now that this isn't a task list, you don't need this constructor. People who want to
  //       extend this object can define their own constructors
//  public Friend(@NonNull final String task) {
//    this.id = ObjectId.get();
//  }

  // ADAM: Might also be worth adding a comment here to specify that these are the getters that
  //       the codec generator will use to generate a BSON document out of a Friend object when
  //       writing to a database instance.
  // Getters
  @NonNull
  @BsonId
  public ObjectId getId() {
    return id;
  }

  @Nullable
  @BsonProperty(OWNER_KEY)
  public ObjectId getOwnerId() {
    return ownerId;
  }

  @Nullable
  @BsonProperty(FRIEND_NAME)
  public String getFriendName() {
    return friendName;
  }

  @Nullable
  @BsonProperty(DATE_ADDED)
  public String getDateAdded() {
    return dateAdded;
  }

  // ADAM: This isn't necessary, and including it actually confuses the POJO codec
  // Setters
//  @BsonIgnore
//  public void setDocumentId(final ObjectId docId) {
//    this.id = docId;
//  }

  // ADAM: This codec is not necessary. The POJO codec which is derived from the annotations
  //       makes manually specifying a codec unnecessary. The reason you had to use this is
  //       because you weren't merging the pojo codec with the default codec registry properly.
  //       (I don't blame you because the Java driver people did not document the codec system
  //       very well. See http://rosslawley.co.uk/mongodb-pojo-support/ for more info)
//  public static class Codec implements CollectibleCodec<Friend> {
//
//    @Override
//    public Friend generateIdIfAbsentFromDocument(final Friend document) {
//      return document;
//    }
//
//    @Override
//    public boolean documentHasId(final Friend document) {
//      return document.getId() == null;
//    }
//
//    @Override
//
//    public BsonValue getDocumentId(final Friend document) {
//      return new BsonString(document.getId().toHexString());
//    }
//
//    @Override
//    public Friend decode(final BsonReader reader, final DecoderContext decoderContext) {
//      final Document document = (new DocumentCodec()).decode(reader, decoderContext);
//      return new Friend(document.getObjectId("_id"),
//              document.getObjectId("owner_id"),
//              document.getString("name"),
//              document.getString("date_added"));
//    }
//
//    @Override
//    public void encode(
//            final BsonWriter writer,
//            final Friend value,
//            final EncoderContext encoderContext
//    ) {
//      final Document document = new Document();
//      if (value.getId() != null) {
//        document.put("_id", value.getId());
//      }
//        document.put("owner_id", value.getOwnerId());
//        document.put("name", value.getFriendName());
//        document.put("date_added", value.getDateAdded());
//
//      (new DocumentCodec()).encode(writer, document, encoderContext);
//    }
//
//    @Override
//    public Class<Friend> getEncoderClass() {
//      return Friend.class;
//    }
//  }
}