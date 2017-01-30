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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

public class NLPProcessorTest {

	private TestRunner testRunner;

	public static final String ATTRIBUTE_INPUT_NAME = "sentence";
	public static final String PROPERTY_NAME_EXTRA = "Extra Resources";
	
    public static final PropertyDescriptor MY_PROPERTY = new PropertyDescriptor
            .Builder().name(ATTRIBUTE_INPUT_NAME)
            .description("A sentence to parse, such as a Tweet.")
            .required(true)
            .expressionLanguageSupported(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    public static final PropertyDescriptor EXTRA_RESOURCE = new PropertyDescriptor.Builder()
    		   .name(PROPERTY_NAME_EXTRA)
    		   .description("The path to one or more Apache OpenNLP Models to add to the classpath. See http://opennlp.sourceforge.net/models-1.5/")
    		   .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    		   .expressionLanguageSupported(true)
    		   .required(true)
    		   .defaultValue("src/main/resources/META-INF/input")
    		   .dynamic(true)
    		   .build();
    
	@Before
	public void init() {
		testRunner = TestRunners.newTestRunner(NLPProcessor.class);
	}

	@Test
	public void testProcessor() {
		
		testRunner.setProperty(MY_PROPERTY, "Tim Spann wrote some code to test NLP with Susan Smith and Doug Jones in New York City, NY and in London, UK.");
		testRunner.setProperty(EXTRA_RESOURCE, "src/main/resources/META-INF/input");
		
		try {
			testRunner.enqueue(new FileInputStream(new File("src/test/resources/test.csv")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.setValidateExpressionUsage(false);
		testRunner.run();
		testRunner.assertValid();
		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(NLPProcessor.REL_SUCCESS);

		for (MockFlowFile mockFile : successFiles) {
			try {
				System.out.println("FILE:" + new String(mockFile.toByteArray(), "UTF-8"));
				System.out.println("Attribute: " + mockFile.getAttribute(NLPProcessor.ATTRIBUTE_OUTPUT_NAME));
				System.out.println("Attribute 2: " + mockFile.getAttribute(NLPProcessor.ATTRIBUTE_OUTPUT_LOCATION_NAME));
				
				assertNotNull(  mockFile.getAttribute(NLPProcessor.ATTRIBUTE_OUTPUT_NAME) );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}
