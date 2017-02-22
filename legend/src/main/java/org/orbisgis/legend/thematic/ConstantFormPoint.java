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
package org.orbisgis.legend.thematic;

import org.orbisgis.coremap.renderer.se.PointSymbolizer;
import org.orbisgis.coremap.renderer.se.Symbolizer;
import org.orbisgis.coremap.renderer.se.common.Uom;
import org.orbisgis.legend.structure.graphic.ConstantFormWKN;
import org.orbisgis.legend.thematic.uom.SymbolUom;

/**
 * This class gathers methods that are common to thematic analysis where
 * the {@code Stroke}, {@code Fill} and well-known name are constant.
 * @author Alexis Guéganno
 */
public abstract class ConstantFormPoint extends SymbolizerLegend
    implements OnVertexOnInterior, SymbolUom {

    private PointSymbolizer pointSymbolizer;

    /**
     * Builds a new {@code ConstantFormPoint} that just have a default {@link
     * PointSymbolizer} in it.
     */
    public ConstantFormPoint(){
            pointSymbolizer = new PointSymbolizer();
    }

    /**
     * Basically set the associated {@link PointSymbolizer}.
     * @param symbolizer
     */
    public ConstantFormPoint(PointSymbolizer symbolizer){
        pointSymbolizer = symbolizer;
    }

    /**
     * Gets the associated {@code PointSymbolizer} instance.
     * @return
     */
    @Override
    public Symbolizer getSymbolizer() {
        return pointSymbolizer;
    }

    /**
     * Gets the {@code MarkGraphicLegend} that must be associated to the inner {@code
     * PointSymbolizer}.
     * @return
     * An instance of {@code MarkGraphicLegend}.
     */
    public abstract ConstantFormWKN getMarkGraphic();

    /**
     * Gets the well-known name that describes the shape of the inner {@link
     * MarkGraphic}.
     * @return
     */
    public String getWellKnownName(){
        return getMarkGraphic().getWellKnownName();
    }

    /**
     * Sets the well-known name that describes the shape of the inner {@link
     * MarkGraphic}.
     * @param str
     * The new {@code WellKnownName}.
     */
    public void setWellKnownName(String str){
        getMarkGraphic().setWellKnownName(str);
    }

    @Override
    public boolean isOnVertex(){
            return pointSymbolizer.isOnVertex();
    }

    @Override
    public void setOnVertex(){
            pointSymbolizer.setOnVertex(true);
    }

    @Override
    public void setOnInterior(){
            pointSymbolizer.setOnVertex(false);
    }

    @Override
    public Uom getStrokeUom(){
            return getMarkGraphic().getStrokeUom();
    }

    @Override
    public void setStrokeUom(Uom u){
            getMarkGraphic().setStrokeUom(u);
    }

    @Override
    public Uom getSymbolUom(){
            return getMarkGraphic().getSymbolUom();
    }

    @Override
    public void setSymbolUom(Uom u){
            getMarkGraphic().setSymbolUom(u);
    }
}
