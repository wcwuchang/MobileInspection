package com.tianjin.MobileInspection.database;

import android.content.Context;

import org.xutils.DbManager;

import java.io.File;

public class DataBaseOpenHelper extends DbManager.DaoConfig{

	private static String dbname = "httptest";

	private static int version = 1;

	private static DbManager.DaoConfig helper;

	/**
	 * 创建默认数据库
	 * 
	 * @return
	 */
	public static DbManager.DaoConfig getDaoConfig(Context context) {
		if (helper == null) {
			helper = new DbManager.DaoConfig().setDbName(dbname).setDbDir(context.getFilesDir()).setDbVersion(version)
					.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
						@Override
						public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
						}
					});
		}
		return helper;
	}

	/**
	 * @param name 数据库名
	 * @param path /path
	 * @param version 版本号
	 * @return
	 */
	public static DbManager.DaoConfig getDaoConfig(Context context, String name, String path, int version) {
		if (helper == null) {
			helper = new DbManager.DaoConfig().setDbName(dbname).setDbDir(new File(path)).setDbVersion(version)
					.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
						@Override
						public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
						}
					});
		}
		return helper;
	}

}
