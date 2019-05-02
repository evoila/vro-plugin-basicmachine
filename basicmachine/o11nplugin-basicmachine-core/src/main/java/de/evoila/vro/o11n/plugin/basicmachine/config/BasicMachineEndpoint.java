package de.evoila.vro.o11n.plugin.basicmachine.config;

import ch.dunes.vso.sdk.endpoints.IEndpointConfiguration;
import ch.dunes.vso.sdk.endpoints.IEndpointConfigurationService;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;
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
public class BasicMachineEndpoint implements EndpointPersister {

    private static final Logger LOG = LoggerFactory.getLogger(BasicMachineEndpoint.class);

    private final Collection<EndpointChangeListener> listeners;

    @Autowired
    private IEndpointConfigurationService endpointConfigurationService;

    public BasicMachineEndpoint() {
        listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<BasicMachineInfo> findAll() {

        Collection<IEndpointConfiguration> configurations;

        try {
            configurations = endpointConfigurationService.getEndpointConfigurations();

            List<BasicMachineInfo> basicMachines = new ArrayList<>(configurations.size());

            for (IEndpointConfiguration config : configurations) {

                BasicMachineInfo machineInfo = convertToBasicMachineInfo(config);

                if (machineInfo != null) {
                    basicMachines.add(machineInfo);
                    LOG.debug("Added " + machineInfo + "\n");
                }

            }

            return basicMachines;


        } catch (IOException e) {
            LOG.error("Failed to read configurations!", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public BasicMachineInfo findById(Sid id) {

        if (id == null) {
            LOG.warn("id can not be null!");
            throw new RuntimeException("id can not be null!");
        }

        IEndpointConfiguration endpointConfiguration;

        try {
            endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(id.toString());

            if (endpointConfiguration == null)
                throw new RuntimeException("Can not find BasicMachine with id:" + id.toString());

            return convertToBasicMachineInfo(endpointConfiguration);

        } catch (IOException e) {
            LOG.error("Can not find BasicMachine with id:" + id.toString(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public BasicMachineInfo save(BasicMachineInfo machineInfo) {

        if (machineInfo == null || machineInfo.getId() == null) {
            LOG.error("Can not save BasicMachine. Is null or id is missing!");
            throw new RuntimeException("BasicMachine is invalid.");
        }

        if (basicMachineAlreadyExists(machineInfo))
            throw new RuntimeException("BasicMachine with same name already exists: " + machineInfo + "\n");

        try {

            IEndpointConfiguration endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(machineInfo.getId().toString());

            if (endpointConfiguration == null) {

                endpointConfiguration = endpointConfigurationService.newEndpointConfiguration(machineInfo.getId().toString());

            }

            convertToIEndpointConfiguration(endpointConfiguration, machineInfo);

            endpointConfigurationService.saveEndpointConfiguration(endpointConfiguration);

            notifyChangeListenerOnSave(machineInfo);

            return machineInfo;

        } catch (IOException e) {
            LOG.error("Error saving BasicMachine: " + machineInfo, e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(BasicMachineInfo machineInfo) {

        try {
            endpointConfigurationService.deleteEndpointConfiguration(machineInfo.getId().toString());
            notifyChangeListenerOnDelete(machineInfo);
        } catch (IOException e) {
            LOG.error("Error while deleting endpoint configuration for BasicMachine: " + machineInfo);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void registerChangeListener(EndpointChangeListener endpointChangeListener) {
        listeners.add(endpointChangeListener);
    }

    @Override
    public void reload() {

        Collection<BasicMachineInfo> result = findAll();

        for (BasicMachineInfo machineInfo : result) {
            notifyChangeListenerOnSave(machineInfo);
        }

    }

    private boolean basicMachineAlreadyExists(BasicMachineInfo machineInfo) {

        BasicMachineInfo result = findBasicMachineByName(machineInfo.getName());

        if (result != null && !((result.getId().toString()).equals(machineInfo.getId().toString()))) {
            return true;
        }

        return false;
    }

    private BasicMachineInfo findBasicMachineByName(String name) {

        if (name == null)
            throw new RuntimeException("Name can not be null!");

        Collection<BasicMachineInfo> result = findAll();

        for (BasicMachineInfo machineInfo : result) {
            if (name.equals(machineInfo.getName()))
                return machineInfo;
        }

        return null;
    }

    private void convertToIEndpointConfiguration(IEndpointConfiguration endpointConfiguration, BasicMachineInfo machineInfo) {

        try {

            endpointConfiguration.setString("id", machineInfo.getId().toString());
            endpointConfiguration.setString("name", machineInfo.getName());
            endpointConfiguration.setString("ipAddress", machineInfo.getIpAddress());
            endpointConfiguration.setString("dnsName", machineInfo.getDnsName());
            endpointConfiguration.setString("cpu", machineInfo.getCpu());
            endpointConfiguration.setString("memory", machineInfo.getMemory());
            endpointConfiguration.setString("operatingSystem", machineInfo.getOperatingSystem());
            endpointConfiguration.setString("diskSize", machineInfo.getDiskSize());
            endpointConfiguration.setString("powerState", machineInfo.getPowerState());
            endpointConfiguration.setString("snapshot", machineInfo.getSnapshot());
            endpointConfiguration.setString("initialUsername", machineInfo.getInitialUsername());
            endpointConfiguration.setString("initialPassword", machineInfo.getInitialPassword());
            endpointConfiguration.setString("description", machineInfo.getDescription());
            endpointConfiguration.setString("json", machineInfo.getJson());

        } catch (Exception e) {
            LOG.error("Can not convert BasicMachine to IEndpointConfiguration!", e);
            throw new RuntimeException(e);
        }

    }

    private BasicMachineInfo convertToBasicMachineInfo(IEndpointConfiguration iEndpointConfiguration) {

        try {

            Sid sid = Sid.valueOf(iEndpointConfiguration.getString("id"));

            BasicMachineInfo machineInfo = new BasicMachineInfo(sid);

            machineInfo.setName(iEndpointConfiguration.getString("name"));
            machineInfo.setIpAddress(iEndpointConfiguration.getString("ipAddress"));
            machineInfo.setDnsName(iEndpointConfiguration.getString("dnsName"));
            machineInfo.setCpu(iEndpointConfiguration.getString("cpu"));
            machineInfo.setMemory(iEndpointConfiguration.getString("memory"));
            machineInfo.setOperatingSystem(iEndpointConfiguration.getString("operatingSystem"));
            machineInfo.setDiskSize(iEndpointConfiguration.getString("diskSize"));
            machineInfo.setPowerState(iEndpointConfiguration.getString("powerState"));
            machineInfo.setSnapshot(iEndpointConfiguration.getString("snapshot"));
            machineInfo.setInitialUsername(iEndpointConfiguration.getString("initialUsername"));
            machineInfo.setInitialPassword(iEndpointConfiguration.getString("initialPassword"));
            machineInfo.setDescription(iEndpointConfiguration.getString("description"));
            machineInfo.setJson(iEndpointConfiguration.getString("json"));

            return machineInfo;

        } catch (IllegalArgumentException e) {
            LOG.error("Can not convert IEndpointConfiguration[" + iEndpointConfiguration.getId() + "] to Type BasicMachine!", e);
            throw new RuntimeException(e);
        }

    }

    private void notifyChangeListenerOnSave(BasicMachineInfo machineInfo) {

        for (EndpointChangeListener listener : listeners) {
            listener.basicMachineSaved(machineInfo);
        }

    }

    private void notifyChangeListenerOnDelete(BasicMachineInfo machineInfo) {

        for (EndpointChangeListener listener : listeners) {
            listener.basicMachineDeleted(machineInfo);
        }

    }

}
