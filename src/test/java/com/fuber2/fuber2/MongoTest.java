package com.fuber2.fuber2;

import java.net.UnknownHostException;
import java.util.ArrayList;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


public class MongoTest {

  public static void main(String[] args) {
    try {
        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("mydb");

        // get a single collection
        DBCollection collection = db.getCollection("Taxi");
        JacksonDBCollection<Taxi, String> jacksonCollection = JacksonDBCollection.wrap(collection, Taxi.class, String.class);

        //Taxi taxi = jacksonCollection.findOne();
        //System.out.println(taxi);
        DBCursor<Taxi> cursor = jacksonCollection.find(new BasicDBObject("Taxi.licensePlate", "a1111123"));
        ArrayList<Taxi> taxis = new ArrayList<Taxi>();
        while(cursor.hasNext())
        {
        	taxis.add(cursor.next());
        }
        System.out.println(cursor.count());
        
        
        BasicDBObject doc = new BasicDBObject("licensePlate", "a55555").
                append("latitude", "10").
                append("longtitude", -5).
                append("isOccupied", false).
        		append("isAvailable", false).
                append("isPink", false);

        collection.insert(doc);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("Taxi.licensePlate", "a55555"+ Math.random());		// Find documents with Joe Smith in name field

        cursor = jacksonCollection.find(searchQuery);

        while (cursor.hasNext())
        {
        	System.out.println(cursor.next());
        }


    } catch (UnknownHostException e) {
        e.printStackTrace();
    } catch (MongoException e) {
        e.printStackTrace();
    }
  }

}