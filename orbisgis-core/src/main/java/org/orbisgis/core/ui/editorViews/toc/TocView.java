/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able
 * to manipulate and create vector and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrbisGIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.orbisgis.core.ui.editorViews.toc;

import java.awt.Component;


import org.orbisgis.core.Services;
import org.orbisgis.core.edition.EditableElement;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.ui.editor.EditorListener;
import org.orbisgis.core.ui.editor.IEditor;
import org.orbisgis.core.ui.editorView.IEditorView;
import org.orbisgis.core.ui.editors.map.MapEditor;
import org.orbisgis.core.ui.editors.table.TableEditableElement;
import org.orbisgis.core.ui.editors.table.TableEditor;
import org.orbisgis.core.ui.views.editor.EditorManager;

public class TocView implements IEditorView {

	private Toc toc = new Toc();
	private EditorListener closingListener = null;

	public Component getComponent() {
		return toc;
	}

	public void loadStatus() {

	}

	public void saveStatus() {

	}

	public void delete() {
		toc.delete();
	}

	public void initialize() {

	}

	public boolean setEditor(IEditor editor) {
		listenEditorClosing();
		EditableElement element = editor.getElement();
		if (editor instanceof MapEditor) {
			toc.setMapContext(element,(MapEditor) editor );
			return true;
		} else if (editor instanceof TableEditor) {
			TableEditableElement tableElement = (TableEditableElement) element;
			MapContext mapContext = tableElement.getMapContext();
			// Get MapContext element
			EditorManager em = Services.getService(EditorManager.class);
			IEditor[] mapEditors = em.getEditors(
					"org.orbisgis.core.ui.editors.Map", mapContext);
			if (mapEditors.length > 0) {
				toc.setMapContext(mapEditors[0].getElement());
				return true;
			} else {
				editorViewDisabled();
				return false;
			}
		}

		return false;
	}

	private void listenEditorClosing() {
		if (closingListener == null) {
			closingListener = new EditorListener() {

				@Override
				public boolean activeEditorClosing(IEditor editor,
						String editorId) {
					if (editorId.equals("org.orbisgis.core.ui.editors.Map")) {
						MapContext mc = (MapContext) editor.getElement()
								.getObject();
						EditorManager em = Services
								.getService(EditorManager.class);
						IEditor[] editors = em
								.getEditors("org.orbisgis.core.ui.editors.Table");
						for (IEditor openEditor : editors) {
							AbstractTableEditableElement el = (AbstractTableEditableElement) openEditor
									.getElement();
							if (el.getMapContext() == mc) {
								if (!em.closeEditor(openEditor)) {
									return false;
								}
							}
						}
					}

					return true;
				}

				@Override
				public void activeEditorClosed(IEditor editor, String editorId) {
				}

				@Override
				public void activeEditorChanged(IEditor previous,
						IEditor current) {
				}
			};
			EditorManager em = Services.getService(EditorManager.class);
			em.addEditorListener(closingListener);
		}
	}

	public void editorViewDisabled() {
		toc.setMapContext(null);
		toc.setMapContext(null, null);
	}
}