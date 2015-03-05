package com.fuber2.fuber2;

import java.util.ArrayList;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;



@Path("/fuber/taxi/get/{licensePlate}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/** 
 * The TaxiResource class implements a restful interface that
 * accepts booking for taxi .
 */
public class TaxiResource {
	private TaxiDao taxiDao;

    public TaxiResource(TaxiDao taxiDao) {
        this.taxiDao=taxiDao;
    }

    @GET
    public Response getTaxi(@PathParam("licensePlate") String licensePlate) {
    	
    	ArrayList taxis = taxiDao.fetchAvailableTaxis(licensePlate);
    	
        if(taxis.isEmpty())
        	return Response.status(Response.Status.NO_CONTENT).entity("[{\"status\":\"No Such taxi\"}]").build();
        
        return Response.status(Response.Status.ACCEPTED).entity("["+taxis.get(0).toString()+"]").build();
    }

}
