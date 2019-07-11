package com.wojciechdm.corrector.names;

import org.apache.commons.text.WordUtils;

class NameCapitalizer {

  String capitalize(String name) {
    return WordUtils.capitalize(name, '-');
  }
}
