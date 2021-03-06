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
package org.encog.ml.prg;

import org.encog.ml.MLMethod;
import org.encog.ml.ea.codec.GeneticCODEC;
import org.encog.ml.ea.genome.Genome;

/**
 * Encode and decode an Encog program between genome and phenotypes. This is a
 * passthrough, as the Encog geneome and phenome are identical.
 */
public class PrgCODEC implements GeneticCODEC {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MLMethod decode(final Genome genome) {
		return genome;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Genome encode(final MLMethod phenotype) {
		return (Genome) phenotype;
	}

}
