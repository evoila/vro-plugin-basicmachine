/*
 * Copyright (C) 2019 Lars Atzinger latzinger@evoila.de
 */

package de.evoila.vro.o11n.plugin.basicmachine.config;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;

import java.util.List;

/**
 * Every Endpoint Configuration must implement this Interface.
 * An Endpoint is a location where the plug-in stores details of
 * the Type {@link BasicMachineInfo} by invoking the Orchestrator Persistence SDK.
 */
public interface EndpointPersister {

    /**
     * Returns a List of all stored configurations (resources under plugin-folder).
     * They will converted to result type {@link BasicMachineInfo} before.
     *
     * @return list of all {@link BasicMachineInfo} that was found
     */
    List<BasicMachineInfo> findAll();

    /**
     * Returns a stored configuration by its ID or null if not found.
     * The configuration will converted to result type {@link BasicMachineInfo} before.
     *
     * @param id of the endpoint configuration
     * @return {@link BasicMachineInfo} or null
     * @throws RuntimeException if id is null or no endpoint configuration with the given id was found.
     */
    BasicMachineInfo findById(Sid id);

    /**
     * Stores {@link de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine} details
     * of the type {@link BasicMachineInfo} on the endpoint.
     *
     * @param basicMachineInfo which should be stored in the endpoint
     * @return the current saved {@link BasicMachineInfo}
     * @throws RuntimeException if given basicMachineInfo or the containing ID is null
     * @throws RuntimeException if {@link BasicMachineInfo} already exists on the endpoint
     */
    BasicMachineInfo save(BasicMachineInfo basicMachineInfo);

    /**
     * Deletes a configuration/resource of type {@link BasicMachineInfo} by its ID.
     *
     * @param basicMachineInfo which should be deleted
     */
    void delete(BasicMachineInfo basicMachineInfo);

    /**
     * Updates a configuration/resource of type {@link BasicMachineInfo}.
     *
     * @param basicMachineInfo which should be updated
     * @return {@link BasicMachineInfo} which was currently updated
     */
    BasicMachineInfo update(BasicMachineInfo basicMachineInfo);

    /**
     * Allows us to subscribe to the events of the endpoint configuration.
     * The endpoint configuration will trigger an event on specified operation,
     * notifying all subscriber to update their data model.
     *
     * @param endpointChangeListener which should listen on this endpoint configuration
     */
    void registerChangeListener(EndpointChangeListener endpointChangeListener);

    /**
     * Forces the endpoint configuration to read all configurations and trigger the
     * {@link EndpointChangeListener}.
     * This method is invoked when the plug-in is loaded on server start-up.
     */
    void reload();

}
