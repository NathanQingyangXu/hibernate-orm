/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.type.descriptor.jdbc;

import java.sql.Types;

import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * Descriptor for {@link Types#LONGVARCHAR LONGVARCHAR} handling.
 *
 * @author Steve Ebersole
 */
public class LongVarcharJdbcType extends VarcharJdbcType {
	public static final LongVarcharJdbcType INSTANCE = new LongVarcharJdbcType();

	private final int jdbcTypeCode;

	public LongVarcharJdbcType() {
		this(Types.LONGVARCHAR);
	}

	public LongVarcharJdbcType(int jdbcTypeCode) {
		this.jdbcTypeCode = jdbcTypeCode;
	}

	@Override
	public String toString() {
		return "LongVarcharTypeDescriptor";
	}

	@Override
	public int getJdbcTypeCode() {
		return jdbcTypeCode;
	}

	@Override
	public JdbcType resolveIndicatedType(
			JdbcTypeIndicators indicators,
			JavaType<?> domainJtd) {
		assert domainJtd != null;

		final TypeConfiguration typeConfiguration = indicators.getTypeConfiguration();
		final JdbcTypeRegistry jdbcTypeRegistry = typeConfiguration.getJdbcTypeRegistry();

		final int jdbcTypeCode;
		if ( indicators.isLob() ) {
			jdbcTypeCode = indicators.isNationalized() ? Types.NCLOB : Types.CLOB;
		}
		else if ( shouldUseMaterializedLob( indicators ) ) {
			jdbcTypeCode = indicators.isNationalized() ? SqlTypes.MATERIALIZED_NCLOB : SqlTypes.MATERIALIZED_CLOB;
		}
		else {
			jdbcTypeCode = indicators.isNationalized() ? Types.LONGNVARCHAR : Types.LONGVARCHAR;
		}

		return jdbcTypeRegistry.getDescriptor( indicators.resolveJdbcTypeCode( jdbcTypeCode ) );
	}
}
