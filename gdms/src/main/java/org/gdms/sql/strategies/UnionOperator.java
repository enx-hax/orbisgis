package org.gdms.sql.strategies;

import org.gdms.data.ExecutionException;
import org.gdms.data.metadata.Metadata;
import org.gdms.data.types.Constraint;
import org.gdms.data.types.Type;
import org.gdms.driver.DriverException;
import org.gdms.driver.ObjectDriver;

public class UnionOperator extends AbstractOperator implements Operator {

	public UnionOperator(Operator op1, Operator op2) {
		this.addChild(op1);
		this.addChild(op2);
	}

	public ObjectDriver getResultContents() throws ExecutionException {
		try {
			return new UnionDriver(getOperator(0).getResult(), getOperator(1)
					.getResult(), getResultMetadata());
		} catch (DriverException e) {
			throw new ExecutionException("Cannot obtain "
					+ "the metadata of the union", e);
		}
	}

	public Metadata getResultMetadata() throws DriverException {
		return getOperator(1).getResultMetadata();
	}

	/**
	 * Checks that the metadata of both sources is identical
	 *
	 * @see org.gdms.sql.strategies.AbstractOperator#validateExpressionTypes()
	 */
	@Override
	public void validateExpressionTypes() throws SemanticException,
			DriverException {
		Metadata m1 = getOperator(0).getResultMetadata();
		Metadata m2 = getOperator(1).getResultMetadata();
		if (m1.getFieldCount() != m2.getFieldCount()) {
			throw new SemanticException("Cannot evaluate "
					+ "union on sources with different field count");
		}
		for (int i = 0; i < m1.getFieldCount(); i++) {
			if (!m1.getFieldName(i).equals(m2.getFieldName(i))) {
				throw new SemanticException("Cannot evaluate union: " + (i + 1)
						+ "th field name does not match");
			}
			Type t1 = m1.getFieldType(i);
			Type t2 = m2.getFieldType(i);
			if (t1.getTypeCode() != t2.getTypeCode()) {
				throw new SemanticException("Cannot evaluate union: " + (i + 1)
						+ "th field type does not match");
			}
			if (t1.getConstraints().length != t2.getConstraints().length) {
				throw new SemanticException("Cannot evaluate union: " + (i + 1)
						+ "th field constraints does not match");
			}
			for (int j = 0; j < t1.getConstraints().length; j++) {
				Constraint c1 = t1.getConstraints()[j];
				Constraint c2 = t2.getConstraint(c1.getConstraintName());
				if (c2 == null) {
					throw new SemanticException("Cannot evaluate union: "
							+ "missing " + c1.getConstraintName()
							+ " constraint in second operator");
				}
				if (!c1.getConstraintValue().equals(c2.getConstraintValue())) {
					throw new SemanticException("Cannot evaluate union: "
							+ c1.getConstraintName()
							+ " constraints does not match");
				}
			}
		}
		super.validateExpressionTypes();
	}

}
