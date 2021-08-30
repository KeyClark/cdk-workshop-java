package com.myorg;

import software.amazon.awscdk.services.lambda.IFunction;

public interface HitCounterProps {
	// Public constructor for the props builder
	public static Builder builder() {
		return new Builder();
	}
	
	// The function for which we want to count URL hits
	IFunction getDownstream();
	
	// The builder for the props interface
	/**
	 * 
	 * @author JakehClark
	 * @exception NullPointerException If downstream property is null
	 *
	 */
	public static class Builder {
		private IFunction downstream;
		
		public Builder downstream(final IFunction function) {
			this.downstream = function;
			return this;
		}
		
		public HitCounterProps build() {
			if(this.downstream == null) {
				throw new NullPointerException("This downstream property is required!");
			}
			
			return new HitCounterProps() {
				@Override
				public IFunction getDownstream() {
					return downstream;
				}
			};
		}
	}
}
