/*
 * Copyright (C) 2019 Lars Atzinger latzinger@evoila.de
 */

package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.plugin.sdk.spring.platform.GlobalPluginNotificationHandler;
import com.vmware.o11n.sdk.modeldriven.FoundObject;
import com.vmware.o11n.sdk.modeldriven.ObjectFactory;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.config.BasicMachineEndpoint;
import de.evoila.vro.o11n.plugin.basicmachine.config.LocalRepository;
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
    private LocalRepository localRepository;
    @Autowired
    private GlobalPluginNotificationHandler notificationHandler;
    @Autowired
    public ObjectFactory objectFactory;

    /**
     * Saves the configuration/resource of type {@link BasicMachineInfo} on the endpoint
     * and caches the {@link BasicMachine} in local storage.
     *
     * @param name            of the machine
     * @param ipAddress       of the machine
     * @param dnsName         of the machine
     * @param cpu             amount
     * @param memory          size
     * @param operatingSystem of the machine
     * @param diskSize        of the machine
     * @param powerState      of the machine
     * @param snapshot
     * @param initialUsername initial username
     * @param initialPassword initial password
     * @param description     of the virtual machine
     * @param json            string
     * @return the newly generated ID of the saved machine
     */
    public String saveBasicMachine(String name, String ipAddress, String dnsName, String cpu, String memory, String operatingSystem, String diskSize, String powerState, String snapshot, String initialUsername, String initialPassword, String description, String json) {

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

    /**
     * Returns a {@link BasicMachine} from local storage by its ID.
     *
     * @param id of the {@link BasicMachine}
     * @return the {@link FoundObject} of type {@link BasicMachine}
     */
    public FoundObject<BasicMachine> getBasicMachineById(String id) {

        Sid sid = Sid.valueOf(id);
        BasicMachine basicMachine = localRepository.findById(sid);

        return convertToFoundObject(sid, BasicMachine.class, basicMachine);
    }

    /**
     * Deletes a {@link BasicMachine} from the endpoint and
     * removes it from local storage.
     *
     * @param id of the BasicMachine which should be deleted
     */
    public void deleteBasicMachine(String id) {

        try {
            Sid sid = Sid.valueOf(id);
            BasicMachine basicMachine = localRepository.findById(sid);

            basicMachineEndpoint.delete(basicMachine.getMachineInfo());

        } catch (IllegalArgumentException e) {
            LOG.error("Failed to delete BasicMachine with id:" + id);
            throw new RuntimeException(e);
        }

    }

    /**
     * Converts/wraps a {@link BasicMachine} into a {@link FoundObject}.
     * This method is needed because this class is a singleton and not a scripting class.
     *
     * @param rootId      if of the root
     * @param desiredType desired type
     * @param modelType   model type
     * @return the converted {@link FoundObject} of type {@link BasicMachine}
     */
    private FoundObject<BasicMachine> convertToFoundObject(Sid rootId, Class<BasicMachine> desiredType, BasicMachine modelType) {

        Sid id = objectFactory.assignId(desiredType, modelType, rootId);

        return new FoundObject(modelType, id);

    }

}
