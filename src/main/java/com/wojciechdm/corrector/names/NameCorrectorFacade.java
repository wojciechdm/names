package com.wojciechdm.corrector.names;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

import info.debatty.java.stringsimilarity.Damerau;

public class NameCorrectorFacade {

  private NameCorrector nameCorrector;

  private NameCorrectorFacade(NamesProvider namesProvider, int maxDistance) {
    nameCorrector =
        new NameCorrector(
            new NameCapitalizer(), new NameMatcher(new Damerau()), namesProvider, maxDistance);
  }

  public static NameCorrectorFacade newInstance(final NamesProvider namesProvider, final int maxDistance){

    checkArgument(nonNull(namesProvider), "namesProvider can not be null");

    return new NameCorrectorFacade(namesProvider, maxDistance);
  }

  public NameDto correct(final String firstName, final String lastName) {

    checkArgument(nonNull(firstName), "firstName can not be null");
    checkArgument(nonNull(lastName), "secondName can not be null");

    return nameCorrector.correct(firstName, lastName);
  }
}
