package de.evoila.vro.o11n.plugin.basicmachine.finder;

import com.vmware.o11n.sdk.modeldriven.FoundObject;
import com.vmware.o11n.sdk.modeldriven.ObjectFinder;
import com.vmware.o11n.sdk.modeldriven.PluginContext;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;

import java.util.List;

public class BasicMachineFinder implements ObjectFinder<BasicMachine> {

    @Override
    public BasicMachine find(PluginContext pluginContext, String s, Sid sid) {
        return null;
    }

    @Override
    public List<FoundObject<BasicMachine>> query(PluginContext pluginContext, String s, String s1) {
        return null;
    }

    @Override
    public Sid assignId(BasicMachine basicMachine, Sid sid) {
        return null;
    }
}
