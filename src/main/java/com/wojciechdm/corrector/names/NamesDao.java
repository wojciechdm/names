package com.wojciechdm.corrector.names;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

class NamesDao implements NamesProvider {

  private DataSource dataSource;

  NamesDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Collection<String> readNames() {
    Collection<String> value = new LinkedList<>();
    ResultSet result;
    Statement statement;
    try (Connection connection = dataSource.getConnection()) {
      statement = connection.createStatement();
      result = statement.executeQuery("SELECT name FROM names.names");
      while (result.next()) {
        value.add(result.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return value;
  }
}
