package com.acepero13.research.profilesimilarity.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListUtilsTest {
    @Test
    void zip() {
        var l1 = List.of(1, 2, 3);
        var l2 = List.of(4, 5, 6);

        List<Integer> result = ListUtils.zip(l1, l2, Integer::sum)
                .collect(Collectors.toList());

        assertThat(result, contains(5, 7, 9));
    }

    @Test
    void cannotZipDifferentSizedElements() {
        assertThrows(IllegalStateException.class, () -> {
            ListUtils.zip(List.of(1, 2), List.of(1, 2, 3), Integer::sum);
        });
    }

    @Test
    void padding() {
        var l1 = List.of(1, 2, 3);
        var result = ListUtils.padding(l1, 0, 3);
        assertThat(result, equalTo(List.of(1, 2, 3, 0, 0, 0)));
    }
}