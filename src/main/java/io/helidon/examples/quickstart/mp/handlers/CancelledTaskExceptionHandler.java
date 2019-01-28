package io.helidon.examples.quickstart.mp.handlers;

import io.helidon.examples.quickstart.mp.ErrorMessage;
import io.helidon.examples.quickstart.mp.exceptions.CancelledTaskException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CancelledTaskExceptionHandler implements ExceptionMapper<CancelledTaskException> {

    @Override
    public Response toResponse(final CancelledTaskException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
