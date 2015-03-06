package com.fuber2.fuber2;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class FuberServiceTest {
	private static final TaxiDao dao = mock(TaxiDao.class);

    private final Taxi taxi = new Taxi("blah",20,30,true,false,true);
	@Before
    public void setup() {
        ArrayList<Taxi> taxis = new ArrayList<Taxi>();
    	taxis.add(taxi);
        when(dao.fetchAvailableTaxis(eq("blah"))).thenReturn(taxis);
        reset(dao);
    }

    @Test
    public void testTaxi() {
        assertThat(dao.save(taxi)).isEqualsToByComparingFields(dao.fetchAvailableTaxis("blah").get(0));
    }

}
