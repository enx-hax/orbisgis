/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.sif.components.button;

import org.orbisgis.sif.icons.SifIcon;

import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JTextField;

public class JButtonTextField extends JTextField {

	private static final int COLUMNS = 8;
	private Icon icon;

	/**
	 * Create a jtextfield with an icon inside
	 * 
	 * @param icon
	 * @param columns
	 */
	public JButtonTextField(Icon icon, int columns) {
		super(columns);
		this.icon = icon;
	}

	/**
	 * Create a jtextfield with an icon inside
	 * 
	 * @param columns
	 */
	public JButtonTextField(int columns) {
		super(columns);
		this.icon = SifIcon.getIcon("small_search");
	}

	/**
	 * Create a jtextfield with an icon inside
	 */
	public JButtonTextField() {
		super(COLUMNS);
		this.icon = SifIcon.getIcon("small_search");
	}

        @Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		this.icon.paintIcon(null, g, 1, 1);
	}

        @Override
	public Insets getInsets() {
		Insets i = super.getInsets();
		i.left += icon.getIconWidth() + 10;
		return i;
	}

}
