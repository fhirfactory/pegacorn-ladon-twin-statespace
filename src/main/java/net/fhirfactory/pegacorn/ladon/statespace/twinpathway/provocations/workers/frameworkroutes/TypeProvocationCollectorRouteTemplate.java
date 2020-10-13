package net.fhirfactory.pegacorn.ladon.statespace.twinpathway.provocations.workers.frameworkroutes;

import net.fhirfactory.pegacorn.ladon.statespace.twinpathway.provocations.workers.buildingblocks.TwinTypeProvocationBaseRouteWUP;
import net.fhirfactory.pegacorn.ladon.statespace.twinpathway.provocations.workers.buildingblocks.TwinTypeProvocationProcessingBean;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.naming.RouteElementNames;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.archetypes.ExternalEgressWUPContainerRoute;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.buildingblocks.WUPContainerIngresGatekeeper;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.buildingblocks.WUPContainerIngresProcessor;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.buildingblocks.WUPIngresConduit;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElement;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeProvocationCollectorRouteTemplate extends TwinTypeProvocationBaseRouteWUP {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalEgressWUPContainerRoute.class);

    private NodeElement wupNode;
    private RouteElementNames nameSet;

    public TypeProvocationCollectorRouteTemplate(CamelContext camelCTX, NodeElement wupNode) {
        super(camelCTX);
        LOG.debug(".StandardWUPContainerRoute(): Entry, context --> ###, wupNode --> {}", wupNode);
        this.wupNode = wupNode;
        nameSet = new RouteElementNames(wupNode.getNodeFunctionToken());
    }

    @Override
    public void configure() {
        LOG.debug(".configure(): Entry!, for wupNode --> {}", this.wupNode);
        LOG.debug("StimuliCollectorRouteTemplate :: EndPointWUPContainerIngresProcessorIngres --> {}", nameSet.getEndPointWUPContainerIngresProcessorIngres());
        LOG.debug("StimuliCollectorRouteTemplate :: EndPointWUPContainerIngresProcessorEgress --> {}", nameSet.getEndPointWUPContainerIngresProcessorEgress());
        LOG.debug("StimuliCollectorRouteTemplate :: EndPointWUPContainerIngresGatekeeperIngres --> {}", nameSet.getEndPointWUPContainerIngresGatekeeperIngres());
        LOG.debug("StimuliCollectorRouteTemplate :: EndPointWUPIngresConduitIngres --> {}", nameSet.getEndPointWUPIngresConduitIngres());
        LOG.debug("StimuliCollectorRouteTemplate :: EndPointWUPIngres --> {}", nameSet.getEndPointWUPIngres());

        from(nameSet.getEndPointWUPContainerIngresProcessorIngres())
                .routeId(nameSet.getRouteWUPContainerIngressProcessor())
                .bean(WUPContainerIngresProcessor.class, "ingresContentProcessor(*, Exchange," + this.wupNode.extractNodeKey() + ")")
                .to(nameSet.getEndPointWUPContainerIngresProcessorEgress());

        from(nameSet.getEndPointWUPContainerIngresProcessorEgress())
                .routeId(nameSet.getRouteIngresProcessorEgress2IngresGatekeeperIngres())
                .to(nameSet.getEndPointWUPContainerIngresGatekeeperIngres());

        from(nameSet.getEndPointWUPContainerIngresGatekeeperIngres())
                .routeId(nameSet.getRouteWUPContainerIngresGateway())
                .bean(WUPContainerIngresGatekeeper.class, "ingresGatekeeper(*, Exchange," + this.wupNode.extractNodeKey() + ")");

        from(nameSet.getEndPointWUPIngresConduitIngres())
                .routeId(nameSet.getRouteIngresConduitIngres2WUPIngres())
                .bean(WUPIngresConduit.class, "forwardIntoWUP(*, Exchange," + this.wupNode.extractNodeKey() + ")")
                .to(nameSet.getEndPointWUPIngres());

        from(nameSet.getEndPointWUPIngres())
                .routeId(nameSet.getRouteCoreWUP())
                .bean(TwinTypeProvocationProcessingBean.class, "collectForQueueing(*, " + specifyTwinTypeName() +")");
    }
    }
}
