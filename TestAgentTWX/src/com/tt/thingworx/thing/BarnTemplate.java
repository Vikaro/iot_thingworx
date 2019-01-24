package com.tt.thingworx.thing;



import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;

import java.util.Random;

import org.slf4j.Logger;


import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.StringPrimitive;

@ThingworxPropertyDefinitions(properties = {	
		@ThingworxPropertyDefinition(name="BuildingID", description="", baseType="STRING",
		aspects={"dataChangeType:ALWAYS",
                "dataChangeThreshold:0",
                "cacheTime:0", 
                "isPersistent:FALSE", 
                "isReadOnly:FALSE", 
                "pushType:ALWAYS", 
                "isFolded:FALSE",
                "defaultValue:0"}),		
		@ThingworxPropertyDefinition(name="Temperature", description="", baseType="NUMBER",
		aspects={"dataChangeType:ALWAYS",
                        "dataChangeThreshold:0",
                        "cacheTime:0", 
                        "isPersistent:FALSE", 
                        "isReadOnly:FALSE", 
                        "pushType:ALWAYS", 
                        "isFolded:FALSE",
                        "defaultValue:0"}),
		@ThingworxPropertyDefinition(name="Watts", description="", baseType="NUMBER",
		aspects={"dataChangeType:ALWAYS",
                "dataChangeThreshold:0",
                "cacheTime:0", 
                "isPersistent:FALSE", 
                "isReadOnly:FALSE", 
                "pushType:ALWAYS", 
                "isFolded:FALSE",
                "defaultValue:0"})
})


public class BarnTemplate extends VirtualThing implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(BarnTemplate.class);
	
	private final static String BUILDINGID_FIELD = "BuildingID";
	private final static String TEMPERATURE_FIELD = "Temperature";
	private final static String WATTS_FIELD = "Watts";
	private String buildingID;
	private Double temperature;
	private Double watts;
	private String thingName = null;
	
	public BarnTemplate(String name, String description, ConnectedThingClient client) {
		super(name, description, client);
		this.getThingShape();
		this.getBindingName();
		thingName = name;
		// Populate the thing shape with the properties, services, and events that are annotated in this code
		try {
			super.initializeFromAnnotations();
		} catch (Exception ex) {
			LOG.error("Not work", ex);
		}
		this.init();
	}
	
	// From the VirtualThing class
		// This method will get called when a connect or reconnect happens
		// Need to send the values when this happens
		// This is more important for a solution that does not send its properties on a regular basis
	public void synchronizeState() {
		// Be sure to call the base class
		super.synchronizeState();
		// Send the property values to ThingWorx when a synchronization is required
		super.syncProperties();
	}

	
	private void init() {
		buildingID = "jeden";
		temperature = new Double(100);
		watts = new Double(100);
	}
	
	@Override
	public void run() {
		try {
			// Delay for a period to verify that the Shutdown service will return
			Thread.sleep(1000);
			// Shutdown the client
			this.getClient().shutdown();
		} catch (Exception ex) {
			LOG.error("Error " + thingName, ex);
		}
		
	}
	
	@Override
	public void processScanRequest() throws Exception {
		temperature = temperature + (Math.random()*20 - 10);
		if(temperature < 0){
			temperature = new Double(0);
		}
		if(temperature > 200){
			temperature = new Double(200);
		}
		
		watts = watts + (Math.random()*20 - 10);
		if(watts < 0){
			watts = new Double(0);
		}
		if(watts > 200){
			watts = new Double(200);
		}
		
		buildingID="jeden";
		
		setBuildingID();
		setTemperature();
		setWatts();
		this.updateSubscribedProperties(5000);
	}

	public void setBuildingID() throws Exception {
		setProperty(BUILDINGID_FIELD, this.buildingID);
	}

	public void setTemperature() throws Exception {
		setProperty(TEMPERATURE_FIELD, this.temperature);
	}

	public void setWatts() throws Exception {
		setProperty(WATTS_FIELD, this.watts);
	}
		
	/**
	@ThingworxServiceDefinition(name="AddTwoNumbers", description="add operation")
	@ThingworxServiceResult(name="result", description="Result", baseType="NUMBER")
	public Double AddTwoNumbers( 
		@ThingworxServiceParameter( name="num1", description="Value 1", baseType="NUMBER") Double num1,
		@ThingworxServiceParameter( name="num2", description="Value 2", baseType="NUMBER") Double num2) throws Exception {
		LOG.info("Add two numbers: " + num1.doubleValue() + " and " + num2.doubleValue());
		
		return num1 + num2;
	}*/
	
}

