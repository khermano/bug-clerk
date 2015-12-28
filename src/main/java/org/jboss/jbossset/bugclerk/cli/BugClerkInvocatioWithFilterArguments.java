package org.jboss.jbossset.bugclerk.cli;

import com.beust.jcommander.Parameter;

public class BugClerkInvocatioWithFilterArguments extends AbstractCommonArguments {

    @Parameter(names = { "-f", "--filter-url" }, description = "URL to search filter")
    private String filterURL;

    public String getFilterURL() {
        return filterURL;
    }

    public void setFilterURL(String filterURL) {
        this.filterURL = filterURL;
    }
}