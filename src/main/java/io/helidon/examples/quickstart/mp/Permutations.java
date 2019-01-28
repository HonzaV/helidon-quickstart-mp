package io.helidon.examples.quickstart.mp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Permutations implements Callable<ArrayList<List<Object>>> {
    private double numberOfPermutations;
    private double currentPermutations;
    private ArrayList<List<Object>> listOfPermutedArrays;
    private List<Object> originalArray;

    public Permutations(final List<Object> originalArray) {
        this.originalArray = originalArray;
        listOfPermutedArrays = new ArrayList<>();
        numberOfPermutations = computePossiblePermutations(originalArray.size());
    }

    /**
     * method for sync requests
     *
     * @param array from request body
     * @return list of all permutations
     */

    public ArrayList<List<Object>> makePermutations(final List<Object> array) {
        collectPermutations(array)
             .map(s -> s.collect(toList()))
             .forEach(listOfPermutedArrays::add);

        return listOfPermutedArrays;
    }

    /**
     *
     * @return current progress
     */
    public double calcProgress() {
        double result = currentPermutations / numberOfPermutations;
        return result * 100;
    }

    /**
     *  method to get Callable for async request
     * @return result of all permutations
     */

    @Override
    public ArrayList<List<Object>> call() {
        collectPermutations(this.originalArray)
                .map(s -> s.collect(toList()))
                .forEach(listOfPermutedArrays::add
                );

        return listOfPermutedArrays;
    }

    private static long computePossiblePermutations(final int n) {
        if(n == 1 || n == 0) {
            return 1;
        }

        return (n) * computePossiblePermutations(n-1);
    }

    private static <T> List<T> permutationHelper(final long no,final LinkedList<T> in,final List<T> out) {

        if (in.isEmpty()) {
            return out;
        }

        long subFactorial = computePossiblePermutations(in.size() - 1);
        out.add(in.remove((int) (no / subFactorial)));

        return permutationHelper(no % subFactorial, in, out);
    }

    private <T> List<T> permutation(final long no,final List<T> items) {

        List<T> result = permutationHelper(
                no,
                new LinkedList<>(Objects.requireNonNull(items)),
                new ArrayList<>()
        );
        currentPermutations++;

        return result;
    }

    private <T> Stream<Stream<T>> collectPermutations(final List<T> array) {

        return LongStream.range(0, computePossiblePermutations(array.size()))
                .mapToObj(no -> permutation(no, array).stream());
    }
}