package dmit2015.restclient;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

public class ForbiddenResponseMapper implements ResponseExceptionMapper<ForbiddenException> {
    @Override
    public ForbiddenException toThrowable(Response response) {
        return new ForbiddenException("You are signed in, but you do not have permission to perform this operation.");
    }

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        return status == Response.Status.FORBIDDEN.getStatusCode();
    }
}