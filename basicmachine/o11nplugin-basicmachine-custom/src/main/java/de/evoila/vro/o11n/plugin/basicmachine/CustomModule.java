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
        plugin.setVersion("${project.version}");
        plugin.setApiPrefix("");
        plugin.setBuild("");
        plugin.setIcon("folder.png");
        plugin.setName("BasicMachine");
        plugin.setDisplayName("BasicMachine");
        plugin.setDescription("BasicMachine Plugin for vRealize Orchestrator");
        plugin.setPackages(Collections.singletonList("o11nplugin-basicmachine-package-${project.version}.package"));
        plugin.setAdaptorClassName(de.evoila.vro.o11n.plugin.basicmachine.BasicMachinePluginAdaptor.class);
    }
}