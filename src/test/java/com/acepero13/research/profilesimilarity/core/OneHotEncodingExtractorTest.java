package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
class OneHotEncodingExtractorTest {

    @Test
    void encodeTags() {
        var extractor = OneHotEncodingExtractor.oneHotEncodingOf(List.of(TAG.values()));
        var result = extractor.convert(List.of(TAG.SPORT, TAG.MUSIC, TAG.CONCERT));
        List<Boolean> booleanResult = result.stream()
                                            .map(Feature::originalValue)
                                            .map(Boolean.class::cast)
                                            .collect(Collectors.toList());

        assertThat(booleanResult, equalTo(List.of(true, false, true, true, false)));

    }

    @Test
    void encodeTagsAsEnum() {
        var extractor = OneHotEncodingExtractor.oneHotEncodingOf(TAG.values());
        var result = extractor.convert(List.of(TAG.SPORT, TAG.MUSIC, TAG.CONCERT));
        List<Boolean> booleanResult = result.stream()
                                            .map(Feature::originalValue)
                                            .map(Boolean.class::cast)
                                            .collect(Collectors.toList());

        assertThat(booleanResult, equalTo(List.of(true, false, true, true, false)));

    }

    private enum TAG implements CategoricalFeature<TAG> {
        SPORT, FAMILY, MUSIC, CONCERT, OTHER;

        @Override
        public TAG originalValue() {
            return this;
        }

        @Override
        public String featureName() {
            return "tag";
        }
    }
}
