package de.evoila.vro.o11n.plugin.basicmachine.config;

import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;

/**
 * Every subscriber of an endpoint configuration must implement this interface.
 * Its method will be invoked by the endpoint configuration update, delete and save operations.
 *
 * @author Lars Atzinger latzinger@evoila.de
 */
public interface EndpointChangeListener {

    /**
     * Operation that should happen when a new configuration/resource was saved on the endpoint.
     *
     * @param basicMachineInfo which was currently saved
     */
    void basicMachineSaved(BasicMachineInfo basicMachineInfo);

    /**
     * Operation that should happen when a existing configuration/resource was updated on the endpoint.
     *
     * @param basicMachineInfo which was currently updated
     */
    void basicMachineUpdated(BasicMachineInfo basicMachineInfo);

    /**
     * Operation that should happen when a existing configuration/resource was deleted on the endpoint.
     *
     * @param basicMachineInfo which was currently deleted
     */
    void basicMachineDeleted(BasicMachineInfo basicMachineInfo);

}
