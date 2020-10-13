package net.fhirfactory.pegacorn.ladon.statespace.twinpathway.provocations.workers.base;

import net.fhirfactory.pegacorn.ladon.statespace.twinpathway.provocations.workers.buildingblocks.TwinTypeProvocationBaseRouteWUP;
import net.fhirfactory.pegacorn.ladon.statespace.twinpathway.provocations.workers.buildingblocks.TwinTypeProvocationProcessingBean;
import net.fhirfactory.pegacorn.petasos.datasets.manager.TopicIM;
import net.fhirfactory.pegacorn.petasos.model.topics.TopicToken;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 *
 */
public abstract class TwinTypeProvocationArchetypeWUP extends TwinTypeProvocationBaseRouteWUP {

    @Inject
    TopicIM topicServer;

    protected String specifyWUPWorkshop() {
        return ("StateSpace");
    }

    abstract protected String specifyTwinTypeName();
    abstract protected String specifyTwinTypeVersion();
    abstract protected Logger getLogger();

    public void addTopicToSubscription(TopicToken newTopic){

    }

    @Override
    public void configure() throws Exception {
        from(getIngresFeed())
                .routeId(getRouteName())
                .bean(TwinTypeProvocationProcessingBean.class, "collectForQueueing(*, " + specifyTwinTypeName() +")");
    }

    private String getIngresFeed(){
        String wupName = specifyTwinTypeName();
        String wupVersion = specifyTwinTypeVersion().replaceAll(".","");
        String ingresFeedString = "seda:" + wupName + "." + wupVersion + ".Ingres";
        return(ingresFeedString);
    }

    private String getRouteName(){
        String wupName = specifyTwinTypeName();
        String wupVersion = specifyTwinTypeVersion().replaceAll(".","");
        String routeId = "StimuliCollector:" + wupName + "." + wupVersion + ".Ingres";
        return(routeId);
    }


}
