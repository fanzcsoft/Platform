/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.tariffswitching.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.opensmartgridplatform.adapter.domain.tariffswitching.application.mapping.DomainTariffSwitchingMapper;
import org.opensmartgridplatform.adapter.domain.tariffswitching.infra.jms.core.OsgpCoreRequestMessageSender;
import org.opensmartgridplatform.adapter.domain.tariffswitching.infra.jms.ws.WebServiceResponseMessageSender;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.Organisation;
import org.opensmartgridplatform.domain.core.entities.Ssld;
import org.opensmartgridplatform.domain.core.exceptions.UnknownEntityException;
import org.opensmartgridplatform.domain.core.repositories.SsldRepository;
import org.opensmartgridplatform.domain.core.services.DeviceDomainService;
import org.opensmartgridplatform.domain.core.services.OrganisationDomainService;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalExceptionType;

public class AbstractService {

    @Autowired
    protected DeviceDomainService deviceDomainService;

    @Autowired
    protected OrganisationDomainService organisationDomainService;

    @Autowired
    protected SsldRepository ssldRepository;

    @Autowired
    @Qualifier("domainTariffSwitchingOutgoingOsgpCoreRequestMessageSender")
    protected OsgpCoreRequestMessageSender osgpCoreRequestMessageSender;

    @Autowired
    protected DomainTariffSwitchingMapper domainCoreMapper;

    @Autowired
    @Qualifier("domainTariffSwitchingOutgoingWebServiceResponseMessageSender")
    protected WebServiceResponseMessageSender webServiceResponseMessageSender;

    protected Device findActiveDevice(final String deviceIdentification) throws FunctionalException {
        return this.deviceDomainService.searchActiveDevice(deviceIdentification, ComponentType.DOMAIN_TARIFF_SWITCHING);
    }

    protected Organisation findOrganisation(final String organisationIdentification) throws FunctionalException {
        Organisation organisation;
        try {
            organisation = this.organisationDomainService.searchOrganisation(organisationIdentification);
        } catch (final UnknownEntityException e) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION,
                    ComponentType.DOMAIN_TARIFF_SWITCHING, e);
        }
        return organisation;
    }

    protected Ssld getSsldForDevice(final Device device) {
        return this.ssldRepository.findOne(device.getId());
    }
}