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
package org.encog.app.generate;

import java.io.File;
import java.util.Date;

import org.encog.Encog;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.script.prop.ScriptProperties;
import org.encog.app.generate.generators.LanguageSpecificGenerator;
import org.encog.app.generate.generators.ProgramGenerator;
import org.encog.app.generate.generators.TemplateGenerator;
import org.encog.app.generate.generators.cs.GenerateCS;
import org.encog.app.generate.generators.java.GenerateEncogJava;
import org.encog.app.generate.generators.js.GenerateEncogJavaScript;
import org.encog.app.generate.generators.mql4.GenerateMQL4;
import org.encog.app.generate.generators.ninja.GenerateNinjaScript;
import org.encog.app.generate.program.EncogGenProgram;
import org.encog.app.generate.program.EncogProgramNode;
import org.encog.ml.MLEncodable;
import org.encog.ml.MLMethod;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

/**
 * Perform Encog code generation. Encog is capable of generating code from
 * several different objects. This code generation will be to the specified
 * target language.
 */
public class EncogCodeGeneration {

	/**
	 * Is the specified method supported for code generation?
	 * 
	 * @param method
	 *            The specified method.
	 * @return True, if the specified method is supported.
	 */
	public static boolean isSupported(final MLMethod method) {
		if (method instanceof BasicNetwork) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The target language for the code generation.
	 */
	private final TargetLanguage targetLanguage;

	/**
	 * True if the data should be embedded.
	 */
	private boolean embedData;

	/**
	 * The language specific code generator.
	 */
	private LanguageSpecificGenerator generator;

	/**
	 * The program that we are generating.
	 */
	private final EncogGenProgram program = new EncogGenProgram();

	/**
	 * Construct the generation object.
	 * 
	 * @param theTargetLanguage
	 *            The target language.
	 */
	public EncogCodeGeneration(final TargetLanguage theTargetLanguage) {
		this.targetLanguage = theTargetLanguage;

		switch (theTargetLanguage) {
		case NoGeneration:
			throw new AnalystCodeGenerationError(
					"No target language has been specified for code generation.");
		case Java:
			this.generator = new GenerateEncogJava();
			break;
		case CSharp:
			this.generator = new GenerateCS();
			break;
		case MQL4:
			this.generator = new GenerateMQL4();
			break;
		case NinjaScript:
			this.generator = new GenerateNinjaScript();
			break;
		case JavaScript:
			this.generator = new GenerateEncogJavaScript();
			break;

		}
	}

	/**
	 * Generate the code from Encog Analyst.
	 * 
	 * @param analyst
	 *            The Encog Analyst object to use for code generation.
	 */
	public void generate(final EncogAnalyst analyst) {

		if (this.targetLanguage == TargetLanguage.MQL4
				|| this.targetLanguage == TargetLanguage.NinjaScript) {
			if (!this.embedData) {
				throw new AnalystCodeGenerationError(
						"MQL4 and Ninjascript must be embedded.");
			}
		}

		if (this.generator instanceof ProgramGenerator) {
			final String methodID = analyst
					.getScript()
					.getProperties()
					.getPropertyString(
							ScriptProperties.ML_CONFIG_MACHINE_LEARNING_FILE);

			final String trainingID = analyst
					.getScript()
					.getProperties()
					.getPropertyString(ScriptProperties.ML_CONFIG_TRAINING_FILE);

			final File methodFile = analyst.getScript().resolveFilename(
					methodID);
			final File trainingFile = analyst.getScript().resolveFilename(
					trainingID);

			generate(methodFile, trainingFile);
		} else {
			((TemplateGenerator) this.generator).generate(analyst);
		}
	}

	/**
	 * Generate from a method and data.
	 * 
	 * @param method
	 *            The machine learning method to generate from.
	 * @param data
	 *            The data to use perform generation.
	 */
	public void generate(final File method, final File data) {
		EncogProgramNode createNetworkFunction = null;
		this.program.addComment("Code generated by Encog v"
				+ Encog.getInstance().getProperties().get(Encog.ENCOG_VERSION));
		this.program.addComment("Generation Date: " + new Date().toString());
		this.program.addComment("Generated code may be used freely");
		this.program.addComment("http://www.heatonresearch.com/encog");
		final EncogProgramNode mainClass = this.program
				.createClass("EncogExample");

		if (this.targetLanguage == TargetLanguage.MQL4
				|| this.targetLanguage == TargetLanguage.NinjaScript) {
			throw new AnalystCodeGenerationError(
					"MQL4 and Ninjascript can only be generated from Encog Analyst");
		}

		if (data != null) {
			mainClass.embedTraining(data);
			if (!(this.generator instanceof GenerateEncogJavaScript)) {
				mainClass.generateLoadTraining(data);
			}
		}

		if (method != null) {
			createNetworkFunction = generateForMethod(mainClass, method);
		}

		final EncogProgramNode mainFunction = mainClass.createMainFunction();

		if (createNetworkFunction != null) {
			mainFunction.createFunctionCall(createNetworkFunction, "MLMethod",
					"method");
		}

		if (data != null) {
			if (!(this.generator instanceof GenerateEncogJavaScript)) {
				mainFunction.createFunctionCall("createTraining", "MLDataSet",
						"training");
			}
		}
		mainFunction
				.addComment("Network and/or data is now loaded, you can add code to train, evaluate, etc.");

		((ProgramGenerator) this.generator).generate(this.program,
				this.embedData);
	}

	/**
	 * GEnerate from a machine learning method.
	 * 
	 * @param mainClass
	 *            The main class.
	 * @param method
	 *            The filename of the method.
	 * @return The newly created node.
	 */
	private EncogProgramNode generateForMethod(
			final EncogProgramNode mainClass, final File method) {

		if (this.embedData) {
			final MLEncodable encodable = (MLEncodable) EncogDirectoryPersistence
					.loadObject(method);
			final double[] weights = new double[encodable.encodedArrayLength()];
			encodable.encodeToArray(weights);
			mainClass.createArray("WEIGHTS", weights);
		}

		return mainClass.createNetworkFunction("createNetwork", method);
	}

	/**
	 * @return the targetLanguage
	 */
	public TargetLanguage getTargetLanguage() {
		return this.targetLanguage;
	}

	/**
	 * @return True, if data should be embeded.
	 */
	public boolean isEmbedData() {
		return this.embedData;
	}

	/**
	 * Save the contents to a string.
	 * 
	 * @return The contents.
	 */
	public String save() {
		return this.generator.getContents();
	}

	/**
	 * Save the contents to the specified file.
	 * 
	 * @param file The file to save to.
	 */
	public void save(final File file) {
		this.generator.writeContents(file);
	}

	/**
	 * Set if data should be embeded.
	 * 
	 * @param embedData
	 *            True, if data should embeded.
	 */
	public void setEmbedData(final boolean embedData) {
		this.embedData = embedData;
	}

}
