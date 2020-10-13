/*
 * Copyright (c) 2020 Mark A. Hunter
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.pegacorn.ladon.statespace.twinpathway.twinstate.common;

import net.fhirfactory.pegacorn.ladon.model.behaviours.BehaviourIdentifier;
import net.fhirfactory.pegacorn.ladon.model.twin.DigitalTwinIdentifier;
import net.fhirfactory.pegacorn.ladon.statespace.stimuli.model.Stimulus;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class TwinInstanceState {
    abstract protected Logger getLogger();

    private TwinRegentResourceTypeEnum twinRegentType;
    private TwinResourceTypeEnum twinResourceType;
    private ConcurrentHashMap<DigitalTwinIdentifier, BehaviourIdentifier> instanceBusyStatus;
    private ConcurrentHashMap<DigitalTwinIdentifier, ConcurrentLinkedQueue<Stimulus>> twinInstanceQueue;

    public TwinInstanceState(){
        this.twinInstanceQueue = new ConcurrentHashMap<>();
        this.instanceBusyStatus = new ConcurrentHashMap<>();
    }

    // Abstract methods

    abstract protected TwinResourceTypeEnum specifyTwinResourceType();
    abstract protected TwinRegentResourceTypeEnum specifyTwinRegentType();

    // Twin Instance Active/Busy Status

    public void lockTwinInstance(DigitalTwinIdentifier twinIdentifier, BehaviourIdentifier behaviourIdentifier){
        if(instanceBusyStatus.containsKey(twinIdentifier)){
            return;
        }
        instanceBusyStatus.put(twinIdentifier, behaviourIdentifier);
    }

    public void unlockTwinInstance(DigitalTwinIdentifier twinIdentifier){
        if(instanceBusyStatus.containsKey(twinIdentifier)){
            instanceBusyStatus.remove(twinIdentifier);
        }
    }

    public boolean isTwinLocked(DigitalTwinIdentifier twinIdentifier){
        if(instanceBusyStatus.containsKey(twinIdentifier)){
            return(true);
        } else {
            return(false);
        }
    }

    public boolean isBusy(DigitalTwinIdentifier twinIdentifier){
        return(isTwinLocked(twinIdentifier));
    }

    public BehaviourIdentifier getTwinActiveBehaviour(DigitalTwinIdentifier twinIdentifier){
        if(instanceBusyStatus.containsKey(twinIdentifier)){
            return(instanceBusyStatus.get(twinIdentifier));
        } else {
            return(null);
        }
    }

    /**
     * This method supports the addition of new activity elements (StimulusPackage).
     *
     * @param twinInstanceIdentifier The Twin Instance unique Identifier for the Digital Twin Type.
     * @param newStimuli The StimulusPackage to be added the the TwinInstance's activity queue.
     */
    public void addStimulus2Queue(DigitalTwinIdentifier twinInstanceIdentifier, Stimulus newStimuli) {
        if (twinInstanceQueue.containsKey(twinInstanceIdentifier)) {
            Queue<Stimulus> twinStimuli = twinInstanceQueue.get(twinInstanceIdentifier);
            if (!twinStimuli.contains(newStimuli)) {
                twinStimuli.add(newStimuli);
            }
        } else {
            ConcurrentLinkedQueue<Stimulus> stimuliQueue = new ConcurrentLinkedQueue<>();
            stimuliQueue.add(newStimuli);
            twinInstanceQueue.put(twinInstanceIdentifier, stimuliQueue);
        }
    }

    /**
     * This method supports the extraction (pop'ing) of the next-in-line activity element from the queue. It also
     * removes that element from the queue.
     *
     * @param twinInstanceIdentifier The Twin Instance unique Identifier for the Digital Twin Type.
     * @return A StimulusPackage from the FIFO queue for the identified Digital Twin.
     */
    public Stimulus getNextStimulus(DigitalTwinIdentifier twinInstanceIdentifier) {
        if (twinInstanceQueue.containsKey(twinInstanceIdentifier)) {
            Stimulus nextStimulusPackage;
            Queue<Stimulus> twinStimuli = twinInstanceQueue.get(twinInstanceIdentifier);
            if (!twinStimuli.isEmpty()){
                nextStimulusPackage = twinStimuli.poll();
                if(twinStimuli.isEmpty()){
                    twinInstanceQueue.remove(twinInstanceIdentifier);
                }
                return(nextStimulusPackage);
            }
            return(null);
        }
        return(null);
    }

    /**
     * This method is used to ascertain if their is activity (StimulusPackage) items within the
     * Digital Twin's Acitivity queue.
     *
     * @param twinInstanceIdentifier The Twin Instance unique Identifier for the Digital Twin Type.
     * @return A count of the number of StimulusPackage elements (Activity) waiting for the Twin Instance to process.
     */
    public int getStimulusCount(DigitalTwinIdentifier twinInstanceIdentifier){
        if (twinInstanceQueue.containsKey(twinInstanceIdentifier)) {
            Stimulus nextStimulusPackage;
            Queue<Stimulus> twinStimuli = twinInstanceQueue.get(twinInstanceIdentifier);
            if (!twinStimuli.isEmpty()) {
                return (twinStimuli.size());
            }
        }
        // If we get to this point, there is no Stimuli for the identifier twinInstanceIdentifier
        // so return ( 0 ).
        return(0);
    }
}
