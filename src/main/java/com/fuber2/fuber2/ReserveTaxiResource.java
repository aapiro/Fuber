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
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
    public Response reserve(@PathParam("latitude") double latitude,@PathParam("longitude") double longitude,@PathParam("pink") boolean isPink) 
    {
    	int distanceRange = 10;
    	int max_tries = 3;
    	int tries =0;
    	//get all taxis in within 30 mile radius.Do it 3 tries and give up
    	while(tries < max_tries)
    	{
        	DBCursor cursor = taxiDao.getCollection().find(new BasicDBObject("latitude", new BasicDBObject("$gt", latitude-10).append("$lte", latitude+10)).append("longitude", new BasicDBObject("$gt", longitude-10).append("$lte", longitude+10)).append("isPink", isPink).append("isActive", true));
        	List<Taxi> taxis = new ArrayList<Taxi>();
            if (cursor.hasNext()) {
            	BasicDBObject obj = (BasicDBObject)cursor.next();
	        	taxis.add(new Taxi(obj.getString("licensePlate"),
	        			obj.getDouble("latitude"),obj.getDouble("longtitude"),
	        			obj.getBoolean("isPink"),obj.getBoolean("isOccupied"),
	        			obj.getBoolean("isActive")));
            }
    		if(taxis !=null && !taxis.isEmpty())
    		{
    			TaxiDistanceComparator c = new TaxiDistanceComparator(latitude, longitude);
    			Collections.sort(taxis,c);
    			//in the sortedcandidates go through list and try to reserve inorder 
    			//iterate remove first try to reserve .keep trying 
    			for(Taxi taxi : taxis) {
    				taxi.reserve();
    				taxiDao.save(taxi);
    				return Response.ok("[\"success\":\"Your reservation has beeb accepted, "+taxi.toString()+"]").build();
    			}
    		}else tries++;
    		}
    	return Response.ok("[\"success\":\"Your reservation has beeb rejected]").build();
    }
}
