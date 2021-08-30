package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

public class HitCounter extends Construct {
	private final Function handler;
	private final Table table;

	/**
	 * Extends the main Construct class. Defines this class as a single resource
	 * 
	 * @param scope The scope in which the construct is defined
	 * @param id A unique identifier for this construct within the current scope
	 * @param props
	 */
	public HitCounter(final Construct scope, final String id, final HitCounterProps props) {
		super(scope, id);
		
		this.table = Table.Builder.create(this, "Hits")
				.partitionKey(Attribute.builder()
						.name("path)")
						.type(AttributeType.STRING)
						.build())
				.build();
		
		// HashMap is a set of key-value pairs
		final Map<String, String> environment = new HashMap<>();
		environment.put("DOWNSTREAM_FUNCTION_NAME", props.getDownstream().getFunctionName());
		environment.put("HITS_TABLE_NAME", this.table.getTableName());
		
		// Define the HitCounter handler
		this.handler = Function.Builder.create(this, "HitCounterHandler")
				.runtime(Runtime.NODEJS_14_X)
				.handler("hitcounter.handler")
				.code(Code.fromAsset("lambda"))
				.environment(environment)
				.build();
		
		// Grant the lambda function read/write permissions to our table, "HITS_TABLE_NAME"
		this.table.grantReadData(this.handler);
		
		// Grant the lambda function invoke permission to the the downstream function
		props.getDownstream().grantInvoke(this.handler);
	}
	
	 
	// The "@return" annotation here is used for JavaDoc
	/**
	 * @return counter definition
	 */
	public Function getHandler() {
		return this.handler;
	}
	
	/**
	 * @return the counter table
	 */
	public Table getTable() {
		return this.table;
	}

}
