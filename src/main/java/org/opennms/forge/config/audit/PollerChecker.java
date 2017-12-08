package org.opennms.forge.config.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.forge.config.audit.model.ServiceCompound;
import org.opennms.netmgt.config.PollerConfigFactory;
import org.opennms.netmgt.config.poller.Monitor;
import org.opennms.netmgt.config.poller.PollerConfiguration;
import org.opennms.netmgt.config.poller.Service;

public class PollerChecker {

    private final String CONFIG_FOLDER;
    private final String CONFIG_FILE = "poller-configuration.xml";

    PollerChecker(String CONFIG_FOLDER) {
        this.CONFIG_FOLDER = CONFIG_FOLDER;
    }

    Map<String, ServiceCompound> updateServiceCompounds(Map<String, ServiceCompound> serviceCompounds) throws IOException, MarshalException, ValidationException {
        PollerConfiguration pollerConfiguration = readPollerConfiguration();

        for (Monitor monitor : pollerConfiguration.getMonitorCollection()) {
            if (!serviceCompounds.containsKey(monitor.getService())) {
                serviceCompounds.put(monitor.getService(), new ServiceCompound(monitor.getService()));
            }
            serviceCompounds.get(monitor.getService()).setMonitor(monitor);
        }
        for (org.opennms.netmgt.config.poller.Package pollerPackage : pollerConfiguration.getPackageCollection()) {
            for (Service service : pollerPackage.getServiceCollection()) {
                if (!serviceCompounds.containsKey(service.getName())) {
                    serviceCompounds.put(service.getName(), new ServiceCompound(service.getName()));
                }
                serviceCompounds.get(service.getName()).setPollerService(service);
            }
        }
        return serviceCompounds;
    }

    private PollerConfiguration readPollerConfiguration() throws IOException, MarshalException, ValidationException {
        PollerConfigFactory.init();
        PollerConfigFactory pollerConfigFactory = new PollerConfigFactory(0, new FileInputStream(new File(CONFIG_FOLDER.concat(File.separator + CONFIG_FILE))), "LocalServer?", false);
        return pollerConfigFactory.getConfiguration();
    }
}
