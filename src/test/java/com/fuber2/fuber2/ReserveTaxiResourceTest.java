package com.fuber2.fuber2;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
        assertThat(resources.client().resource("/fuber/taxi/reserve/10/20/true").get(Taxi.class))
                .isEqualTo(taxi);
        verify(dao).fetchActiveTaxiByLocation(10,20,true);
        
        //assert even if you provide location as 240 320 values are corrected to +-90,+-180
        //assertThat(resources.client().resource("/fuber/taxi/reserve/100/200/true").get(Taxi.class))
        //isEqualTo(taxi);
        
        //insert 10,20 and 20,30 .reserve at 10,10 should return 10,20
      //insert -5,-5 and -15,-30 .reserve at -1,-1 should return -5,-5
    }
}
