package claygminx.worshipppt.common.entity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class WorshipPropertyBean {

    private final PropertyChangeSupport pcs;

    protected WorshipPropertyBean() {
        pcs = new PropertyChangeSupport(this);
    }

    protected PropertyChangeSupport getPcs() {
        return pcs;
    }

    protected void firePropertyChange(String name, Object oldValue, Object newValue) {
        if (!Objects.equals(oldValue, newValue) && getPcs().hasListeners(name)) {
            getPcs().firePropertyChange(name, oldValue, newValue);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
