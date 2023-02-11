package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.scores.CosineMetric;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class MetricTest {

    final Metric scorer = new CosineMetric();

    @Test
    void usersWithSameProfileHavePerfectScore() {
        var userProfile1 = createEcoProfile();
        var userProfile2 = createEcoProfile();

        var result = scorer.similarityScore(NormalizedVector.of(userProfile1.vector()), NormalizedVector.of(userProfile2.vector()));

        assertThat(result, closeTo(1.0, 0.1));
    }

    @Test void usersWithOppositeProfilesHasZeroScore(){
        var userProfile1 = createEcoProfile();
        var userProfile2 = UserProfile.builder()
                .isInterestedInEcoProducts(false)
                .drivesInEcoMode(false)
                .likesToBuyEcoProducts(false)
                .gender(UserProfile.Gender.FEMALE)
                .salary(0)
                .numberOfChildren(0)
                .build();

        var result = scorer.similarityScore(NormalizedVector.of(userProfile1.vector()), NormalizedVector.of(userProfile2.vector()));

        assertThat(result, closeTo(0.0, 0.1));
    }

    private static UserProfile createEcoProfile() {
        return UserProfile.builder()
                .isInterestedInEcoProducts(true)
                .drivesInEcoMode(true)
                .likesToBuyEcoProducts(true)
                .gender(UserProfile.Gender.MALE)
                .salary(1)
                .numberOfChildren(1)
                .build();
    }

}