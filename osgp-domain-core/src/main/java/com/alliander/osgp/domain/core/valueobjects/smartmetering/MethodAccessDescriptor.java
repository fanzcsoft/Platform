/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodAccessDescriptor implements Serializable {
    private static final long serialVersionUID = 8227300665604316411L;

    private final List<MethodAccessItem> methodAccesItem;

    public MethodAccessDescriptor(final List<MethodAccessItem> methodAccesItem) {
        this.methodAccesItem = Collections.unmodifiableList(methodAccesItem);
    }

    public List<MethodAccessItem> getMethodAccesItem() {
        return new ArrayList<>(this.methodAccesItem);
    }
}
