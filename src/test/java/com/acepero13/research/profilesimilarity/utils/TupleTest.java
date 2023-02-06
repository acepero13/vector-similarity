package com.acepero13.research.profilesimilarity.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    @Test
    void createsSimpleTuple() {
        Tuple<Integer, Integer> tup = Tuple.of(1, 2);
        assertEquals(1, tup.first());
        assertEquals(2, tup.second());
    }

    @Test
    void useFunctionalInterface() {
        Tuple.of(1, 2).apply((_1, _2) -> {
            assertEquals(1, _1);
            assertEquals(2, _2);
        });
    }

}