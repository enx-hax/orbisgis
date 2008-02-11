package org.gdms.sql.function.alphanumeric;

import org.gdms.data.types.Type;
import org.gdms.data.types.TypeFactory;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;
import org.gdms.sql.ColumnValue;
import org.gdms.sql.FunctionTest;
import org.gdms.sql.strategies.IncompatibleTypesException;

public class AlphanumericFunctionTest extends FunctionTest {

	public void testString2Int() throws Exception {
		// Test null input
		String2IntFunction function = new String2IntFunction();
		Value res = evaluate(function, new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		res = evaluate(function, ValueFactory.createValue("2"));
		assertTrue(res.getType() == Type.INT);
		assertTrue(res.getAsInt() == 2);

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue(54), ValueFactory
					.createValue(7));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {

		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue(true));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

	}

	public void testString2Double() throws Exception {
		// Test null input
		String2DoubleFunction function = new String2DoubleFunction();
		Value res = evaluate(function, new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		res = evaluate(function, ValueFactory.createValue("2.3"));
		assertTrue(res.getType() == Type.DOUBLE);
		assertTrue(res.getAsDouble() == 2.3);

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue(54), ValueFactory
					.createValue(7));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {

		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue(true));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

	}

	public void testAverage() throws Exception {
		// Test null input
		Average avg = new Average();
		Value res = evaluate(avg, new ColumnValue(Type.INT, ValueFactory
				.createNullValue()));
		assertTrue(res.isNull());
		avg = new Average();
		res = evaluate(avg, new ColumnValue(Type.DOUBLE, ValueFactory
				.createNullValue()));
		res = evaluate(avg, ValueFactory.createValue(5));
		assertTrue(res.getAsDouble() == 5);

		// Test normal input value and type
		avg = new Average();
		res = evaluate(avg, ValueFactory.createValue(2));
		res = evaluate(avg, ValueFactory.createValue(4));
		res = evaluate(avg, new ColumnValue(Type.INT, ValueFactory
				.createNullValue()));
		assertTrue(res.getAsDouble() == 3);

		// Test too many parameters
		try {
			res = evaluate(avg, ValueFactory.createValue(54), ValueFactory
					.createValue(6));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {

		}

		// Test wrong parameter type
		try {
			res = evaluate(avg, ValueFactory.createValue(true));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testString2Boolean() throws Exception {
		// Test null input
		String2BooleanFunction function = new String2BooleanFunction();
		Value res = evaluate(function, new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		res = evaluate(function, ValueFactory.createValue("true"));
		assertTrue(res.getType() == Type.BOOLEAN);
		assertTrue(res.getAsBoolean() == true);

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue("true"),
					ValueFactory.createValue("false"));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue(false));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testConcatenate() throws Exception {
		// Test null input
		ConcatenateFunction function = new ConcatenateFunction();
		Value res = evaluate(function, new ColumnValue(Type.STRING,
				ValueFactory.createValue("asd")), new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		res = evaluate(function, ValueFactory.createValue("ere"), ValueFactory
				.createValue("ere"), ValueFactory.createValue("ere"));
		assertTrue(res.getType() == Type.STRING);
		assertTrue(res.getAsString().equals("ereereere"));

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue("as"));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue(3));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testCount() throws Exception {
		// Test null input
		Count function = new Count();
		Value res = evaluate(function, new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.getAsInt() == 0);

		// Test normal input value and type
		function = new Count();
		res = evaluate(function, ValueFactory.createValue(3));
		res = evaluate(function, ValueFactory.createNullValue());
		assertTrue(res.getType() == Type.LONG);
		assertTrue(res.getAsInt() == 1);

		function = new Count();
		res = evaluate(function, new ColumnValue(Type.STRING, ValueFactory
				.createNullValue()), new ColumnValue(Type.STRING, ValueFactory
				.createNullValue()));
		res = evaluate(function, new ColumnValue(Type.STRING, ValueFactory
				.createNullValue()), new ColumnValue(Type.STRING, ValueFactory
				.createNullValue()));
		assertTrue(res.getType() == Type.LONG);
		assertTrue(res.getAsInt() == 2);

		// Test too many parameters
		try {
			res = evaluate(function, new Value[0]);
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testMax() throws Exception {
		// Test null input
		Max function = new Max();
		Value res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		function = new Max();
		res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createValue((byte) 3)));
		res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createNullValue()));
		assertTrue(res.getType() == Type.BYTE);
		assertTrue(res.getAsInt() == 3);

		function = new Max();
		res = evaluate(function, ValueFactory.createValue(3f));
		res = evaluate(function, new ColumnValue(Type.FLOAT, ValueFactory
				.createNullValue()));
		assertTrue(res.getType() == Type.FLOAT);
		assertTrue(res.getAsInt() == 3);

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue(3), ValueFactory
					.createValue(3));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue("f"));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testMin() throws Exception {
		// Test null input
		Min function = new Min();
		Value res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		function = new Min();
		res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createValue((byte) 3)));
		res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createNullValue()));
		assertTrue(res.getType() == Type.BYTE);
		assertTrue(res.getAsInt() == 3);

		function = new Min();
		res = evaluate(function, ValueFactory.createValue(3f));
		res = evaluate(function, new ColumnValue(Type.FLOAT, ValueFactory
				.createNullValue()));
		assertTrue(res.getType() == Type.FLOAT);
		assertTrue(res.getAsInt() == 3);

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue(3), ValueFactory
					.createValue(3));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue("f"));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testSum() throws Exception {
		// Test null input
		Sum function = new Sum();
		Value res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		function = new Sum();
		res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createValue((byte) 3)));
		res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createValue((byte) 3)));
		res = evaluate(function, new ColumnValue(Type.BYTE, ValueFactory
				.createNullValue()));
		assertTrue(TypeFactory.isNumerical(res.getType()));
		assertTrue(res.getAsInt() == 6);

		function = new Sum();
		res = evaluate(function, ValueFactory.createValue(3f));
		res = evaluate(function, new ColumnValue(Type.FLOAT, ValueFactory
				.createNullValue()));
		assertTrue(res.getType() == Type.FLOAT);
		assertTrue(res.getAsInt() == 3);

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue(3), ValueFactory
					.createValue(3));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue("f"));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testString2Date() throws Exception {
		// Test null input
		String2DateFunction function = new String2DateFunction();
		Value res = evaluate(function, new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.isNull());
		res = evaluate(function, new ColumnValue(Type.STRING, ValueFactory
				.createValue("12/8/1923")), new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		String date = "12/9/1990";
		res = evaluate(function, ValueFactory.createValue(date));
		assertTrue(res.getType() == Type.DATE);
		assertTrue(res.getAsDate().getTime() == String2DateFunction.dateFormat
				.parse(date).getTime());

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue("as"),
					ValueFactory.createValue("as"), ValueFactory
							.createValue("as"));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue(3));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}

	public void testStrLength() throws Exception {
		// Test null input
		StrLength function = new StrLength();
		Value res = evaluate(function, new ColumnValue(Type.STRING,
				ValueFactory.createNullValue()));
		assertTrue(res.isNull());

		// Test normal input value and type
		res = evaluate(function, ValueFactory.createValue("fer"));
		assertTrue(res.getType() == Type.INT);
		assertTrue(res.getAsInt() == 3);

		// Test too many parameters
		try {
			res = evaluate(function, ValueFactory.createValue("as"),
					ValueFactory.createValue("as"));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}

		// Test wrong parameter type
		try {
			res = evaluate(function, ValueFactory.createValue(3));
			assertTrue(false);
		} catch (IncompatibleTypesException e) {
		}
	}
}
