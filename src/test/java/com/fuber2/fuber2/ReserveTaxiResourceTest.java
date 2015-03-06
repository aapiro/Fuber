package com.fuber2.fuber2;

import javax.ws.rs.core.Response;

import io.dropwizard.testing.junit.ResourceTestRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//http://localhost:8080/fuber/taxi/register/a890/2/3/true
public class ReserveTaxiResourceTest{

    private static final TaxiDao dao = mock(TaxiDao.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ReserveTaxiResource(dao))
            .build();

    private final Taxi taxi = new Taxi("blah",10,20,true,false,true);

    @Before
    public void setup() {
        when(dao.fetchActiveTaxiByLocation(10,20,true)).thenReturn(taxi);
        reset(dao);
    }
    

    @Test
    public void testReserveTaxi() {
    	
    	//System.out.println(resources.client().resource("/fuber/taxi/reserve/10/20/true").get(Taxi.class));
        assertThat(resources.client().resource("/fuber/taxi/reserve/10/20/true")
        		.get(Taxi.class))
                .isEqualTo(taxi);
        verify(dao).fetchActiveTaxiByLocation(10,20,true);
        
        //assert even if you provide location as 240 320 values are corrected to +-180,+-90,
        //assertThat(resources.client().resource("/fuber/taxi/reserve/240/320/true")
        		//.get(Taxi.class))
                //.has("\"Latitude\":180");
        
        //assert reserved cab not available with a repeat request till its free
        //assertThat(resources.client().resource("/fuber/taxi/reserve/10/20/true")
        		//.get(Taxi.class))
                //.has("not available");
        
        //If there are no taxis available, you reject the customers request.
        
        //insert 10,20 and 20,30 .reserve at 10,10 should return 10,20
        resources.client().resource("/fuber/taxi/register/blah1/10/20/true").get(Taxi.class);
        resources.client().resource("/fuber/taxi/register/blah2/10/20/true").get(Taxi.class);
        Taxi taxi1 = new Taxi("blah1",10,20,true,true,true);
        assertThat(resources.client().resource("/fuber/taxi/reserve/10/20/true")
        		.get(Taxi.class))
                .isEqualTo(taxi1);
        
        //insert -5,-5 and -15,-30 .reserve at -1,-1 should return -5,-5
        resources.client().resource("/fuber/taxi/register/blah3/-5/-5/true").get(Taxi.class);
        resources.client().resource("/fuber/taxi/register/blah4/-15/-30/true").get(Taxi.class);
        Taxi taxi2 = new Taxi("blah3",-5,-5,true,true,true);
        assertThat(resources.client().resource("/fuber/taxi/reserve/-1/-1/true")
        		.get(Taxi.class))
                .isEqualTo(taxi2);
        
      //insert 177,88 .reserve at 200,97 should return 177,88
        resources.client().resource("/fuber/taxi/register/blah4/-177/88/true").get(Taxi.class);
        Taxi taxi3 = new Taxi("blah3",177,88,true,true,true);
        assertThat(resources.client().resource("/fuber/taxi/reserve/200/97/true")
        		.get(Taxi.class))
                .isEqualTo(taxi3);
    }
}
