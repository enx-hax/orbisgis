/*
 * The GDMS library (Generic Datasources Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). GDMS is produced  by the geomatic team of the IRSTV
 * Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of GDMS.
 *
 * GDMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GDMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GDMS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.gdms.data.values;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.gdms.sql.strategies.IncompatibleTypesException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Clase padre de todos los wrappers sobre tipos del sistema
 *
 * @author Fernando Gonz�lez Cort�s
 */
public abstract class AbstractValue implements Value {
	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.values.value);
	 */
	public Value and(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#or(com.hardcode.gdbms.engine.values.value);
	 */
	public Value or(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#producto(com.hardcode.gdbms.engine.values.value);
	 */
	public Value producto(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#suma(com.hardcode.gdbms.engine.values.value);
	 */
	public Value suma(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.sql.instruction.Operations#inversa(org.gdms.data.values.Value)
	 */
	public Value inversa() throws IncompatibleTypesException {
		throw new IncompatibleTypesException(this
				+ " does not have inverse value");
	}

	/**
	 * @see org.gdms.sql.instruction.Operations#equals(org.gdms.data.values.Value)
	 */
	public Value equals(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.sql.instruction.Operations#notEquals(org.gdms.data.values.Value)
	 */
	public Value notEquals(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.sql.instruction.Operations#greater(org.gdms.data.values.Value)
	 */
	public Value greater(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.sql.instruction.Operations#less(org.gdms.data.values.Value)
	 */
	public Value less(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.sql.instruction.Operations#greaterEqual(org.gdms.data.values.Value)
	 */
	public Value greaterEqual(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.sql.instruction.Operations#lessEqual(org.gdms.data.values.Value)
	 */
	public Value lessEqual(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.data.values.Operations#like(org.gdms.data.values.Value)
	 */
	public Value like(Value value) throws IncompatibleTypesException {
		throw new IncompatibleTypesException("Cannot operate with " + value
				+ " and " + this);
	}

	/**
	 * @see org.gdms.data.values.Value#doEquals(java.lang.Object)
	 */
	public boolean doEquals(Object obj) {
		if (obj instanceof Value) {
			try {
				return ((BooleanValue) this.equals((Value) obj)).getValue();
			} catch (IncompatibleTypesException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return doEquals(obj);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return doHashCode();
	}

	@Override
	public String toString() {
		return getStringValue(ValueWriter.internalValueWriter);
	}

	public byte[] getAsBinary() throws IncompatibleTypesException {
		if (isNull()) {
			return null;
		}
		throw new IncompatibleTypesException("This value is not binary: "
				+ toString() + "(" + getType() + ")");
	}

	public boolean getAsBoolean() throws IncompatibleTypesException {
		if (isNull()) {
			return false;
		}
		throw new IncompatibleTypesException("This value is not boolean: "
				+ toString() + "(" + getType() + ")");
	}

	public byte getAsByte() throws IncompatibleTypesException {
		if (isNull()) {
			return 0;
		}
		throw new IncompatibleTypesException("This value is not byte: "
				+ toString() + "(" + getType() + ")");
	}

	public Date getAsDate() throws IncompatibleTypesException {
		if (isNull()) {
			return null;
		}
		throw new IncompatibleTypesException("This value is not date: "
				+ toString() + "(" + getType() + ")");
	}

	public double getAsDouble() throws IncompatibleTypesException {
		if (isNull()) {
			return 0;
		}
		throw new IncompatibleTypesException("This value is not double: "
				+ toString() + "(" + getType() + ")");
	}

	public float getAsFloat() throws IncompatibleTypesException {
		if (isNull()) {
			return 0;
		}
		throw new IncompatibleTypesException("This value is not float: "
				+ toString() + "(" + getType() + ")");
	}

	public Geometry getAsGeometry() throws IncompatibleTypesException {
		if (isNull()) {
			return null;
		}
		throw new IncompatibleTypesException("This value is not geometry: "
				+ toString() + "(" + getType() + ")");
	}

	public int getAsInt() throws IncompatibleTypesException {
		if (isNull()) {
			return 0;
		}
		throw new IncompatibleTypesException("This value is not integer: "
				+ toString() + "(" + getType() + ")");
	}

	public long getAsLong() throws IncompatibleTypesException {
		if (isNull()) {
			return 0;
		}
		throw new IncompatibleTypesException("This value is not long: "
				+ toString() + "(" + getType() + ")");
	}

	public short getAsShort() throws IncompatibleTypesException {
		if (isNull()) {
			return 0;
		}
		throw new IncompatibleTypesException("This value is not short: "
				+ toString() + "(" + getType() + ")");
	}

	public String getAsString() throws IncompatibleTypesException {
		if (isNull()) {
			return null;
		}
		throw new IncompatibleTypesException("This value is not string: "
				+ toString() + "(" + getType() + ")");
	}

	public Time getAsTime() throws IncompatibleTypesException {
		if (isNull()) {
			return null;
		}
		throw new IncompatibleTypesException("This value is not time: "
				+ toString() + "(" + getType() + ")");
	}

	public Timestamp getAsTimestamp() throws IncompatibleTypesException {
		if (isNull()) {
			return null;
		}
		throw new IncompatibleTypesException("This value is not timestamp: "
				+ toString() + "(" + getType() + ")");
	}

	public ValueCollection getAsValueCollection()
			throws IncompatibleTypesException {
		if (isNull()) {
			return null;
		}
		throw new IncompatibleTypesException(
				"This value is not a value collection: " + toString() + "("
						+ getType() + ")");
	}

	public boolean isNull() {
		return false;
	}

}
