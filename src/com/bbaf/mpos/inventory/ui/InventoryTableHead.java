package com.bbaf.mpos.inventory.ui;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

public class InventoryTableHead extends TableRow {
	
	private TextView headCheckBox;
	private TextView headProductId;
	private TextView headProductName;
	private TextView headQuantity;

	public InventoryTableHead(Context context) {
		super(context);
		
		headCheckBox = new TextView(context);
		headCheckBox.setPadding(5, 5, 20, 5);
		addView(headCheckBox);
		
		headProductId = new TextView(context);
		headProductId.setText("ProductId");
		headProductId.setPadding(0, 0, 20, 0);
		addView(headProductId);
		
		headProductName = new TextView(context);
		headProductName.setText("ProductName");
		headProductName.setPadding(0, 0, 20, 0);
		addView(headProductName);
		
		headQuantity = new TextView(context);
		headQuantity.setText("Quantity");
		addView(headQuantity);
	}

}
