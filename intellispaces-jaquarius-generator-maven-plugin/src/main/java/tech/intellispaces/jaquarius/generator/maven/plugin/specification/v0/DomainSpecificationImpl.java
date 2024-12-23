package tech.intellispaces.jaquarius.generator.maven.plugin.specification.v0;

import java.util.List;

record DomainSpecificationImpl(
    String label,
    String did,
    String description,
    List<ParentDomainSpecification> parents
) implements DomainSpecification {
}
