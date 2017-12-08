package org.opennms.forge.config.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.forge.config.audit.model.ServiceCompound;
import org.opennms.netmgt.config.CollectdConfig;
import org.opennms.netmgt.config.CollectdConfigFactory;
import org.opennms.netmgt.config.CollectdPackage;
import org.opennms.netmgt.config.collectd.Collector;
import org.opennms.netmgt.config.collectd.Service;

class CollectionChecker {

    private final String CONFIG_FOLDER;
    private final String CONFIG_FILE = "collectd-configuration.xml";

    CollectionChecker(final String CONFIG_FOLDER) {
        this.CONFIG_FOLDER = CONFIG_FOLDER;
    }

    Map<String, ServiceCompound> updateServiceCompounds(Map<String, ServiceCompound> serviceCompounds) throws IOException, MarshalException, ValidationException {
        CollectdConfig collectdConfiguration = readCollectdConfiguration();

        for (CollectdPackage collectdPackage : collectdConfiguration.getPackages()) {
            for (Service collectdService : collectdPackage.getPackage().getServiceCollection()) {
                if (!serviceCompounds.containsKey(collectdService.getName())) {
                    serviceCompounds.put(collectdService.getName(), new ServiceCompound(collectdService.getName()));
                }
                serviceCompounds.get(collectdService.getName()).setCollectdService(collectdService);
            }
        }
        for (Collector collector : collectdConfiguration.getConfig().getCollectorCollection()) {
            if (!serviceCompounds.containsKey(collector.getService())) {
                    serviceCompounds.put(collector.getService(), new ServiceCompound(collector.getService()));
                }
                serviceCompounds.get(collector.getService()).setCollector(collector);
        }
        return serviceCompounds;
    }

    private CollectdConfig readCollectdConfiguration() throws MarshalException, ValidationException, IOException, FileNotFoundException {
        CollectdConfigFactory.init();
        CollectdConfigFactory collectdConfigFactory = new CollectdConfigFactory(new FileInputStream(new File(CONFIG_FOLDER.concat(File.separator + CONFIG_FILE))), "LocalServer?", false);
        return collectdConfigFactory.getCollectdConfig();
    }
}
