package de.evoila.vro.o11n.plugin.basicmachine.relater;

import com.vmware.o11n.sdk.modeldriven.ObjectRelater;
import com.vmware.o11n.sdk.modeldriven.PluginContext;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.config.LocalRepository;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BasicMachineRelater implements ObjectRelater<BasicMachine> {

    @Autowired
    private LocalRepository localRepository;

    @Override
    public List<BasicMachine> findChildren(PluginContext pluginContext, String s, String s1, Sid sid) {
        return new ArrayList<>(localRepository.findAll());
    }

}
