package de.evoila.vro.o11n.plugin.basicmachine;

import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;
import de.evoila.vro.o11n.plugin.basicmachine.finder.BasicMachineFinder;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import de.evoila.vro.o11n.plugin.basicmachine.relater.RootHasConnections;

public class CustomMapping extends AbstractMapping {
    @SuppressWarnings("unchecked")
    @Override
    public void define() {
        wrap(BasicMachine.class).andFind().using(BasicMachineFinder.class);
        relateRoot().to(BasicMachine.class).using(RootHasConnections.class).as("basicmachines");
    }
}