package com.fuber2.fuber2;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Dimple Joseph
 *
 */
@Path("/fuber/taxi/register/{licensePlate}/{latitude}/{longitude}/{isPink}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/** 
 * The TaxiResource class implements a restful interface that
 * accepts booking for taxi .
 */
public class RegisterTaxiResource {
	private TaxiDao taxiDao;

	public RegisterTaxiResource(TaxiDao taxiDao) {
		this.taxiDao = taxiDao;
	}

	// if already registered update to new location else insert
	@GET
	public Response registerTaxi(
			@PathParam("licensePlate") String licensePlate,
			@PathParam("latitude") double latitude,
			@PathParam("longitude") double longitude,
			@PathParam("isPink") boolean isPink) {

		Taxi taxi = taxiDao.registerTaxi(licensePlate, latitude, longitude,
				isPink);
		if (taxi == null)
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("[{\"status\":\"Sorry could not register at this time\"}]")
					.build();

		return Response.ok("success").entity("[" + taxi.toString() + "]")
				.build();
	}

}
