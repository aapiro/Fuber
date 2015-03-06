package com.fuber2.fuber2;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fuber/taxi/unregister/{licenseplate}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/** 
 * The UnregisterTaxiResource class implements a restful interface that
 * removes the active taxi .
 */
public class UnRegisterTaxiResource {
	private TaxiDao taxiDao;

	public UnRegisterTaxiResource(TaxiDao taxiDao) {
		this.taxiDao = taxiDao;
	}

	@GET
	public Response unregisterTaxi(
			@PathParam("licenseplate") String licensePlate) {
		Taxi taxi = taxiDao.unregisterTaxi(licensePlate);
		return Response.ok("success")
				.entity("{\"data\":" + taxi.toString() + "}").build();
	}

}
