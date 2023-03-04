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
            generateExtraData(r, u);

            users[i] = u;
        }

        return users;
    }

    private static void generateExtraData(Random r, User u) {
        PredicateHelper.ifTrue(u.ecoFriendly == EcoFriendlyBehavior.HIGH)
                       .and(r.nextDouble() < 0.8)
                       .then(() -> u.drivingStyle = User.DrivingStyle.ECO)
                       .elseDo(() -> u.drivingStyle = User.DrivingStyle.SPORTY);

        PredicateHelper.ifTrue(u.hasTag(TAG.AUDI))
                       .then(() -> u.carType = User.CarType.AUDI_E_TRON);

        PredicateHelper.ifTrue(u.hasTag(TAG.ID3))
                       .then(() -> u.carType = User.CarType.VW_ID3);

        PredicateHelper.ifTrue(u.drivingStyle == User.DrivingStyle.SPORTY)
                .and(r.nextDouble() < 0.8)
                .then(() -> u.chargingType = User.ChargingType.MAXIMUM_REST_CHARGE)
                .elseDo(() -> u.chargingType = User.ChargingType.SHORT);


        PredicateHelper.ifTrue(u.drivingStyle == User.DrivingStyle.ECO)
                       .and(r.nextDouble() < 0.8)
                       .then(() -> u.chargingType = User.ChargingType.SHORT)
                       .elseDo(() -> u.chargingType = User.ChargingType.MAXIMUM_REST_CHARGE);

    }

    private static void generateTags(Random r, User u) {


        addRandomTag(r, u, TAG.MUSIC);
        addRandomTag(r, u, TAG.EATING);


        addRandomTag(r, u, TAG.SPORT);
        addRandomTag(r, u, TAG.TRAINING);
        addRandomTag(r, u, TAG.RECREATION);

        PredicateHelper.ifTrue(r.nextDouble() < 0.5)
                       .then(() -> u.addTag(TAG.AUDI))
                       .elseDo(() -> u.addTag(TAG.ID3));

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
    DrivingStyle drivingStyle;
    CarType carType;
    ChargingType chargingType;
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

    public DrivingStyle getDrivingStyle() {
        return drivingStyle;
    }

    public User setDrivingStyle(DrivingStyle drivingStyle) {
        this.drivingStyle = drivingStyle;
        return this;
    }

    public CarType getCarType() {
        return carType;
    }

    public User setCarType(CarType carType) {
        this.carType = carType;
        return this;
    }

    public ChargingType getChargingType() {
        return chargingType;
    }

    public User setChargingType(ChargingType chargingType) {
        this.chargingType = chargingType;
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

    public boolean hasTag(TAG tag) {
        return tags.stream().anyMatch(t -> t.equals(tag));
    }

    public enum DrivingStyle {
        ECO, SPORTY, UNDEFINED
    }

    public enum CarType {
        AUDI_E_TRON, VW_ID3
    }

    public enum ChargingType {
        SHORT, MAXIMUM_REST_CHARGE;
    }
}



