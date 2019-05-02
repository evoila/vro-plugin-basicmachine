package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.sdk.modeldriven.Sid;

import java.util.Objects;

public class BasicMachineInfo{

    public static final String TYPE = "BasicMachineInfo";

    private final Sid id;

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
        id = Sid.unique();
    }

    public BasicMachineInfo(String name, String ipAddress, String dnsName, String cpu, String memory, String operatingSystem, String diskSize, String powerState, String snapshot, String initialUsername, String initialPassword, String description, String json) {
        super();
        id = Sid.unique();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDnsName() {
        return dnsName;
    }

    public void setDnsName(String dnsName) {
        this.dnsName = dnsName;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
    }

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getInitialUsername() {
        return initialUsername;
    }

    public void setInitialUsername(String initialUsername) {
        this.initialUsername = initialUsername;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Sid getId() {
        return id;
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
        return "BasicMachineInfo["
                + "id=" + id
                + " name=" + name
                + " ipAddress=" + ipAddress
                + " dnsName=" + dnsName
                + " cpu=" + cpu
                + " memory=" + memory
                + " operatingSystem=" + operatingSystem
                + " diskSize=" + diskSize
                + " powerState=" + powerState
                + " snapshot=" + snapshot
                + " initialUsername=" + initialUsername
                + " initialPassword=" + initialPassword
                + " description=" + description
                + " json=" + json + "]";
    }

}
