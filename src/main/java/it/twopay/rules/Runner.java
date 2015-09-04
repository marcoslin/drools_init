package it.twopay.rules;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runner {
	public static Logger log = LoggerFactory.getLogger("it.twopay.rules.Runner");

    protected KieContainer kc;

    public Runner() {
        kc = KieServices.Factory.get().getKieClasspathContainer();
    }

}
