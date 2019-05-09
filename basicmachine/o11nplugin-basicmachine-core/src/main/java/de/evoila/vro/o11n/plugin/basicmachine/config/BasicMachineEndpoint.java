/*
 * Copyright (C) 2019 Lars Atzinger latzinger@evoila.de
 */

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


/**
 *
 */
@Component
public class BasicMachineEndpoint implements EndpointPersister {

    private static final Logger LOG = LoggerFactory.getLogger(BasicMachineEndpoint.class);

    private final Collection<EndpointChangeListener> listeners;

    @Autowired
    private IEndpointConfigurationService endpointConfigurationService;

    public BasicMachineEndpoint() {
        listeners = new CopyOnWriteArrayList<>();
    }

    /**
     * {@inheritDoc}
     *
     * @return list of all {@link BasicMachineInfo} that was found
     */
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

    /**
     * {@inheritDoc}
     *
     * @param id of the endpoint configuration
     * @return {@link BasicMachineInfo} or null
     * @throws RuntimeException if id is null or no endpoint configuration with the given id was found.
     */
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

    /**
     * {@inheritDoc}
     *
     * @param basicMachineInfo which should be stored in the endpoint
     * @return the current saved {@link BasicMachineInfo}
     * @throws RuntimeException if given basicMachineInfo or the containing ID is null
     * @throws RuntimeException if {@link BasicMachineInfo} already exists on the endpoint
     */
    @Override
    public BasicMachineInfo save(BasicMachineInfo basicMachineInfo) {

        if (basicMachineInfo == null || basicMachineInfo.getId() == null) {
            LOG.error("Can not save BasicMachine. Is null or id is missing!");
            throw new RuntimeException("BasicMachine is invalid.");
        }

        if (basicMachineInfoAlreadyExists(basicMachineInfo))
            throw new RuntimeException("BasicMachine with same id already exists: " + basicMachineInfo + "\n");

        try {

            IEndpointConfiguration endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(basicMachineInfo.getId().toString());

            if (endpointConfiguration == null) {

                endpointConfiguration = endpointConfigurationService.newEndpointConfiguration(basicMachineInfo.getId().toString());

            }

            convertToIEndpointConfiguration(endpointConfiguration, basicMachineInfo);

            endpointConfigurationService.saveEndpointConfiguration(endpointConfiguration);

            notifyChangeListenerOnSave(basicMachineInfo);

            return basicMachineInfo;

        } catch (IOException e) {
            LOG.error("Error saving BasicMachine: " + basicMachineInfo, e);
            throw new RuntimeException(e);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param basicMachineInfo which should be updated
     * @return {@link BasicMachineInfo} which was currently updated
     */
    @Override
    public BasicMachineInfo update(BasicMachineInfo basicMachineInfo) {

        try {
            IEndpointConfiguration endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(basicMachineInfo.getId().toString());

            if (endpointConfiguration == null) {
                LOG.error("Error while updating endpoint configuration for BasicMachine: " + basicMachineInfo);
                throw new RuntimeException("Can not update BasicMachine. BasicMachine does not exist.");
            }

            convertToIEndpointConfiguration(endpointConfiguration, basicMachineInfo);

            endpointConfigurationService.saveEndpointConfiguration(endpointConfiguration);

        } catch (IOException e) {
            LOG.error("Error updating BasicMachine: " + basicMachineInfo, e);
            throw new RuntimeException(e);
        }

        notifyChangeListenerOnUpdate(basicMachineInfo);

        return basicMachineInfo;
    }

    /**
     * {@inheritDoc}
     *
     * @param basicMachineInfo which should be deleted
     */
    @Override
    public void delete(BasicMachineInfo basicMachineInfo) {

        try {
            endpointConfigurationService.deleteEndpointConfiguration(basicMachineInfo.getId().toString());
            notifyChangeListenerOnDelete(basicMachineInfo);
        } catch (IOException e) {
            LOG.error("Error while deleting endpoint configuration for BasicMachine: " + basicMachineInfo);
            throw new RuntimeException(e);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param endpointChangeListener which should listen on this endpoint configuration
     */
    @Override
    public void registerChangeListener(EndpointChangeListener endpointChangeListener) {
        listeners.add(endpointChangeListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload() {

        Collection<BasicMachineInfo> result = findAll();

        for (BasicMachineInfo machineInfo : result) {
            notifyChangeListenerOnUpdate(machineInfo);
        }

    }

    /**
     * Checks if a endpoint configuration for this {@link BasicMachineInfo} already exists by using its ID.
     *
     * @param basicMachineInfo to be checked
     * @return true if endpoint configuration already exists false otherwise
     */
    private boolean basicMachineInfoAlreadyExists(BasicMachineInfo basicMachineInfo) {

        if (basicMachineInfo.getId() == null)
            throw new RuntimeException("Id can not be null!");

        Collection<BasicMachineInfo> result = findAll();

        for (BasicMachineInfo bmi : result) {
            if (basicMachineInfo.getId().equals(bmi.getId()))
                return true;
        }

        return false;
    }

    /**
     * Finds a {@link BasicMachineInfo} configuration/resource by its name.
     *
     * @param name of the {@link BasicMachineInfo}
     * @return the found {@link BasicMachineInfo} or null
     * @throws RuntimeException if given name is null
     */
    @Deprecated
    private BasicMachineInfo findBasicMachineInfoByName(String name) {

        if (name == null)
            throw new RuntimeException("Name can not be null!");

        Collection<BasicMachineInfo> result = findAll();

        for (BasicMachineInfo machineInfo : result) {
            if (name.equals(machineInfo.getName()))
                return machineInfo;
        }

        return null;
    }

    /**
     * Copies the content of the type {@link BasicMachineInfo} into the type {@link IEndpointConfiguration}.
     *
     * @param endpointConfiguration existing {@link IEndpointConfiguration} not null
     * @param basicMachineInfo      existing {@link BasicMachineInfo} not null
     * @throws Exception
     */
    private void convertToIEndpointConfiguration(IEndpointConfiguration endpointConfiguration, BasicMachineInfo basicMachineInfo) {

        try {

            endpointConfiguration.setString("id", basicMachineInfo.getId().toString());
            endpointConfiguration.setString("name", basicMachineInfo.getName());
            endpointConfiguration.setString("ipAddress", basicMachineInfo.getIpAddress());
            endpointConfiguration.setString("dnsName", basicMachineInfo.getDnsName());
            endpointConfiguration.setString("cpu", basicMachineInfo.getCpu());
            endpointConfiguration.setString("memory", basicMachineInfo.getMemory());
            endpointConfiguration.setString("operatingSystem", basicMachineInfo.getOperatingSystem());
            endpointConfiguration.setString("diskSize", basicMachineInfo.getDiskSize());
            endpointConfiguration.setString("powerState", basicMachineInfo.getPowerState());
            endpointConfiguration.setString("snapshot", basicMachineInfo.getSnapshot());
            endpointConfiguration.setString("initialUsername", basicMachineInfo.getInitialUsername());
            endpointConfiguration.setString("initialPassword", basicMachineInfo.getInitialPassword());
            endpointConfiguration.setString("description", basicMachineInfo.getDescription());
            endpointConfiguration.setString("json", basicMachineInfo.getJson());

        } catch (Exception e) {
            LOG.error("Can not convert BasicMachine to IEndpointConfiguration!", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Creates a new {@link BasicMachineInfo} and copies the content of the given {@link IEndpointConfiguration} into it.
     *
     * @param iEndpointConfiguration which content should be copied
     * @return the created {@link BasicMachineInfo} with the content of the iEndpointConfiguration
     * @throws Exception
     */
    private BasicMachineInfo convertToBasicMachineInfo(IEndpointConfiguration iEndpointConfiguration) {

        try {

            Sid sid = Sid.valueOf(iEndpointConfiguration.getString("id"));

            BasicMachineInfo basicMachineInfo = new BasicMachineInfo(sid);

            basicMachineInfo.setName(iEndpointConfiguration.getString("name"));
            basicMachineInfo.setIpAddress(iEndpointConfiguration.getString("ipAddress"));
            basicMachineInfo.setDnsName(iEndpointConfiguration.getString("dnsName"));
            basicMachineInfo.setCpu(iEndpointConfiguration.getString("cpu"));
            basicMachineInfo.setMemory(iEndpointConfiguration.getString("memory"));
            basicMachineInfo.setOperatingSystem(iEndpointConfiguration.getString("operatingSystem"));
            basicMachineInfo.setDiskSize(iEndpointConfiguration.getString("diskSize"));
            basicMachineInfo.setPowerState(iEndpointConfiguration.getString("powerState"));
            basicMachineInfo.setSnapshot(iEndpointConfiguration.getString("snapshot"));
            basicMachineInfo.setInitialUsername(iEndpointConfiguration.getString("initialUsername"));
            basicMachineInfo.setInitialPassword(iEndpointConfiguration.getString("initialPassword"));
            basicMachineInfo.setDescription(iEndpointConfiguration.getString("description"));
            basicMachineInfo.setJson(iEndpointConfiguration.getString("json"));

            return basicMachineInfo;

        } catch (IllegalArgumentException e) {
            LOG.error("Can not convert IEndpointConfiguration[" + iEndpointConfiguration.getId() + "] to Type BasicMachine!", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Notifies all subscriber of this endpoint configuration that a configuration/resource was updated.
     *
     * @param basicMachineInfo which was updated
     */
    private void notifyChangeListenerOnUpdate(BasicMachineInfo basicMachineInfo) {

        for (EndpointChangeListener listener : listeners) {
            listener.basicMachineUpdated(basicMachineInfo);
        }

    }

    /**
     * Notifies all subscriber of this endpoint configuration that a configuration/resource was saved.
     *
     * @param basicMachineInfo which was saved
     */
    private void notifyChangeListenerOnSave(BasicMachineInfo basicMachineInfo) {

        for (EndpointChangeListener listener : listeners) {
            listener.basicMachineSaved(basicMachineInfo);
        }

    }

    /**
     * Notifies all subscriber of this endpoint configuration that a configuration/resource was deleted.
     *
     * @param basicMachineInfo which was deleted
     */
    private void notifyChangeListenerOnDelete(BasicMachineInfo basicMachineInfo) {

        for (EndpointChangeListener listener : listeners) {
            listener.basicMachineDeleted(basicMachineInfo);
        }

    }

}
