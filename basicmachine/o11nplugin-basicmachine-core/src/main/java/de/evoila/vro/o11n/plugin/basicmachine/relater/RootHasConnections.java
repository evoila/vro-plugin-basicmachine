package de.evoila.vro.o11n.plugin.basicmachine.relater;

import com.vmware.o11n.sdk.modeldriven.ObjectRelater;
import com.vmware.o11n.sdk.modeldriven.PluginContext;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;

import java.util.List;

public class RootHasConnections implements ObjectRelater<BasicMachine> {

    @Override
    public List<BasicMachine> findChildren(PluginContext pluginContext, String s, String s1, Sid sid) {
        return null;
    }

}
