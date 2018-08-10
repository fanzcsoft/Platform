/**
 * Copyright 2018 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GpsCoordinates implements Serializable {

    private static final long serialVersionUID = 454785458685030204L;

    @Column
    protected Float latitude;
    @Column
    protected Float longitude;

    protected GpsCoordinates() {
        // Default constructor
    }

    public GpsCoordinates(final Float latitude, final Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    @Override
    public String toString() {
        return "GpsCoordinates [latitude=" + this.latitude + ", longitude=" + this.longitude + "]";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GpsCoordinates)) {
            return false;
        }

        final GpsCoordinates other = (GpsCoordinates) obj;
        return Objects.equals(this.latitude, other.latitude) && Objects.equals(this.longitude, other.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude);
    }
}