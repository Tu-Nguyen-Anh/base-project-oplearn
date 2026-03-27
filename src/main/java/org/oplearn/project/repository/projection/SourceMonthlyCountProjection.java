package org.oplearn.project.repository.projection;

public interface SourceMonthlyCountProjection {
    Long getSourceId();
    String getSourceName();
    Integer getMonth();
    Long getCount();
}
