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
package org.orbisgis.mapeditor.map.mapsManager;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.MutableTreeNode;

import org.orbisgis.sif.components.fstree.TreeNodeBusy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.orbisgis.coremap.layerModel.MapContext;
import org.orbisgis.coremap.layerModel.mapcatalog.RemoteMapContext;
import org.orbisgis.coremap.layerModel.mapcatalog.Workspace;
import org.orbisgis.sif.components.fstree.AbstractTreeNodeContainer;
import org.orbisgis.sif.components.fstree.DropDestinationTreeNode;
import org.orbisgis.sif.components.fstree.PopupTreeNode;
import org.orbisgis.mapeditorapi.MapElement;
import org.orbisgis.mapeditor.map.TransferableMap;
import org.orbisgis.mapeditor.map.icons.MapEditorIcons;
import org.orbisgis.mapeditor.map.mapsManager.jobs.DownloadRemoteMapContext;
import org.orbisgis.mapeditor.map.mapsManager.jobs.UploadMapContext;
import org.orbisgis.sif.components.fstree.TreeNodeCustomIcon;
import org.orbisgis.sif.common.MenuCommonFunctions;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * A workspace on a remote server, hold a list of map context
 * @author Nicolas Fortin
 */
public class TreeNodeWorkspace extends AbstractTreeNodeContainer implements DropDestinationTreeNode, PopupTreeNode, TreeNodeCustomIcon {
        private static final I18n I18N = I18nFactory.getI18n(TreeNodeWorkspace.class);
        private static final Logger LOGGER = LoggerFactory.getLogger(TreeNodeWorkspace.class);
        AtomicBoolean downloaded = new AtomicBoolean(false);
        Workspace workspace;
        /**
         * Constructor
         * @param workspace Workspace structure
         */
        public TreeNodeWorkspace(Workspace workspace) {
                this.workspace = workspace;
                setLabel(workspace.getWorkspaceName());
                setEditable(false);
        }
        
        /**
         * Add this Remote map context
         * @param context 
         */
        public void addContext(RemoteMapContext context) {
                model.insertNodeInto(new TreeNodeRemoteMap(context), this, getChildCount());
        }
        
        /**
         * Refresh the content of the workspace
         */
        public void update() {
                List<MutableTreeNode> childrenToRemove = new ArrayList<MutableTreeNode>(children);
                for (MutableTreeNode child : childrenToRemove) {
                        model.removeNodeFromParent(child);
                }
                // Insert busy Node
                TreeNodeBusy busyNode = new TreeNodeBusy();
                model.insertNodeInto(busyNode, this, 0);
                // Launch the download job
                new DownloadRemoteMapContext(this,busyNode).execute();
        }
        

        @Override
        public int getChildCount() {
                if(!downloaded.getAndSet(true)) {
                        update();
                }
                return super.getChildCount();
        }
        
        /**
         * 
         * @return Workspace structure
         */
        public Workspace getWorkspace() {
                return workspace;
        }
        
        @Override
        public void setUserObject(Object o) {
                //Rename workspace ?
        }

        @Override
        public boolean canImport(TransferSupport ts) {
                return ts.isDataFlavorSupported(TransferableMap.mapFlavor);
        }

        @Override
        public boolean importData(TransferSupport ts) {
                // Uploading of a Map
                try {
                        // Retrieve the MapContext
                        Object mapObj = ts.getTransferable().getTransferData(TransferableMap.mapFlavor);
                        MapElement[] mapArray = (MapElement[])mapObj;
                        if(mapArray.length!=0) {
                                MapContext mapToUpload = mapArray[0].getMapContext();
                                new UploadMapContext(mapToUpload, this).execute();
                        }
                        return true;
                } catch (UnsupportedFlavorException ex) {
                        return false;
                } catch (IOException ex) {
                        LOGGER.error(ex.getLocalizedMessage(),ex);
                        return false;
                }
                
        }

        @Override
        public void feedPopupMenu(JPopupMenu menu) {
                if(downloaded.get()) {
                        JMenuItem updateMenu = new JMenuItem(I18N.tr("Update"),
                                MapEditorIcons.getIcon("refresh"));
                        updateMenu.setToolTipText(I18N.tr("Download the workspace content"));
                        updateMenu.setActionCommand("Update");
                        updateMenu.addActionListener(
                                EventHandler.create(ActionListener.class,this,"update"));
                        MenuCommonFunctions.updateOrInsertMenuItem(menu, updateMenu);
                }
        }
        
    @Override
    public ImageIcon getLeafIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageIcon getClosedIcon() {
        return MapEditorIcons.getIcon("folder");
    }
 
    @Override
    public ImageIcon getOpenIcon() {
        return MapEditorIcons.getIcon("folder_open");
    }
}
