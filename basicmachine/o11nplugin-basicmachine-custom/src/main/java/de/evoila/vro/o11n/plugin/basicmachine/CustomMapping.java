package de.evoila.vro.o11n.plugin.basicmachine;

import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;
import de.evoila.vro.o11n.plugin.basicmachine.finder.BasicMachineFinder;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;

public class CustomMapping extends AbstractMapping {
    @SuppressWarnings("unchecked")
    @Override
    public void define() {
        enumerate(java.math.RoundingMode.class);
        wrap(BasicMachine.class).andFind().using(BasicMachineFinder.class);
    }
}