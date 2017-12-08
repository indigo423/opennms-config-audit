package org.opennms.forge.config.audit;

import java.io.File;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.opennms.forge.config.audit.model.ServiceCompound;
import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.foreignsource.PluginConfig;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionInterface;
import org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;

class RequisitionChecker {

    private final String CONFIG_FOLDER;
    private final String IMPORTS_FOLDER = "imports" + File.separator;
    private final String FOREIGEN_SOURCES_FOLDER = "foreign-sources" + File.separator;

    RequisitionChecker(String CONFIG_FOLDER) {
        this.CONFIG_FOLDER = CONFIG_FOLDER;
    }

    Map<String, ServiceCompound> updateServiceCompounds(Map<String, ServiceCompound> serviceCompounds) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Requisition.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        File importsFolder = new File(CONFIG_FOLDER.concat(File.separator + IMPORTS_FOLDER));

        for (File file : importsFolder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                Requisition requisition = (Requisition) jaxbUnmarshaller.unmarshal(file);
                serviceCompounds = processRequisitions(requisition, serviceCompounds);
            }
        }

        jaxbContext = JAXBContext.newInstance(ForeignSource.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        importsFolder = new File(CONFIG_FOLDER.concat(File.separator + FOREIGEN_SOURCES_FOLDER));

        for (File file : importsFolder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                ForeignSource foreignSource = (ForeignSource) jaxbUnmarshaller.unmarshal(file);
                serviceCompounds = processForeignSources(foreignSource, serviceCompounds);
            }
        }

        return serviceCompounds;
    }

    private Map<String, ServiceCompound> processRequisitions(Requisition requisition, Map<String, ServiceCompound> serviceCompounds) {

        for (RequisitionNode node : requisition.getNodes()) {
            for (RequisitionInterface requisitionInterface : node.getInterfaces()) {
                for (RequisitionMonitoredService service : requisitionInterface.getMonitoredServices()) {
                    if (!serviceCompounds.containsKey(service.getServiceName())) {
                        serviceCompounds.put(service.getServiceName(), new ServiceCompound(service.getServiceName()));
                    }
                    serviceCompounds.get(service.getServiceName()).setRequisitionMonitoredService(service);
                }
            }
        }

        return serviceCompounds;
    }

    private Map<String, ServiceCompound> processForeignSources(ForeignSource foreignSource, Map<String, ServiceCompound> serviceCompounds) {
        for (PluginConfig detector : foreignSource.getDetectors()) {
            if (!serviceCompounds.containsKey(detector.getName())){
                serviceCompounds.put(detector.getName(), new ServiceCompound(detector.getName()));
            }
            serviceCompounds.get(detector.getName()).setDetector(detector);
        }
        return serviceCompounds;
    }
}
