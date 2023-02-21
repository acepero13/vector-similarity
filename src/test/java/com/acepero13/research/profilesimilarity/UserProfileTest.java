package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.classifier.MostSimilar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class UserProfileTest {
    private static final Boolean YES = true;
    private static final Boolean NO = false;

    private static UserProfile createUser1() {
        return UserProfile.builder()
                .drivesInEcoMode(YES)
                .likesToBuyEcoProducts(YES)
                .isInterestedInEcoProducts(YES)
                .salary(30_000)
                .gender(UserProfile.Gender.FEMALE)
                .numberOfChildren(1)
                .name("u1")
                .build();
    }

    private static UserProfile createUser3() {
        return UserProfile.builder()
                .drivesInEcoMode(NO)
                .likesToBuyEcoProducts(YES)
                .isInterestedInEcoProducts(YES)
                .salary(50_000)
                .gender(UserProfile.Gender.FEMALE)
                .numberOfChildren(2)
                .name("u3")
                .build();
    }

    private static UserProfile createUser2() {
        return UserProfile.builder()
                .drivesInEcoMode(NO)
                .likesToBuyEcoProducts(NO)
                .isInterestedInEcoProducts(YES)
                .salary(55_000)
                .gender(UserProfile.Gender.MALE)
                .name("u2")
                .numberOfChildren(1)
                .build();
    }

    @Test
    void usingProfiles() {


        var u1 = createUser1();
        var u2 = createUser2();
        var target = createUser3();

        var dataset = MostSimilar.withDefaultMetric(u1, u2);

        Vectorizable mostSimilarUser = dataset.mostSimilarTo(target);

        assertThat(mostSimilarUser, equalTo(u1));
    }

    @Test
    @DisplayName("target object has some missing features. Compute similarity only using those features")
    void compare() {
        /*
         * The idea is:
         * - we have a target object which we want to find the most similar object
         * - From every object we want to compare the target object against, we remove those features that are not set in the target object
         * - We need a minimum of features to start the comparison if there are less features than a threshold, we do not compare
         *
         * at the end, we can infer the missing features...
         */

        var target = UserProfile.builder()
                .drivesInEcoMode(YES)
                .salary(20_000)
                .gender(UserProfile.Gender.FEMALE)
                .name("target")
                .numberOfChildren(1)
                .build();

        UserProfile user1 = createUser1();
        UserProfile user2 = createUser2();
        var dataset = MostSimilar.withDefaultMetric(user1, user2);

        Vectorizable mostSimilarUser = dataset.mostSimilarTo(target);

        assertThat(mostSimilarUser, equalTo(user1));

    }


    @Test
    void missingFeaturesAreNotAddedToVector() {
        var target = UserProfile.builder()
                .drivesInEcoMode(YES)
                .salary(60_000)
                .gender(UserProfile.Gender.MALE)
                .name("target")
                .numberOfChildren(1)
                .build();

        assertThat(target.vector().size(), equalTo(4));
    }

    @Test
    void excludeFeatures() {
        var target = UserProfile.builder()
                .drivesInEcoMode(YES)
                .salary(60_000)
                .gender(UserProfile.Gender.MALE)
                .name("target")
                .numberOfChildren(1)
                .build();

        var another = createUser1();

        assertThat(another.vector(target.features()).size(), equalTo(4));
    }
}