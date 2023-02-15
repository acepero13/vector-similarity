package com.acepero13.research.profilesimilarity.api.features;

public abstract class AbstractNumericalFeature <T extends Number> extends Number implements NumericalFeature<T>{

    private final T value;
    private final String name;
    private final double weight;

    public AbstractNumericalFeature(T value, String name, double weight) {
        this.value = value;
        this.name = name;
        this.weight = weight;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public double featureValue() {
        return toDouble();
    }

    protected abstract Double toDouble();

    @Override
    public T originalValue() {
        return value;
    }

    @Override
    public String featureName() {
        return name;
    }

    @Override
    public int intValue() {
        return toDouble().intValue();
    }

    @Override
    public long longValue() {
        return toDouble().longValue();
    }

    @Override
    public float floatValue() {
        return toDouble().floatValue();
    }

    @Override
    public double doubleValue() {
        return toDouble();
    }
}
