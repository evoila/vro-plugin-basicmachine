package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.plugin.sdk.spring.platform.GlobalPluginNotificationHandler;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.config.BasicMachineEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class BasicMachineManager {

    private static final Logger LOG = LoggerFactory.getLogger(BasicMachineManager.class);

    @Autowired
    private BasicMachineEndpoint basicMachineEndpoint;
    @Autowired
    private GlobalPluginNotificationHandler notificationHandler;

    public String save(String name, String ipAddress, String dnsName, String cpu, String memory, String operatingSystem, String diskSize, String powerState, String snapshot, String initialUsername, String initialPassword, String description, String json) {

        BasicMachineInfo machineInfo = new BasicMachineInfo();
        machineInfo.setName(name);
        machineInfo.setIpAddress(ipAddress);
        machineInfo.setDnsName(dnsName);
        machineInfo.setCpu(cpu);
        machineInfo.setMemory(memory);
        machineInfo.setOperatingSystem(operatingSystem);
        machineInfo.setDiskSize(diskSize);
        machineInfo.setPowerState(powerState);
        machineInfo.setSnapshot(snapshot);
        machineInfo.setInitialUsername(initialUsername);
        machineInfo.setInitialPassword(initialPassword);
        machineInfo.setDescription(description);
        machineInfo.setJson(json);

        machineInfo = basicMachineEndpoint.save(machineInfo);

        notificationHandler.notifyElementsInvalidate();

        LOG.info("Saved BasicMachine{" +
                "machineInfo=" + machineInfo +
                '}');

        return machineInfo.getId().toString();
    }

    public void delete(String id) {

        try {
            Sid sid = Sid.valueOf(id);
            BasicMachineInfo machineInfo = basicMachineEndpoint.findById(sid);

            if (machineInfo != null)
                basicMachineEndpoint.delete(machineInfo);

        } catch (IllegalArgumentException e) {
            LOG.error("Failed to delete BasicMachine with id:" + id);
            throw new RuntimeException(e);
        }

    }

}
