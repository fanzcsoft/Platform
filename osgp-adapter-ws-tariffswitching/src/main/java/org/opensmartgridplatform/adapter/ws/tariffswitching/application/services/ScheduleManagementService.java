/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.tariffswitching.application.services;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import org.opensmartgridplatform.adapter.ws.tariffswitching.infra.jms.TariffSwitchingRequestMessage;
import org.opensmartgridplatform.adapter.ws.tariffswitching.infra.jms.TariffSwitchingRequestMessageSender;
import org.opensmartgridplatform.adapter.ws.tariffswitching.infra.jms.TariffSwitchingRequestMessageType;
import org.opensmartgridplatform.adapter.ws.tariffswitching.infra.jms.TariffSwitchingResponseMessageFinder;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.Organisation;
import org.opensmartgridplatform.domain.core.services.CorrelationIdProviderService;
import org.opensmartgridplatform.domain.core.validation.Identification;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceFunction;
import org.opensmartgridplatform.domain.core.valueobjects.Schedule;
import org.opensmartgridplatform.domain.core.valueobjects.ScheduleEntry;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.DeviceMessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;

@Service(value = "wsTariffSwitchingScheduleManagementService")
@Transactional(value = "transactionManager")
@Validated
public class ScheduleManagementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleManagementService.class);

    @Autowired
    private DomainHelperService domainHelperService;

    @Autowired
    private CorrelationIdProviderService correlationIdProviderService;

    @Autowired
    private TariffSwitchingRequestMessageSender tariffSwitchingRequestMessageSender;

    @Autowired
    private TariffSwitchingResponseMessageFinder tariffSwitchingResponseMessageFinder;

    /**
     * Constructor
     */
    public ScheduleManagementService() {
        // Parameterless constructor required for transactions...
    }

    public String enqueueSetTariffSchedule(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification,
            @NotNull @Size(min = 1, max = 50) @Valid final List<ScheduleEntry> mapAsList, final DateTime scheduledTime,
            final int messagePriority) throws FunctionalException {

        final Organisation organisation = this.domainHelperService.findOrganisation(organisationIdentification);
        final Device device = this.domainHelperService.findActiveDevice(deviceIdentification);

        this.domainHelperService.isAllowed(organisation, device, DeviceFunction.SET_TARIFF_SCHEDULE);
        this.domainHelperService.isInMaintenance(device);

        LOGGER.debug("enqueueSetTariffSchedule called with organisation {} and device {}", organisationIdentification,
                deviceIdentification);

        final String correlationUid = this.correlationIdProviderService.getCorrelationId(organisationIdentification,
                deviceIdentification);

        final Schedule schedule = new Schedule(mapAsList);

        final DeviceMessageMetadata deviceMessageMetadata = new DeviceMessageMetadata(deviceIdentification,
                organisationIdentification, correlationUid,
                TariffSwitchingRequestMessageType.SET_TARIFF_SCHEDULE.name(), messagePriority,
                scheduledTime == null ? null : scheduledTime.getMillis());

        final TariffSwitchingRequestMessage message = new TariffSwitchingRequestMessage.Builder()
                .deviceMessageMetadata(deviceMessageMetadata).request(schedule).build();

        this.tariffSwitchingRequestMessageSender.send(message);

        return correlationUid;
    }

    public ResponseMessage dequeueSetTariffScheduleResponse(final String correlationUid) throws OsgpException {

        return this.tariffSwitchingResponseMessageFinder.findMessage(correlationUid);
    }

}
