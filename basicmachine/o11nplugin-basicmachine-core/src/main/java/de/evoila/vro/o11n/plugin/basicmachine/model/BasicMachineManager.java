package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.plugin.sdk.annotation.VsoMethod;
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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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

        return convertToFoundObject(basicMachine);
    }

    /**
     * Returns all {@link BasicMachine} found on local storage.
     *
     * @return list containing all found {@link BasicMachine} or null
     */
    public List<FoundObject<BasicMachine>> allBasicMachines() {
        Collection<BasicMachine> basicMachines = localRepository.findAll();

        if (basicMachines.size() == 0)
            return null;

        return convertToFoundObjects(basicMachines);
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
     * @return array containing all found {@link BasicMachine} or null
     */
    public List<FoundObject<BasicMachine>> getBasicMachinesByOwner(String owner) {
        Collection<BasicMachine> basicMachines = localRepository.findAll();

        if (basicMachines.size() == 0)
            return null;

        List<BasicMachine> result = basicMachines.parallelStream().filter(basicMachine -> basicMachine.getOwner().equals(owner)).collect(Collectors.toList());

        return convertToFoundObjects(result);

    }

    /**
     * Searches all {@link BasicMachine} by a given name.
     *
     * @param name which should be filtered
     * @return array containing all found {@link BasicMachine} or null
     */
    public List<FoundObject<BasicMachine>> getBasicMachinesByName(String name) {
        Collection<BasicMachine> basicMachines = localRepository.findAll();

        if (basicMachines.size() == 0)
            return null;

        List<BasicMachine> result = basicMachines.parallelStream().filter(basicMachine -> basicMachine.getName().equals(name)).collect(Collectors.toList());

        return convertToFoundObjects(basicMachines);
    }

    /**
     * Searches all {@link BasicMachine} by a given ip-address.
     *
     * @param ipAddress which should be filtered
     * @return array containing all found {@link BasicMachine} or null
     */
    public List<FoundObject<BasicMachine>> getBasicMachinesByIpAddress(String ipAddress) {
        Collection<BasicMachine> basicMachines = localRepository.findAll();

        if (basicMachines.size() == 0)
            return null;

        List<BasicMachine> result = basicMachines.parallelStream().filter(basicMachine -> basicMachine.getIpAddress().equals(ipAddress)).collect(Collectors.toList());

        return convertToFoundObjects(basicMachines);

    }

    /**
     * Searches all {@link BasicMachine} by a given dns-name.
     *
     * @param dnsName which should be filtered
     * @return array containing all found {@link BasicMachine} or null
     */
    public List<FoundObject<BasicMachine>> getBasicMachinesByDnsName(String dnsName) {
        Collection<BasicMachine> basicMachines = localRepository.findAll();

        if (basicMachines.size() == 0)
            return null;

        List<BasicMachine> result = basicMachines.parallelStream().filter(basicMachine -> basicMachine.getDnsName().equals(dnsName)).collect(Collectors.toList());

        return convertToFoundObjects(basicMachines);
    }

    /**
     * Searches all {@link BasicMachine} by a given operating system.
     *
     * @param operatingSystem which should be filtered
     * @return array containing all found {@link BasicMachine} or null
     */
    public List<FoundObject<BasicMachine>> getBasicMachinesByOperatingSystem(String operatingSystem) {
        Collection<BasicMachine> basicMachines = localRepository.findAll();

        if (basicMachines.size() == 0)
            return null;

        List<BasicMachine> result = basicMachines.parallelStream().filter(basicMachine -> basicMachine.getOperatingSystem().equals(operatingSystem)).collect(Collectors.toList());

        return convertToFoundObjects(basicMachines);
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

    /**
     * Converts/wraps a {@link BasicMachine} into a {@link FoundObject}.
     *
     * @param basicMachine machine which should be converted
     * @return the converted {@link FoundObject} of type {@link BasicMachine}
     */
    private FoundObject<BasicMachine> convertToFoundObject(BasicMachine basicMachine) {
        return convertToFoundObject(basicMachine.getInternalId(), BasicMachine.class, basicMachine);
    }

    /**
     * Converts a collection of {@link BasicMachine} to an Array of {@link FoundObject} from Type {@link BasicMachine}.
     *
     * @param basicMachines collection of machines which should be converted
     * @return array of found {@link FoundObject} of Type {@link BasicMachine}
     */
    private FoundObject<BasicMachine>[] convertToFoundObjectsToArray(Collection<BasicMachine> basicMachines) {

        List<FoundObject<BasicMachine>> convertedMachines = basicMachines
                .stream()
                .map(basicMachine -> convertToFoundObject(basicMachine))
                .collect(Collectors.toList());

        FoundObject<BasicMachine>[] foundObjects = new FoundObject[convertedMachines.size()];

        for (int i = 0; i < foundObjects.length; i++) {
            foundObjects[i] = convertedMachines.get(i);
        }

        return foundObjects;
    }

    /**
     * Converts a collection of {@link BasicMachine} to an List of {@link FoundObject} from Type {@link BasicMachine}.
     *
     * @param basicMachines collection of machines which should be converted
     * @return list of found {@link FoundObject} of Type {@link BasicMachine}
     */
    private List<FoundObject<BasicMachine>> convertToFoundObjects(Collection<BasicMachine> basicMachines) {

        List<FoundObject<BasicMachine>> convertedMachines = basicMachines
                .stream()
                .map(basicMachine -> convertToFoundObject(basicMachine))
                .collect(Collectors.toList());

        return convertedMachines;
    }

}
