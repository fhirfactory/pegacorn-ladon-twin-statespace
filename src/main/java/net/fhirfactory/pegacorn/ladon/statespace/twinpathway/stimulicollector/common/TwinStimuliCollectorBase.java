package net.fhirfactory.pegacorn.ladon.statespace.twinpathway.stimulicollector.common;

import net.fhirfactory.pegacorn.petasos.datasets.manager.TopicIM;
import net.fhirfactory.pegacorn.petasos.model.topics.TopicToken;
import net.fhirfactory.pegacorn.petasos.wup.archetypes.MOAStandardWUP;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public abstract class TwinStimuliCollectorBase extends MOAStandardWUP {

    @Inject
    TopicIM topicServer;

    @Override
    protected Set<TopicToken> specifySubscriptionTopics() {
        return (new HashSet<>());
    }

    @Override
    protected String specifyWUPInstanceName() {
        String wupName = "Ladon.StateSpace.StimuliCollector." + specifyTwinTypeName();
        return(wupName);
    }

    @Override
    protected String specifyWUPVersion() {
        return(specifyTwinTypeVersion());
    }

    @Override
    protected String specifyWUPWorkshop() {
        return ("StateSpace");
    }

    abstract protected String specifyTwinTypeName();
    abstract protected String specifyTwinTypeVersion();

    public void addTopicToSubscription(TopicToken newTopic){
        if(newTopic != null ){
            topicServer.addTopicSubscriber(newTopic,getWupTopologyNodeElement().getNodeInstanceID());
        }
    }

    @Override
    public void configure() throws Exception {

        from(ingresFeed())
                .routeId(getNameSet().getRouteCoreWUP())
                .bean(TwinStimuliCollectorProcessingBean.class, "collectForQueueing(*, " + specifyTwinTypeName() +")")
                .to(egressFeed());
    }

}
