package com.acepero13.research.profilesimilarity.testmodels;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.annotations.Numerical;
import com.acepero13.research.profilesimilarity.annotations.Vectorizable;
import lombok.Data;

import java.util.List;

@Vectorizable
@Data
public class User {
    @Numerical(name = "age", type = Integer.class)
    private final int age;
    @Numerical
    private final int income;
    @Numerical
    private final int height;
    @Categorical(name = "study")
    private final STUDY study;
    @Categorical(name = "hobbies", oneHotEncoding = true)
    private final List<NEW_HOBBIES> hobbies;

    public static User defaultUser() {
        return new User(35, 70_000, 174, STUDY.MASTER, List.of(NEW_HOBBIES.MUSIC, NEW_HOBBIES.SPORT));
    }

    private enum STUDY {
        BACHELOR, MASTER, PHD
    }

    // TODO: Add Categorical Annotation here??
    private enum NEW_HOBBIES {
        READ, SPORT, MUSIC
    }

}
