package se.jaitco.queueticketapi.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class UnauthorizedEntryPointTest {

    private final UnauthorizedEntryPoint classUnderTest = new UnauthorizedEntryPoint();

    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    public void testCommence() throws Exception {
        classUnderTest.commence(null, httpServletResponse, null);

        Mockito.verify(httpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

}