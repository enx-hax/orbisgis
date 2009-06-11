package org.orbisgis.core.ui.views.geocognition.actions;

import org.gdms.sql.function.Function;
import org.gdms.sql.function.FunctionManager;
import org.orbisgis.core.Services;
import org.orbisgis.core.geocognition.Geocognition;
import org.orbisgis.core.geocognition.GeocognitionElement;
import org.orbisgis.core.geocognition.sql.GeocognitionBuiltInFunction;
import org.orbisgis.core.geocognition.sql.GeocognitionFunctionFactory;
import org.orbisgis.core.ui.views.geocognition.action.IGeocognitionAction;
import org.orbisgis.errorManager.ErrorManager;

public class UnRegisterBuiltInFunction implements IGeocognitionAction {

	@Override
	public boolean accepts(Geocognition geocog, GeocognitionElement element) {
		if (GeocognitionFunctionFactory.BUILT_IN_FUNCTION_ID.equals(element
				.getTypeId())) {
			String registered = element.getProperties().get(
					GeocognitionBuiltInFunction.REGISTERED);
			if ((registered != null)
					&& registered
							.equals(GeocognitionBuiltInFunction.IS_REGISTERED)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean acceptsSelectionCount(Geocognition geocog, int selectionCount) {
		return selectionCount > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Geocognition geocognition, GeocognitionElement element) {
		if (GeocognitionFunctionFactory.BUILT_IN_FUNCTION_ID.equals(element
				.getTypeId())) {
			Class<? extends Function> fnc = (Class<? extends Function>) element
					.getObject();
			try {
				FunctionManager.remove(fnc.newInstance().getName());
			} catch (InstantiationException e) {
				Services.getService(ErrorManager.class).error("Bug!", e);
			} catch (IllegalAccessException e) {
				Services.getService(ErrorManager.class).error("Bug!", e);
			}
		}
	}

}