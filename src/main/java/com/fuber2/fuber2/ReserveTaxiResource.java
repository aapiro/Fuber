package com.fuber2.fuber2;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sun.jersey.api.core.InjectParam;

@Path("/fuber/taxi/reserve/{latitude}/{longitude}/{isPink}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/** 
 * The TaxiResource class implements a restful interface that
 * accepts booking for taxi .
 * TODO move all Db functions to DAO
 */
public class ReserveTaxiResource {
	private TaxiDao taxiDao;

    public ReserveTaxiResource(TaxiDao taxiDao) {
        this.taxiDao=taxiDao;
    }

    @SuppressWarnings("unchecked")
	@GET
    public Response reserve(@PathParam("latitude") double latitude,@PathParam("longitude") double longitude,@PathParam("isPink") boolean isPink) 
    {
    	int distanceRange = 10;
    	int max_tries = 3;
    	int tries =0;
    	//get all taxis in within 30 mile square.Do it 3 tries and give up
    	while(tries < max_tries)
    	{
    		BasicDBObject andQuery = new BasicDBObject();
    		  List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    		  obj.add(new BasicDBObject("latitude", new BasicDBObject("$gt",latitude-distanceRange ).append("$lt", latitude+distanceRange)));
    		  obj.add(new BasicDBObject("longitude", new BasicDBObject("$gt",longitude-distanceRange ).append("$lt", longitude+distanceRange)));
    		  obj.add(new BasicDBObject("isPink", isPink));
    		  //TODO not removing inactive and occupied taxis
    		  //TODO negative coordinate test
    		  //obj.add(new BasicDBObject("isActive", true));
    		  //obj.add(new BasicDBObject("isOccupied", false));
    		  andQuery.put("$and", obj);
    		  System.out.println(andQuery.toString());
    		
    		DBCursor cursor = taxiDao.getCollection().find(andQuery);

        	
        	List<Taxi> taxis = new ArrayList<Taxi>();
            if (cursor.hasNext()) {
            	BasicDBObject obj1=(BasicDBObject)cursor.next();
            	System.out.println(obj1);
	        	taxis.add(new Taxi(obj1.getString("licensePlate"),
	        			obj1.getDouble("latitude"),
	        			obj1.getDouble("longitude"),
	        			obj1.getBoolean("isPink"),
	        			obj1.getBoolean("isOccupied"),
	        			obj1.getBoolean("isActive")));
            }
    		if(taxis !=null && !taxis.isEmpty())
    		{
    			TaxiDistanceComparator c = new TaxiDistanceComparator(latitude, longitude);
    			Collections.sort(taxis,c);
    			//in the sortedcandidates go through list and try to reserve inorder 
    			//iterate remove first try to reserve .keep trying 
    			for(Taxi taxi : taxis) {
    				cursor = taxiDao.getCollection().find(new BasicDBObject("licensePlate", taxi.getLicensePlate()).append("isOccupied", false));
    	            if (cursor.hasNext()) {
    	            	taxi.reserve();
    	            	taxiDao.save(taxi);
    	            	return Response.ok("[\"status\":\"Your reservation was successful, "+taxi.toString()+"]").build();
    	            }
    				
    			}
    		}
    		tries++;
    	}
    
        	return Response.status(Response.Status.UNAUTHORIZED).entity("[{\"status\":\"Your reservation could not be made\"}]").build();
       
    }
}
