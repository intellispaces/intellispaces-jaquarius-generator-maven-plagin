package tech.intellispaces.jaquarius.generator.maven.plugin.propeties;

import org.apache.maven.plugin.MojoExecutionException;
import tech.intellispaces.general.collection.CollectionFunctions;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.jaquarius.generator.maven.plugin.properties.Properties;

import java.util.List;
import java.util.Map;

class MapBasedProperties implements Properties {
  private final String path;
  private final String name;
  private final Map<String, Object> map;

  MapBasedProperties(String path, String name, Map<String, Object> map) {
    this.path = path;
    this.name = name;
    this.map = map;
  }

  @Override
  public String path() {
    return path;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String readString(String propertyName) throws MojoExecutionException {
    Object value = map.get(propertyName);
    if (value == null) {
      throw new MojoExecutionException("The property '" + joinPath(propertyName) + "' is not found");
    }
    if (value instanceof String) {
      return (String) value;
    }
    throw new MojoExecutionException("Value of the property " + joinPath(propertyName) + " is not string");
  }

  @Override
  public String readStringNullable(String propertyName) throws MojoExecutionException {
    Object value = map.get(propertyName);
    if (value == null) {
      return null;
    }
    if (value instanceof String) {
      return (String) value;
    }
    throw new MojoExecutionException("Value of the property " + joinPath(propertyName) + " is not string");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Properties readProperties(String propertyName) throws MojoExecutionException {
    Object value = map.get(propertyName);
    if (value == null) {
      throw new MojoExecutionException("The property '" + joinPath(propertyName) + "' is not found");
    }
    if (value instanceof Map) {
      return PropertiesProvider.get(joinPath(propertyName), propertyName, (Map<String, Object>) value);
    }
    throw new MojoExecutionException("Value of the property " + joinPath(propertyName) + " is not map");
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Properties> readLabeledPropertiesList(String propertyName) throws MojoExecutionException {
    Object value = map.get(propertyName);
    if (value == null) {
      throw new MojoExecutionException("The property '" + joinPath(propertyName) + "' is not found");
    }
    if (value instanceof Map) {
      var valueMap = (Map<String, Object>) value;
      return CollectionFunctions.mapEach(valueMap.entrySet(), e -> {
        if (e.getValue() instanceof Map) {
          return PropertiesProvider.get(
              joinPath(propertyName, e.getKey()),
              e.getKey(),
              (Map<String, Object>) e.getValue()
          );
        } else {
          throw new MojoExecutionException("Value of the property " + joinPath(propertyName, e.getKey()) + " is not map");
        }
      });
    }
    throw new MojoExecutionException("Value of the property " + joinPath(propertyName) + " is not list");
  }

  String joinPath(String secondPath) {
    return StringFunctions.join(path, secondPath, ".");
  }

  String joinPath(String secondPath, String thirdPath) {
    return StringFunctions.join(path, secondPath, thirdPath, ".");
  }
}
