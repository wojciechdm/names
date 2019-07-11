package com.wojciechdm.corrector.names;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.Stream;

class NameCorrectorTest {

  private NameCorrector nameCorrector;
  private NameCapitalizer nameCapitalizerMock = mock(NameCapitalizer.class);
  private NameMatcher nameMatcherMock = mock(NameMatcher.class);
  private NamesProvider namesProviderMock = mock(NamesProvider.class);

  @BeforeEach
  void setUp() {
    nameCorrector =
        new NameCorrector(nameCapitalizerMock, nameMatcherMock, namesProviderMock, 3);
  }

  @AfterEach
  void tearDown() {
    reset(nameCapitalizerMock);
    reset(nameMatcherMock);
    reset(namesProviderMock);
  }

  @ParameterizedTest
  @MethodSource("validNames")
  void shouldReturnCorrectedName(
      String firstName, String lastName,
      String firstNameExpected, String lastNameExpected,
      List<String> namesToUppercase,
      List<String> uppercaseName,
      String matchingName) {
    // given
    when(nameCapitalizerMock.capitalize(namesToUppercase.get(0)))
        .thenReturn(uppercaseName.get(0));
    when(nameCapitalizerMock.capitalize(namesToUppercase.get(1)))
        .thenReturn(uppercaseName.get(1));
    when(nameMatcherMock.match(anyString(), anyCollection(), anyInt()))
        .thenReturn(matchingName);
    when(namesProviderMock.readNames()).thenReturn(List.of("", ""));
    boolean isValidExpected = true;
    // when
    NameDto nameDtoActual = nameCorrector.correct(firstName, lastName);
    String firstNameActual = nameDtoActual.getFirstName();
    String lastNameActual = nameDtoActual.getLastName();
    boolean isValidActual = nameDtoActual.isValid();
    // then
    assertEquals(firstNameExpected, firstNameActual);
    assertEquals(lastNameExpected, lastNameActual);
    assertEquals(isValidExpected, isValidActual);
  }

  @ParameterizedTest
  @MethodSource("invalidNames")
  void shouldReturnNameDtoWithFalseIsValidFieldInsteadCorrectedNameWhenNameIsInvalid(String firstName, String lastName) {
    // given
    when(nameCapitalizerMock.capitalize(anyString())).thenReturn("");
    when(nameMatcherMock.match(anyString(), anyCollection(), anyInt()))
        .thenReturn("");
    when(namesProviderMock.readNames()).thenReturn(List.of());
    boolean isValidExpected = false;
    // when
    NameDto nameDtoActual = nameCorrector.correct(firstName, lastName);
    boolean isValidActual = nameDtoActual.isValid();
    String firstNameActual = nameDtoActual.getFirstName();
    String lastNameActual = nameDtoActual.getLastName();
    // then
    assertEquals(isValidExpected, isValidActual);
    assertNull(firstNameActual);
    assertNull(lastNameActual);
  }

  static Stream<Arguments> validNames() {
    return Stream.of(
        Arguments.of(
            "adam", "nowak",
            "Adam", "Nowak",
            List.of("adam", "nowak"),
            List.of("Adam", "Nowak"),
            "adam"),
        Arguments.of(
            "adam", "nowak",
            "Adam", "Nowak",
            List.of("adam", "nowak"),
            List.of("Adam", "Nowak"),
           "adam"),
        Arguments.of(
            "adma", "nowak",
            "Adam", "Nowak",
            List.of("adam", "nowak"),
            List.of("Adam", "Nowak"),
            "adam"),
        Arguments.of(
            "  adam  ", " nowak  ",
            "Adam", "Nowak",
            List.of("adam", "nowak"),
            List.of("Adam", "Nowak"),
            "adam"),
        Arguments.of(
            "adam", "nowak-kowalski",
            "Adam", "Nowak-Kowalski",
            List.of("adam", "nowak-kowalski"),
            List.of("Adam", "Nowak-Kowalski"),
            "adam"),
        Arguments.of(
            "aDAm", "Nowak",
            "Adam", "Nowak",
            List.of("adam", "nowak"),
            List.of("Adam", "Nowak"),
           "adam"),
        Arguments.of(
            "ADAM", "NOWAK",
            "Adam", "Nowak",
            List.of("adam", "nowak"),
            List.of("Adam", "Nowak"),
            "adam"));
  }

  static Stream<Arguments> invalidNames() {
    return Stream.of(
        Arguments.of("adam", "00nowak"),
        Arguments.of("", ""),
        Arguments.of("adam", "-nowak"),
        Arguments.of("adam49", "nowak"),
        Arguments.of("_adam", "nowak"),
        Arguments.of("6566", "4454545"),
        Arguments.of("   ", "    "),
        Arguments.of("!@#$%^", "*&(_)"));
  }
}
