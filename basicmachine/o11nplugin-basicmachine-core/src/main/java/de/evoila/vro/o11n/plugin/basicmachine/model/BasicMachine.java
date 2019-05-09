/*
 * Copyright (C) 2019 Lars Atzinger latzinger@evoila.de
 */

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

    @Override
    public Sid getInternalId() {
        return machineInfo.getId();
    }

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
                "machineInfo=" + machineInfo +
                '}';
    }

    public synchronized void update(BasicMachineInfo machineInfo) {

        if (machineInfo != null && !(this.machineInfo.getId().equals(machineInfo.getId()))) {
            throw new RuntimeException("BasicMachine ID does not match. Can not update different Object");
        }

        this.machineInfo = machineInfo;
    }

    private void stateChanged(){
        basicMachineEndpoint.update(machineInfo);
    }

    public String getName() {
        return machineInfo.getName();
    }

    public void setName(String name) {
        machineInfo.setName(name);
        stateChanged();
    }

    public String getIpAddress() {
        return machineInfo.getIpAddress();
    }

    public void setIpAddress(String ipAddress) {
        machineInfo.setIpAddress(ipAddress);
        stateChanged();
    }

    public String getDnsName() {
        return machineInfo.getDnsName();
    }

    public void setDnsName(String dnsName) {
        machineInfo.setDnsName(dnsName);
        stateChanged();
    }

    public String getCpu() {
        return machineInfo.getCpu();
    }

    public void setCpu(String cpu) {
        machineInfo.setCpu(cpu);
        stateChanged();
    }

    public String getMemory() {
        return machineInfo.getMemory();
    }

    public void setMemory(String memory) {
        machineInfo.setMemory(memory);
        stateChanged();
    }

    public String getOperatingSystem() {
        return machineInfo.getOperatingSystem();
    }

    public void setOperatingSystem(String operatingSystem) {
        machineInfo.setOperatingSystem(operatingSystem);
        stateChanged();
    }

    public String getDiskSize() {
        return machineInfo.getDiskSize();
    }

    public void setDiskSize(String diskSize) {
        machineInfo.setDiskSize(diskSize);
        stateChanged();
    }

    public String getPowerState() {
        return machineInfo.getPowerState();
    }

    public void setPowerState(String powerState) {
        machineInfo.setPowerState(powerState);
        stateChanged();
    }

    public String getSnapshot() {
        return machineInfo.getSnapshot();
    }

    public void setSnapshot(String snapshot) {
        machineInfo.setSnapshot(snapshot);
        stateChanged();
    }

    public String getInitialUsername() {
        return machineInfo.getInitialUsername();
    }

    public void setInitialUsername(String initialUsername) {
        machineInfo.setInitialUsername(initialUsername);
        stateChanged();
    }

    public String getInitialPassword() {
        return machineInfo.getInitialPassword();
    }

    public void setInitialPassword(String initialPassword) {
        machineInfo.setInitialPassword(initialPassword);
        stateChanged();
    }

    public String getDescription() {
        return machineInfo.getDescription();
    }

    public void setDescription(String description) {
        machineInfo.setDescription(description);
        stateChanged();
    }

    public String getJson() {
        return machineInfo.getDescription();
    }

    public void setJson(String json) {
        machineInfo.setJson(json);
        stateChanged();
    }

    public String getDisplayName() {
        return getMachineInfo().getName() + " [" + getMachineInfo().getIpAddress() + "]";
    }



}
