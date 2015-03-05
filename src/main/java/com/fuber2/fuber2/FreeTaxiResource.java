package com.fuber2.fuber2;



import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;


@Path("/fuber/taxi/free/{licenseplate}/{latitude}/{longtitude}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/** 
 * The TaxiResource class implements a restful interface that
 * accepts booking for taxi .
 */
public class FreeTaxiResource {
	
	private DB db;
	private TaxiDao taxiDao = new TaxiDao();

    public FreeTaxiResource(DB db) {
        this.db = db;
    }
    @GET
    public Response free(@PathParam("licenseplate") String licensePlate,@PathParam("latitude") double latitude,@PathParam("longtitude") double longtitude)
    {
    	DBCollection col = db.getCollection("Taxi");
    	DBCursor cursor = col.find(new BasicDBObject("licensePlate", licensePlate));
    	Taxi taxi=null;
        ResourceHelper.notFoundIfNull(cursor);
        try{
	        while(cursor.hasNext())
	        {
	        	BasicDBObject obj = (BasicDBObject)cursor.next();
	        	taxi = new Taxi(obj.getString("licensePlate"),
	        			obj.getDouble("latitude"),obj.getDouble("longtitude"),
	        			obj.getBoolean("isPink"),obj.getBoolean("isOccupied"),
	        			obj.getBoolean("isActive"));
	        	taxi.free(latitude, longtitude);
	        	taxiDao.save(taxi,col);
	        }
    	} finally {
    	   cursor.close();
    	}	
        
    	return Response.ok("success").entity("["+taxi.toString()+"]").build();
    }
    
}
