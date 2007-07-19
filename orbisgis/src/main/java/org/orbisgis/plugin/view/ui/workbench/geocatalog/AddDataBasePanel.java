package org.orbisgis.plugin.view.ui.workbench.geocatalog;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.gdms.driver.DriverException;
import org.gdms.driver.TableDescription;
import org.gdms.driver.h2.H2spatialDriver;
import org.orbisgis.plugin.TempPluginServices;

public class AddDataBasePanel extends JPanel {

	// Names for databases drivers. Please put the driver name used by "call
	// register..."
	private final static String h2 = "h2";

	private final static String postgrey = "PostGrey";

	private final static String hsql = "HSQL";

	private final static String mysql = "MySQL";

	private final static String odbc = "ODBC";
	
	public final static String savePath = "save/";
	
	public final static String saveFile = savePath + "connexion";

	private String[] type = { h2, postgrey, hsql, mysql, odbc };

	private static final long serialVersionUID = 1L;

	private JPanel upperPan = null;

	private JPanel lowerPan = null;

	private DefaultMutableTreeNode root = new DefaultMutableTreeNode();

	private DefaultMutableTreeNode table = new DefaultMutableTreeNode("TABLES");

	private DefaultMutableTreeNode view = new DefaultMutableTreeNode("VIEWS");

	private JScrollPane scrollPane = null;

	private JTree tree = null;

	private JComboBox typeDB = null;

	private JTextField connectName = null;

	private JTextField host = null;

	private JTextField port = null;

	private JTextField DBName = null;

	private JTextField userName = null;

	private JPasswordField password = null;

	private AddDataBaseListener acl = null;

	private HashMap<String, DataBaseParameters> dbParameters = null;

	public AddDataBasePanel() {
		JLabel label = null;
		upperPan = new JPanel();
		lowerPan = new JPanel();

		setLayout(new CRFlowLayout());
		acl = new AddDataBaseListener();

		/** ******************UPPER PANE******************* */
		upperPan.setLayout(new GridLayout(8, 2, 10, 10));// Lines, Columns,
		// HorizontalSpace,
		// VerticalSpace

		label = new JLabel("Connection name:");
		upperPan.add(label);

		connectName = new JTextField();
		connectName.addKeyListener(new MyKeyListener());
		upperPan.add(connectName);

		label = new JLabel("Database type:");
		upperPan.add(label);

		typeDB = new JComboBox(type);
		typeDB.setActionCommand("CHANGEDB");
		typeDB.addActionListener(acl);
		upperPan.add(typeDB);

		label = new JLabel("Host:");
		upperPan.add(label);

		host = new JTextField();
		host.setEnabled(false);
		upperPan.add(host);

		label = new JLabel("Port:");
		upperPan.add(label);

		port = new JTextField();
		port.setEnabled(false);
		upperPan.add(port);

		label = new JLabel("Database name:");
		upperPan.add(label);

		DBName = new JTextField();
		DBName.setText("c://tmp//database//demo");
		upperPan.add(DBName);

		label = new JLabel("User name:");
		upperPan.add(label);

		userName = new JTextField();
		upperPan.add(userName);

		label = new JLabel("Password:");
		upperPan.add(label);

		password = new JPasswordField();
		upperPan.add(password);

		/** ******************LOWER PANE******************* */
		lowerPan.setLayout(new CRFlowLayout());

		label = new JLabel("Choose a table...");
		lowerPan.add(label);

		lowerPan.add(new CarriageReturn());

		JButton list = new JButton("Connect");
		list.setActionCommand("LISTTABLES");
		list.addActionListener(acl);
		lowerPan.add(list);

		tree = new JTree(root);
		tree.setRootVisible(false);
		scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(220, 200));
		addNode("Please connect to a database first", root);
		tree.setEnabled(false);
		lowerPan.add(scrollPane);

		add(upperPan);
		add(new CarriageReturn());
		add(lowerPan);

		//TODO Improve the code...
		
		File file = new File(saveFile);
		File path = new File(savePath);
		
		if (!path.exists()) {
			System.err.println("Creating " + path.getPath() + " directory");
			path.mkdir();
		}
		
		try {
			ObjectInputStream in = new ObjectInputStream(
				      new FileInputStream(file));
			try {
				dbParameters = (HashMap<String, DataBaseParameters>)in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();
		} catch (FileNotFoundException e) {
			dbParameters = new HashMap<String, DataBaseParameters>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return driver host port DBName userName password
	 */
	public String[] getParameters() {
		String tableName = (String) tree.getLastSelectedPathComponent()
				.toString();
		String name = tableName;
		String portString = port.getText();

		int i = 0;
		String tmpName = name;
		while (TempPluginServices.dsf.existDS(tmpName)) {
			i++;
			tmpName = name + "_" + i;
		}
		name = tmpName;

		// If port is null assume port = 0
		if (portString.length() == 0) {
			portString = "0";
		}

		// DBType, host, port, path/name, user, password, tableName, alias
		String parameters[] = { (String) typeDB.getSelectedItem(),
				host.getText(), portString, DBName.getText(),
				userName.getText(), getPassword(), tableName, name };
		return parameters;
	}

	public String getPassword() {
		String passwd = "";
		char[] pass = password.getPassword();
		int length = pass.length;
		for (int i = 0; i < length; i++) {
			passwd = passwd + pass[i];
		}
		return passwd;
	}

	public void addNode(String name, DefaultMutableTreeNode father) {
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
		father.add(child);

		// expand the path and refresh
		tree.scrollPathToVisible(new TreePath(child.getPath()));
		tree.updateUI();
	}

	private void saveParameters() {
		String connectionName = connectName.getText();
		if (!dbParameters.containsKey(connectionName)) {
			DataBaseParameters parameters = new DataBaseParameters(
					(String) typeDB.getSelectedItem(), host.getText(), port
							.getText(), DBName.getText(), userName.getText());
			dbParameters.put(connectionName, parameters);

			File file = new File(saveFile);
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(file));
				out.writeObject(dbParameters);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadParameters(String connectionName) {
		DataBaseParameters parameters = null;
		if (dbParameters.containsKey(connectionName)) {
			parameters = (DataBaseParameters) dbParameters.get(connectionName);
			typeDB.setSelectedItem(parameters.getDbType());
			host.setText(parameters.getHost());
			port.setText(parameters.getPort());
			DBName.setText(parameters.getDbName());
			userName.setText(parameters.getUserName());
		}
	}

	private class AddDataBaseListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if ("LISTTABLES".equals(e.getActionCommand())) {

				saveParameters();

				H2spatialDriver driver = new H2spatialDriver();

				// Clears the tree
				table.removeAllChildren();
				view.removeAllChildren();
				root.removeAllChildren();

				try {
					Connection c = driver.getConnection(null, 0, DBName
							.getText(), userName.getText(), getPassword());
					TableDescription[] tables = driver.getTables(c);
					c.close();

					for (int i = 0; i < tables.length; i++) {
						DefaultMutableTreeNode node = table;
						if (tables[i].getType().equalsIgnoreCase("VIEW")) {
							node = view;
						}
						addNode(tables[i].getName(), node);
					}

					// Displays only what necessary
					if (!table.isLeaf()) {
						root.add(table);
					}

					if (!view.isLeaf()) {
						root.add(view);
					}
					tree.setEnabled(true);
					tree.updateUI();

				} catch (SQLException e1) {
					addNode(e1.getMessage(), root);
				} catch (DriverException e1) {
					e1.printStackTrace();
				}
			} else if ("CHANGEDB".equals(e.getActionCommand())) {
				System.err.println("NOT IMPLEMENTED");
			}
		}
	}

	private class MyKeyListener implements KeyListener {

		/*
		 * Once key is typed, connectName contains the text *BEFORE* the key was
		 * typed, so maybe we will need to add the last character
		 * (e.getKeyChar())
		 */
		private boolean addCompletion = true;

		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_BACK_SPACE || code == KeyEvent.VK_DELETE
					|| code == KeyEvent.VK_ENTER) {
				addCompletion = false;
			} else
				addCompletion = true;
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
			String param = connectName.getText();

			if (addCompletion) {
				param = param + e.getKeyChar();
			}
			loadParameters(param);
		}

	}

}
