/*
 * Copyright (C) 2019 Lars Atzinger latzinger@evoila.de
 */

package de.evoila.vro.o11n.plugin.basicmachine.config;

import ch.dunes.vso.sdk.endpoints.IEndpointConfiguration;
import ch.dunes.vso.sdk.endpoints.IEndpointConfigurationService;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Concrete Implementation of an configuration endpoint.
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

        LOG.debug("Reading all endpoint configurations...");

        Collection<IEndpointConfiguration> configurations;

        try {
            configurations = endpointConfigurationService.getEndpointConfigurations();

            List<BasicMachineInfo> basicMachines = new ArrayList<>(configurations.size());

            for (IEndpointConfiguration config : configurations) {

                BasicMachineInfo machineInfo = convertToBasicMachineInfo(config);

                if (machineInfo != null) {
                    basicMachines.add(machineInfo);
                    LOG.debug("Found " + machineInfo);
                }

            }

            LOG.debug("Done reading endpoint configurations.");

            return basicMachines;

        } catch (IOException e) {
            LOG.error("Failed while reading endpoint configurations!", e);
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
            LOG.warn("SID of BasicMachine can not be null!");
            return null;
        }

        IEndpointConfiguration endpointConfiguration;

        try {
            endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(id.toString());

            if (endpointConfiguration == null) {
                LOG.warn("Can not find BasicMachine with id:" + id.toString());
                return null;
            }

            BasicMachineInfo basicMachineInfo = convertToBasicMachineInfo(endpointConfiguration);

            return basicMachineInfo;

        } catch (IOException e) {
            LOG.error("Failed while reading endpoint configuration for id:" + id.toString(), e);
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

        if (basicMachineInfo == null) {
            LOG.error("Can not save BasicMachine. The given BasicMachineInfo is null!");
            throw new RuntimeException("Can not save BasicMachine. The given BasicMachineInfo is null.");
        }

        if (basicMachineInfo.getId() == null) {
            LOG.error("Can not save BasicMachine. ID is missing.");
            throw new RuntimeException("Can not save BasicMachine. ID is missing!");
        }

        if (basicMachineInfoAlreadyExists(basicMachineInfo)) {
            LOG.error("Can not save BasicMachine. BasicMachine already exists => " + basicMachineInfo);
            throw new RuntimeException("Can not save BasicMachine. BasicMachine already exists => " + basicMachineInfo);
        }

        try {

            IEndpointConfiguration endpointConfiguration = endpointConfigurationService.newEndpointConfiguration(basicMachineInfo.getId().toString());
            convertToIEndpointConfiguration(endpointConfiguration, basicMachineInfo);
            endpointConfigurationService.saveEndpointConfiguration(endpointConfiguration);

            notifyChangeListenerOnSave(basicMachineInfo);

            return basicMachineInfo;

        } catch (IOException e) {
            LOG.error("Failed while saving endpoint configuration for " + basicMachineInfo, e);
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

        if (basicMachineInfo == null) {
            LOG.error("Can not update BasicMachine. The given BasicMachineInfo is null!");
            throw new RuntimeException("Can not update BasicMachine. The given BasicMachineInfo is null.");
        }

        if (basicMachineInfo.getId() == null) {
            LOG.error("Can not update BasicMachine. ID is missing.");
            throw new RuntimeException("Can not update BasicMachine. ID is missing!");
        }

        try {

            String id = basicMachineInfo.getId().toString();
            IEndpointConfiguration endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(id);

            if (endpointConfiguration == null) {
                LOG.error("Can not update BasicMachine. BasicMachine with ID [" + id + "] already exists!");
                throw new RuntimeException("Can not update BasicMachine. BasicMachine with ID [" + id + "] already exists!");
            }

            convertToIEndpointConfiguration(endpointConfiguration, basicMachineInfo);
            endpointConfigurationService.saveEndpointConfiguration(endpointConfiguration);

        } catch (IOException e) {
            LOG.error("Failed while updating endpoint configuration for " + basicMachineInfo, e);
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

        if (basicMachineInfo == null) {
            LOG.error("Can not delete BasicMachine. The given BasicMachineInfo is null!");
            throw new RuntimeException("Can not delete BasicMachine. The given BasicMachineInfo is null.");
        }

        if (basicMachineInfo.getId() == null) {
            LOG.error("Can not delete BasicMachine. ID is missing.");
            throw new RuntimeException("Can not delete BasicMachine. ID is missing!");
        }

        try {
            String id = basicMachineInfo.getId().toString();
            endpointConfigurationService.deleteEndpointConfiguration(id);
            notifyChangeListenerOnDelete(basicMachineInfo);
        } catch (IOException e) {
            LOG.error("Failed while deleting endpoint configuration for " + basicMachineInfo, e);
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
        LOG.debug("EndpointChangeListener subscribed to this endpoint configuration.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload() {

        LOG.debug("Reloading resources...");

        Collection<BasicMachineInfo> result = findAll();

        for (BasicMachineInfo machineInfo : result) {
            notifyChangeListenerOnUpdate(machineInfo);
        }

        LOG.debug("Finished reloading resources.");

    }

    /**
     * Checks if a endpoint configuration for this {@link BasicMachineInfo} already exists by using its ID.
     *
     * @param basicMachineInfo to be checked
     * @return true if endpoint configuration already exists false otherwise
     */
    private boolean basicMachineInfoAlreadyExists(BasicMachineInfo basicMachineInfo) {

        if (basicMachineInfo.getId() == null) {
            LOG.error("basicMachineInfoAlreadyExists(): ID can not be null!");
            throw new RuntimeException("basicMachineInfoAlreadyExists(): ID can not be null!");
        }

        try {
            IEndpointConfiguration endpointConfiguration = endpointConfigurationService.getEndpointConfiguration(basicMachineInfo.getId().toString());

            if (endpointConfiguration == null)
                return false;

        } catch (IOException e) {
            LOG.error("Failed while checking if BasicMachine already exists!");
            throw new RuntimeException(e);
        }

        return true;
    }

    /*
    @Deprecated
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
    */


    /**
     * Finds a {@link BasicMachineInfo} configuration/resource by its name.
     *
     * @param name of the {@link BasicMachineInfo}
     * @return the found {@link BasicMachineInfo} or null
     * @throws RuntimeException if given name is null
     */
    @Deprecated
    private BasicMachineInfo findBasicMachineInfoByName(String name) {

        if (name == null) {
            LOG.error("findBasicMachineInfoByName() -> Name can not be null!");
            throw new RuntimeException("findBasicMachineInfoByName() -> Name can not be null!");
        }

        Collection<BasicMachineInfo> result = findAll();

        BasicMachineInfo basicMachineInfo = null;

        Iterator<BasicMachineInfo> iterator = result.iterator();
        while ((iterator.hasNext()) && basicMachineInfo == null) {
            BasicMachineInfo machineInfo = iterator.next();
            if (name.equals(machineInfo.getName()))
                basicMachineInfo = machineInfo;
        }

        return basicMachineInfo;
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

            endpointConfiguration.setString("owner", basicMachineInfo.getOwner());
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
            LOG.error("Failed while converting BasicMachine to IEndpointConfiguration for " + basicMachineInfo, e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Creates a new {@link BasicMachineInfo} and copies the content of the given {@link IEndpointConfiguration} into it.
     *
     * @param endpointConfiguration which content should be copied
     * @return the created {@link BasicMachineInfo} with the content of the iEndpointConfiguration
     * @throws Exception
     */
    private BasicMachineInfo convertToBasicMachineInfo(IEndpointConfiguration endpointConfiguration) {

        try {

            Sid sid = Sid.valueOf(endpointConfiguration.getString("id"));

            BasicMachineInfo basicMachineInfo = new BasicMachineInfo(sid);

            basicMachineInfo.setOwner(endpointConfiguration.getString("owner"));
            basicMachineInfo.setName(endpointConfiguration.getString("name"));
            basicMachineInfo.setIpAddress(endpointConfiguration.getString("ipAddress"));
            basicMachineInfo.setDnsName(endpointConfiguration.getString("dnsName"));
            basicMachineInfo.setCpu(endpointConfiguration.getString("cpu"));
            basicMachineInfo.setMemory(endpointConfiguration.getString("memory"));
            basicMachineInfo.setOperatingSystem(endpointConfiguration.getString("operatingSystem"));
            basicMachineInfo.setDiskSize(endpointConfiguration.getString("diskSize"));
            basicMachineInfo.setPowerState(endpointConfiguration.getString("powerState"));
            basicMachineInfo.setSnapshot(endpointConfiguration.getString("snapshot"));
            basicMachineInfo.setInitialUsername(endpointConfiguration.getString("initialUsername"));
            basicMachineInfo.setInitialPassword(endpointConfiguration.getString("initialPassword"));
            basicMachineInfo.setDescription(endpointConfiguration.getString("description"));
            basicMachineInfo.setJson(endpointConfiguration.getString("json"));

            return basicMachineInfo;

        } catch (IllegalArgumentException e) {
            LOG.error("Failed while converting IEndpointConfiguration [" + endpointConfiguration.getId() + "] to BasicMachineInfo!", e);
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
