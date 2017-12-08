package org.opennms.forge.config.audit.model;

import java.util.Objects;
import org.opennms.netmgt.config.collectd.Collector;
import org.opennms.netmgt.config.poller.Monitor;
import org.opennms.netmgt.config.poller.Service;
import org.opennms.netmgt.provision.persist.foreignsource.PluginConfig;
import org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService;

public class ServiceCompound {
    private final String name;
    private org.opennms.netmgt.config.poller.Service pollerService = null;
    private Monitor monitor = null;
    private String pollerProblem = "";
    private org.opennms.netmgt.config.collectd.Service collectdService = null;
    private org.opennms.netmgt.config.collectd.Collector collector = null;
    private String collectorProblem = "";
    private RequisitionMonitoredService requisitionMonitoredService = null;
    private PluginConfig detector = null;
    private Boolean provisioned = true;
    private String problems = "";
    
    public ServiceCompound(String name) {
        this.name = name;
    }

    public Service getPollerService() {
        return pollerService;
    }

    public void setPollerService(Service pollerService) {
        this.pollerService = pollerService;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public org.opennms.netmgt.config.collectd.Service getCollectdService() {
        return collectdService;
    }

    public void setCollectdService(org.opennms.netmgt.config.collectd.Service collectdService) {
        this.collectdService = collectdService;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public String getPollerProblem() {
        return pollerProblem;
    }

    public void setPollerProblem(String pollerProblem) {
        this.pollerProblem = pollerProblem;
    }

    public String getCollectorProblem() {
        return collectorProblem;
    }

    public void setCollectorProblem(String collectorProblem) {
        this.collectorProblem = collectorProblem;
    }

    public RequisitionMonitoredService getRequisitionMonitoredService() {
        return requisitionMonitoredService;
    }

    public void setRequisitionMonitoredService(RequisitionMonitoredService requisitionMonitoredService) {
        this.requisitionMonitoredService = requisitionMonitoredService;
    }

    public PluginConfig getDetector() {
        return detector;
    }

    public void setDetector(PluginConfig detector) {
        this.detector = detector;
    }

    public Boolean isProvisioned() {
        return provisioned;
    }

    public void setProvisioned(Boolean provisioned) {
        this.provisioned = provisioned;
    }

    public String getProblems() {
        return problems;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }

    public String getName() {
        return name;
    }

    public void checkServiceCompound() {
        //collection
        if (collectdService != null && collector != null) {
            collectorProblem = "";
        }
        if (collectdService != null && collector == null) {
            collectorProblem = collectorProblem.concat(" collectdService has no collector ");
        }
        if (collectdService == null && collector != null) {
            collectorProblem = collectorProblem.concat(" collector has no collectdService ");
        }
        
        //polling
        if (pollerService != null && monitor != null) {
            pollerProblem = "";
        }
        if (pollerService != null && monitor == null) {
            pollerProblem = pollerProblem.concat(" pollerService has no monitor ");
        }
        if (pollerService == null && monitor != null) {
            pollerProblem = pollerProblem.concat(" monitor has no pollerService ");
        }
        
        //provisioned
        if (requisitionMonitoredService == null && detector == null) {
            provisioned = false;
        }
        
        if (provisioned) {
            if (pollerService == null && monitor == null && collectdService == null && collector == null) {
                problems = problems.concat("No Polling or Collecting configured for USED service!");
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServiceCompound other = (ServiceCompound) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "ServiceCompound{" + "name=" + name + ", pollerService=" + pollerService + ", monitor=" + monitor + ", pollerProblem=" + pollerProblem + ", collectdService=" + collectdService + ", collector=" + collector + ", collectorProblem=" + collectorProblem + ", requisitionMonitoredService=" + requisitionMonitoredService + ", detector=" + detector + ", provisioned=" + provisioned + ", problems=" + problems + '}';
    }

}
