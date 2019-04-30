package de.evoila.vro.o11n.plugin.basicmachine.config;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
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
        return localStorage.get(id);
    }

    public Collection<BasicMachine> findAll(){
        return localStorage.values();
    }

    @Override
    public void basicMachineSaved(BasicMachine basicMachine) {
        BasicMachine basicMachineToSave = (BasicMachine) applicationContext.getBean("basicMachine", basicMachine);
        localStorage.put(basicMachine.getId(), basicMachineToSave);
    }

    @Override
    public void basicMachineUpdated(BasicMachine basicMachine) {
        throw new UnsupportedOperationException("basicMachineUpdated(BasicMachine basicMachine) is not implemented yet!");
    }

    @Override
    public void basicMachineDeleted(BasicMachine basicMachine) {
        localStorage.remove(basicMachine.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        configPersister.registerChangeListener(this);
        configPersister.refresh();

    }

}
