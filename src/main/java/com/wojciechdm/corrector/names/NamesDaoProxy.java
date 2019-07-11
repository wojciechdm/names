package com.wojciechdm.corrector.names;

import static com.google.common.base.Preconditions.*;
import static java.time.Duration.*;
import static java.util.Objects.*;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Collection;

public class NamesDaoProxy implements NamesProvider {

  private NamesDao namesDao;
  private Collection<String> names;
  private LocalDateTime lastFetch;

  private NamesDaoProxy(DataSource dataSource) {
    this.namesDao = new NamesDao(dataSource);
  }

  public static NamesDaoProxy newInstance(final DataSource dataSource) {

    checkArgument(nonNull(dataSource), "dataSource can not be null");

    return new NamesDaoProxy(dataSource);
  }

  @Override
  public Collection<String> readNames() {
    if ((isNull(lastFetch)
        || isNull(names)
        || between(lastFetch, LocalDateTime.now()).toHours() >= 24)) {
      names = namesDao.readNames();
      lastFetch = LocalDateTime.now();
    }
    return names;
  }
}
