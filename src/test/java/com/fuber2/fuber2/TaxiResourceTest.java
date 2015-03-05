package com.fuber2.fuber2;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

//http://localhost:8080/fuber/taxi/register/a890/2/3/true
public class TaxiResourceTest{

    private static final TaxiDao dao = mock(TaxiDao.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TaxiResource(dao))
            .build();

    private final Taxi taxi = new Taxi("blah",20,30,true,false,true);

    @Before
    public void setup() {
        when(dao.fetchTaxi(eq("blah"))).thenReturn(taxi);
        reset(dao);
    }

    @Test
    public void testGetTaxi() {
        assertThat(resources.client().resource("/fuber/taxi/get/blah").get(Taxi.class))
                .isEqualTo(taxi);
        verify(dao).fetchTaxi("blah");
    }
}
