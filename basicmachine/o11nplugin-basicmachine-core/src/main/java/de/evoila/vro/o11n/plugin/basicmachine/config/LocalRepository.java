package de.evoila.vro.o11n.plugin.basicmachine.config;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocalRepository implements ConfigChangeListener, InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ConfigPersisterImpl configPersister;

    private Map<Sid, BasicMachine> localStorage;

    public LocalRepository() {
        localStorage = new ConcurrentHashMap<>();
    }

    public BasicMachine findById(Sid id){
        return localStorage.get(id.getId());
    }

    public Collection<BasicMachine> findAll(){
        return localStorage.values();
    }

    @Override
    public void basicMachineSaved(BasicMachineInfo machineInfo) {
        BasicMachine newMachine = (BasicMachine) applicationContext.getBean("basicMachine", machineInfo);
        localStorage.put(machineInfo.getId(), newMachine);
    }

    @Override
    public void basicMachineUpdated(BasicMachineInfo machineInfo) {
        throw new UnsupportedOperationException("basicMachineUpdated(BasicMachine basicMachine) is not implemented yet!");
    }

    @Override
    public void basicMachineDeleted(BasicMachineInfo machineInfo) {
        localStorage.remove(machineInfo.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        configPersister.registerChangeListener(this);
        configPersister.reload();

    }

}
