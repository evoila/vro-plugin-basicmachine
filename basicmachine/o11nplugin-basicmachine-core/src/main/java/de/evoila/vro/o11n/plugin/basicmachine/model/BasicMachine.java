package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.sdk.modeldriven.Findable;
import com.vmware.o11n.sdk.modeldriven.Sid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Qualifier(value = "basicMachine")
@Scope(value = "prototype")
public class BasicMachine implements Findable {

    public static final String TYPE = "BasicMachine";

    private BasicMachineInfo machineInfo;

    public BasicMachine(BasicMachineInfo machineInfo) {
        this.machineInfo = machineInfo;
    }

    public BasicMachineInfo getMachineInfo() {
        return machineInfo;
    }

    public String getDisplayName() {
        return getMachineInfo().getName() + " [" + getMachineInfo().getIpAddress() + "]";
    }

    @Override
    public Sid getInternalId() {
        return getMachineInfo().getId();
    }

    @Override
    public void setInternalId(Sid sid) {
        // set via constructor
    }
}
