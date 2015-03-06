package com.fuber2.fuber2;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class FreeTaxiResourceTest {
private static final TaxiDao dao = mock(TaxiDao.class);
    
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TaxiResource(dao))
            .build();

    private final Taxi taxi = new Taxi("blah",20,30,true,true,true);
    private final Taxi freeTaxi = new Taxi("blah",10,20,true,false,true);

    @Before
    public void setup() {
        ArrayList<Taxi> taxis = new ArrayList<Taxi>();
    	taxis.add(taxi);
        when(dao.freeTaxi("blah",10,20)).thenReturn(freeTaxi);
        reset(dao);
    }

    @Test
    public void testFreeTaxi() {
        assertThat(resources.client().resource("/fuber/taxi/free/blah/10/20").get(Taxi.class))
                .isEqualTo(freeTaxi);
        verify(dao).freeTaxi("blah",10,20);
    }

}
