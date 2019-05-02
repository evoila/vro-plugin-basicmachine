package de.evoila.vro.o11n.plugin.basicmachine.config;

import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;

public interface ConfigChangeListener {

    void basicMachineSaved(BasicMachineInfo machineInfo);

    void basicMachineUpdated(BasicMachineInfo machineInfo);

    void basicMachineDeleted(BasicMachineInfo machineInfo);

}
