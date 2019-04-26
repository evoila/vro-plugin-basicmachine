package de.evoila.vro.o11n.plugin.basicmachine.config;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;

import java.util.List;

public interface ConfigPersister {

    List<BasicMachine> findAll();

    BasicMachine findById(Sid id);

    BasicMachine save(BasicMachine basicMachine);

    void delete(BasicMachine basicMachine);

    void addChangeListener(ConfigChangeListener configChangeListener);

    void refresh();

}
