package de.evoila.vro.o11n.plugin.basicmachine;

import com.vmware.o11n.sdk.modeldriven.AbstractModelDrivenAdaptor;

public class BasicMachinePluginAdaptor extends AbstractModelDrivenAdaptor {
    private static final String[] CONFIG_LOCATIONS = { "classpath:de/evoila/vro/o11n/plugin/basicmachine/plugin.xml" };

    private static final String RUNTIME_PROPERTIES_LOCATION = "de/evoila/vro/o11n/plugin/basicmachine_gen/runtime-config.properties";

    @Override
    protected String[] getConfigLocations() {
        return CONFIG_LOCATIONS;
    }

    @Override
    protected String getRuntimeConfigurationPath() {
        return RUNTIME_PROPERTIES_LOCATION;
    }

}