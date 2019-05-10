package de.evoila.vro.o11n.plugin.basicmachine;

import com.vmware.o11n.sdk.modeldrivengen.mapping.*;

import com.vmware.o11n.sdk.modeldrivengen.model.*;
import com.google.inject.*;

import java.util.*;

/**
 * @author Lars Atzinger latzinger@evoila.de
 */
public class CustomModule extends AbstractModule {

    private final Plugin plugin;

    @Override
    protected void configure() {
        bind(AbstractMapping.class).toInstance(new CustomMapping());
        bind(Plugin.class).toInstance(plugin);
    }

    public CustomModule() {
        this.plugin = new Plugin();

        plugin.setApiPrefix("");
        plugin.setIcon("folder.png");
        plugin.setDescription("BasicMachine Plugin");
        plugin.setBuild("");
        plugin.setDisplayName("BasicMachine");
        plugin.setName("BasicMachine");
        plugin.setPackages(Collections.singletonList("de.evoila.vro.o11n.plugin.basicmachine"));
        plugin.setAdaptorClassName(de.evoila.vro.o11n.plugin.basicmachine.BasicMachinePluginAdaptor.class);
    }
}