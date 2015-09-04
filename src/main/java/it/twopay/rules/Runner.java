package it.twopay.rules;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class Runner {
	public static Logger log = LoggerFactory.getLogger("it.twopay.rules.Runner");

    protected KieContainer kc;
    protected List<Object> objects;

    public Runner() {
        kc = KieServices.Factory.get().getKieClasspathContainer();
        objects = new ArrayList<>();
    }

    public void insert(Object object) {
    	this.objects.add(object);
    };
    
    public abstract void process();
    
}
