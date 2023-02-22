package utils;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum Gender {
    MALE,
    FEMALE
}

enum EcoFriendlyBehavior {
    LOW,
    HIGH
}

enum TAG {
    FAMILY, SPORT, ECO, SAFETY, AUDI, VW, EATING, HANDLING, ID3, MUSIC, RACING, TRAINING, TUNING, RECREATION
}

public class UserDataGenerator {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        int numUsers = 100;
        User[] users = generateUserData(numUsers);


        try {
            objectMapper.writeValue(new File("users.json"), List.of(users));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static User[] generateUserData(int numUsers) {
        User[] users = new User[numUsers];
        Random r = new Random();
        for (int i = 0; i < numUsers; i++) {
            User u = new User();
            u.age = (int) Math.round(r.nextGaussian() * 15 + 40);
            u.income = (int) Math.round(r.nextGaussian() * 20000 + 50000);
            u.gender = r.nextBoolean()
                    ? Gender.MALE
                    : Gender.FEMALE;
            u.numChildren = r.nextInt(4);

            generateEcoFriendly(r, u);
            generateTags(r, u);

            users[i] = u;
        }

        return users;
    }

    private static void generateTags(Random r, User u) {


        addRandomTag(r, u, TAG.MUSIC);
        addRandomTag(r, u, TAG.EATING);
        addRandomTag(r, u, TAG.AUDI);
        addRandomTag(r, u, TAG.ID3);
        addRandomTag(r, u, TAG.SPORT);
        addRandomTag(r, u, TAG.TRAINING);
        addRandomTag(r, u, TAG.RECREATION);


        PredicateHelper
                .ifTrue(u.numChildren > 0)
                .and(r.nextDouble() < 0.7)
                .then(() -> u.addTag(TAG.FAMILY));

        PredicateHelper.ifTrue(u.ecoFriendly.equals(EcoFriendlyBehavior.HIGH))
                .and(r.nextDouble() < 0.8)
                .then(() -> u.addTag(TAG.ECO));


        PredicateHelper.ifTrue(u.gender.equals(Gender.FEMALE))
                .and(r.nextDouble() < 0.6)
                .then(() -> u.addTag(TAG.SAFETY));

        PredicateHelper.ifTrue(u.gender.equals(Gender.MALE))
                .and(r.nextDouble() < 0.6)
                .then(() -> u.addTag(TAG.RACING));

        PredicateHelper.ifTrue(u.gender.equals(Gender.MALE))
                .and(r.nextDouble() < 0.6)
                .then(() -> u.addTag(TAG.TUNING));

    }

    private static void addRandomTag(Random r, User u, TAG tag) {
        PredicateHelper.ifTrue(r.nextDouble() < 0.5)
                .then(() -> u.addTag(tag));
    }

    private static void generateEcoFriendly(Random r, User u) {
        if (u.income > 60000 && u.age < 35 && u.gender == Gender.FEMALE) {
            u.ecoFriendly = isEcoFriendly(r.nextDouble() < 0.8);
        } else if (u.income > 60000 && u.age < 35) {
            u.ecoFriendly = isEcoFriendly(r.nextDouble() < 0.7);

        } else if (u.income > 60000 && u.age <= 55) {
            u.ecoFriendly = isEcoFriendly(r.nextDouble() < 0.6);
        } else if (u.income <= 60000 && u.age < 35) {
            u.ecoFriendly = isEcoFriendly(r.nextDouble() < 0.5);
        } else if (u.income <= 60000 && u.age <= 55) {
            u.ecoFriendly = isEcoFriendly(r.nextDouble() < 0.4);
        } else {
            u.ecoFriendly = isEcoFriendly(r.nextDouble() < 0.3);
        }
    }

    private static EcoFriendlyBehavior isEcoFriendly(boolean value) {
        return value
                ? EcoFriendlyBehavior.HIGH
                : EcoFriendlyBehavior.LOW;
    }
}

class User {
    int age;
    int income;
    Gender gender;
    int numChildren;
    EcoFriendlyBehavior ecoFriendly;
    List<TAG> tags = new ArrayList<>();

    @Override
    public String toString() {
        return "Age: " + age + ", Income: $" + income + ", Gender: " + gender +
                ", Num Children: " + numChildren + ", Eco-Friendly: " + ecoFriendly;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public int getIncome() {
        return income;
    }

    public User setIncome(int income) {
        this.income = income;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public User setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public User setNumChildren(int numChildren) {
        this.numChildren = numChildren;
        return this;
    }

    public EcoFriendlyBehavior getEcoFriendly() {
        return ecoFriendly;
    }

    public User setEcoFriendly(EcoFriendlyBehavior ecoFriendly) {
        this.ecoFriendly = ecoFriendly;
        return this;
    }

    public User addTag(TAG tag) {
        this.tags.add(tag);
        return this;
    }

    public List<TAG> getTags() {
        return tags;
    }

    public void setTags(List<TAG> tags) {
        this.tags = tags;
    }
}



