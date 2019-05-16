package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.google.common.base.Objects;
import com.vmware.o11n.sdk.modeldriven.Findable;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.config.BasicMachineEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represents a BasicMachine which containing different information and configuration parameters.
 * All these information are capsuled in an Object of the type {@link BasicMachineInfo}.
 *
 * @author Lars Atzinger latzinger@evoila.de
 */
@Component
@Qualifier(value = "basicMachine")
@Scope(value = "prototype")
public class BasicMachine implements Findable {

    public static final String TYPE = "BasicMachine";

    private static final Logger LOG = LoggerFactory.getLogger(BasicMachine.class);

    @Autowired
    private BasicMachineEndpoint basicMachineEndpoint;

    private BasicMachineInfo machineInfo;

    public BasicMachine(BasicMachineInfo machineInfo) {
        this.machineInfo = machineInfo;
    }

    public BasicMachineInfo getMachineInfo() {
        return machineInfo;
    }

    /**
     * {@inheritDoc}
     *
     * @return sid of this {@link BasicMachine}
     */
    @Override
    public Sid getInternalId() {
        return machineInfo.getId();
    }

    /**
     * This method is unused because the ID is generated in the constructor.
     *
     * @param sid
     */
    @Override
    public void setInternalId(Sid sid) {
        // set via constructor
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicMachine that = (BasicMachine) o;
        return Objects.equal(machineInfo, that.machineInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(machineInfo);
    }

    @Override
    public String toString() {
        return "BasicMachine{" +
                machineInfo +
                '}';
    }

    /**
     * Updates the {@link BasicMachineInfo} when the endpoint has changed its configuration.
     * This method is called by the subscribed endpoint automatically.
     *
     * @param basicMachineInfo which was updated
     */
    public synchronized void update(BasicMachineInfo basicMachineInfo) {

        if (basicMachineInfo == null) {
            LOG.error("Failed while updating BasicMachine (basicMachineInfo is null)!");
            throw new RuntimeException("Failed while updating BasicMachine (basicMachineInfo is null)!");
        }

        if (!(basicMachineInfo.getId().equals(getInternalId()))) {
            LOG.error("Failed while updating BasicMachine (ID's are not matching).");
            throw new RuntimeException("Failed while updating BasicMachine (ID's are not matching).");
        }

        this.machineInfo = basicMachineInfo;
        LOG.debug(basicMachineInfo + " has changed its configuration.");
    }

    /**
     * Forces the endpoint configuration to update its persisted configuration/resource.
     */
    private void stateChanged() {
        basicMachineEndpoint.update(machineInfo);
    }

    /**
     * @return the name of the {@link BasicMachine}
     */
    public String getName() {
        return machineInfo.getName();
    }

    /**
     * Changes the name of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param name which should be set
     */
    public void setName(String name) {
        machineInfo.setName(name);
        stateChanged();
    }

    /**
     * @return the ip-address of the {@link BasicMachine}
     */
    public String getIpAddress() {
        return machineInfo.getIpAddress();
    }

    /**
     * Changes the ip-address of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param ipAddress which should be set
     */
    public void setIpAddress(String ipAddress) {
        machineInfo.setIpAddress(ipAddress);
        stateChanged();
    }

    /**
     * @return dns-name of the {@link BasicMachine}
     */
    public String getDnsName() {
        return machineInfo.getDnsName();
    }

    /**
     * Changes the dns-name of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param dnsName which should be set
     */
    public void setDnsName(String dnsName) {
        machineInfo.setDnsName(dnsName);
        stateChanged();
    }

    /**
     * @return the amount of cpus of the {@link BasicMachine}
     */
    public String getCpu() {
        return machineInfo.getCpu();
    }

    /**
     * Changes the amount of cpus of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param cpu which should be set
     */
    public void setCpu(String cpu) {
        machineInfo.setCpu(cpu);
        stateChanged();
    }

    /**
     * @return the amount of memory of the {@link BasicMachine}
     */
    public String getMemory() {
        return machineInfo.getMemory();
    }

    /**
     * Changes the amount of memory of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param memory which should be set
     */
    public void setMemory(String memory) {
        machineInfo.setMemory(memory);
        stateChanged();
    }

    /**
     * @return operating system of the {@link BasicMachine}
     */
    public String getOperatingSystem() {
        return machineInfo.getOperatingSystem();
    }

    /**
     * Changes the operating system of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param operatingSystem which should be set
     */
    public void setOperatingSystem(String operatingSystem) {
        machineInfo.setOperatingSystem(operatingSystem);
        stateChanged();
    }

    /**
     * @return disk size of thr {@link BasicMachine}
     */
    public String getDiskSize() {
        return machineInfo.getDiskSize();
    }

    /**
     * Changes the amount of disk size of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param diskSize which should be set
     */
    public void setDiskSize(String diskSize) {
        machineInfo.setDiskSize(diskSize);
        stateChanged();
    }

    /**
     * @return power state of the {@link BasicMachine}
     */
    public String getPowerState() {
        return machineInfo.getPowerState();
    }

    /**
     * Changes the power state of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param powerState which should be set
     */
    public void setPowerState(String powerState) {
        machineInfo.setPowerState(powerState);
        stateChanged();
    }

    /**
     * @return the snapshots of the {@link BasicMachine}
     */
    public String getSnapshot() {
        return machineInfo.getSnapshot();
    }

    /**
     * Changes the snapshots of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param snapshot which should be set
     */
    public void setSnapshot(String snapshot) {
        machineInfo.setSnapshot(snapshot);
        stateChanged();
    }

    /**
     * @return the initial username of the {@link BasicMachine}
     */
    public String getInitialUsername() {
        return machineInfo.getInitialUsername();
    }

    /**
     * Changes the initial username of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param initialUsername which should be set
     */
    public void setInitialUsername(String initialUsername) {
        machineInfo.setInitialUsername(initialUsername);
        stateChanged();
    }

    /**
     * @return the initial password of the {@link BasicMachine}
     */
    public String getInitialPassword() {
        return machineInfo.getInitialPassword();
    }

    /**
     * Changes the initial password of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param initialPassword which should be set
     */
    public void setInitialPassword(String initialPassword) {
        machineInfo.setInitialPassword(initialPassword);
        stateChanged();
    }

    /**
     * @return the description of the {@link BasicMachine}
     */
    public String getDescription() {
        return machineInfo.getDescription();
    }

    /**
     * Changes the amount of disk size of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param description which should be set
     */
    public void setDescription(String description) {
        machineInfo.setDescription(description);
        stateChanged();
    }

    /**
     * @return the attached json string
     */
    public String getJson() {
        return machineInfo.getDescription();
    }

    /**
     * Changes the attached json string of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param json which should be set
     */
    public void setJson(String json) {
        machineInfo.setJson(json);
        stateChanged();
    }

    /**
     * Changes the owner of the {@link BasicMachine} and forces the endpoint to update its
     * persisted configuration/resource.
     *
     * @param owner which should be set
     */
    public void setOwner(String owner) {
        machineInfo.setOwner(owner);
        stateChanged();
    }

    /**
     * @return the owner of the {@link BasicMachine}
     */
    public String getOwner() {
        return machineInfo.getOwner();
    }

    /**
     * @return the display name which will shown in the inventory of the plugin
     */
    public String getDisplayName() {
        return getMachineInfo().getName();
    }


}
