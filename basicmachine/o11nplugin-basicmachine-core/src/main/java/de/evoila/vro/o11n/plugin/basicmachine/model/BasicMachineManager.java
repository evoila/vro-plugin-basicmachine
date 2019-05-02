package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.plugin.sdk.spring.platform.GlobalPluginNotificationHandler;
import de.evoila.vro.o11n.plugin.basicmachine.config.ConfigPersisterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class BasicMachineManager {

    @Autowired
    private ConfigPersisterImpl configPersister;
    @Autowired
    private GlobalPluginNotificationHandler notificationHandler;

    public String save(String name){

        BasicMachineInfo machineInfo = new BasicMachineInfo();
        machineInfo.setName(name);

        machineInfo = configPersister.save(machineInfo);

        notificationHandler.notifyElementsInvalidate();

        return machineInfo.getId().toString();
    }

}
