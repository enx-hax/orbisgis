package org.orbisgis.editorViews.toc.actions.cui;

import java.awt.Component;

import org.orbisgis.renderer.symbol.Symbol;

public interface ISymbolEditor {

	/**
	 * Set the symbol editor listener. 
	 * 
	 * @param listener
	 */
	void setSymbolEditorListener(SymbolEditorListener listener);
	
	/**
	 * Set the symbol this editor have to edit
	 *
	 * @param symbol
	 */
	void setSymbol(Symbol symbol);

	/**
	 * Get the symbol currently in edition
	 *
	 * @return
	 */
	Symbol getSymbol();

	/**
	 * Return true if the editor can edit this symbol, false otherwise
	 *
	 * @param symbol
	 * @return
	 */
	boolean accepts(Symbol symbol);

	/**
	 * Return the swing component that will perform the edition
	 *
	 * @return
	 */
	Component getComponent();

	/**
	 * Creates a new empty instance of this editor
	 *
	 * @return
	 */
	ISymbolEditor newInstance();
}
