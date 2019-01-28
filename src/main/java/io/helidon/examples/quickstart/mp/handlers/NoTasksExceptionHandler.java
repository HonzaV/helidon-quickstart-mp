package io.helidon.examples.quickstart.mp.handlers;

import io.helidon.examples.quickstart.mp.ErrorMessage;
import io.helidon.examples.quickstart.mp.exceptions.NoTasksException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NoTasksExceptionHandler implements ExceptionMapper<NoTasksException> {

    @Override
    public Response toResponse(NoTasksException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
