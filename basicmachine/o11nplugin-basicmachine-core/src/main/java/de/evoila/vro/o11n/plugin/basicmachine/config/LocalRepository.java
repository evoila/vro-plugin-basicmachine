/*
 * Copyright (C) 2019 Lars Atzinger latzinger@evoila.de
 */

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
public class LocalRepository implements EndpointChangeListener, InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BasicMachineEndpoint basicMachineEndpoint;

    private Map<Sid, BasicMachine> basicmachines;

    public LocalRepository() {
        basicmachines = new ConcurrentHashMap<>();
    }

    public BasicMachine findById(Sid id) {
        return basicmachines.get(id.getId());
    }

    public Collection<BasicMachine> findAll() {
        return basicmachines.values();
    }

    @Override
    public void basicMachineSaved(BasicMachineInfo machineInfo) {
        BasicMachine newMachine = (BasicMachine) applicationContext.getBean("basicMachine", machineInfo);
        basicmachines.put(machineInfo.getId(), newMachine);
    }

    @Override
    public void basicMachineUpdated(BasicMachineInfo machineInfo) {

        BasicMachine basicMachine = basicmachines.get(machineInfo.getId());

        if(basicMachine != null){
            basicMachine.update(machineInfo);
        } else{
            basicMachine = (BasicMachine) applicationContext.getBean("basicMachine", machineInfo);
            basicmachines.put(machineInfo.getId(), basicMachine);
        }

    }

    @Override
    public void basicMachineDeleted(BasicMachineInfo machineInfo) {
        basicmachines.remove(machineInfo.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        basicMachineEndpoint.registerChangeListener(this);
        basicMachineEndpoint.reload();
    }

}
