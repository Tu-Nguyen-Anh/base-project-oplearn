package org.oplearn.project.service;

import java.util.List;
import java.util.Set;

public interface PresenceService {
    void setOnline(Long userId);

    void setOffline(Long userId);

    boolean isOnline(Long userId);

    Set<Long> getOnlineUserIds(List<Long> userIds);

    Long getLastSeen(Long userId);
}
