/*
 * Encog(tm) Core v3.4 - Java Version
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-core
 
 * Copyright 2008-2016 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.ml.world;

import org.encog.util.logging.EncogLogging;


public class WorldError extends RuntimeException {

	/**
	 * World errors.
	 */
	private static final long serialVersionUID = -5909341149180956178L;

	/**
	 * Construct a message exception.
	 * 
	 * @param msg
	 *            The exception message.
	 */
	public WorldError(final String msg) {
		super(msg);
		EncogLogging.log(EncogLogging.LEVEL_ERROR, msg);
	}

	/**
	 * Construct an exception that holds another exception.
	 * 
	 * @param t
	 *            The other exception.
	 */
	public WorldError(final Throwable t) {
		super(t);
		EncogLogging.log(EncogLogging.LEVEL_ERROR, t);
	}

	/**
	 * Construct an exception that holds another exception.
	 * 
	 * @param msg
	 *            A message.
	 * @param t
	 *            The other exception.
	 */
	public WorldError(final String msg, final Throwable t) {
		super(msg, t);
		EncogLogging.log(EncogLogging.LEVEL_ERROR, msg);
		EncogLogging.log(EncogLogging.LEVEL_ERROR, t);
	}
}
