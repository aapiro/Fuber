package com.fuber2.fuber2;

import java.util.ArrayList;

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
		
		BasicDBObject oldDoc = new BasicDBObject("licensePlate", taxi.getLicensePlate()).append("isActive", true);
		BasicDBObject doc = new BasicDBObject("licensePlate", taxi.getLicensePlate())
        .append("latitude", taxi.getLatitude())
        .append("longitude", taxi.getLongitude())
        .append("isPink",taxi.isPink())
        .append("isOccupied",taxi.isOccupied())
        .append("isActive",taxi.isActive());
		System.out.println(doc.toString());
		if(col.findOne(oldDoc) != null)
			col.update(oldDoc, doc);
		else
			col.insert(doc);
		return taxi;
	}
	
    public Taxi update(Taxi taxi) {
		
		BasicDBObject oldDoc = new BasicDBObject("licensePlate", taxi.getLicensePlate()).append("isActive", true);
		BasicDBObject doc = new BasicDBObject("licensePlate", taxi.getLicensePlate())
        .append("latitude", taxi.getLatitude())
        .append("longitude", taxi.getLongitude())
        .append("isPink",taxi.isPink())
        .append("isOccupied",taxi.isOccupied())
        .append("isActive",taxi.isActive());
		System.out.println(doc.toString());
		col.update(oldDoc, doc);
		
		return taxi;
	}


	public ArrayList<Taxi> fetchAvailableTaxis(String licensePlate) {
		ArrayList<Taxi> taxis = new ArrayList<Taxi>();
		BasicDBObject query = new BasicDBObject("licensePlate", licensePlate).append("isActive", true);
    	DBCursor cursor = col.find(query);
    	
        ResourceHelper.notFoundIfNull(cursor);
        
        
        try{
	        while(cursor.hasNext())
	        {
	        	BasicDBObject obj = (BasicDBObject)cursor.next();
	        	taxis.add(new Taxi(obj.getString("licensePlate"),
	        			obj.getDouble("latitude"),obj.getDouble("longtitude"),
	        			obj.getBoolean("isPink"),obj.getBoolean("isOccupied"),
	        			obj.getBoolean("isActive")));
	        }
    	} finally {
    	   cursor.close();
    	}
		return taxis;
	}

	
	public Object fetchActiveTaxiByLocation(int latitude, int longitude,
			boolean isPink) {
		return col.find(new BasicDBObject("latitude", new BasicDBObject("$gt", latitude-10).append("$lte", latitude+10)).append("longitude", new BasicDBObject("$gt", longitude-10).append("$lte", longitude+10)).append("isPink", isPink).append("isActive", true));
    	
		
	}
}
