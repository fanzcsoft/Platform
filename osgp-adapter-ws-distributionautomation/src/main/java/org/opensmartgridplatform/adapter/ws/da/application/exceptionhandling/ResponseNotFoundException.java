/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.da.application.exceptionhandling;

import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;

public class ResponseNotFoundException extends OsgpException {

    private static final long serialVersionUID = 1706342594144271262L;

    public ResponseNotFoundException(final ComponentType componentType, final String message) {
        super(componentType, message);
    }
}
