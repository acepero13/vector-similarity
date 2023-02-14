package com.acepero13.research.profilesimilarity.io;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.classifier.Knn;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MNISTReader {
    private final String filePath;
    private final String labelFilePath;
    public static final int LABEL_FILE_MAGIC_NUMBER = 2049;
    public static final int IMAGE_FILE_MAGIC_NUMBER = 2051;

    public static void main(String[] args) throws IOException {
        var basePath = "/home/alvaro/Documents/data/mnist/dataset/";
        var train = new MNISTReader(basePath + "train-images.idx3-ubyte", basePath + "train-labels.idx1-ubyte").load();
        var test = new MNISTReader(basePath + "t10k-images.idx3-ubyte", basePath + "t10k-labels.idx1-ubyte").load();

        var knn = new Knn(11, vector -> NormalizedVector.of(vector.divide(255.0)), train);

        long correctClassification = test.stream()
                .parallel()
                .map(testImage -> Tuple.of(knn.fit(testImage).classify(NUMBER.class), testImage.toFeatureVector().categorical().get(0)))
                .filter(t -> t.first() == t.second())
                .count();

        System.out.println("accuracy = " + ((double) correctClassification / test.size()));
    }

    public MNISTReader(String filePath, String labelFilePath) {
        this.filePath = filePath;
        this.labelFilePath = labelFilePath;
    }

    public List<Vectorizable> load() throws IOException {
        DataInputStream imageStream = new DataInputStream(new FileInputStream(filePath));
        int magic = imageStream.readInt();
        if (magic != IMAGE_FILE_MAGIC_NUMBER) {
            throw new IllegalArgumentException("Invalid image file magic number: " + magic);
        }

        DataInputStream labelStream = new DataInputStream(new FileInputStream(labelFilePath));
        int magicLabel = labelStream.readInt();
        if (magicLabel != LABEL_FILE_MAGIC_NUMBER) {
            throw new IllegalArgumentException("Invalid label file magic number: " + magic);
        }

        int numImages = imageStream.readInt() / 10;
        int numRows = imageStream.readInt();
        int numCols = imageStream.readInt();
        var total = numRows * numCols;
        int numLabels = labelStream.readInt();
        List<Vectorizable> featureVectors = new ArrayList<>();
        for (int i = 0; i < numImages; i++) {
            featureVectors.add(new LabeledImage(imageStream, labelStream, total));

        }
        labelStream.close();
        imageStream.close();
        return featureVectors;
    }

    private static class LabeledImage extends AbstractVectorizable {
        private LabeledImage(DataInputStream imageStream, DataInputStream labelStream, int total) throws IOException {
            for (int j = 0; j < total; j++) {
                addNonNullFeature(Features.integerFeature(imageStream.readUnsignedByte(), "feature_" + j));
            }
            addNonNullFeature(NUMBER.of(labelStream.readUnsignedByte()));
        }
    }

    enum NUMBER implements CategoricalFeature<NUMBER> {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

        public static Feature<?> of(int number) {
            switch (number) {
                case 1:
                    return ONE;
                case 2:
                    return TWO;
                case 3:
                    return THREE;
                case 4:
                    return FOUR;
                case 5:
                    return FIVE;
                case 6:
                    return SIX;
                case 7:
                    return SEVEN;
                case 8:
                    return EIGHT;
                case 9:
                    return NINE;
                default:
                    return ZERO;
            }
        }

        @Override
        public NUMBER originalValue() {
            return this;
        }

        @Override
        public String featureName() {
            return "number";
        }
    }
}