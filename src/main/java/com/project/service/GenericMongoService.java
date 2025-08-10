package com.project.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenericMongoService {
    private final MongoClient client;

    public GenericMongoService(MongoClient client) {
        this.client = client;
    }

    public List<String> listDatabases() {
        return client.listDatabaseNames().into(new ArrayList<>());
    }

    public List<String> listCollections(String db) {
        MongoDatabase database = client.getDatabase(db);
        List<String> names = new ArrayList<>();
        for (String n : database.listCollectionNames()) names.add(n);
        return names;
    }

    public long count(String db, String coll, Document filter) {
        return getCollection(db, coll).countDocuments(filter != null ? filter : new Document());
    }

    public List<Document> find(String db, String coll,
                               Document filter, Document projection, Document sort,
                               int page, int size) {
        if (page < 1) page = 1;
        if (size < 1 || size > 500) size = 20; // 合理上限
        int skip = (page - 1) * size;

        var col = getCollection(db, coll);
        var cursor = col.find(filter != null ? filter : new Document());

        if (projection != null && !projection.isEmpty()) cursor.projection(projection);
        if (sort != null && !sort.isEmpty()) cursor.sort(sort);

        return cursor.skip(skip).limit(size).into(new ArrayList<>());
    }

    private MongoCollection<Document> getCollection(String db, String coll) {
        return client.getDatabase(db).getCollection(coll);
    }
}
