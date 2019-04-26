package de.evoila.vro.o11n.plugin.basicmachine.model;

import com.vmware.o11n.sdk.modeldriven.Sid;
import de.evoila.vro.o11n.plugin.basicmachine.config.ConfigPersisterImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class RootObject {

    @Autowired
    ConfigPersisterImpl configPersister;

    public static final String TYPE = "RootObject";

    private final Sid id;

    public RootObject() {
        this(Sid.unique());
    }

    public RootObject(Sid id) {
        this.id = id;
    }

    public Sid getId() {
        return id;
    }

    public ConfigPersisterImpl getConfigPersister() {
        return configPersister;
    }

    public void setConfigPersister(ConfigPersisterImpl configPersister) {
        this.configPersister = configPersister;
    }
}
