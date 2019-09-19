package de.evoila.vro.o11n.plugin.basicmachine.config;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachine;
import de.evoila.vro.o11n.plugin.basicmachine.model.BasicMachineInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concrete implementation of an {@link EndpointChangeListener}.
 * This class is used to cache all configurations/resources which are stored on the
 * subscribed endpoint configuration locally.
 * The local cache is updated automatically when the {@link EndpointPersister}
 * which this class is subscribed to, changes its configurations/resources.
 *
 * @author Lars Atzinger latzinger@evoila.de
 */
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

    /**
     * Find local cached {@link BasicMachine} by its ID.
     *
     * @param id of the associated configuration/resource
     * @return local saved {@link BasicMachine} or null
     */
    public BasicMachine findById(Sid id) {
    	BasicMachineInfo basicMachineInfo = basicMachineEndpoint.findById(id);
    	BasicMachine basicMachine = (BasicMachine) applicationContext.getBean("basicMachine", basicMachineInfo);
    	basicmachines.put(basicMachine.getInternalId(), basicMachine);
    	return basicMachine;
    }

    /**
     * Returns all local cached {@link BasicMachine}.
     *
     * @return a collection of all local cached {@link BasicMachine} or null
     */
    public Collection<BasicMachine> findAll() {
        basicmachines = new ConcurrentHashMap<>();
    	List<BasicMachineInfo> basicMachineInfos = basicMachineEndpoint.findAll();
    	for (BasicMachineInfo basicMachineInfo : basicMachineInfos) {
    		BasicMachine basicMachine = (BasicMachine) applicationContext.getBean("basicMachine", basicMachineInfo);
        	basicmachines.put(basicMachine.getInternalId(), basicMachine);
		}
        return basicmachines.values();
    }

    /**
     * {@inheritDoc}
     *
     * @param basicMachineInfo which was currently saved
     */
    @Override
    public void basicMachineSaved(BasicMachineInfo basicMachineInfo) {
        BasicMachine newMachine = (BasicMachine) applicationContext.getBean("basicMachine", basicMachineInfo);
        basicmachines.put(basicMachineInfo.getId(), newMachine);
    }

    /**
     * {@inheritDoc}
     *
     * @param basicMachineInfo which was currently updated
     */
    @Override
    public void basicMachineUpdated(BasicMachineInfo basicMachineInfo) {

        BasicMachine basicMachine = basicmachines.get(basicMachineInfo.getId());

        if (basicMachine != null) {
            basicMachine.update(basicMachineInfo);
        } else {
            basicMachine = (BasicMachine) applicationContext.getBean("basicMachine", basicMachineInfo);
            basicmachines.put(basicMachineInfo.getId(), basicMachine);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param basicMachineInfo which was currently deleted
     */
    @Override
    public void basicMachineDeleted(BasicMachineInfo basicMachineInfo) {
        basicmachines.remove(basicMachineInfo.getId());
    }

    /**
     * Subscribe to {@link BasicMachineEndpoint} to retrieve
     * notifications when a endpoint configurations is saved, removed or updated.
     * On plugin start-up all persisted endpoint configurations will loaded into local cache.
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        basicMachineEndpoint.registerChangeListener(this);
        basicMachineEndpoint.reload();
    }

}
