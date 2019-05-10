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


/**
 * vRO Singleton for save, delete and update BasicMachines on the endpoint.
 *
 * @author Lars Atzinger latzinger@evoila.de
 */
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
     * @param owner           of the machine
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
    public String saveBasicMachine(String owner, String name, String ipAddress, String dnsName, String cpu, String memory, String operatingSystem, String diskSize, String powerState, String snapshot, String initialUsername, String initialPassword, String description, String json) {

        BasicMachineInfo basicMachineInfo = new BasicMachineInfo();
        basicMachineInfo.setOwner(owner);
        basicMachineInfo.setName(name);
        basicMachineInfo.setIpAddress(ipAddress);
        basicMachineInfo.setDnsName(dnsName);
        basicMachineInfo.setCpu(cpu);
        basicMachineInfo.setMemory(memory);
        basicMachineInfo.setOperatingSystem(operatingSystem);
        basicMachineInfo.setDiskSize(diskSize);
        basicMachineInfo.setPowerState(powerState);
        basicMachineInfo.setSnapshot(snapshot);
        basicMachineInfo.setInitialUsername(initialUsername);
        basicMachineInfo.setInitialPassword(initialPassword);
        basicMachineInfo.setDescription(description);
        basicMachineInfo.setJson(json);

        basicMachineInfo = basicMachineEndpoint.save(basicMachineInfo);

        notificationHandler.notifyElementsInvalidate();

        LOG.info("Saved " + basicMachineInfo);

        return basicMachineInfo.getId().toString();
    }

    /**
     * Returns a {@link BasicMachine} from local storage by its ID.
     *
     * @param id of the {@link BasicMachine}
     * @return the {@link FoundObject} of type {@link BasicMachine} or null
     */
    public FoundObject<BasicMachine> getBasicMachineById(String id) {

        Sid sid = Sid.valueOf(id);
        BasicMachine basicMachine = localRepository.findById(sid);

        if (basicMachine == null)
            return null;

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

            LOG.info("Deleted BasicMachine with id:" + id);

        } catch (IllegalArgumentException e) {
            LOG.error("Failed while deleting BasicMachine with id:" + id, e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Searches all {@link BasicMachine} by a given owner.
     *
     * @param owner which should be filtered
     * @return array containing all found {@link BasicMachine}
     */
    public FoundObject<BasicMachine>[] getBasicMachineByOwner(String owner) {
        return null;
    }

    /**
     * Searches all {@link BasicMachine} by a given name.
     *
     * @param name which should be filtered
     * @return array containing all found {@link BasicMachine}
     */
    public FoundObject<BasicMachine>[] getBasicMachineByName(String name) {
        return null;
    }

    /**
     * Searches all {@link BasicMachine} by a given ip-address.
     *
     * @param ipAddress which should be filtered
     * @return array containing all found {@link BasicMachine}
     */
    public FoundObject<BasicMachine>[] getBasicMachineByIpAddress(String ipAddress) {
        return null;
    }

    /**
     * Searches all {@link BasicMachine} by a given dns-name.
     *
     * @param dnsName which should be filtered
     * @return array containing all found {@link BasicMachine}
     */
    public FoundObject<BasicMachine>[] getBasicMachineByDnsName(String dnsName) {
        return null;
    }

    /**
     * Searches all {@link BasicMachine} by a given operating system.
     *
     * @param operatingSystem which should be filtered
     * @return array containing all found {@link BasicMachine}
     */
    public FoundObject<BasicMachine>[] getBasicMachineByOperatingSystem(String operatingSystem) {
        return null;
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
