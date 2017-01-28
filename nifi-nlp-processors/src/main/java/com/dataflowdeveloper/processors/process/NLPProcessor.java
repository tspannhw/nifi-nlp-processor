/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dataflowdeveloper.processors.process;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;
import com.google.gson.Gson; 
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
   
@Tags({"nlpprocessor"})
@CapabilityDescription("Run OpenNLP Name Finder")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class NLPProcessor extends AbstractProcessor {

	public static final String ATTRIBUTE_OUTPUT_NAME = "names";
	public static final String ATTRIBUTE_INPUT_NAME = "sentence";
	
    public static final PropertyDescriptor MY_PROPERTY = new PropertyDescriptor
            .Builder().name(ATTRIBUTE_INPUT_NAME)
            .description("sentence")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name("success")
            .description("Successfully extracted people.")
            .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("Failed to extract people.")
            .build();
    
    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    private OpenNLPService service;
    
    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        descriptors.add(MY_PROPERTY);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {
    	return;
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        if ( flowFile == null ) {
        	flowFile = session.create();
        }
        final AtomicReference<String> valueRef = new AtomicReference<>();
         
        String sentence = flowFile.getAttribute(ATTRIBUTE_INPUT_NAME);
        String sentence2 = context.getProperty(ATTRIBUTE_INPUT_NAME).getValue();

        if ( sentence == null) {   
        	sentence = sentence2;
        	getLogger().info("Input Sent:" + sentence);
        }
        
        service = new OpenNLPService();   

        String outputJSON = null;
        
        try {
        	//service.
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        
/*        SoupService soupService = new SoupService();
        List<PrintableLink> value = null;
        String outputJSON = null;
        try {
        	value = soupService.extract(url,null);
			
			if ( value == null) {
				return;
			}
			
			 Gson gson = new Gson();
			 outputJSON = gson.toJson(value);	
			 valueRef.set(outputJSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/

		try {
			//Output Flowfile			
			//flowFile = session.putAttribute(flowFile, ATTRIBUTE_OUTPUT_NAME, outputJSON);

			flowFile = session.write(flowFile, new OutputStreamCallback() {
				@Override
				public void process(OutputStream out) throws IOException {
					out.write(valueRef.get().getBytes());
				}
		    });
			
			session.transfer(flowFile, REL_SUCCESS);
		} catch (Exception e) {
			getLogger().error("Unable to process NLP Processor file");
			session.transfer(flowFile, REL_FAILURE);
		}
    }
}
