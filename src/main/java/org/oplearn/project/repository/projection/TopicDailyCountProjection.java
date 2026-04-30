package org.oplearn.project.repository.projection;

public interface TopicDailyCountProjection {
    Long getTopicId();
    String getTopicName();
    Integer getDay();
    Long getCount();
}
