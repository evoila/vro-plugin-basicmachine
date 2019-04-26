package de.evoila.vro.o11n.plugin.basicmachine.config;

import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;

public interface ConfigChangeListener {

    void basicMachineSaved(BasicMachine basicMachine);

    void basicMachineUpdated(BasicMachine basicMachine);

    void basicMachineDeleted(BasicMachine basicMachine);

}
