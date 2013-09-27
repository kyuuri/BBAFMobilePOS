package com.bbaf.mpos.inventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bbaf.mpos.ProductDescription;
import com.bbaf.mpos.ProductQuantity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InventoryDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "bbafmpos.db";
	private static final String TABLE_INVENTORY = "inventory";
	private static final String TABLE_QUANTITY = "quantity";
	private static final int DATABASE_VERSION = 1;

	public InventoryDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// date text yyyy-MM-dd HH:mm:ss
		String sql = String
				.format("CREATE TABLE %s (_key INTEGER PRIMARY KEY, ProductId INTEGER, ProductName TEXT, Price DOUBLE, Cost DOUBLE, DateModified TEXT);",
						TABLE_INVENTORY);
		db.execSQL(sql);

		sql = String
				.format("CREATE TABLE %s (ProductId INTEGER, ProductQuantity INTEGER);",
						TABLE_QUANTITY);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "USE TABLE IF EXISTS " + TABLE_INVENTORY;
		db.execSQL(sql);

		sql = "USE TABLE IF EXISTS " + TABLE_QUANTITY;
		db.execSQL(sql);
	}

	public long addProduct(ProductDescription product) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues value = new ContentValues();
			value.put("ProductId", product.getId());
			value.put("ProductName", product.getName());
			value.put("Price", product.getPrice());
			value.put("Cost", product.getCost());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDateandTime = sdf.format(new Date());
			value.put("DateModified", currentDateandTime);

			long rows = db.insert(TABLE_INVENTORY, null, value);

			db.close();
			return rows; // return rows inserted.

		} catch (Exception e) {
			return -1;
		}
	}
	
	public long setQuantity(ProductDescription product, int quantity) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues value = new ContentValues();
			value.put("ProductId", product.getId());
			value.put("ProductQuantity", quantity);
			
			long rows = db.insert(TABLE_QUANTITY, null, value);

			db.close();
			return rows; // return rows inserted.

		} catch (Exception e) {
			return -1;
		}
	}
	

	public ProductDescription getProduct(int id) {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			ProductDescription product = null;
			Log.d("table", "before cursor");
			Cursor cursor = db.query(TABLE_INVENTORY, new String[] { "*" },
					"ProductId=?", new String[] {String.valueOf(id)}, null,
					null, null, null);
			Log.d("table", "before if");
			if (cursor != null) {
				Log.d("table", "if");
				if (cursor.moveToFirst()) {
					Log.d("table", "if loop");
					/**
					 * 0 = _key 1 = ProductId 2 = ProductName 3 = Price 4 = Cost
					 * 5 = DateModified
					 */
					product = new ProductDescription(cursor.getInt(0),
							cursor.getInt(1), cursor.getString(2),
							cursor.getDouble(3), cursor.getDouble(4),
							cursor.getString(5));
				}
			}
			cursor.close();
			db.close();
			return product;
		} catch (Exception e) {
			Log.d("table", "ex");
			return null;
		}
	}

	public ArrayList<ProductDescription> getAllProduct() {
		try {
			ArrayList<ProductDescription> productList = new ArrayList<ProductDescription>();

			SQLiteDatabase db = this.getReadableDatabase();

			String strSQL = "SELECT  * FROM " + TABLE_INVENTORY;
			Cursor cursor = db.rawQuery(strSQL, null);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						ProductDescription product = new ProductDescription(
								cursor.getInt(0), cursor.getInt(1),
								cursor.getString(2), cursor.getDouble(3),
								cursor.getDouble(4), cursor.getString(5));
						productList.add(product);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			db.close();
			return productList;
		} catch (Exception e) {
			return null;
		}
	}
	
	public ProductQuantity getQuantity(int id) {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			ProductQuantity quantity = null;

			Cursor cursor = db.query(TABLE_QUANTITY, new String[] { "*" },
					"ProductId=?", new String[] {String.valueOf(id)}, null,
					null, null, null);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					/**
					 * 0 = _key 1 = ProductId 2 = ProductName 3 = Price 4 = Cost
					 * 5 = DateModified
					 */
					quantity = new ProductQuantity(cursor.getInt(0),
							cursor.getInt(1));
				}
			}
			cursor.close();
			db.close();
			return quantity;
		} catch (Exception e) {
			return null;
		}
	}
	
	private void editProduct() {
		
	}
	
	private void editQuantity() {
		
	}
	
	public long removeProduct(ProductDescription product) {
		try {
			
			SQLiteDatabase db = this.getWritableDatabase();
				
			long rows =  db.delete(TABLE_INVENTORY, "ProductId=?", new String[] {String.valueOf(product.getId())});
			db.delete(TABLE_QUANTITY, "ProductId=?", new String[] {String.valueOf(product.getId())});

			db.close();
			return rows; // return rows delete.

		 } catch (Exception e) {
		    return -1;
		 }
	}

}
