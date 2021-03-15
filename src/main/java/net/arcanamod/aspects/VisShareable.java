package net.arcanamod.aspects;

/**
 * VisShareable marks a block as being an aspect handling block. Blocks that
 * implement this interface will be checked by the Research Table for vis.
 */
public interface VisShareable{
	
	boolean isVisShareable();

	boolean isManual();

	boolean isSecure();
}