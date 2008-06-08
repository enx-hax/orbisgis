package org.orbisgis.editorViews.toc.actions.cui.gui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.gdms.data.types.GeometryConstraint;
import org.gdms.data.values.Value;
import org.gdms.driver.DriverException;
import org.orbisgis.renderer.RenderPermission;
import org.orbisgis.renderer.legend.CircleSymbol;
import org.orbisgis.renderer.legend.Interval;
import org.orbisgis.renderer.legend.IntervalLegend;
import org.orbisgis.renderer.legend.LabelLegend;
import org.orbisgis.renderer.legend.Legend;
import org.orbisgis.renderer.legend.LineSymbol;
import org.orbisgis.renderer.legend.NullSymbol;
import org.orbisgis.renderer.legend.PolygonSymbol;
import org.orbisgis.renderer.legend.ProportionalLegend;
import org.orbisgis.renderer.legend.Symbol;
import org.orbisgis.renderer.legend.SymbolComposite;
import org.orbisgis.renderer.legend.SymbolFactory;
import org.orbisgis.renderer.legend.UniqueSymbolLegend;
import org.orbisgis.renderer.legend.UniqueValueLegend;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class ImageLegend {
	private BufferedImage im;

	public ImageLegend(Legend[] leg) {
		createImage(leg);
	}

	private void createImage(Legend[] leg) {

		int width = 0;
		int height = 0;

		for (int i = 0; i < leg.length; i++) {
			Dimension dim = getDimension(leg[i]);
			if (dim.width>width){
				width=dim.width;
			}
			height = height + dim.height;
		}
		Dimension dimFinal = new Dimension(width, height);

		im = new BufferedImage(dimFinal.width, dimFinal.height,
				BufferedImage.TYPE_INT_ARGB);
		
		paintImage(leg);

	}

	private void paintImage(Legend[] leg) {
		int end=0;
		for (int i=0; i<leg.length; i++){
			end = paintImage(leg[i], end);
		}
	}
	
	private int paintImage(Legend leg, int end){
		Graphics g = im.getGraphics();
		Graphics2D g2 = null;
		if (g instanceof Graphics2D) {
			g2 = (Graphics2D) g;
		}else{
			return -1;
		}
		
		if (leg instanceof UniqueSymbolLegend) {
			UniqueSymbolLegend usl = (UniqueSymbolLegend)leg;
			
			paintSymbol(usl.getSymbol(), end, g);
			setText(usl.getSymbol().getName(), end, g);
			
			end += 30;
		}

		if (leg instanceof UniqueValueLegend) {
			UniqueValueLegend uvl = (UniqueValueLegend) leg;
			int numberOfClas = uvl.getClassificationValues().length;
			Value[] vals = uvl.getClassificationValues();
			for (int i=0; i<numberOfClas; i++){
				paintSymbol(uvl.getValueSymbol(vals[i]), end, g);
				setText(uvl.getValueSymbol(vals[i]).getName(), end, g);
				end+=30;
			}
			if (!(uvl.getDefaultSymbol() instanceof NullSymbol)){
				paintSymbol(uvl.getDefaultSymbol(), end, g);
				setText("Default", end, g);
				end+=30;
			}
			
		}

		if (leg instanceof IntervalLegend) {
			IntervalLegend il = (IntervalLegend) leg;
			int numberOfInterv = il.getIntervals().size();
			ArrayList<Interval> inters = il.getIntervals();
			for (int i=0; i<numberOfInterv; i++){
				paintSymbol(il.getSymbolInterval(inters.get(i)), end, g);
				setText(il.getSymbolInterval(inters.get(i)).getName(), end, g);
				end+=30;
			}
			if (!(il.getDefaultSymbol() instanceof NullSymbol)){
				paintSymbol(il.getDefaultSymbol(), end, g);
				setText("Default", end, g);
				end+=30;
			}
		}

		if (leg instanceof ProportionalLegend) {
			ProportionalLegend pl = (ProportionalLegend) leg;
			paintProportionalLegend(pl.getFillColor(), pl.getOutlineColor(), end, g);
			setText("Proportional", end, g);
			end += 30;
		}

		if (leg instanceof LabelLegend) {
			g2.setColor(Color.BLUE);
			g2.drawLine(5, end+1, 45, end+1);
			end += 30;
		}
		
		
		return end;
	}

	
	private void paintProportionalLegend(Color fillColor, Color outline, int end, Graphics g) {
		Symbol s1 = SymbolFactory.createCirclePointSymbol(outline, fillColor, 28);
		Symbol s2 = SymbolFactory.createCirclePointSymbol(outline, fillColor, 10);
		GeometryFactory gf = new GeometryFactory();
		Geometry geom = null;
		Geometry geom2 = null;
		
		
		
		try {
			geom = gf.createPoint(new Coordinate(15, end+15));
			
			s1.draw((Graphics2D) g, geom, new AffineTransform(),
					new RenderPermission() {

						public boolean canDraw(Envelope env) {
							return true;
						}

					});
			
			geom2 = gf.createPoint(new Coordinate(15, end+24));
			
			s2.draw((Graphics2D) g, geom2, new AffineTransform(),
					new RenderPermission() {

						public boolean canDraw(Envelope env) {
							return true;
						}

					});
		} catch (DriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void setText(String name, int end, Graphics g) {
		((Graphics2D) g).setColor(Color.black);
		((Graphics2D) g).drawString(name, 55, end+17);
	}

	public int getConstraint(Symbol sym) {
		if (sym instanceof LineSymbol) {
			return GeometryConstraint.LINESTRING;
		}
		if (sym instanceof CircleSymbol) {
			return GeometryConstraint.POINT;
		}
		if (sym instanceof PolygonSymbol) {
			return GeometryConstraint.POLYGON;
		}
		if (sym instanceof SymbolComposite) {
			return GeometryConstraint.MIXED;
		}
		return GeometryConstraint.MIXED;
	}
	
	private void paintSymbol(Symbol s, int end, Graphics g) {
		int constr = getConstraint(s);
		
		try {
			GeometryFactory gf = new GeometryFactory();
			Geometry geom = null;
			
			switch (constr) {
			case GeometryConstraint.LINESTRING:
			case GeometryConstraint.MULTI_LINESTRING:
				geom = gf.createLineString(new Coordinate[] {
						new Coordinate(5, end+15), new Coordinate(45, end+15) });
	
				s.draw((Graphics2D) g, geom, new AffineTransform(),
						new RenderPermission() {
	
							public boolean canDraw(Envelope env) {
								return true;
							}
	
						});
	
				break;
			case GeometryConstraint.POINT:
			case GeometryConstraint.MULTI_POINT:
				geom = gf.createPoint(new Coordinate(25, end+15));
	
				s.draw((Graphics2D) g, geom, new AffineTransform(),
						new RenderPermission() {
	
							public boolean canDraw(Envelope env) {
								return true;
							}
	
						});
	
				break;
			case GeometryConstraint.POLYGON:
			case GeometryConstraint.MULTI_POLYGON:
				Coordinate[] coords = { new Coordinate(5, end+2),
						new Coordinate(45, end+2), new Coordinate(45, end+28),
						new Coordinate(5, end+28), new Coordinate(5, end+2) };
				CoordinateArraySequence seq = new CoordinateArraySequence(
						coords);
				geom = gf.createPolygon(new LinearRing(seq, gf), null);
	
				s.draw((Graphics2D) g, geom, new AffineTransform(),
						new RenderPermission() {
	
							public boolean canDraw(Envelope env) {
								return true;
							}
	
						});
	
				break;
			case GeometryConstraint.MIXED:
				SymbolComposite comp = (SymbolComposite) s;
				Symbol sym;
				int numberOfSymbols = comp.getSymbolCount();
				for (int i = 0; i < numberOfSymbols; i++) {
					sym = comp.getSymbol(i);
					if (sym instanceof LineSymbol) {
						geom = gf
								.createLineString(new Coordinate[] {
										new Coordinate(5, end+15), new Coordinate(45, end+15)});
	
						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {
	
									public boolean canDraw(Envelope env) {
										return true;
									}
	
								});
					}
	
					if (sym instanceof CircleSymbol) {
						geom = gf.createPoint(new Coordinate(25, end+15));
	
						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {
	
									public boolean canDraw(Envelope env) {
										return true;
									}
	
								});
					}
	
					if (sym instanceof PolygonSymbol) {
						Coordinate[] coordsP = { new Coordinate(5, end+2),
								new Coordinate(45, end+2), new Coordinate(45, end+28),
								new Coordinate(5, end+28), new Coordinate(5, end+2) };
						CoordinateArraySequence seqP = new CoordinateArraySequence(
								coordsP);
						geom = gf.createPolygon(new LinearRing(seqP, gf), null);
	
						sym.draw((Graphics2D) g, geom, new AffineTransform(),
								new RenderPermission() {
	
									public boolean canDraw(Envelope env) {
										return true;
									}
	
								});
					}
	
				}
				break;
	
			}
		} catch (DriverException e) {
			((Graphics2D) g).drawString("Cannot generate preview", 0, 0);
		} catch (NullPointerException e) {
			((Graphics2D) g).drawString("Cannot generate preview: ", 0, 0);
			System.out.println(e.getMessage());
		}

		
	}

	private Dimension getDimension(Legend leg) {
		int height = 0;
		int width = 200;

		if (leg instanceof UniqueSymbolLegend) {
			height = 30;
		}

		if (leg instanceof UniqueValueLegend) {
			UniqueValueLegend uvl = (UniqueValueLegend) leg;
			int numberOfClas = uvl.getClassificationValues().length;
			height = 30 * numberOfClas;
			if (!(uvl.getDefaultSymbol() instanceof NullSymbol)){
				height+=30;
			}
		}

		if (leg instanceof IntervalLegend) {
			IntervalLegend il = (IntervalLegend) leg;
			int numberOfInterv = il.getIntervals().size();
			height = 30 * numberOfInterv;
			if (!(il.getDefaultSymbol() instanceof NullSymbol)){
				height+=30;
			}
		}

		if (leg instanceof ProportionalLegend) {
			height = 30;
		}

		if (leg instanceof LabelLegend) {
			height = 30;
		}

		return new Dimension(width, height);
	}

	public BufferedImage getIm() {
		return im;
	}

	public void setLeg(Legend[] leg) {
		createImage(leg);
	}

}
