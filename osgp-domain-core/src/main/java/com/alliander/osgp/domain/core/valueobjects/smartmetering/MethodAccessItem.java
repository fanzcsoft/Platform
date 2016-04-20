/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;

public class MethodAccessItem implements Serializable {
    private static final long serialVersionUID = -4754883636623267158L;

    private final int methodId;
    private final MethodAccesModeType accessMode;

    public MethodAccessItem(final int methodId, final MethodAccesModeType accessMode) {
        this.methodId = methodId;
        this.accessMode = accessMode;
    }

    public int getMethodId() {
        return this.methodId;
    }

    public MethodAccesModeType getAccessMode() {
        return this.accessMode;
    }
}
