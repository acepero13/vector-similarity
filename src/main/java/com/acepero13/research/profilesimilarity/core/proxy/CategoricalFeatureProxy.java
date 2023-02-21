package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.exceptions.VectorizableProxyException;
import com.acepero13.research.profilesimilarity.utils.ListUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class CategoricalFeatureProxy implements InvocationHandler {


    private final CategoricalWrapper wrapper;

    public CategoricalFeatureProxy(Object target, String name) {
        this.wrapper = new CategoricalWrapper(target, name);
    }

    public static <A> CategoricalFeature<A> of(Object object, String name) {
        if (object instanceof CategoricalFeature) {
            return (CategoricalFeature<A>) object;
        }
        return (CategoricalFeature<A>) Proxy.newProxyInstance(
                CategoricalFeature.class.getClassLoader(),
                new Class[]{CategoricalFeature.class},
                new CategoricalFeatureProxy(object, name)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws VectorizableProxyException {
        String methodName = method.getName();
        switch (methodName) {
            case "originalValue":
                return wrapper.originalValue();
            case "featureName":
                return wrapper.featureName();
            case "weight":
                return wrapper.weight();
            case "featureValue":
                return wrapper.featureValue();
            case "isWhiteListed":
                var feats = FeaturesHelper.features(args);
                FeaturesHelper.checkArguments(args, FeaturesHelper.exactly(ListUtils.class));
                return wrapper.isWhiteListed(feats);
            case "toString":
                return wrapper.toString();
            case "hashCode":
                return wrapper.hashCode();
            case "equals":
                FeaturesHelper.checkArguments(args, 1, FeaturesHelper.oneOf(CategoricalFeatureProxy.class, CategoricalFeature.class));
                return wrapper.equals(args[0]);
            case "matches":
                FeaturesHelper.checkArguments(args, FeaturesHelper.exactly(CategoricalFeature.class));
                return wrapper.matches((CategoricalFeature<?>) args[0]);
            default:  throw new VectorizableProxyException("Error calling undefined method for Categoricaly Feature: " + methodName);

        }
    }

    @Override
    public String toString() {
        return wrapper.toString();
    }


    @SuppressWarnings("rawtypes")
    private static class CategoricalWrapper implements CategoricalFeature {
        private final String name;
        private final Object target;

        CategoricalWrapper(Object target, String name) {
            this.name = name;
            this.target = target;
        }

        @Override
        public Object originalValue() {
            return target;
        }

        @Override
        public String featureName() {
            return name;
        }

        @Override
        public String toString() {
            return target.toString();
        }
    }
}
