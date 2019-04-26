package de.evoila.vro.o11n.plugin.basicmachine;

import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;

public class CustomMapping extends AbstractMapping {
    @SuppressWarnings("unchecked")
    @Override
    public void define() {
        enumerate(java.math.RoundingMode.class);
    }
}