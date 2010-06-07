package org.orbisgis.core.renderer.se;

import com.vividsolutions.jts.geom.Geometry;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.xml.bind.JAXBElement;
import org.orbisgis.core.renderer.persistance.se.AreaSymbolizerType;
import org.orbisgis.core.renderer.persistance.se.LineSymbolizerType;
import org.orbisgis.core.renderer.persistance.se.PointSymbolizerType;
import org.orbisgis.core.renderer.persistance.se.RasterSymbolizerType;
import org.orbisgis.core.renderer.persistance.se.SymbolizerType;
import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.driver.DriverException;
import org.orbisgis.core.renderer.persistance.se.TextSymbolizerType;
import org.orbisgis.core.renderer.se.parameter.ParameterException;
import org.orbisgis.core.renderer.se.parameter.geometry.GeometryParameter;

/**
 * Entry point for all kind of symbolizer
 * This abstract class contains only the name, the geom and a description of the symbolizer
 * @todo Add a general draw method that fit well for vectors and raster; implement fetch default geometry
 * @author maxence
 */
public abstract class Symbolizer implements SymbolizerNode, Comparable{

    protected static final String DEFAULT_NAME = "Symbolizer Name";
    protected static final String VERSION = "1.9";

    public Symbolizer() {
        name = Symbolizer.DEFAULT_NAME;
        desc = "";
    }

    public Symbolizer(JAXBElement<? extends SymbolizerType> st) {
        SymbolizerType t = st.getValue();

        if (t.getName() != null) {
            this.name = t.getName();
        } else {
            this.name = Symbolizer.DEFAULT_NAME;
        }

        if (t.getVersion() != null && ! t.getVersion().equals(Symbolizer.VERSION)) {
            System.out.println("Unsupported Style version!");
        }

        if (t.getDescription() != null){
            // TODO 
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        desc = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public GeometryParameter getGeometry() {
        return the_geom;
    }

    public void setGeometry(GeometryParameter the_geom) {
        this.the_geom = the_geom;
    }

    public Geometry getTheGeom(SpatialDataSourceDecorator sds, long fid) throws DriverException, ParameterException {
        if (the_geom != null) {
            return the_geom.getTheGeom(sds, fid);
        } else {
            return sds.getGeometry(fid);
        }
    }

    @Override
    public SymbolizerNode getParent() {
        return parent;
    }

    @Override
    public void setParent(SymbolizerNode node) {
        this.parent = node;
    }

    public void setJAXBProperty(SymbolizerType s) {
        s.setDescription(null); // TODO !!
        s.setName(name);
        s.setVersion("1.9");
    }

    public void setPropertiesFromJAXB(SymbolizerType st) {
        if (st.getName() != null) {
            this.name = st.getName();
        }

        if (st.getDescription() != null) {
            // TODO IMplement
        }

        /*if (st.getVersion() != null){
         * // TODO IMplement
        }*/
    }

    public static Symbolizer createSymbolizerFromJAXBElement(JAXBElement<? extends SymbolizerType> st) {
        if (st.getDeclaredType() == org.orbisgis.core.renderer.persistance.se.AreaSymbolizerType.class) {
            return new AreaSymbolizer((JAXBElement<AreaSymbolizerType>) st);
        } else if (st.getDeclaredType() == org.orbisgis.core.renderer.persistance.se.LineSymbolizerType.class) {
            return new LineSymbolizer((JAXBElement<LineSymbolizerType>) st);
        } else if (st.getDeclaredType() == org.orbisgis.core.renderer.persistance.se.PointSymbolizerType.class) {
            return new PointSymbolizer((JAXBElement<PointSymbolizerType>) st);
        } else if (st.getDeclaredType() == org.orbisgis.core.renderer.persistance.se.TextSymbolizerType.class) {
            return new TextSymbolizer((JAXBElement<TextSymbolizerType>) st);
        } else if (st.getDeclaredType() == org.orbisgis.core.renderer.persistance.se.RasterSymbolizerType.class) {
            return new RasterSymbolizer((JAXBElement<RasterSymbolizerType>) st);
        } else {
            System.out.println("NULLLLLLL => " + st.getDeclaredType());
            return null;
        }
    }

     @Override
    public int compareTo(Object o) {
         Symbolizer s = (Symbolizer)o;

         if (s.level < this.level)
             return 1;
         else if (s.level == this.level)
             return 0;
         else
             return -1;
    }

    public Rule getRule(){
        SymbolizerNode pIt = this.parent;
        System.out.println ("First parent: " + pIt);
        while (! (pIt instanceof Rule)){
            pIt = pIt.getParent();
            System.out.println ("Parent: " + pIt);
        }

        return (Rule)pIt;
    }

    public abstract void draw(Graphics2D g2, SpatialDataSourceDecorator sds, long fid) throws ParameterException, IOException, DriverException;

    public abstract JAXBElement<? extends SymbolizerType> getJAXBElement();
    protected String name;
    protected String desc;
    protected GeometryParameter the_geom;

    private SymbolizerNode parent;

    protected int level = -1;

}