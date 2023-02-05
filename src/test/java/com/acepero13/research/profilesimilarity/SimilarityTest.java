package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.scores.CosineSimilarity;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class SimilarityTest {

    Similarity scorer = new CosineSimilarity();

    @Test
    void usersWithSameProfileHavePerfectScore() {
        var userProfile1 = createEcoProfile();
        var userProfile2 = createEcoProfile();

        var result = scorer.similarityScore(userProfile1.vector(), userProfile2.vector());

        assertThat(result, closeTo(1.0, 0.1));
    }

    @Test void usersWithOppositeProfilesHasZeroScore(){
        var userProfile1 = createEcoProfile();
        var userProfile2 = UserProfile.builder()
                .isInterestedInEcoProducts(false)
                .drivesInEcoMode(false)
                .likesToBuyEcoProducts(false)
                .gender(UserProfile.Gender.FEMALE)
                .build();

        var result = scorer.similarityScore(userProfile1.vector(), userProfile2.vector());

        assertThat(result, closeTo(0.0, 0.1));
    }

    private static UserProfile createEcoProfile() {
        return UserProfile.builder()
                .isInterestedInEcoProducts(true)
                .drivesInEcoMode(true)
                .likesToBuyEcoProducts(true)
                .gender(UserProfile.Gender.MALE)
                .build();
    }

}