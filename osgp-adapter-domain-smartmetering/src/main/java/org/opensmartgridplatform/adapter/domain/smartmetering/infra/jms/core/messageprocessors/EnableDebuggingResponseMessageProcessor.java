/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.messageprocessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.opensmartgridplatform.adapter.domain.smartmetering.application.services.ManagementService;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.OsgpCoreResponseMessageProcessor;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceFunction;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.DeviceMessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;

@Component
public class EnableDebuggingResponseMessageProcessor extends OsgpCoreResponseMessageProcessor {

    @Autowired
    private ManagementService managementService;

    public EnableDebuggingResponseMessageProcessor() {
        super(DeviceFunction.ENABLE_DEBUGGING);
    }

    @Override
    protected boolean hasRegularResponseObject(final ResponseMessage responseMessage) {
        // Only the result is used, no need to check the dataObject.
        return true;
    }

    @Override
    protected void handleMessage(final DeviceMessageMetadata deviceMessageMetadata,
            final ResponseMessage responseMessage, final OsgpException osgpException) {

        this.managementService.handleEnableDebuggingResponse(deviceMessageMetadata, responseMessage.getResult(),
                osgpException);
    }
}