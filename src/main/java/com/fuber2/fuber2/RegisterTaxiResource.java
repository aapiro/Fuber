package com.fuber2.fuber2;





import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Path("/fuber/taxi/register/{licensePlate}/{latitude}/{longitude}/{isPink}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/** 
 * The TaxiResource class implements a restful interface that
 * accepts booking for taxi .
 */
public class RegisterTaxiResource {
	
	private DB db;
	private TaxiDao taxiDao = new TaxiDao();

    public RegisterTaxiResource(DB db) {
        this.db = db;
    }
    
    //if already registered update to new location else insert
    @GET
    public Response registerTaxi(@PathParam("licensePlate") String licensePlate,@PathParam("latitude") double latitude,@PathParam("longitude") double longitude,@PathParam("isPink") boolean isPink) 
    {

        DBCollection col = db.getCollection("Taxi");
    	DBCursor cursor = col.find(new BasicDBObject("licensePlate", licensePlate).append("isActive", true));
    	Taxi taxi=new Taxi(licensePlate,latitude,longitude,isPink,false,true);
        ResourceHelper.notFoundIfNull(cursor);
        try{
	        while(cursor.hasNext())
	        {
	        	
	        	BasicDBObject obj = (BasicDBObject)cursor.next();
	        	if(obj.getDouble("latitude") != latitude || obj.getDouble("longitude")!= longitude)
	        	{
	        		taxi = new Taxi(obj.getString("licensePlate"),
	        			latitude,longitude,
	        			obj.getBoolean("isPink"),false,
	        			true);
	        	}
	        	
	        }
	        
	        taxiDao.save(taxi,col);
    	} finally {
    	   cursor.close();
    	}	
        
    	return Response.ok("success").entity("["+taxi.toString()+"]").build();
    }
    
    

}
