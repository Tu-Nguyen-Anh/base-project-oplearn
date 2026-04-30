package org.oplearn.project.repository.projection;

public interface TopicMonthlyCountProjection {
    Long getTopicId();
    String getTopicName();
    Integer getMonth();
    Long getCount();
}
