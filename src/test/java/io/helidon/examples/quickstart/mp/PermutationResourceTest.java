package io.helidon.examples.quickstart.mp;

import io.helidon.examples.quickstart.mp.exceptions.CancelledTaskException;
import io.helidon.examples.quickstart.mp.exceptions.EmptyListException;
import io.helidon.examples.quickstart.mp.exceptions.NoTasksException;
import io.netty.handler.codec.http.HttpResponseStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PermutationResourceTest {
    private List<Object> requestList;

    @Mock
    Permutations permutations;

    @InjectMocks
    PermutationResource permutationResource;

    @Before
    public void intialize() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, EmptyListException {
        requestList = new ArrayList<>();

        MockitoAnnotations.initMocks(this);
        Field field = permutationResource.getClass().getDeclaredField("futures");
        field.setAccessible(true);
        field.set(permutationResource, prepareFutures());
    }

    @Test
    public void testPermutation() throws EmptyListException {
        Response response = permutationResource.getPermutations(false, prepareIntList());
        assertEquals(HttpResponseStatus.OK.code(), response.getStatus());

        response = permutationResource.getPermutations(false, prepareStringList());
        assertEquals(HttpResponseStatus.OK.code(), response.getStatus());

        response = permutationResource.getPermutations(true, prepareIntList());
        assertEquals(HttpResponseStatus.ACCEPTED.code(), response.getStatus());

        response = permutationResource.getPermutations(true, prepareStringList());
        assertEquals(HttpResponseStatus.ACCEPTED.code(), response.getStatus());
    }

    @Test(expected = EmptyListException.class)
    public void testPermutationsException() throws EmptyListException {
        permutationResource.getPermutations(false, null);
    }

    @Test
    public void testProgress() throws NoTasksException, CancelledTaskException {
        Response response = permutationResource.getPermutationsProgress(0);

        assertEquals(HttpResponseStatus.OK.code(), response.getStatus());
    }

    private List<Object> prepareIntList() {
        requestList.clear();
        requestList.add(1);
        requestList.add(2);
        requestList.add(3);
        requestList.add(4);

        return requestList;
    }

    private List<Object> prepareStringList() {
        requestList.clear();
        requestList.add("first");
        requestList.add("second");
        requestList.add("third");
        requestList.add("fourth");

        return requestList;
    }

    private List<Future<ArrayList<List<Object>>>> prepareFutures() throws EmptyListException {
        List<Future<ArrayList<List<Object>>>> futures = new LinkedList<>();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        permutationResource.getPermutations(true, prepareStringList());
        Future<ArrayList<List<Object>>> task = exec.submit(permutations);
        futures.add(task);

        return futures;
    }
}
