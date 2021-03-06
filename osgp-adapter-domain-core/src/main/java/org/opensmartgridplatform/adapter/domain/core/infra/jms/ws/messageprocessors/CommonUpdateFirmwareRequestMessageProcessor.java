/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.core.infra.jms.ws.messageprocessors;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.opensmartgridplatform.adapter.domain.core.application.services.FirmwareManagementService;
import org.opensmartgridplatform.adapter.domain.core.infra.jms.ws.WebServiceRequestMessageProcessor;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceFunction;
import org.opensmartgridplatform.domain.core.valueobjects.FirmwareUpdateMessageDataContainer;
import org.opensmartgridplatform.shared.infra.jms.Constants;
import org.opensmartgridplatform.shared.wsheaderattribute.priority.MessagePriorityEnum;

/**
 * Class for processing common update firmware request messages
 */
@Component("domainCoreCommonUpdateFirmwareRequestMessageProcessor")
public class CommonUpdateFirmwareRequestMessageProcessor extends WebServiceRequestMessageProcessor {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUpdateFirmwareRequestMessageProcessor.class);

    @Autowired
    @Qualifier("domainCoreFirmwareManagementService")
    private FirmwareManagementService firmwareManagementService;

    public CommonUpdateFirmwareRequestMessageProcessor() {
        super(DeviceFunction.UPDATE_FIRMWARE);
    }

    @Override
    public void processMessage(final ObjectMessage message) {
        LOGGER.debug("Processing common update firmware request message");

        String correlationUid = null;
        String messageType = null;
        int messagePriority = MessagePriorityEnum.DEFAULT.getPriority();
        String organisationIdentification = null;
        String deviceIdentification = null;
        Boolean isScheduled = null;
        Long scheduleTime = null;

        try {
            correlationUid = message.getJMSCorrelationID();
            messageType = message.getJMSType();
            messagePriority = message.getJMSPriority();
            organisationIdentification = message.getStringProperty(Constants.ORGANISATION_IDENTIFICATION);
            deviceIdentification = message.getStringProperty(Constants.DEVICE_IDENTIFICATION);
            isScheduled = message.getBooleanProperty(Constants.IS_SCHEDULED);
            if (message.propertyExists(Constants.SCHEDULE_TIME)) {
                scheduleTime = message.getLongProperty(Constants.SCHEDULE_TIME);
            }
        } catch (final JMSException e) {
            LOGGER.error("UNRECOVERABLE ERROR, unable to read ObjectMessage instance, giving up.", e);
            LOGGER.debug("correlationUid: {}", correlationUid);
            LOGGER.debug("messageType: {}", messageType);
            LOGGER.debug("messagePriority: {}", messagePriority);
            LOGGER.debug("organisationIdentification: {}", organisationIdentification);
            LOGGER.debug("deviceIdentification: {}", deviceIdentification);
            LOGGER.debug("isScheduled: {}", isScheduled);
            return;
        }

        try {
            final FirmwareUpdateMessageDataContainer firmwareUpdateMessageDataContainer = (FirmwareUpdateMessageDataContainer) message
                    .getObject();

            LOGGER.info("Calling application service function: {}", messageType);

            this.firmwareManagementService.updateFirmware(organisationIdentification, deviceIdentification,
                    correlationUid, firmwareUpdateMessageDataContainer, scheduleTime, messageType, messagePriority);

        } catch (final Exception e) {
            this.handleError(e, correlationUid, organisationIdentification, deviceIdentification, messageType,
                    messagePriority);
        }
    }
}
