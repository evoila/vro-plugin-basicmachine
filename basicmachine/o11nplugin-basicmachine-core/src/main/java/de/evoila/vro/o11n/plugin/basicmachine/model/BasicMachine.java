package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.sdk.modeldriven.Findable;
import com.vmware.o11n.sdk.modeldriven.Sid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Qualifier(value = "basicmachine")
@Scope(value = "prototype")
public class BasicMachine implements Findable {

    public static final String TYPE = "BasicMachine";

    private final Sid id;
    private String name;
    private String ipAddress;
    private String dnsName;
    private String cpu;
    private String memory;
    private String operatingSystem;
    private String disksize;
    private String powerState;
    private String snapshot;
    private String initialUsername;
    private String initialPassword;
    private String description;
    private String json;

    public BasicMachine(){
        this.id = Sid.unique();
    }

    public BasicMachine(String name, String ipAddress, String dnsName, String cpu, String memory, String operatingSystem, String disksize, String powerState, String snapshot, String initialUsername, String initialPassword, String description, String json) {
        this();
        this.name = name;
        this.ipAddress = ipAddress;
        this.dnsName = dnsName;
        this.cpu = cpu;
        this.memory = memory;
        this.operatingSystem = operatingSystem;
        this.disksize = disksize;
        this.powerState = powerState;
        this.snapshot = snapshot;
        this.initialUsername = initialUsername;
        this.initialPassword = initialPassword;
        this.description = description;
        this.json = json;
    }

    @Override
    public Sid getInternalId() {
        return id;
    }

    @Override
    public void setInternalId(Sid sid) {
        // will be initialized via constructor
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

    public String getDisksize() {
        return disksize;
    }

    public void setDisksize(String disksize) {
        this.disksize = disksize;
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
}
