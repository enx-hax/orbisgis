package org.orbisgis.geoview.renderer.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.gdms.driver.DriverException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class LabelSymbol implements Symbol {

	private int fontSize;
	private String text;

	public LabelSymbol(String text, int fontSize) {
		this.text = text;
		this.fontSize = fontSize;
	}

	public void draw(Graphics2D g, Geometry geom, AffineTransform at)
			throws DriverException {
		Font font = g.getFont();
		g.setFont(font.deriveFont(fontSize));
		FontMetrics metrics = g.getFontMetrics(font);
		// get the height of a line of text in this font and render context
		int hgt = metrics.getHeight();
		// get the advance of my text in this font and render context
		int adv = metrics.stringWidth(text);
		// calculate the size of a box to hold the text with some padding.
		Dimension size = new Dimension(adv + 2, hgt + 2);
		Point interiorPoint = geom.getInteriorPoint();
		Point2D p = new Point2D.Double(interiorPoint.getX(), interiorPoint
				.getY());
		p = at.transform(p, null);
		double x = p.getX() - size.getWidth() / 2;
		double y = p.getY() - size.getHeight() / 2;
		g.setColor(Color.black);
		g.drawString(text, (int) x, (int) y);
	}

	public boolean willDraw(Geometry geom) {
		return true;
	}

}
