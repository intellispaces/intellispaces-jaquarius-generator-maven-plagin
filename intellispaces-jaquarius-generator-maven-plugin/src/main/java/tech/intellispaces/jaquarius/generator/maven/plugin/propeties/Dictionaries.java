package tech.intellispaces.jaquarius.generator.maven.plugin.propeties;

import tech.intellispaces.jaquarius.generator.maven.plugin.properties.Dictionary;

import java.util.Map;

public interface Dictionaries {

  static Dictionary get(Map<String, Object> map) {
    return get(null, null, map);
  }

  static Dictionary get(String path, String name, Map<String, Object> map) {
    return new MapBasedDictionary(path, name, map);
  }
}
