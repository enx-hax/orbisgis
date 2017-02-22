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
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
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
package org.orbisgis.view.toc.actions.cui.legend.components;


import org.orbisgis.legend.thematic.proportional.ProportionalPoint;
import org.orbisgis.view.toc.actions.cui.components.CanvasSE;

import javax.sql.DataSource;

/**
 * Field combo box for Proportional Points.
 *
 * @author Adam Gouge
 */
public final class PPointFieldsComboBox extends ProportionalFieldsComboBox {

    /**
     * Constructor
     *
     * @param ds      DataSource
     * @param legend  Legend
     * @param preview Preview
     */
    public PPointFieldsComboBox(DataSource ds,String table,
                                ProportionalPoint legend,
                                CanvasSE preview) {
        super(ds,table, legend, preview);
        init();
    }

    @Override
    protected void setFirstAndSecondValues(double[] minAndMax) {
        // We use sqrt because of the definition of a proportional symbol.
        // We want the area to grow proportionally to the value, not the
        // width/height. If we used the raw values, the area would be
        // proportional to the square of the input values.
        getLegend().setFirstData(Math.sqrt(minAndMax[0]));
        getLegend().setSecondData(Math.sqrt(minAndMax[1]));
    }
}
