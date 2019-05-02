package de.evoila.vro.o11n.plugin.basicmachine;

import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;
import de.evoila.vro.o11n.plugin.basicmachine.finder.BasicMachineFinder;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineManager;
import de.evoila.vro.o11n.plugin.basicmachine.relater.BasicMachineRelater;

public class CustomMapping extends AbstractMapping {

    @Override
    public void define() {
        singleton(BasicMachineManager.class);
        wrap(BasicMachine.class).andFind().using(BasicMachineFinder.class).withIcon("default-16x16.png");
        relateRoot().to(BasicMachine.class).using(BasicMachineRelater.class).as("basicmachines");
    }
}