package de.evoila.vro.o11n.plugin.basicmachine.config;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;

import java.util.List;

public interface ConfigPersister {

    List<BasicMachineInfo> findAll();

    BasicMachineInfo findById(Sid id);

    BasicMachineInfo save(BasicMachineInfo basicMachineInfo);

    void delete(BasicMachineInfo basicMachineInfo);

    void registerChangeListener(ConfigChangeListener configChangeListener);

    void reload();

}
