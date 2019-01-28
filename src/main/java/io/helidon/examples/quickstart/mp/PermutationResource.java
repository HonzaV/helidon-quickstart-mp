/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy collectPermutations the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.examples.quickstart.mp;


import io.helidon.examples.quickstart.mp.exceptions.CancelledTaskException;
import io.helidon.examples.quickstart.mp.exceptions.EmptyListException;
import io.helidon.examples.quickstart.mp.exceptions.NoTasksException;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.ok;

/**
 * Reource for permutations example
 */
@Path("/permutations")
@RequestScoped
public class PermutationResource {

    @Resource
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static List<Future<ArrayList<List<Object>>>> futures = new LinkedList<>();
    private static Permutations permutations;
    private static final Logger LOGGER = Logger.getLogger(PermutationResource.class.getName());

    private static DecimalFormat twoDecimals = new DecimalFormat(".##");
    /**
     * resource to get permutations of a given list.
     * @param list of elements to be permutated
     * @return all possible permutations
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPermutations(@QueryParam("async") boolean async, List<Object> list) throws EmptyListException {
        LOGGER.info("Recieved request, async: " + async + " array: " + list.toString() );
        if (list == null) {
            throw new EmptyListException("The input list is empty.");
        }
        permutations = new Permutations(list);
        if (async) {
            Future<ArrayList<List<Object>>> task = executorService.submit(permutations);
            futures.add(task);

            JsonObject json = Json.createObjectBuilder().add("taskId", futures.indexOf(task)).build();
            return Response.accepted(json).build();

        } else {
            ArrayList<List<Object>> response = permutations.makePermutations(list);

            return ok(response).build();
        }
    }

    /**
     * get result or progress of async task
     *
     * @param futureTaskId obtained as a response of async parmutations call
     * @return result if task is done, otherwise current progress in percents
     */

    @Path("/progress/{futureTaskId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPermutationsProgress(@PathParam("futureTaskId") int futureTaskId) throws CancelledTaskException,
            NoTasksException {
        LOGGER.info("Recieved request on task " + futureTaskId + "state" );
        try {
            Future<ArrayList<List<Object>>> f = futures.get(futureTaskId);

            if (permutations != null && f != null) {
                if (futures.get(futureTaskId).isDone()) {
                    try {
                        ArrayList<List<Object>> response = f.get();
                        futures.remove(futureTaskId);

                        LOGGER.info("Task " + futureTaskId + "has been completed and removed");
                        return ok(response).build();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new CancelledTaskException("Task has been cancelled", e);
                    }
                } else {
                    JsonObject json = Json.createObjectBuilder().add("currentProgressInPercents",
                            twoDecimals.format(permutations.calcProgress())).build();

                    LOGGER.info("Task " + futureTaskId + " has not finished yet.");
                    return Response.status(Response.Status.OK).entity(json).build();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new NoTasksException("No tasks in list");
        }

        return null;
    }
}
