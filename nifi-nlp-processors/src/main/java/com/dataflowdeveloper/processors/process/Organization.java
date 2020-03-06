package com.dataflowdeveloper.processors.process;

import java.util.StringJoiner;

/**
 *
 */
public class Organization {

    private String organization;

    public Organization() {
        super();
    }

    public Organization(String organization) {
        super();
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return new StringJoiner( ", ", Organization.class.getSimpleName() + "[", "]" )
                .add( "organization='" + organization + "'" )
                .toString();
    }
}
