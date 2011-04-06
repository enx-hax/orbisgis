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
package org.gdms.driver.gdms;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gdms.data.DataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.metadata.Metadata;
import org.gdms.data.types.Type;
import org.gdms.data.values.Value;
import org.gdms.driver.DriverException;
import org.gdms.driver.FileReadWriteDriver;
import org.gdms.driver.GDMSModelDriver;
import org.gdms.source.SourceManager;
import org.orbisgis.progress.IProgressMonitor;
import org.orbisgis.utils.FileUtils;

public class GdmsDriver extends GDMSModelDriver implements FileReadWriteDriver {

        static final byte VERSION_NUMBER = 3;
        private GdmsReader reader;

        public void copy(File in, File out) throws IOException {
                FileUtils.copy(in, out);
        }

        public void createSource(String path, Metadata metadata,
                DataSourceFactory dataSourceFactory) throws DriverException {
                try {
                        GdmsWriter writer = new GdmsWriter(new File(path));
                        writer.writeMetadata(0, metadata);
                        writer.close();
                } catch (IOException e) {
                        throw new DriverException("Could not create source: ", e);
                }
        }

        public void writeFile(File file, DataSource dataSource, IProgressMonitor pm)
                throws DriverException {
                try {
                        GdmsWriter writer = new GdmsWriter(file);
                        writer.write(dataSource, pm);
                        writer.close();
                } catch (IOException e) {
                        throw new DriverException(e.getMessage(), e);
                }
        }

        public void close() throws DriverException {
                try {
                        reader.close();
                        reader = null;
                } catch (IOException e) {
                        throw new DriverException(e);
                }
        }

        public void open(File file) throws DriverException {
                try {
                        reader = new GdmsReader(file);
                        reader.readMetadata();
                } catch (IOException e) {
                        throw new DriverException(e);
                }
        }

        public boolean isOpen() {
                return reader != null;
        }

        public Metadata getMetadata() throws DriverException {
                if (reader == null) {
                        return null;
                }
                return reader.getMetadata();
        }

        public int getType() {
                // by default this is a file driver
                int type = SourceManager.FILE;

                // when it is actually bound to a file, we check for additional data
                // (getMetadata() != null) excludes the case of a corrupted gdms file
                if (reader != null && reader.getMetadata() != null) {
                        try {
                                Metadata m = reader.getMetadata();
                                for (int i = 0; i < m.getFieldCount(); i++) {
                                        switch (m.getFieldType(i).getTypeCode()) {
                                                case Type.GEOMETRY:
                                                        type |= SourceManager.VECTORIAL;
                                                        break;
                                                case Type.RASTER:
                                                        type |= SourceManager.RASTER;
                                                        break;
                                        }
                                }
                        } catch (DriverException ex) {
                        }
                        return type;
                } else {
                        // if we cannot tell, we add VECTORIAL
                        // WARNING: this is needed so that GDMS can be considered
                        // a writable vectorial driver
                        //
                        // 01/11/2011
                        // TODO: getType should be split between a method that
                        // returns the type of the current file loaded by the driver
                        // and a static method that returns the types this driver
                        // can work with
                        return type | SourceManager.VECTORIAL;
                }
                
        }

        public void setDataSourceFactory(DataSourceFactory dsf) {
        }

        public String getDriverId() {
                return "GDMS driver";
        }

        public Value getFieldValue(long rowIndex, int fieldId)
                throws DriverException {
                return reader.getFieldValue(rowIndex, fieldId);
        }

        public long getRowCount() throws DriverException {
                return reader.getRowCount();
        }

        public Number[] getScope(int dimension) throws DriverException {
                return reader.getScope(dimension);
        }

        public boolean isCommitable() {
                return true;
        }

        public String validateMetadata(Metadata metadata) {
                return null;
        }

        @Override
        public String[] getFileExtensions() {
                return new String[]{"gdms"};
        }

        @Override
        public String getTypeDescription() {
                return "GDMS native file";
        }

        @Override
        public String getTypeName() {
                return "GDMS";
        }
}
