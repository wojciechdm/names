package com.wojciechdm.corrector.names;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import info.debatty.java.stringsimilarity.Damerau;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.Stream;

class NameMatcherTest {

  private NameMatcher nameMatcher;
  private Damerau damerauMock = mock(Damerau.class);

  @BeforeEach
  void setUp() {
    nameMatcher = new NameMatcher(damerauMock);
  }

  @AfterEach
  void tearDown() {
    reset(damerauMock);
  }

  @ParameterizedTest
  @MethodSource("namesAndDistances")
  void shouldMatchName(
      String nameToMatch,
      List<String> existingNames,
      int maxDistance,
      double firstDistance,
      double secondDistance,
      double thirdDistance,
      String expected) {
    // given
    given(damerauMock.distance(nameToMatch, existingNames.get(0))).willReturn(firstDistance);
    given(damerauMock.distance(nameToMatch, existingNames.get(1))).willReturn(secondDistance);
    given(damerauMock.distance(nameToMatch, existingNames.get(2))).willReturn(thirdDistance);
    // when
    String actual = nameMatcher.match(nameToMatch, existingNames, maxDistance);
    // then
    assertEquals(expected, actual);
  }

  static Stream<Arguments> namesAndDistances() {
    return Stream.of(
        Arguments.of(
            "adam", List.of("first", "second", "third"), 4, 3.0, 3.0, 3.0, "first"),
        Arguments.of(
            "adam", List.of("first", "second", "third"), 3, 3.0, 3.0, 3.0, "first"),
        Arguments.of(
            "adam", List.of("first", "second", "third"), 2, 3.0, 3.0, 3.0, "adam"),
        Arguments.of(
            "adam", List.of("first", "second", "third"), 3, 3.0, 1.0, 2.0, "second"),
        Arguments.of(
            "adam", List.of("first", "second", "third"), 3, 4.0, 2.0, 1.0, "third"),
        Arguments.of(
            "adam", List.of("first", "second", "third"), 3, 1.0, 3.0, 3.0, "first"));
  }
}
