package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.sdk.modeldriven.Sid;

import java.util.Objects;
import java.util.UUID;

/**
 * Containing all necessary information and configuration parameters
 * of the {@link BasicMachine}.
 *
 * @author Lars Atzinger latzinger@evoila.de
 */
public class BasicMachineInfo {

    public static final String TYPE = "BasicMachineInfo";

    private final Sid id;

    private String owner;
    private String name;
    private String ipAddress;
    private String dnsName;
    private String cpu;
    private String memory;
    private String operatingSystem;
    private String diskSize;
    private String powerState;
    private String snapshot;
    private String initialUsername;
    private String initialPassword;
    private String description;
    private String json;

    public BasicMachineInfo(Sid id) {
        super();
        this.id = id;
    }

    public BasicMachineInfo() {
        super();
        id = Sid.valueOf(UUID.randomUUID().toString());
    }

    public BasicMachineInfo(String owner, String name, String ipAddress, String dnsName, String cpu, String memory, String operatingSystem, String diskSize, String powerState, String snapshot, String initialUsername, String initialPassword, String description, String json) {
        super();
        id = Sid.valueOf(UUID.randomUUID().toString());
        this.owner = owner;
        this.name = name;
        this.ipAddress = ipAddress;
        this.dnsName = dnsName;
        this.cpu = cpu;
        this.memory = memory;
        this.operatingSystem = operatingSystem;
        this.diskSize = diskSize;
        this.powerState = powerState;
        this.snapshot = snapshot;
        this.initialUsername = initialUsername;
        this.initialPassword = initialPassword;
        this.description = description;
        this.json = json;
    }

    /**
     * @return name of the {@link BasicMachine}
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of the {@link BasicMachine}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return ip-address of the {@link BasicMachine}
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress of the {@link BasicMachine}
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return dns-name of the {@link BasicMachine}
     */
    public String getDnsName() {
        return dnsName;
    }

    /**
     * @param dnsName of the {@link BasicMachine}
     */
    public void setDnsName(String dnsName) {
        this.dnsName = dnsName;
    }

    /**
     * @return cpus of the {@link BasicMachine}
     */
    public String getCpu() {
        return cpu;
    }

    /**
     * @param cpu of the {@link BasicMachine}
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    /**
     * @return memory of the {@link BasicMachine}
     */
    public String getMemory() {
        return memory;
    }

    /**
     * @param memory of the {@link BasicMachine}
     */
    public void setMemory(String memory) {
        this.memory = memory;
    }

    /**
     * @return operating system of the {@link BasicMachine}
     */
    public String getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @param operatingSystem of the {@link BasicMachine}
     */
    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * @return disk size of the {@link BasicMachine}
     */
    public String getDiskSize() {
        return diskSize;
    }

    /**
     * @param diskSize of the {@link BasicMachine}
     */
    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
    }

    /**
     * @return power state of the {@link BasicMachine}
     */
    public String getPowerState() {
        return powerState;
    }

    /**
     * @param powerState of the {@link BasicMachine}
     */
    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    /**
     * @return snapshots of the {@link BasicMachine}
     */
    public String getSnapshot() {
        return snapshot;
    }

    /**
     * @param snapshot of the {@link BasicMachine}
     */
    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * @return initial username of the {@link BasicMachine}
     */
    public String getInitialUsername() {
        return initialUsername;
    }

    /**
     * @param initialUsername of the {@link BasicMachine}
     */
    public void setInitialUsername(String initialUsername) {
        this.initialUsername = initialUsername;
    }

    /**
     * @return initial password of the {@link BasicMachine}
     */
    public String getInitialPassword() {
        return initialPassword;
    }

    /**
     * @param initialPassword of the {@link BasicMachine}
     */
    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    /**
     * @return description of the {@link BasicMachine}
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description of the {@link BasicMachine}
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return json attached to the {@link BasicMachine}
     */
    public String getJson() {
        return json;
    }

    /**
     * @param json which should attached to the {@link BasicMachine}
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * @return ID of the {@link BasicMachine}
     */
    public Sid getId() {
        return id;
    }

    /**
     * @return owner of the {@link BasicMachine}
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner which should bet set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicMachineInfo that = (BasicMachineInfo) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public String toString() {
        return "BasicMachineInfo{" +
                "id=" + id +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", dnsName='" + dnsName + '\'' +
                ", cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", diskSize='" + diskSize + '\'' +
                ", powerState='" + powerState + '\'' +
                ", snapshot='" + snapshot + '\'' +
                ", initialUsername='" + initialUsername + '\'' +
                ", initialPassword='" + initialPassword + '\'' +
                ", description='" + description + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
