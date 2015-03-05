package com.fuber2.fuber2;

import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;


public class TaxiDao {
	private DB db;
	private DBCollection col;
	
	public TaxiDao() {}

    public TaxiDao(DB db) {
        this.db = db;
        col = db.getCollection("Taxi");
    }
    
    public DBCollection getCollection()
    {
    	return col;
    }
    
    public Taxi save(Taxi taxi)
    {
    	return save(taxi,col);
    }

	public Taxi save(Taxi taxi,DBCollection col) {
		
		BasicDBObject doc = new BasicDBObject("licencePlate", taxi.getLicensePlate())
        .append("latitude", taxi.getLatitude())
        .append("longitude", taxi.getLongitude())
        .append("isPink",taxi.isPink())
        .append("isOccupied",taxi.isOccupied())
        .append("isAvailable",taxi.isActive());
		System.out.println(doc.toString());
		col.insert(doc);
		return taxi;
	}


	public Object fetchTaxi(String licensePlate) {
		return col.find(new BasicDBObject("licensePlate", licensePlate));
    	
	}

	
	public Object fetchActiveTaxiByLocation(int latitude, int longitude,
			boolean isPink) {
		return col.find(new BasicDBObject("latitude", new BasicDBObject("$gt", latitude-10).append("$lte", latitude+10)).append("longitude", new BasicDBObject("$gt", longitude-10).append("$lte", longitude+10)).append("isPink", isPink).append("isActive", true));
    	
		
	}
}
