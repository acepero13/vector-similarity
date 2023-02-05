package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.DataSet;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class UserProfileTest {
    private static final Boolean YES = true;
    private static final Boolean NO = false;

    @Test
    void usingProfiles() {


        var u1 = UserProfile.builder()
                .drivesInEcoMode(YES)
                .likesToBuyEcoProducts(YES)
                .isInterestedInEcoProducts(YES)
                .salary(1400)
                .gender(UserProfile.Gender.FEMALE)
                .numberOfChildren(1)
                .name("u1")
                .build();


        var u2 = UserProfile.builder()
                .drivesInEcoMode(NO)
                .likesToBuyEcoProducts(NO)
                .isInterestedInEcoProducts(YES)
                .salary(3100)
                .gender(UserProfile.Gender.MALE)
                .name("u2")
                .numberOfChildren(1)
                .build();

        var u3 = UserProfile.builder()
                .drivesInEcoMode(NO)
                .likesToBuyEcoProducts(YES)
                .isInterestedInEcoProducts(YES)
                .salary(3000)
                .gender(UserProfile.Gender.FEMALE)
                .numberOfChildren(2)
                .name("u3")
                .build();

        var dataset = new DataSet(u1, u2);

        Vectorizable mostSimilarUser = dataset.mostSimilarTo(u3);

        assertThat(mostSimilarUser, equalTo(u1));


    }
}