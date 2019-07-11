package com.wojciechdm.corrector.names;

public class NameDto {

  private String firstName;
  private String lastName;
  private boolean isValid;

  public NameDto(String firstName, String lastName, boolean isValid) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.isValid = isValid;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public boolean isValid() {
    return isValid;
  }
}
