package org.opennms.forge.config.audit.renderer;

import java.util.Map;

import org.opennms.forge.config.audit.model.ServiceCompound;

import jxl.write.WriteException;

public class ServiceCompoundsRendererTabOutput {

    private final static String TABEL_HEADER = "\tPollerConfiguration\t\t\tDatacollection\t\t\tRequisitions\t\nService Name\tDefinition\tClass\tProblems\tDefinition\tClass\tProblems\tForced\tDetector\tUsed\tProblems";

    public void render(Map<String, ServiceCompound> serviceCompounds) throws WriteException {
        System.out.println("------------------------- COPY ME -------------------------");
        System.out.println(TABEL_HEADER);
        for (Map.Entry<String, ServiceCompound> entry : serviceCompounds.entrySet()) {
            System.out.println(render(entry.getValue()));
        }
        System.out.println("------------------------- All " + serviceCompounds.size() + " ServiceCompounds DONE -------------------------");
    }

    private String render(ServiceCompound sC) {
        sC.checkServiceCompound();
        StringBuilder sb = new StringBuilder();
        sb.append(sC.getName());
        sb.append("\t");

        if (sC.getPollerService() != null) {
            sb.append("X");
        } else {
            sb.append("");
        }
        sb.append("\t");

        if (sC.getMonitor() != null) {
            sb.append(sC.getMonitor().getClassName().substring(sC.getMonitor().getClassName().lastIndexOf(".") + 1));
        } else {
            sb.append("");
        }
        sb.append("\t");

        sb.append(sC.getPollerProblem().trim());
        sb.append("\t");

        if (sC.getCollectdService() != null) {
            sb.append("X");
        } else {
            sb.append("");
        }
        sb.append("\t");

        if (sC.getCollector() != null) {
            sb.append(sC.getCollector().getClassName().substring(sC.getCollector().getClassName().lastIndexOf(".") + 1));
        } else {
            sb.append("");
        }
        sb.append("\t");

        sb.append(sC.getCollectorProblem().trim());
        sb.append("\t");

        if (sC.getRequisitionMonitoredService() != null) {
            sb.append("X");
        } else {
            sb.append("");
        }
        sb.append("\t");

        if (sC.getDetector() != null) {
            sb.append(sC.getDetector().getPluginClass().substring(sC.getDetector().getPluginClass().lastIndexOf(".") + 1));
        } else {
            sb.append("");
        }
        sb.append("\t");

        if (sC.isProvisioned()) {
            sb.append("X");
        } else {
            sb.append("");
        }
        sb.append("\t");

        sb.append(sC.getProblems().trim());
        sb.append("\t");

        return sb.toString();
    }
}
