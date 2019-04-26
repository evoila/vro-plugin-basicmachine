package de.evoila.vro.o11n.plugin.basicmachine.config;

import ch.dunes.vso.sdk.endpoints.IEndpointConfiguration;
import ch.dunes.vso.sdk.endpoints.IEndpointConfigurationService;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigPersisterImpl implements ConfigPersister {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigPersisterImpl.class);

    private final Collection<ConfigChangeListener> listeners;

    @Autowired
    IEndpointConfigurationService endpointConfigurationService;

    public ConfigPersisterImpl() {
        listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<BasicMachine> findAll() {

        Collection<IEndpointConfiguration> configurations;

        try {
            configurations = endpointConfigurationService.getEndpointConfigurations();

            List<BasicMachine> basicMachines = new ArrayList<>(configurations.size());

            for(IEndpointConfiguration config: configurations){

                BasicMachine basicMachine = convertToBasicMachine(config);

                if(basicMachine != null){
                    basicMachines.add(basicMachine);
                    LOG.debug("Added "  + basicMachine + "\n");
                }

            }

            return basicMachines;


        } catch (IOException e) {
            LOG.error("Failed to read configurations!", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public BasicMachine findById(Sid id) {
        return null;
    }

    @Override
    public BasicMachine save(BasicMachine basicMachine) {
        return null;
    }

    @Override
    public void delete(BasicMachine basicMachine) {

    }

    @Override
    public void addChangeListener(ConfigChangeListener configChangeListener) {
        listeners.add(configChangeListener);
    }

    @Override
    public void refresh() {

    }

    private BasicMachine convertToBasicMachine(IEndpointConfiguration iEndpointConfiguration){

        BasicMachine basicMachine = null;

        try{

            Sid sid = Sid.valueOf(iEndpointConfiguration.getString("id"));

            basicMachine = new BasicMachine(sid);

            basicMachine.setName(iEndpointConfiguration.getString("name"));
            basicMachine.setIpAddress(iEndpointConfiguration.getString("ipAddress"));
            basicMachine.setDnsName(iEndpointConfiguration.getString("dnsName"));
            basicMachine.setCpu(iEndpointConfiguration.getString("cpu"));
            basicMachine.setMemory(iEndpointConfiguration.getString("memory"));
            basicMachine.setOperatingSystem(iEndpointConfiguration.getString("operatingSystem"));
            basicMachine.setDiskSize(iEndpointConfiguration.getString("diskSize"));
            basicMachine.setPowerState(iEndpointConfiguration.getString("powerState"));
            basicMachine.setSnapshot(iEndpointConfiguration.getString("snapshot"));
            basicMachine.setInitialUsername(iEndpointConfiguration.getString("initialUsername"));
            basicMachine.setInitialPassword(iEndpointConfiguration.getString("initialPassword"));
            basicMachine.setDescription(iEndpointConfiguration.getString("description"));
            basicMachine.setJson(iEndpointConfiguration.getString("json"));

        } catch (IllegalArgumentException e){
            LOG.warn("Can not convert IEndpointConfiguration[" + iEndpointConfiguration.getId() + "] to Type BasicMachine!", e);
        }

        return basicMachine;
    }

}
