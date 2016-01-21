package org.jboss.jbossset.bugclerk.aphrodite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.jbossset.bugclerk.utils.URLUtils;
import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.config.AphroditeConfig;
import org.jboss.set.aphrodite.config.IssueTrackerConfig;
import org.jboss.set.aphrodite.config.TrackerType;
import org.jboss.set.aphrodite.domain.Comment;
import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.aphrodite.spi.AphroditeException;
import org.jboss.set.aphrodite.spi.NotFoundException;

public final class AphroditeClient {

    private final Aphrodite aphrodite;

    private static final int DEFAULT_ISSUE_LIMIT = 500;

    public AphroditeClient(IssueTrackerConfig issueTrackerConfig) {
        try {
            List<IssueTrackerConfig> issueTrackerConfigs = new ArrayList<IssueTrackerConfig>(1);
            issueTrackerConfigs.add(issueTrackerConfig);
            aphrodite = Aphrodite.instance(AphroditeConfig.issueTrackersOnly(issueTrackerConfigs));
        } catch (AphroditeException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Issue> retrievePayload(String filterUrl) {
        try {
            return aphrodite.searchIssuesByFilter(URLUtils.createURLFromString(filterUrl));
        } catch (NotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void addComments(Map<Issue, Comment> comments) {
        aphrodite.addCommentToIssue(comments);
    }

    public List<Issue> loadIssues(List<String> ids) {
        // FIXME: add bulk method in Aphrodite
        List<Issue> issues = new ArrayList<Issue>(ids.size());
        for (String id : ids)
            try {
                issues.add(aphrodite.getIssue(URLUtils.createURLFromString(id)));
            } catch (NotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        return issues;
    }

    public static IssueTrackerConfig buildTrackerConfig(String trackerUrl, String username, String password, TrackerType type) {
        return new IssueTrackerConfig(URLUtils.getServerUrl(trackerUrl), username, password, type, DEFAULT_ISSUE_LIMIT);
    }
}
