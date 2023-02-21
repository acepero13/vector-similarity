package utils;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.classifier.KnnMixedData;
import com.acepero13.research.profilesimilarity.core.classifier.result.KnnResult;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */

class UserDataTest {
    private static List<FeatureVector> dataset = new ArrayList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    //@BeforeAll

    public static void setUp() throws IOException {
        List<User> users = objectMapper.readValue(load("users.json"), new TypeReference<>() {
        });

        dataset = users.stream().map(User::toFeatureVector).collect(Collectors.toList());

    }

    private static InputStream load(String filename) {
        return UserDataTest.class.getClassLoader().getResourceAsStream(filename);
    }
    @Test
    @Disabled
    void findMostSimilar() {

        var highEarningFemale = new User(25, 80_000, Gender.FEMALE, 1, null);
        KnnMixedData knn = KnnMixedData.of(3, dataset);
        KnnResult result = knn.fit(highEarningFemale.toFeatureVector());
        CategoricalFeature<?> actualBehavior = result
                .classify(EcoFriendlyBehavior.class);

        assertThat(actualBehavior, equalTo(EcoFriendlyBehavior.HIGH));
    }

    enum Gender implements CategoricalFeature<Gender> {
        MALE,
        FEMALE;

        @Override
        public Gender originalValue() {
            return this;
        }

        @Override
        public String featureName() {
            return "gender";
        }
    }

    enum EcoFriendlyBehavior implements CategoricalFeature<EcoFriendlyBehavior> {
        LOW,
        HIGH;

        @Override
        public EcoFriendlyBehavior originalValue() {
            return this;
        }

        @Override
        public String featureName() {
            return "eco-friendly-behavior";
        }
    }

    private static class User extends AbstractVectorizable {

        final int age;
        final int income;
        final Gender gender;
        final int numChildren;
        final EcoFriendlyBehavior ecoFriendly;

        public User(@JsonProperty("age") int age,
                    @JsonProperty("income") int income,
                    @JsonProperty("gender") Gender gender,
                    @JsonProperty("numChildren") int numChildren,
                    @JsonProperty("ecoFriendly") EcoFriendlyBehavior ecoFriendlyBehavior
        ) {

            this.age = age;
            this.income = income;
            this.gender = gender;
            this.numChildren = numChildren;
            this.ecoFriendly = ecoFriendlyBehavior;

            addNonNullFeature(Features.integerFeature(age, "age"))
                    .addNonNullFeature(Features.integerFeature(income, "income"))
                    .addNonNullFeature(gender)
                    .addNonNullFeature(Features.integerFeature(numChildren, "numberOfChildren"))
                    .addNonNullFeature(ecoFriendlyBehavior);

        }


        @Override
        public String toString() {
            return "Age: " + age + ", Income: $" + income + ", Gender: " + gender +
                    ", Num Children: " + numChildren + ", Eco-Friendly: " + ecoFriendly;
        }


        
    }
}
