package io.helidon.examples.quickstart.mp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PermutationsTest {

    private static int FACTORIAL = 24;
    private static double CURRENT_PERMUTATIONS = 10.0;
    private List<Object> requestList = new ArrayList<>();

    @InjectMocks
    Permutations permutations = new Permutations(prepareList());

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        Field field = permutations.getClass().getDeclaredField("currentPermutations");
        field.setAccessible(true);
        field.setDouble(permutations, CURRENT_PERMUTATIONS);
    }

    @Test
    public void getPermutations() {
        ArrayList<List<Object>> result = permutations.makePermutations(prepareList());

        assertEquals(FACTORIAL, result.size());
    }

    @Test
    public void testCalcProgress() {
        double result = permutations.calcProgress();
        System.out.println(result);

        assertTrue(result != 0.0);
    }

    private List<Object> prepareList() {
        requestList.clear();
        requestList.add(1);
        requestList.add(2);
        requestList.add(3);
        requestList.add(4);

        return requestList;
    }
}
