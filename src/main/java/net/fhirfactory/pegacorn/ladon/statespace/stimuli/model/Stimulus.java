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
package net.fhirfactory.pegacorn.ladon.statespace.stimuli.model;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.ladon.model.behaviours.BehaviourIdentifier;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Stimulus {
    private List<Resource> stimulusResourceSet;
    private StimulusIdentifier id;
    private FDNToken originalUoW;
    private Date creationDate;

    public Stimulus(StimulusIdentifier newId, FDNToken oriUoW, List<Resource> resourceSet){
        this.stimulusResourceSet = new ArrayList<Resource>();
        this.stimulusResourceSet.addAll(resourceSet);
        this.originalUoW = oriUoW;
        this.id = newId;
    }

    public Stimulus(){
        this.stimulusResourceSet = new ArrayList<Resource>();
        this.originalUoW = null;
        this.id = null;
    }

    public List<Resource> getStimulusResourceSet() {
        return stimulusResourceSet;
    }

    public void setStimulusResourceSet(List<Resource> stimulusResourceSet) {
        this.stimulusResourceSet.clear();
        this.stimulusResourceSet.addAll(stimulusResourceSet);
    }

    public StimulusIdentifier getId() {
        return id;
    }

    public void setId(StimulusIdentifier id) {
        this.id = id;
    }

    public FDNToken getOriginalUoW() {
        return originalUoW;
    }

    public void setOriginalUoW(FDNToken originalUoW) {
        this.originalUoW = originalUoW;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
