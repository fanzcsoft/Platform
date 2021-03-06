/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.messageprocessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.opensmartgridplatform.adapter.domain.smartmetering.application.services.MonitoringService;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.OsgpCoreResponseMessageProcessor;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceFunction;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.AlarmRegisterResponseDto;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.DeviceMessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;

@Component
public class ReadAlarmRegisterResponseMessageProcessor extends OsgpCoreResponseMessageProcessor {

    @Autowired
    private MonitoringService monitoringService;

    protected ReadAlarmRegisterResponseMessageProcessor() {
        super(DeviceFunction.READ_ALARM_REGISTER);
    }

    @Override
    protected boolean hasRegularResponseObject(final ResponseMessage responseMessage) {
        return responseMessage.getDataObject() instanceof AlarmRegisterResponseDto;
    }

    @Override
    protected void handleMessage(final DeviceMessageMetadata deviceMessageMetadata,
            final ResponseMessage responseMessage, final OsgpException osgpException) {

        final AlarmRegisterResponseDto alarmRegisterDto = (AlarmRegisterResponseDto) responseMessage.getDataObject();

        this.monitoringService.handleReadAlarmRegisterResponse(deviceMessageMetadata, responseMessage.getResult(),
                osgpException, alarmRegisterDto);
    }
}
