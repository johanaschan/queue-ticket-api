package se.jaitco.queueticketapi.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by johanaschan on 2016-10-24.
 */
public class SecurityContextHolderServiceTest {

    private final SecurityContextHolderService classUnderTest = new SecurityContextHolderService();

    @Mock
    private Authentication authentication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAuthentication() {
        Authentication authentication = classUnderTest.getAuthentication();

        Assert.assertThat(authentication, is(nullValue()));
    }

    @Test
    public void testSetAuthentication() {
        classUnderTest.setAuthentication(authentication);

        Assert.assertThat(classUnderTest.getAuthentication(), is(authentication));
    }

}