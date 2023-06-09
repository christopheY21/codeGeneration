/**
 *
 * $Id$
 */
package statechart.validation;

import org.eclipse.emf.common.util.EList;

import statechart.Node;
import statechart.Variable;

/**
 * A sample validator interface for {@link statechart.Node}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface NodeValidator {
	boolean validate();

	boolean validateName(String value);
	boolean validateLabel(String value);
	boolean validateType(String value);
	boolean validateActivity(String value);
	boolean validateVariables(EList<Variable> value);
	boolean validateChildren(EList<Node> value);
	boolean validateFather(Node value);
	boolean validateActions(String value);
	boolean validateMetadata(String value);
}
