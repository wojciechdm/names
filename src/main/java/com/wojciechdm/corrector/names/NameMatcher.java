package com.wojciechdm.corrector.names;

import info.debatty.java.stringsimilarity.Damerau;

import java.util.*;

class NameMatcher {

  private Damerau damerau;

  NameMatcher(Damerau damerau) {
    this.damerau = damerau;
  }

  String match(String name, Collection<String> existingNames, int maxDistance) {
    return existingNames.stream()
        .map(existingName -> Map.entry(damerau.distance(name, existingName), existingName))
        .filter(existingName -> existingName.getKey() <= maxDistance)
        .min(Comparator.comparingDouble(Map.Entry::getKey))
        .map(Map.Entry::getValue)
        .orElse(name);
  }
}
