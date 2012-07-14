package net.cloudengine.util;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.util.StringHelper;

/**
 * <p>
 * Agrega un prefijo a los nombres de tablas y convierte el camelcase de los
 * nombres de atributos en underscores.
 * </p>
 */
public class CustomNamingStrategy implements NamingStrategy {

	private static String PREFIX = "tp_";
	public static final NamingStrategy INSTANCE = new CustomNamingStrategy();

	public String classToTableName(String className) {
		return getPrefix() + addUnderscores(StringHelper.unqualify(className));
	}

	public String propertyToColumnName(String propertyName) {
		return addUnderscores(StringHelper.unqualify(propertyName));
	}

	public String tableName(String tableName) {
		String res = addUnderscores(tableName);
		if (!tableName.startsWith(getPrefix()))
			res = getPrefix() + tableName;
		return res;
	}

	public String columnName(String columnName) {
		return addUnderscores(columnName);
	}

	protected static String addUnderscores(String name) {
		StringBuffer buf = new StringBuffer(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++) {
			if ('_' != buf.charAt(i - 1)
					&& (Character.isUpperCase(buf.charAt(i)) && !Character
							.isUpperCase(buf.charAt(i + 1)))
					|| (Character.isUpperCase(buf.charAt(i)) && !Character
							.isUpperCase(buf.charAt(i - 1)))) {
				buf.insert(i++, '_');
			}
		}
		return buf.toString().toLowerCase();
	}

	public String collectionTableName(String ownerEntity,
			String ownerEntityTable, String associatedEntity,
			String associatedEntityTable, String propertyName) {
		return tableName(ownerEntityTable + '_'
				+ propertyToColumnName(propertyName));
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return columnName(joinedColumn);
	}

	public String foreignKeyColumnName(String propertyName,
			String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		String header = propertyName != null ? StringHelper
				.unqualify(propertyName) : propertyTableName;
		if (header == null)
			throw new AssertionFailure("NamingStrategy not properly filled");
		return columnName(header); // + "_" + referencedColumnName not used for
									// backward compatibility
	}

	/**
	 * Return the column name or the unqualified property name Modificado
	 */
	public String logicalColumnName(String columnName, String propertyName) {
		return addUnderscores(StringHelper.isNotEmpty(columnName) ? columnName
				: StringHelper.unqualify(propertyName));
	}

	/**
	 * Returns either the table name if explicit or if there is an associated
	 * table, the concatenation of owner entity table and associated table
	 * otherwise the concatenation of owner entity table and the unqualified
	 * property name
	 */
	public String logicalCollectionTableName(String tableName,
			String ownerEntityTable, String associatedEntityTable,
			String propertyName) {
		if (tableName != null) {
			return tableName;
		}
		// use of a stringbuffer to workaround a JDK bug
		return new StringBuffer(ownerEntityTable)
				.append("_")
				.append(associatedEntityTable != null ? associatedEntityTable
						: StringHelper.unqualify(propertyName)).toString();
	}

	/**
	 * Return the column name if explicit or the concatenation of the property
	 * name and the referenced column
	 */
	public String logicalCollectionColumnName(String columnName,
			String propertyName, String referencedColumn) {
		return StringHelper.isNotEmpty(columnName) ? columnName : StringHelper
				.unqualify(propertyName) + "_" + referencedColumn;
	}

	protected String getPrefix() {
		return PREFIX;
	}
}