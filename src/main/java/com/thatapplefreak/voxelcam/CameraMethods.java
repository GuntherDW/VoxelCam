package com.thatapplefreak.voxelcam;

import java.lang.reflect.Method;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.util.ModUtilities;

import net.minecraft.client.Minecraft;

/**
 * Wrapper for obf/mcp reflection-accessed private methods, added to centralise
 * the locations I have to update the obfuscated method names
 *
 * @author Adam Mummery-Smith
 * @param
 * 			<P>
 *            Parent class type
 * @param <R>
 *            Method return type
 */
public class CameraMethods<P, R> {

	public static class Void {}

	/**
	 * Class to which this field belongs
	 */
	public final Class<P> parentClass;

	/**
	 * Name used to access the field, determined at init
	 */
	private final String methodName;

	/**
	 * Method
	 */
	private final Method method;

	/**
	 * Creates a new private field entry
	 *
	 * @param owner
	 * @param mcpName
	 * @param name
	 */
	@SuppressWarnings("deprecation")
	private CameraMethods(Class<P> owner, Obf mapping, Class<?>... parameterTypes) {
		this.parentClass = owner;
		this.methodName = ModUtilities.getObfuscatedFieldName(mapping);

		Method method = null;

		try {
			method = this.parentClass.getDeclaredMethod(this.methodName, parameterTypes);
			method.setAccessible(true);
		} catch (SecurityException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		}

		this.method = method;
	}

	/**
	 * Invoke the method and return a value
	 *
	 * @param instance
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public R invoke(P instance, Object... args) {
		try {
			return (R) this.method.invoke(instance, args);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Invoke a method that returns void
	 *
	 * @param instance
	 * @param args
	 */
	public void invokeVoid(P instance, Object... args) {
		try {
			this.method.invoke(instance, args);
		} catch (Exception ex) {
		}
	}

	public static final CameraMethods<Minecraft, Void> resize = new CameraMethods<Minecraft, Void>(Minecraft.class, new Obf("func_71370_a", "a", "resize") {}, int.class, int.class);
}