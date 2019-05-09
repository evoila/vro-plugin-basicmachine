/*
 * Copyright (C) 2019 Lars Atzinger latzinger@evoila.de
 */

package de.evoila.vro.o11n.plugin.basicmachine.finder;

import com.vmware.o11n.sdk.modeldriven.FoundObject;
import com.vmware.o11n.sdk.modeldriven.ObjectFinder;
import com.vmware.o11n.sdk.modeldriven.PluginContext;
import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.config.LocalRepository;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class BasicMachineFinder implements ObjectFinder<BasicMachine> {

    @Autowired
    private LocalRepository localRepository;

    /**
     * {@inheritDoc}
     *
     * @param pluginContext
     * @param s
     * @param sid
     * @return
     */
    @Override
    public BasicMachine find(PluginContext pluginContext, String s, Sid sid) {
        return localRepository.findById(sid);
    }

    /**
     * {@inheritDoc}
     *
     * @param pluginContext
     * @param s
     * @param query
     * @return
     */
    @Override
    public List<FoundObject<BasicMachine>> query(PluginContext pluginContext, String s, String query) {

        Collection<BasicMachine> basicMachines = localRepository.findAll();

        List<FoundObject<BasicMachine>> result = new LinkedList<>();
        boolean returnAll = "".equals(query);
        for (BasicMachine basicMachine : basicMachines) {
            if (returnAll ||
                    basicMachine.getDisplayName().toLowerCase().startsWith(query.toLowerCase())) {
                FoundObject<BasicMachine> foundObject = new FoundObject<>(basicMachine);
                result.add(foundObject);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param basicMachine
     * @param sid
     * @return
     */
    @Override
    public Sid assignId(BasicMachine basicMachine, Sid sid) {
        return basicMachine.getInternalId();
    }

}
