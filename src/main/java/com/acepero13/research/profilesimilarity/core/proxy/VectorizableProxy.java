package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.annotations.Numerical;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.OneHotEncodingExtractor;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class VectorizableProxy implements InvocationHandler {
    private final Object target;
    private final VectorizableProxyWrapper vectorWrapper;
    private String name;

    public VectorizableProxy(Object target) {
        this.target = target;
        this.vectorWrapper = Objects.requireNonNull(from(target));
    }

    public static <T> List<FeatureVector> ofFeatureVector(List<T> objects) {
        return objects.stream()
                .map(VectorizableProxy::of)
                .map(Vectorizable::toFeatureVector)
                .collect(Collectors.toList());
    }

    private VectorizableProxyWrapper from(Object target) {
        var opAnnotation = AnnotationHelper.getAnnotation(target, com.acepero13.research.profilesimilarity.annotations.Vectorizable.class);
        VectorizableProxyWrapper wrapper;
        if (opAnnotation.isPresent()) {
            wrapper = new VectorizableProxyWrapper(target);
            addNumericalFeatures(target, wrapper); // TODO: Separate into helper class
            addCategoricalFeatures(target, wrapper);

        } else {
            wrapper = new VectorizableProxyWrapper(target);
        }
        return wrapper;
    }

    private static void addCategoricalFeatures(Object target, VectorizableProxyWrapper wrapper) {
        List<Tuple<Field, Categorical>> fields = AnnotationHelper.getAnnotatedFields(target, Categorical.class);
        fields.forEach(wrapper::addCategorical);
    }

    private static void addNumericalFeatures(Object target, VectorizableProxyWrapper wrapper) {
        List<Tuple<Field, Numerical>> fields = AnnotationHelper.getAnnotatedFields(target, Numerical.class);
        fields.forEach(wrapper::addNumerical);
    }

    public static Vectorizable of(Object object) {
        if (object instanceof Vectorizable) {
            return (Vectorizable) object;
        }
        return (Vectorizable) Proxy.newProxyInstance(
                Vectorizable.class.getClassLoader(),
                new Class[]{Vectorizable.class},
                new VectorizableProxy(object)
        );
    }

    public static <T> List<Vectorizable> of(List<T> objects) {
        return objects.stream()
                .map(VectorizableProxy::of)
                .collect(Collectors.toList());
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws RuntimeException {
        String methodName = method.getName();
        switch (methodName) {
            case "features":
                return vectorWrapper.features();
            case "toFeatureVector":
                return vectorWrapper.toFeatureVector();
            case "vector":
                return executeVector(args);
            case "numericalFeatures":
                return vectorWrapper.numericalFeatures();
            case "toString":
                return vectorWrapper.toString();
            case "equals":
                return objectEquals(args[0]); //TODO: Check type
        }
        throw new RuntimeException("Error calling undefined method: " + methodName); // TODO: raise proper exception
    }

    public boolean objectEquals(Object o) {
        if (this.target == o) return true;
        if (o == null ) return false;

        if(isProxyClass(o)) {
            VectorizableProxy ruleProxy = toVectorizableProxy(o);
            return isEqualToTarget(ruleProxy);
        } else if (o instanceof Vectorizable) {
            return equalToRealVectorizable((Vectorizable) o);
        }
        return false;
    }


    private boolean equalToRealVectorizable(Vectorizable another) {
        return another.features().equals(vectorWrapper.features());
    }

    private boolean isEqualToTarget(VectorizableProxy another) {
        if(this.getClass() != another.getClass()) return false;
        return vectorWrapper.features().equals(another.vectorWrapper.features());
    }

    private static VectorizableProxy toVectorizableProxy(Object another) {
        return (VectorizableProxy) Proxy.getInvocationHandler(another);
    }

    private static boolean isProxyClass(Object anotherRule) {
        return Proxy.isProxyClass(anotherRule.getClass());
    }


    private Vector<Double> executeVector(Object[] args) {
        if (args == null) {
            return vectorWrapper.vector();
        } else {
            return vectorWrapper.vector(FeaturesHelper.features(args));
        }
    }


    @ToString
    @EqualsAndHashCode(callSuper = false)
    private static class VectorizableProxyWrapper extends AbstractVectorizable {

        private final Object target;

        public VectorizableProxyWrapper(Object target) {
            this.target = target;
        }

        //TODO: Refactor all these adds
        public void addNumerical(Tuple<Field, Numerical> tuple) {
            Numerical annotation = tuple.second();
            Field field = tuple.first();

            var metadata = MetaData.of(annotation, field);
            try {
                create(field, metadata);
            } catch (IllegalAccessException e) {
                // TODO: Change this
                throw new RuntimeException(e);
            }
        }

        public void addCategorical(Tuple<Field, Categorical> tuple) {
            Categorical annotation = tuple.second();
            Field field = tuple.first();
            field.setAccessible(true);
            if (annotation.oneHotEncoding()) {
                addAsOneHotEncoding(annotation, field);
            } else {
                addSingleFeatureCategorical(annotation, field);
            }
        }

        private void addAsOneHotEncoding(Categorical annotation, Field field) {
            // TODO: Refactor this
            //TODO: Write proper name with prefix
            try {
                var targetObject = field.get(target);
                var type = annotation.type();

                if (targetObject instanceof List) {

                    List<?> originalValues = new ArrayList<>(((List<?>) targetObject));

                    List<?> values = new ArrayList<>(((List<?>) targetObject)).stream()
                            .map(c -> CategoricalFeatureProxy.of(c, annotation.name()))
                            .collect(Collectors.toList());
                    if (!values.isEmpty() && !originalValues.isEmpty()) {
                        var firstItem = originalValues.get(0);
                        if (firstItem instanceof Enum) {
                            List<CategoricalFeature> allElements = allValuesForOneHot(annotation, firstItem.getClass().getEnumConstants());


                            var extractor = OneHotEncodingExtractor.oneHotEncodingOf(allElements);
                            var features = extractor.convert((List<CategoricalFeature>) values);
                            int a = 0;
                            features.forEach(this::addNonNullFeature);
                        }
                    }
                    else if(type.isEnum() ){
                        // TODO: Add empty case
                        // We need to manually specify the type
                        List<CategoricalFeature> allElements = allValuesForOneHot(annotation, type.getEnumConstants());
                        var extractor = OneHotEncodingExtractor.oneHotEncodingOf(allElements);
                        var features = extractor.convert(new ArrayList<>());
                        features.forEach(this::addNonNullFeature);
                    }
                    else {
                        throw new RuntimeException("I cannot infer the type of the categorical feature for one-hot-encoding."
                                + annotation.name()
                                + "Please, include the type parameter in the Categorical annotation");// TODO: Throw dedicated
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e); // TODO: change this
            }
        }


        private void addSingleFeatureCategorical(Categorical annotation, Field field) {
            var metadata = MetaData.of(annotation, field);
            try {
                var targetObject = field.get(target);
                var feature = CategoricalFeatureProxy.of(targetObject, metadata.name);
                addNonNullFeature(feature);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e); // TODO: change this
            }
        }

        private void create(Field field, MetaData metadata) throws IllegalAccessException {
            Class<?> fieldType = field.getType();
            field.setAccessible(true);
            if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                addNonNullFeature(Features.integerFeature((Integer) field.get(target), metadata.name, metadata.weight));
            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                addNonNullFeature(Features.doubleFeature((Double) field.get(target), metadata.name, metadata.weight));
            } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                addNonNullFeature(Features.booleanFeature((Boolean) field.get(target), metadata.name, metadata.weight));
            }
        }


    }

    private static List<CategoricalFeature> allValuesForOneHot(Categorical annotation, Object[] constants) {
        return Arrays.stream(constants)
                .filter(Objects::nonNull)
                .map(c -> CategoricalFeatureProxy.of(c, annotation.name()))
                .map(CategoricalFeature.class::cast)
                .collect(Collectors.toList());
    }

    @Data
    private static class MetaData {
        private final String name;
        private final double weight;
        private final Class<?> type;

        public static MetaData of(Numerical annotation, Field field) {
            String name = getNameFrom(annotation, field);
            Class<?> type = getTypeFrom(annotation, field);

            return new MetaData(name, annotation.weight(), type);
        }

        public static MetaData of(Categorical annotation, Field field) {
            String name = getNameFrom(annotation, field);
            Class<?> type = getTypeFrom(annotation, field);

            return new MetaData(name, 1.0, type);
        }


        private static String getNameFrom(Categorical annotation, Field field) {
            String name = annotation.name();
            if (name.isEmpty()) {
                return field.getName();
            }
            return name;
        }

        private static Class<?> getTypeFrom(Numerical annotation, Field field) {
            Class<?> type = annotation.type();
            if (type.equals(Objects.class)) {
                return field.getType();
            }
            return type;
        }

        private static Class<?> getTypeFrom(Categorical annotation, Field field) {
            return field.getType();
        }

        private static String getNameFrom(Numerical annotation, Field field) {
            String name = annotation.name();
            if (name.isEmpty()) {
                return field.getName();
            }
            return name;
        }


    }
}
