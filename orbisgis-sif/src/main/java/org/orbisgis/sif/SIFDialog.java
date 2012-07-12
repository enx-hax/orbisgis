/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 * 
 *
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
 *
 * or contact directly:
 * info _at_ orbisgis.org
 */
package org.orbisgis.sif;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;


public class SIFDialog extends AbstractOutsideFrame {

	private JButton btnOk;

	private JButton btnCancel;

	private SimplePanel simplePanel;

	

	public SIFDialog(Window owner, boolean okCancel) {
		super(owner);
		init(okCancel);
	}

	private void init(boolean okCancel) {
		this.setLayout(new BorderLayout());

		btnOk = new JButton(i18n.tr("Ok"));
		btnOk.setBorderPainted(false);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exit(true);
			}

		});
		getRootPane().setDefaultButton(btnOk);
		btnCancel = new JButton(i18n.tr("Cancel"));
		btnCancel.setBorderPainted(false);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exit(false);
			}

		});
		JPanel pnlButtons = new JPanel();
		pnlButtons.add(btnOk);
		if (okCancel) {
			pnlButtons.add(btnCancel);
		}
		this.add(pnlButtons, BorderLayout.SOUTH);
		

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void setComponent(SimplePanel simplePanel) {
		this.simplePanel = simplePanel;
		this.add(simplePanel, BorderLayout.CENTER);
		this.setIconImage(getPanel().getIconImage());
	}

	

	@Override
	protected SimplePanel getPanel() {
		return simplePanel;
	}

       
}
