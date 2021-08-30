package com.myorg;

import com.github.eladb.dynamotableviewer.TableViewer;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;


public class CdkWorkshopJavaStack extends Stack {
    public CdkWorkshopJavaStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkWorkshopJavaStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);
        
        // Defines the new lambda resource
        final Function hello = Function.Builder.create(this, "HelloHandler")
        		.runtime(Runtime.NODEJS_14_X)
        		.code(Code.fromAsset("lambda"))
        		.handler("hello.handler")
        		.build();
        
        // Defines the hitcounter resource
        final HitCounter helloWithCounter = new HitCounter(this, "HelloHitCounter", HitCounterProps.builder()
                .downstream(hello)
                .build());
        
        // Defines an API gateway REST API resource backed by the "hello" function
        LambdaRestApi.Builder.create(this, "EndPoint")
        	.handler(helloWithCounter.getHandler())
        	.build();
        
        // Defines a viewer for the Hitcounts table
        TableViewer.Builder.create(this, "ViewerHitCount")
	        .title("Hello Hits")
	        .table(helloWithCounter.getTable())
	        .build();
        
    }
}
