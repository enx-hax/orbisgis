package org.gdms.data.indexes.btree;

import java.io.IOException;

import org.gdms.data.values.Value;

public interface BTreeNode {
	public boolean isLeaf();

	/**
	 * Inserts the element. if there is not enough space reorganizes the tree
	 *
	 * @param v
	 * @param rowIndex
	 *            index where the value is at
	 * @return The smaller value not in the left neighbour if it has changed
	 * @throws IOException
	 */
	public Value insert(Value v, int rowIndex) throws IOException;

	/**
	 * Deletes the element. The tree can be reorganized to match btree
	 * restrictions
	 *
	 * @param v
	 * @param row
	 * @return The new root if the reorganization of the tree changed the root
	 * @throws IOException
	 */
	public boolean delete(Value v, int row) throws IOException;

	/**
	 * Sets the parent of a node
	 *
	 * @param m
	 */
	public void setParentDir(int parentDir);

	/**
	 * Gets the smaller value in the subtree represented by this node and its
	 * children that is not present in the subtree represented by the 'treeNode'
	 * node and its children
	 *
	 * @param treeNode
	 * @return
	 * @throws IOException
	 */
	public Value getSmallestValueNotIn(BTreeNode treeNode) throws IOException;

	/**
	 * Get the leaf that contains the smallest values
	 *
	 * @return
	 * @throws IOException
	 */
	public BTreeLeaf getFirstLeaf() throws IOException;

	/**
	 * Checks that the tree is well formed. Throws any exception if it's not.
	 * Just for debugging purposes
	 *
	 * @throws IOException
	 */
	public void checkTree() throws IOException;

	/**
	 * Gets the parent of the node. The root node returns null
	 *
	 * @return
	 * @throws IOException
	 */
	public BTreeInteriorNode getParent() throws IOException;

	/**
	 * Gets the representation of this node as a byte array
	 *
	 * @return
	 * @throws IOException
	 */
	public byte[] getBytes() throws IOException;

	/**
	 * Saves permanently this node at disk
	 *
	 * @throws IOException
	 */
	public void save() throws IOException;

	/**
	 * Get the position in the file of this node
	 *
	 * @return
	 */
	public int getDir();

	/**
	 * Return true if the node is valid
	 *
	 * @return
	 * @throws IOException
	 */
	public boolean isValid() throws IOException;

	/**
	 * Splits the node
	 *
	 * @return
	 * @throws IOException
	 */
	public BTreeNode splitNode() throws IOException;

	public BTreeNode getNewRoot() throws IOException;

	public void mergeWithLeft(BTreeNode leftNeighbour) throws IOException;

	public void mergeWithRight(BTreeNode rightNeighbour) throws IOException;

	public boolean canGiveElement() throws IOException;

	public void moveFirstTo(BTreeNode node) throws IOException;

	public void moveLastTo(BTreeNode node) throws IOException;

	public int[] getIndex(RangeComparator minComparator,
			RangeComparator maxComparator) throws IOException;

	public Value[] getAllValues() throws IOException;

	public Value getSmallestValue() throws IOException;

	public boolean contains(Value value) throws IOException;
}
