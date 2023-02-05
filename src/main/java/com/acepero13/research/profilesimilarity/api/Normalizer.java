package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.Vector;

//TODO: Think about a Decorator instead of a normalizer
public interface Normalizer {


    Vector normalize(Vector vector);
}
