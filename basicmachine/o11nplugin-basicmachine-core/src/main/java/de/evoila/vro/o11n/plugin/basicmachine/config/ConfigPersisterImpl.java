package de.evoila.vro.o11n.plugin.basicmachine.config;

import ch.dunes.vso.sdk.endpoints.IEndpointConfiguration;
import ch.dunes.vso.sdk.endpoints.IEndpointConfigurationService;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
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

            for (IEndpointConfiguration config : configurations) {

                BasicMachine basicMachine = convertToBasicMachine(config);

                if (basicMachine != null) {
                    basicMachines.add(basicMachine);
                    LOG.debug("Added " + basicMachine + "\n");
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

        if (id == null) {
            LOG.warn("Sid can not be null!");
            return null;
        }

        IEndpointConfiguration endpointConfiguration;

        try {
            endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(id.toString());

            return convertToBasicMachine(endpointConfiguration);

        } catch (IOException e) {
            LOG.error("Can not find BasicMachine with id:" + id.toString(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public BasicMachine save(BasicMachine basicMachine) {

        if (basicMachine == null || basicMachine.getId() == null) {
            LOG.error("Can not save BasicMachine. Is null or id is missing!");
            throw new RuntimeException("BasicMachine is invalid.");
        }

        if (basicMachineAlreadyExists(basicMachine))
            throw new RuntimeException("BasicMachine with same name already exists: " + basicMachine + "\n");

        try {

            IEndpointConfiguration endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(basicMachine.getId().toString());

            if (endpointConfiguration == null) {

                endpointConfiguration = convertToIEndpointConfiguration(basicMachine);

            }

            endpointConfigurationService.saveEndpointConfiguration(endpointConfiguration);

            notifyChangeListener(basicMachine);

            return basicMachine;

        } catch (IOException e) {
            LOG.error("Error saving BasicMachine: " + basicMachine, e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(BasicMachine basicMachine) {

        try {
            endpointConfigurationService.deleteEndpointConfiguration(basicMachine.getId().toString());
        } catch (IOException e) {
            LOG.error("Error while deleting endpoint configuration for BasicMachine: " + basicMachine);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void registerChangeListener(ConfigChangeListener configChangeListener) {
        listeners.add(configChangeListener);
    }

    @Override
    public void refresh() {

        Collection<BasicMachine> result = findAll();

        for (BasicMachine basicMachine : result) {
            notifyChangeListener(basicMachine);
        }

    }

    private boolean basicMachineAlreadyExists(BasicMachine basicMachine) {

        BasicMachine result = findBasicMachineByName(basicMachine.getName());

        if (result != null && !((result.getId().toString()).equals(basicMachine.getId().toString()))) {
            return true;
        }

        return false;
    }

    private BasicMachine findBasicMachineByName(String name) {

        if (name == null)
            throw new RuntimeException("Name can not be null!");

        Collection<BasicMachine> basicMachines = findAll();

        for (BasicMachine basicMachine : basicMachines) {
            if (name.equals(basicMachine.getName()))
                return basicMachine;
        }

        return null;
    }

    private IEndpointConfiguration convertToIEndpointConfiguration(BasicMachine basicMachine) {

        IEndpointConfiguration endpointConfiguration;

        try {
            endpointConfiguration = endpointConfigurationService.newEndpointConfiguration(basicMachine.getId().toString());

            endpointConfiguration.setString("id", basicMachine.getId().toString());
            //
            endpointConfiguration.setString("name", basicMachine.getName());
            endpointConfiguration.setString("ipAddress", basicMachine.getIpAddress());
            endpointConfiguration.setString("dnsName", basicMachine.getDnsName());
            endpointConfiguration.setString("cpu", basicMachine.getCpu());
            endpointConfiguration.setString("memory", basicMachine.getMemory());
            endpointConfiguration.setString("operatingSystem", basicMachine.getOperatingSystem());
            endpointConfiguration.setString("diskSize", basicMachine.getDiskSize());
            endpointConfiguration.setString("powerState", basicMachine.getPowerState());
            endpointConfiguration.setString("snapshot", basicMachine.getSnapshot());
            endpointConfiguration.setString("initialUsername", basicMachine.getInitialUsername());
            endpointConfiguration.setString("initialPassword", basicMachine.getInitialPassword());
            endpointConfiguration.setString("description", basicMachine.getDescription());
            endpointConfiguration.setString("json", basicMachine.getJson());

        } catch (IOException e) {
            LOG.error("Can not convert BasicMachine to IEndpointConfiguration!", e);
            throw new RuntimeException(e);
        }


        return endpointConfiguration;
    }

    private BasicMachine convertToBasicMachine(IEndpointConfiguration iEndpointConfiguration) {

        BasicMachine basicMachine = null;

        try {

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

        } catch (IllegalArgumentException e) {
            LOG.error("Can not convert IEndpointConfiguration[" + iEndpointConfiguration.getId() + "] to Type BasicMachine!", e);
            throw new RuntimeException(e);
        }

        return basicMachine;
    }

    private void notifyChangeListener(BasicMachine basicMachine) {

        for (ConfigChangeListener listener : listeners) {
            listener.basicMachineSaved(basicMachine);
        }

    }

}
