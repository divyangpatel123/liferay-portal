/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.security.pacl.test;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.security.pacl.PACLExecutionTestListener;
import com.liferay.portal.security.pacl.PACLIntegrationJUnitTestRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Raymond Augé
 */
@ExecutionTestListeners(listeners = {PACLExecutionTestListener.class})
@RunWith(PACLIntegrationJUnitTestRunner.class)
public class SQLTest {

	@Test
	public void testCreate() throws Exception {
		try {
			executePreparedStatement(
				"create table TestPACL_CreateFailure (userId bigint)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement(
				"create table TestPACL_CreateFailure (userId bigint)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement(
				"create table TestPACL_CreateSuccess (userId bigint)");

			executePreparedStatement("drop table TestPACL_CreateSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement(
				"create table TestPACL_CreateSuccess (userId bigint)");

			executeStatement("drop table TestPACL_CreateSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}
	}

	@Test
	public void testDrop() throws Exception {
		try {
			executePreparedStatement("drop table TestPACL_DropFailure");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement("drop table TestPACL_DropFailure");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement(
				"create table TestPACL_DropSuccess (userId bigint)");

			executePreparedStatement("drop table TestPACL_DropSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement(
				"create table TestPACL_DropSuccess (userId bigint)");

			executeStatement("drop table TestPACL_DropSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}
	}

	@Test
	public void testInsert() throws Exception {
		try {
			executePreparedStatement(
				"insert into TestPACL_InsertFailure values (1)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement("insert into TestPACL_InsertFailure values (1)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement(
				"create table TestPACL_InsertSuccess (userId bigint)");

			executePreparedStatement(
				"insert into TestPACL_InsertSuccess values (1)");

			executePreparedStatement("drop table TestPACL_InsertSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement(
				"create table TestPACL_InsertSuccess (userId bigint)");

			executeStatement("insert into TestPACL_InsertSuccess values (1)");

			executeStatement("drop table TestPACL_InsertSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}
	}

	@Test
	public void testReplace() throws Exception {
		if (!isMySQL()) {
			return;
		}

		try {
			executePreparedStatement(
				"replace TestPACL_ReplaceFailure (userId) values (1)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement(
				"replace TestPACL_ReplaceFailure (userId) values (1)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement(
				"create table TestPACL_ReplaceSuccess (userId bigint)");

			executePreparedStatement(
				"replace TestPACL_ReplaceSuccess (userId) values (1)");

			executePreparedStatement("drop table TestPACL_ReplaceSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement(
				"create table TestPACL_ReplaceSuccess (userId bigint)");

			executeStatement(
				"replace TestPACL_ReplaceSuccess (userId) values (1)");

			executeStatement("drop table TestPACL_ReplaceSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}
	}

	@Test
	public void testSelect() throws Exception {
		try {
			executePreparedStatement(
				"select * from Counter inner join User_ on User_.userId = " +
					"Counter.currentId");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement(
				"select * from Counter inner join User_ on User_.userId = " +
					"Counter.currentId");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement("select * from Counter");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement("select * from Counter");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executePreparedStatement("select * from TestPACL_Bar");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement("select * from TestPACL_Bar");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executePreparedStatement("select * from User_");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement("select * from User_");

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

	@Test
	public void testTruncate() throws Exception {
		if (!isMySQL()) {
			return;
		}

		try {
			executePreparedStatement("truncate table TestPACL_TruncateFailure");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement("truncate table TestPACL_TruncateFailure");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement(
				"create table TestPACL_TruncateSuccess (userId bigint)");

			executePreparedStatement("truncate table TestPACL_TruncateSuccess");

			executePreparedStatement("drop table TestPACL_TruncateSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement(
				"create table TestPACL_TruncateSuccess (userId bigint)");

			executeStatement("truncate table TestPACL_TruncateSuccess");

			executeStatement("drop table TestPACL_TruncateSuccess");
		}
		catch (SecurityException se) {
			Assert.fail();
		}
	}

	@Test
	public void testUpdate() throws Exception {
		try {
			executeDB(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"-123");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executePreparedStatement(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"-123");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeStatement(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"-123");
		}
		catch (SecurityException se) {
			Assert.fail();
		}

		try {
			executeDB(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"(select userId from User_)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"(select userId from User_)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement(
				"update ListType set name = 'Test PACL' where listTypeId = " +
					"(select userId from User_)");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeDB(
				"update User_ set firstName = 'Test PACL' where userId = -123");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executePreparedStatement(
				"update User_ set firstName = 'Test PACL' where userId = -123");

			Assert.fail();
		}
		catch (SecurityException se) {
		}

		try {
			executeStatement(
				"update User_ set firstName = 'Test PACL' where userId = -123");

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

	protected void executeDB(String sql) throws Exception {
		DB db = DBFactoryUtil.getDB();

		db.runSQL(sql);
	}

	protected void executePreparedStatement(String sql) throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DataAccess.getConnection();

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.execute();
		}
		catch (SQLException se) {
		}
		finally {
			DataAccess.cleanUp(connection, preparedStatement);
		}
	}

	protected void executeStatement(String sql) throws Exception {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = DataAccess.getConnection();

			statement = connection.createStatement();

			statement.execute(sql);
		}
		catch (SQLException se) {
		}
		finally {
			DataAccess.cleanUp(connection, statement);
		}

	}

	protected boolean isMySQL() {
		DB db = DBFactoryUtil.getDB();

		String dbType = db.getType();

		if (dbType.equals(DB.TYPE_MYSQL)) {
			return true;
		}

		return false;
	}

}