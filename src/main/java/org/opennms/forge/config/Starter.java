package org.opennms.forge.config.audit;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.opennms.forge.config.audit.model.ServiceCompound;
import org.opennms.forge.config.audit.renderer.ServiceCompoundsRendererExcel;
import org.opennms.forge.config.audit.renderer.ServiceCompoundsRendererTabOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starter {

    private final static Logger LOGGER = LoggerFactory.getLogger(Starter.class);
    private final static String FOLDER_PARAMETER = "ConfigFolder";
    private final static String OUT_FILE = "OutPutFile";

    public static void main(String[] args) throws Exception {
        LOGGER.info("Hello World");
        File configFolder = null;
        if (System.getProperty(FOLDER_PARAMETER, null) != null) {
            configFolder = new File((String) System.getProperty(FOLDER_PARAMETER));

            if (configFolder.exists() && configFolder.canRead() && configFolder.isDirectory()) {
                LOGGER.info("Looking for opennms configs at {}", configFolder.getAbsolutePath());

            } else {
                LOGGER.error("Something is wrong with the provided {} with {}", FOLDER_PARAMETER, configFolder.getAbsolutePath());
                LOGGER.debug("Checking file {}", configFolder.getAbsolutePath());
                LOGGER.debug("exists {}", configFolder.exists());
                LOGGER.debug("canRead {}", configFolder.canRead());
                LOGGER.debug("isDirectory {}", configFolder.isDirectory());
                System.exit(1);
            }
        } else {
            LOGGER.error("Please add this parameter at starttime -D{}=YourConfigFolder", FOLDER_PARAMETER);
            LOGGER.error("If you want an excel file as output at -D{}=YourOutFile", OUT_FILE);
            System.exit(1);
        }

        Map<String, ServiceCompound> serviceCompounds = new TreeMap<>();

        PollerChecker pollerChecker = new PollerChecker(configFolder.getAbsolutePath());
        CollectionChecker collectionChecker = new CollectionChecker(configFolder.getAbsolutePath());
        RequisitionChecker requisitionChecker = new RequisitionChecker(configFolder.getAbsolutePath());

        serviceCompounds = pollerChecker.updateServiceCompounds(serviceCompounds);
        serviceCompounds = collectionChecker.updateServiceCompounds(serviceCompounds);
        serviceCompounds = requisitionChecker.updateServiceCompounds(serviceCompounds);

        if (System.getProperty(OUT_FILE, null) != null) {
            File outPutFile = new File(System.getProperty(OUT_FILE));
            ServiceCompoundsRendererExcel rendererExcel = new ServiceCompoundsRendererExcel(outPutFile);
            rendererExcel.render(serviceCompounds);
            LOGGER.info("Excle file was created at {}", outPutFile.getAbsolutePath());
        } else {
            ServiceCompoundsRendererTabOutput rendererTabOutput = new ServiceCompoundsRendererTabOutput();
            rendererTabOutput.render(serviceCompounds);
            LOGGER.info("If you want an excel file as output at -D{}=YourOutFile", OUT_FILE);
        }

        LOGGER.info("Thanks for computing with OpenNMS");
    }
}
