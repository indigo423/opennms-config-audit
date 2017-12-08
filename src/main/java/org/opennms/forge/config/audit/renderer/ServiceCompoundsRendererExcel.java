package org.opennms.forge.config.audit.renderer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.opennms.forge.config.audit.model.ServiceCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ServiceCompoundsRendererExcel {
    private final static String TABEL_HEADER = "\tPollerConfiguration\t\t\tDatacollection\t\t\tRequisitions\t\nService Name\tDefinition\tClass\tProblems\tDefinition\tClass\tProblems\tForced\tDetector\tUsed\tProblems";
    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceCompoundsRendererExcel.class);
    private final File OUT_FILE;

    public ServiceCompoundsRendererExcel(File OUT_FILE) {
        this.OUT_FILE = OUT_FILE;
    }

    public void render(Map<String, ServiceCompound> serviceCompounds) throws WriteException {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(OUT_FILE);
            WritableSheet sheet = workbook.createSheet("Results", 0);

            //Write Header into Sheet
            String[] headers = TABEL_HEADER.split("\n");
            Integer rowIndex = 0;
            Integer cellIndex = 0;
            for (String header : headers) {
                String[] headerRow = header.split("\t");
                for (String headerCell : headerRow) {
                    Label lable = new Label(cellIndex, rowIndex, headerCell);
                    sheet.addCell(lable);
                    cellIndex++;
                }
                rowIndex++;
                cellIndex = 0;
            }

            //Write ServiceCompounds
            for (ServiceCompound serviceCompound : serviceCompounds.values()) {
                serviceCompound.checkServiceCompound();
                Label lable = new Label(cellIndex, rowIndex, serviceCompound.getName());
                sheet.addCell(lable);
                cellIndex++;

                if (serviceCompound.getPollerService() != null) {
                    lable = new Label(cellIndex, rowIndex, "X");
                } else {
                    lable = new Label(cellIndex, rowIndex, "");
                }
                sheet.addCell(lable);
                cellIndex++;

                if (serviceCompound.getMonitor() != null) {
                    lable = new Label(cellIndex, rowIndex, serviceCompound.getMonitor().getClassName().substring(serviceCompound.getMonitor().getClassName().lastIndexOf(".") + 1));
                } else {
                    lable = new Label(cellIndex, rowIndex, "");
                }
                sheet.addCell(lable);
                cellIndex++;

                lable = new Label(cellIndex, rowIndex, serviceCompound.getPollerProblem().trim());
                sheet.addCell(lable);
                cellIndex++;

                if (serviceCompound.getCollectdService() != null) {
                    lable = new Label(cellIndex, rowIndex, "X");
                } else {
                    lable = new Label(cellIndex, rowIndex, "");
                }
                sheet.addCell(lable);
                cellIndex++;

                if (serviceCompound.getCollector() != null) {
                    lable = new Label(cellIndex, rowIndex, serviceCompound.getCollector().getClassName().substring(serviceCompound.getCollector().getClassName().lastIndexOf(".") + 1));
                } else {
                    lable = new Label(cellIndex, rowIndex, "");
                }
                sheet.addCell(lable);
                cellIndex++;

                lable = new Label(cellIndex, rowIndex, serviceCompound.getCollectorProblem().trim());
                sheet.addCell(lable);
                cellIndex++;

                if (serviceCompound.getRequisitionMonitoredService() != null) {
                    lable = new Label(cellIndex, rowIndex, "X");
                } else {
                    lable = new Label(cellIndex, rowIndex, "");
                }
                sheet.addCell(lable);
                cellIndex++;

                if (serviceCompound.getDetector() != null) {
                    lable = new Label(cellIndex, rowIndex, serviceCompound.getDetector().getPluginClass().substring(serviceCompound.getDetector().getPluginClass().lastIndexOf(".") + 1));
                } else {
                    lable = new Label(cellIndex, rowIndex, "");
                }
                sheet.addCell(lable);
                cellIndex++;

                if (serviceCompound.isProvisioned()) {
                    lable = new Label(cellIndex, rowIndex, "X");
                } else {
                    lable = new Label(cellIndex, rowIndex, "");
                }
                sheet.addCell(lable);
                cellIndex++;

                lable = new Label(cellIndex, rowIndex, serviceCompound.getProblems());
                sheet.addCell(lable);
                cellIndex++;

                rowIndex++;
                cellIndex = 0;
            }
            workbook.write();
            workbook.close();
        } catch (IOException ex) {
            LOGGER.error("Writing the Excel file faild for {}", OUT_FILE.getAbsolutePath(), ex);
        }
    }
}
