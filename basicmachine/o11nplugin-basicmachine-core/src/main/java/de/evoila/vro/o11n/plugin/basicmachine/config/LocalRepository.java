package de.evoila.vro.o11n.plugin.basicmachine.config;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocalRepository implements ConfigChangeListener, ApplicationContextAware, InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ConfigPersister configPersister;

    private Map<Sid, BasicMachine> localStorage;

    public LocalRepository() {
        localStorage = new ConcurrentHashMap<Sid, BasicMachine>();
    }

    public BasicMachine findById(Sid id){
        return localStorage.get(id);
    }

    public Collection<BasicMachine> findAll(){
        return localStorage.values();
    }

    @Override
    public void basicMachineSaved(BasicMachine basicMachine) {

        BasicMachine tmp = localStorage.get(basicMachine.getId());

        if(tmp == null){
            tmp = (BasicMachine) applicationContext.getBean("basicMachine", basicMachine);
            localStorage.put(basicMachine.getId(), tmp);
        }

    }

    @Override
    public void basicMachineUpdated(BasicMachine basicMachine) {
        throw new UnsupportedOperationException("basicMachineUpdated(BasicMachine basicMachine) is not implemented yet!");
    }

    @Override
    public void basicMachineDeleted(BasicMachine basicMachine) {

        BasicMachine tmp = localStorage.remove(basicMachine.getId());

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        configPersister.registerChangeListener(this);
        configPersister.refresh();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
