package com.acepero13.research.profilesimilarity.testmodels;

import com.acepero13.research.profilesimilarity.annotations.CategoricalFeature;
import com.acepero13.research.profilesimilarity.annotations.NumericalFeature;
import com.acepero13.research.profilesimilarity.annotations.Vectorizable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Vectorizable
@Data
public class User {
    @NumericalFeature(name = "age", type = Integer.class)
    private final int age;
    @NumericalFeature
    private final int income;
    @NumericalFeature
    private final int height;
    @CategoricalFeature(name = "study")
    private final STUDY study;
    @CategoricalFeature(name = "hobbies", oneHotEncoding = true)
    private final List<HOBBY> hobbies;

    public static User defaultUser() {
        return new User(35, 70_000, 174, STUDY.MASTER, new ArrayList<>());
    }

    private enum STUDY {
        BACHELOR, MASTER, PHD
    }

}
