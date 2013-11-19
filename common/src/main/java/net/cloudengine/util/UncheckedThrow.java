package net.cloudengine.util;

public final class UncheckedThrow {
	
	private UncheckedThrow() {
	}

	public static void throwUnchecked(final Exception ex) {
		// Now we use the 'generic' method. Normally the type T is inferred
		// from the parameters. However you can specify the type also explicit!
		// Now we du just that! We use the RuntimeException as type!
		// That means the throwsUnchecked throws an unchecked exception!
		// Since the types are erased, no type-information is there to prevent
		// this!
		if (ex != null) {
			UncheckedThrow.<RuntimeException> throwsUnchecked(ex);
		}
	}

	/**
	 * Remember, Generics are erased in Java. So this basically throws an
	 * Exception. The real Type of T is lost during the compilation
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Exception> void throwsUnchecked(Exception toThrow)
			throws T {
		// Since the type is erased, this cast actually does nothing!!!
		// we can throw any exception
		throw (T) toThrow;
	}
}