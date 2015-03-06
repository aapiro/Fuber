package com.fuber2.fuber2;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fuber/taxi/free/{licenseplate}/{latitude}/{longitude}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/** 
 * The TaxiResource class implements a restful interface that
 * frees the taxi and update location for use by next customer.
 */
public class FreeTaxiResource {
	private TaxiDao taxiDao;

	public FreeTaxiResource(TaxiDao taxiDao) {
		this.taxiDao = taxiDao;
	}

	@GET
	public Response free(@PathParam("licenseplate") String licensePlate,
			@PathParam("latitude") double latitude,
			@PathParam("longitude") double longitude) {
		Taxi taxi = taxiDao.freeTaxi(licensePlate, latitude, longitude);

		if (taxi == null)
			return Response.status(Response.Status.NO_CONTENT)
					.entity("[{\"status\":\"Could not free taxi\"}]").build();

		return Response.ok("success").entity("[" + taxi.toString() + "]")
				.build();

	}

}
