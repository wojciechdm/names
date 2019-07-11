package com.wojciechdm.corrector.names;

import java.util.Collection;
import java.util.regex.*;

class NameCorrector {

  private static final Pattern FIRST_NAME_REGEX = Pattern.compile("[a-ząęłóźćżśń]+");
  private static final Pattern LAST_NAME_REGEX =
      Pattern.compile("[a-ząęłóźćżśń]+(-[a-ząęłóźćżśń]+)?");

  private NameCapitalizer nameCapitalizer;
  private NameMatcher nameMatcher;
  private NamesProvider namesProvider;
  private int maxDistance;

  NameCorrector(
      NameCapitalizer nameCapitalizer,
      NameMatcher nameMatcher,
      NamesProvider namesProvider,
      int maxDistance) {
    this.nameCapitalizer = nameCapitalizer;
    this.nameMatcher = nameMatcher;
    this.namesProvider = namesProvider;
    this.maxDistance = maxDistance;
  }

  NameDto correct(String firstName, String lastName) {
    String lowercaseFirstName = firstName.toLowerCase().trim();
    String lowercaseLastName = lastName.toLowerCase().trim();
    return isValidFormat(lowercaseFirstName, lowercaseLastName)
        ? new NameDto(
            nameCapitalizer.capitalize(match(lowercaseFirstName, maxDistance)),
            nameCapitalizer.capitalize(lowercaseLastName),
            true)
        : new NameDto(null, null, false);
  }

  private boolean isValidFormat(String firstName, String lastName) {
    return FIRST_NAME_REGEX.matcher(firstName).matches()
        && LAST_NAME_REGEX.matcher(lastName).matches();
  }

  private String match(String name, int maxDistance) {
    Collection<String> names = namesProvider.readNames();
    return names.contains(name) ? name : nameMatcher.match(name, names, maxDistance);
  }
}
