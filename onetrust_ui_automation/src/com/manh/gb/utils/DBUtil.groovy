package com.manh.gb.utils

import java.net.URL;
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import org.testng.Reporter;
import java.util.Properties;

import org.atmosphere.util.anngbation.AnngbationDetector.*;
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.testng.Assert




import org.testng.Assert
import org.w3c.dom.Document
import org.w3c.dom.NodeList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

import com.manh.test.util.SQLExecuteSupport
import com.manh.test.util.spreadsheet.ExcelSpreadsheetModel
import com.manh.test.util.spreadsheet.ExcelWorkbook
import com.gb.ap4.tools.PostData
import com.google.common.base.Stopwatch;

class DBUtil {

	
	Properties gbProperties
	Properties envProperties
	URL gbprourl =null
	URL envprourl=null
	DBUtil(){
		gbprourl = ClassLoader.getSystemResource("testdata/properties/gb.properties");
		envprourl = ClassLoader.getSystemResource("testdata/properties/Env.properties");
	   gbProperties=new Properties();
	   gbProperties.load(gbprourl.openStream());
	   envProperties=new Properties();
	   envProperties.load(envprourl.openStream());
	}
	
	
	
	//=====================================================DOM DB Setup=======================================================================
	
	public Connection getDOMConnection(){
		
		def DB_TYPE=gbProperties.getProperty("DB_TYPE")
		def JDBC_URL=gbProperties.getProperty("JDBC_URL")
		def DB_USER=gbProperties.getProperty("DB_USER")
		def DB_PASSWORD=gbProperties.getProperty("DB_PASSWORD")
		def DEVICE_ID=gbProperties.getProperty("DEVICE_ID")

		if (DB_TYPE.equalsIgnoreCase("Oracle"))
		{
			Class.forName("oracle.jdbc.OracleDriver");
		}
		else if(DB_TYPE.equalsIgnoreCase("Sqlserver"))
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}
		else if(DB_TYPE.equalsIgnoreCase("DB2"))
		{

			Class.forName("com.ibm.db2.jcc.DB2Driver");
		}
		
		Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
		return conn
	}
	
	
	public void dbVerifyTxn(String intxnID,String inTxnType,String ItemID, String QTY, String amount,String TxnStatus){

		boolean rc
		String POSTxn="select TRANSACTION_STATUS,special_order_number,TRANSACTION_SUB_TgbAL,TRANSACTION_TgbAL_AMOUNT,ASSOCIATE_NAME from a_pos_transaction  where special_order_number='"+intxnID+"' and TRANSACTION_TYPE_ID='"+inTxnType+"' order by transaction_capture_date DEsc"
		String POSTxnDtl="select Item_ID,ordered_qty from a_pos_transaction_detail where entity_id in (select A_pos_transaction_id from a_pos_transaction where special_order_number='"+intxnID+"')"
		Thread.sleep(2000)
		def ListPOSTxn=dbExecuteQuery(POSTxn)
		def ListPOSTxnDtl=dbExecuteQuery(POSTxnDtl)
		def dbTxnstatus=ListPOSTxn[0][0]
		def dbTxnID=ListPOSTxn[0][1]
		def dbSubTgbal=ListPOSTxn[0][2]
		def dbTxnTgbal=ListPOSTxn[0][3]
		def dbTxnUser=ListPOSTxn[0][4]
		def dbTxnType=ListPOSTxn[0][5]
		def dbItem=ListPOSTxnDtl[0][0]
		def dbOrdQty=ListPOSTxnDtl[0][1]
		if (ItemID !=""){
			if (dbItem==ItemID){
				rc=true
			}
			else
			{
				rc=false
			}
			Assert.assertEquals(rc,true,"Item ID is ngb matching" );
		}

		if (QTY !=""){
			if (dbOrdQty==QTY){
				rc=true
			}
			else
			{
				rc=false
			}
			Assert.assertEquals(rc,true,"Quantity is ngb matching" );
		}

		if (amount !=""){
			if (dbOrdQty==QTY){
				rc=true
			}
			else
			{
				rc=false
			}
			Assert.assertEquals(rc,true,"Amount is ngb matching" );
		}

		if (dbTxnstatus !=""){
			if (dbTxnstatus==TxnStatus){
				rc=true
			}
			else
			{
				rc=false
			}
			Assert.assertEquals(rc,true,"Transaction status is ngb matching" );
		}

		Assert.assertEquals(rc,true,"Transaction validation failed" );


	}
	
	public void dbVerifyPostVoidTxn(String inPostVoidTxnID){
		
		boolean rc
		String PostVoidTxn="select parent_transaction_number from a_pos_transaction where special_order_number='"+ inPostVoidTxnID +"' order by created_dttm desc"
		Thread.sleep(2000)
		def ListPostVoidTxn=dbExecuteQuery(PostVoidTxn)
			
		def dbParentTxnNumber=ListPostVoidTxn[0][0]
		
		if (dbParentTxnNumber!="" || dbParentTxnNumber!=null){
			rc=true
		}
		else
		{
			rc=false
		}
		Assert.assertEquals(rc,true,"Post Void ngb happen correctly" );
		
	}
	
	public String dbGetParentTxnID(String intxnID){
		
		String POSTxn="select TRANSACTION_NUMBER from a_pos_transaction  where special_order_number='"+intxnID+"'"
		Thread.sleep(2000)
		def ListPOSTxn=dbExecuteQuery(POSTxn)
		def dbParentTxnID=ListPOSTxn[0][0]
	
		return dbParentTxnID.toString()
	}
	
	public void dbValidateTxnReturn(String inParentTxnID,String subtgbal,String amount,String status ){
		Thread.sleep(4000)
		boolean rc
		String POSTxn="select transaction_type_ID from a_pos_transaction  where PARENT_TRANSACTION_NUMBER='"+inParentTxnID+"'"

		def ListPOSTxn=dbExecuteQuery(POSTxn)
		String dbTxnType=ListPOSTxn[0][0].toString()


		if (dbTxnType=="20"){
			rc=true
		}
		else
		{
			rc=false
			Assert.assertEquals(rc,true,"Txn type expected is 20 for return but found "+dbTxnType );
		}
	}
	
	
	public void dbUpdateForDOM(String inPOSTxnID, String UniqueASNID, String UniqueInvNo, String UniqueDONo ){
		
		String tcPurchaseOrdersID=dbcheckCgbxn(inPOSTxnID)
		
		String SQL1="update PURCHASE_ORDERS set purchase_orders_status = 850 , PAYMENT_STATUS =30 where tc_purchase_orders_id ='"+tcPurchaseOrdersID+"'"
		String SQL2="update PURCHASE_ORDERS_LINE_ITEM set purchase_orders_line_status = 850 where purchase_orders_id = (select purchase_orders_id from PURCHASE_ORDERS where tc_purchase_orders_id ='"+tcPurchaseOrdersID+"' )"
		String SQL3="INSERT INTO ORDERS(ORDER_ID, TC_COMPANY_ID, ACTUAL_COST_CURRENCY_CODE, ACTUAL_COST, BATCH_ID, BILLING_METHOD, OVERRIDE_BILLING_METHOD, BILL_OF_LADING_NUMBER, BILL_TO_ADDRESS_1, BILL_TO_ADDRESS_2, BILL_TO_ADDRESS_3, BILL_TO_CITY, BILL_TO_STATE_PROV, BILL_TO_POSTAL_CODE, BILL_TO_COUNTY, BILL_TO_CONTACT_NAME, BILL_TO_PHONE_NUMBER, BILL_TO_FAX_NUMBER, BILL_TO_EMAIL, BILL_TO_COUNTRY_CODE, BILL_FACILITY_ALIAS_ID, BILL_TO_FACILITY_NAME, BILL_FACILITY_ID, BILL_TO_TITLE, BLOCK_AUTO_CREATE, BUDG_COST_CURRENCY_CODE, BUDG_COST, FREIGHT_REVENUE_CURRENCY_CODE, FREIGHT_REVENUE, BUSINESS_PARTNER_ID, CREATION_TYPE, DELIVERY_START_DTTM, DELIVERY_END_DTTM, DELIVERY_REQ, DELIVERY_TZ, D_ADDRESS_1, D_ADDRESS_2, D_ADDRESS_3, D_CITY, D_STATE_PROV, D_POSTAL_CODE, D_COUNTY, D_CONTACT, D_PHONE_NUMBER, D_FAX_NUMBER, D_EMAIL, D_COUNTRY_CODE, D_DOCK_ID, D_FACILITY_ALIAS_ID, D_FACILITY_ID, D_DOCK_DOOR_ID, D_FACILITY_NAME, IS_D_POBOX, DSG_Mgb_ID, DSG_CARRIER_ID, DSG_SERVICE_LEVEL_ID, DSG_EQUIPMENT_ID, HAS_IMPORT_ERROR, HAS_NgbES, HAS_SOFT_CHECK_ERRORS, CREATED_SOURCE_TYPE, CREATED_SOURCE, LAST_UPDATED_SOURCE_TYPE, LAST_UPDATED_SOURCE, INBOUND_REGION_ID, INCgbERM_FACILITY_ALIAS_ID, INCgbERM_FACILITY_ID, INCgbERM_ID, INCgbERM_LOC_AVA_DTTM, INCgbERM_LOC_AVA_TIME_ZONE_ID, IS_CANCELLED, IS_HAZMAT, IS_IMPORTED, IS_PERISHABLE, IS_SUSPENDED, MONETARY_VALUE, MV_CURRENCY_CODE, ORDER_DATE_DTTM, ORDER_TYPE, DISTRO_NUMBER, ORIG_BUDG_COST, O_ADDRESS_1, O_ADDRESS_2, O_ADDRESS_3, O_CITY, O_STATE_PROV, O_POSTAL_CODE, O_COUNTY, O_CONTACT, O_PHONE_NUMBER, O_FAX_NUMBER, O_EMAIL, O_COUNTRY_CODE, O_DOCK_ID, O_FACILITY_ALIAS_ID, O_FACILITY_ID, O_DOCK_DOOR_ID, O_FACILITY_NAME, OUTBOUND_REGION_ID, PACKAGING, PICKUP_START_DTTM, PICKUP_END_DTTM, PICKUP_TZ, PRIORITY, PRODUCT_CLASS_ID, PRgbECTION_LEVEL_ID, MERCHANDIZING_DEPARTMENT_ID, SHIPMENT_ID, ORDER_STATUS, TC_ORDER_ID, TC_SHIPMENT_ID, TRANS_RESP_CODE, UN_NUMBER_ID, BASELINE_COST_CURRENCY_CODE, BASELINE_COST, BASELINE_Mgb_ID, BASELINE_SERVICE_LEVEL_ID, BASELINE_CARRIER_ID, BLOCK_AUTO_CONSOLIDATE, COMPARTMENT_NO, CONS_RUN_ID, DEST_SHIP_THRU_FAC_ALIAS_ID, DEST_SHIP_THRU_FACILITY_ID, DRIVER_TYPE_ID, DROPOFF_PICKUP, DSG_TRACTOR_EQUIPMENT_ID, IS_BOOKING_REQUIRED, HAS_SPLIT, IS_PARTIALLY_PLANNED, MOVEMENT_OPTION, MOVE_TYPE, RELEASE_DESTINATION, IN_TRANSIT_ALLOCATION, NORMALIZED_BASELINE_COST, ORDER_LOADING_SEQ, ORIGIN_SHIP_THRU_FAC_ALIAS_ID, ORIGIN_SHIP_THRU_FACILITY_ID, PARENT_ORDER_ID, PARENT_TYPE, PATH_ID, PATH_SET_ID, PLAN_D_FACILITY_ALIAS_ID, PLAN_D_FACILITY_ID, PLAN_O_FACILITY_ALIAS_ID, PLAN_O_FACILITY_ID, PROD_SCHED_REF_NUMBER, SCHED_DOW, ADDR_CODE, ADVT_DATE, ADDR_VALID, DISTRIBUTION_SHIP_VIA, BILL_ACCT_NBR, BOL_BREAK_ATTR, CHUTE_ID, COD_FUNDS, CUST_BROKER_ACCT_NBR, DC_CTR_NBR, DO_STATUS, DO_TYPE, DOCS_ONLY_SHPMT, DUTY_AND_TAX, DUTY_TAX_ACCT_NBR, DUTY_TAX_PAYMENT_TYPE, EST_LPN, EST_LPN_BRIDGED, EST_PALLET, FREIGHT_FORWARDER_ACCT_NBR, REF_SHIPMENT_NBR, REF_STOP_SEQ, FTSR_NBR, GLOBAL_LOCN_NBR, TAX_ID, IMPORTER_DEFN, INTL_GOODS_DESC, IS_BACK_ORDERED, LANG_ID, LINE_HAUL_SHIP_VIA, LPN_CUBING_INDIC, MAJOR_MINOR_ORDER, MAJOR_ORDER_CTRL_NBR, MAJOR_ORDER_GRP_ATTR, MHE_FLAG, MHE_ORD_STATE, ORDER_CONSOL_LOCN_ID, ORDER_CONSOL_PROFILE, ORDER_PRINT_DTTM, ORIGINAL_ASSIGNED_SHIP_VIA, PACK_WAVE_NBR, PALLET_CUBING_INDIC, PARTIAL_LPN_OPTION, PARTIES_RELATED, PNH_FLAG, PRE_PACK_FLAG, PRE_STICKER_CODE, PRIMARY_MAXI_ADDR_NBR, RTE_SWC_NBR, RTE_TO, RTE_TYPE_1, RTE_TYPE_2, RTE_WAVE_NBR, ACTUAL_SHIPPED_DTTM, SHIP_GROUP_ID, SHIP_GROUP_SEQUENCE, SHPNG_CHRG, STAGE_INDIC, STORE_NBR, TgbAL_NBR_OF_LPN, TgbAL_NBR_OF_PLT, WM_ORDER_STATUS, PACK_SLIP_PRT_CNT, SECONDARY_MAXI_ADDR_NBR, REPL_WAVE_NBR, ORDER_RECON_DTTM, CUBING_STATUS, EST_PALLET_BRIDGED, MANIFEST_NBR, NON_MACHINEABLE, EXT_PURCHASE_ORDER, CUSTOMER_ID, D_NAME, PURCHASE_ORDER_NUMBER, DSG_STATIC_ROUTE_ID, DYNAMIC_ROUTING_REQD, SCHED_PICKUP_DTTM, SCHED_DELIVERY_DTTM, ZONE_SKIP_HUB_LOCATION_ID, DSG_HUB_LOCATION_ID, EQUIPMENT_TYPE, MUST_RELEASE_BY_DTTM, IS_ORIGINAL_ORDER, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, REF_NUM1, REF_NUM2, REF_NUM3, REF_NUM4, REF_NUM5, SPL_INSTR_CODE_1, SPL_INSTR_CODE_2, SPL_INSTR_CODE_3, SPL_INSTR_CODE_4, SPL_INSTR_CODE_5, SPL_INSTR_CODE_6, SPL_INSTR_CODE_7, SPL_INSTR_CODE_8, SPL_INSTR_CODE_9, SPL_INSTR_CODE_10, FIRST_ZONE, LAST_ZONE, NBR_OF_ZONES, SINGLE_UNIT_FLAG, TgbAL_NBR_OF_UNITS, CANCEL_DTTM, ASSIGNED_STATIC_ROUTE_ID, DSG_SHIP_VIA, FREIGHT_CLASS, AES_ITN, COD_RETURN_COMPANY_NAME, PURCHASE_ORDER_ID, DYNAMIC_REQUEST_SENT, ASSIGNED_Mgb_ID, ASSIGNED_SERVICE_LEVEL_ID, ASSIGNED_CARRIER_ID, ASSIGNED_EQUIPMENT_ID, COMMODITY_CODE_ID, COD_AMOUNT, BILL_TO_NAME, ACCT_RCVBL_CODE, ADVT_CODE, IS_ROUTED, IS_CUSTOMER_PICKUP, IS_DIRECT_ALLOWED, RTE_ATTR, PLAN_DUE_DTTM, PRTL_SHIP_CONF_FLAG, PRTL_SHIP_CONF_STATUS, ALLOW_PRE_BILLING, PRE_BILL_STATUS, LANE_NAME, DECLARED_VALUE, DV_CURRENCY_CODE, COD_CURRENCY_CODE, WEIGHT_UOM_ID_BASE, DESTINATION_ACTION, ORDER_RECEIVED, WAVE_ID, WAVE_OPTION_ID, TRANS_PLAN_OWNER, MARK_FOR, DELIVERY_OPTIONS, BOL_TYPE, LPN_LABEL_TYPE, PACK_SLIP_TYPE, PALLET_CONTENT_LABEL_TYPE, NBR_OF_LABEL, NBR_OF_PAKNG_SLIPS, CONTNT_LABEL_TYPE, NBR_OF_CONTNT_LABEL, MANIF_TYPE, PRINT_CANADIAN_CUST_INVC_FLAG, PRINT_COO, PRINT_DOCK_RCPT_FLAG, PRINT_INV, PRINT_NAFTA_COO_FLAG, PRINT_OCEAN_BOL_FLAG, PRINT_PKG_LIST_FLAG, PRINT_SED, PRINT_SHPR_LTR_OF_INSTR_FLAG, TRANS_PLAN_DIRECTION, DSG_VOYAGE_FLIGHT, HAZ_OFFEROR_NAME, ORDER_SHIPMENT_SEQ, MOVEMENT_TYPE, STAGING_LOCN_ID, PICKLIST_ID, EFFECTIVE_RANK, ACCT_RCVBL_ACCT_NBR, UPSMI_COST_CENTER, DSG_TRAILER_NUMBER, CHASE_ELIGIBLE, SHIPPING_CHANNEL, IS_GUARANTEED_DELIVERY, FAILED_GUARANTEED_DELIVERY, APPLY_LPNTYPE_FOR_ORDER, PO_TYPE_ATTR) VALUES (SEQ_ORDER_ID.nextval , (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"'), null, null, null, 0, null, null, null, null, null, null, null, null, null, 'Arpita', null, null, null, null, null, null, null, null, 0, null, null, 'AED', null, null, 50, getDate(), getDate(), 'NEI', 9, '2300 Windy Ridge', null, null, 'Atlanta', 'GA', '30339-5665', null, 'Arpita', null, null, 'n@manh.com', 'US', null, null, null, 0, null, 0, null, null, null, null, 0, 0, 0, 1, 'Groovy', 1, 'Groovy', null, null, null, null, getDate(), null, 0, 0, 0, 0, 0, 0, 'USD', getDate(), 'Auto-Reg', null, null, '501 Gamble Street', null, null, 'Tallahassee', 'FL', '32310', null, null, null, null, null, 'US', null, '03-TN', (Select FACILITY_ID From FACILITY_ALIAS where  FACILITY_ALIAS_ID  =  '03-TN'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"') ), 0, null, null, null, getDate(), getDate(), 9, null, null, null, null, null, (Select ORDER_STATUS From ORDER_STATUS where  DESCRIPTION  =  'Delivered' ), '"+UniqueDONo+"', null, 'SHP', null, null, null, null, null, null, 0, null, null, null, null, null, null, null, 0, 0, 0, null, null, null, 0, null, null, null, null, null, 0, null, null, null, null, null, null, null, null, null, getDate(), null, null, null, null, null, null, null, null, (Select ORDER_STATUS From DO_STATUS where  DESCRIPTION  =  'Shipped' ), (Select DO_TYPE From DO_TYPE where  DESCRIPTION  =  'Customer Order' ), 'N', null, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, 'N', null, null, 0, null, null, null, null, null, null, null, getDate(), null, null, 0, null, 'N', '0', 0, null, null, null, null, null, null, null, getDate(), null, 0, 0, 0, null, 0, 0, null, null, null, null, getDate(), null, 0, null, 0, null, null, 'Arpita', '"+tcPurchaseOrdersID+"', null, null, getDate(), getDate(), null, null, null, getDate(), 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, getDate(), null, 'STD', null, null, null, (Select PURCHASE_ORDERS_ID From PURCHASE_ORDERS where  TC_PURCHASE_ORDERS_ID  =  '"+tcPurchaseOrdersID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"') ), null, null, null, null, null, null, null, 'Arpita', null, null, 0, '0', '0', null, getDate(), 0, null, 0, '0', null, null, null, null, null, '03', 0, null, null, null, null, '03', null, null, null, null, null, null, null, null, null, '0', '0', '0', '0', '0', '0', '0', '0', '0', null, null, null, null, null, null, null, 'a', null, null, null, 0, null, 0, '-1', 0, null )"
		String SQL4="INSERT INTO ORDER_LINE_ITEM(ORDER_ID, LINE_ITEM_ID, TC_ORDER_LINE_ID, TC_COMPANY_ID, PRODUCT_CLASS_ID, PRgbECTION_LEVEL_ID, COMMODITY_CLASS, REF_FIELD1, REF_FIELD2, REF_FIELD3, REF_FIELD4, REF_FIELD5, REF_FIELD6, REF_FIELD7, REF_FIELD8, REF_FIELD9, REF_FIELD10, REF_NUM1, REF_NUM2, REF_NUM3, REF_NUM4, REF_NUM5, ADJUSTED_ORDER_QTY, MASTER_ORDER_ID, MO_LINE_ITEM_ID, RTS_ID, RTS_LINE_ITEM_ID, QTY_UOM_ID_BASE, SHIPPED_QTY, RECEIVED_QTY, COMMODITY_CODE_ID, UN_NUMBER_ID, ITEM_ID, ITEM_NAME, DESCRIPTION, IS_HAZMAT, IS_GIFT, IS_CHASE_CREATED_LINE, ORIG_BUDG_COST, BUDG_COST, BUDG_COST_CURRENCY_CODE, FREIGHT_REVENUE, FREIGHT_REVENUE_CURRENCY_CODE, ACTUAL_COST, ACTUAL_COST_CURRENCY_CODE, PARENT_LINE_ITEM_ID, PACKAGE_TYPE_ID, MV_SIZE_UOM_ID, MV_CURRENCY_CODE, UNIT_MONETARY_VALUE, TgbAL_MONETARY_VALUE, STACK_LENGTH_VALUE, STACK_LENGTH_STANDARD_UOM, STACK_WIDTH_VALUE, STACK_WIDTH_STANDARD_UOM, STACK_HEIGHT_VALUE, STACK_HEIGHT_STANDARD_UOM, STACK_DIAMETER_VALUE, STACK_DIAMETER_STANDARD_UOM, SKU_GTIN, STD_PACK_QTY, UNIT_TAX_AMOUNT, IS_CANCELLED, DELIVERY_START_DTTM, DELIVERY_END_DTTM, MERCHANDIZING_DEPARTMENT_ID, ALLOCATED_QTY, PPACK_QTY, ACTUAL_SHIPPED_DTTM, PICKUP_END_DTTM, PICKUP_START_DTTM, PURCHASE_ORDER_LINE_NUMBER, REPL_WAVE_RUN, STORE_DEPT, ALLOCATION_SOURCE_LINE_ID, ALLOCATION_SOURCE_ID, PRE_RECEIPT_STATUS, QTY_UOM_ID, REFERENCE_LINE_ITEM_ID, REFERENCE_ORDER_ID, STD_PALLET_QTY, STD_SUB_PACK_QTY, PROD_STAT, LINE_TYPE, IS_EMERGENCY, WAVE_PROC_TYPE, VAS_PROCESS_TYPE, USER_CANCELED_QTY, UNITS_PAKD, UNIT_WT, UNIT_VOL, UNIT_PRICE_AMOUNT, UNIT_COST, STD_LPN_WT, STD_LPN_VOL, STD_LPN_QTY, STD_BUNDLE_QTY, ALLOC_TYPE, ASSORT_NBR, BATCH_NBR, BATCH_REQUIREMENT_TYPE, CHUTE_ASSIGN_TYPE, CNTRY_OF_ORGN, CUSTOMER_ITEM, DELIVERY_REFERENCE_NUMBER, EVENT_CODE, EXP_INFO_CODE, EXT_SYS_LINE_ITEM_ID, INVN_TYPE, ITEM_ATTR_1, ITEM_ATTR_2, ITEM_ATTR_3, ITEM_ATTR_4, ITEM_ATTR_5, LPN_BRK_ATTRIB, LPN_TYPE, MANUFACTURING_DTTM, MERCH_GRP, MERCH_TYPE, SEGMENT_NAME, MINOR_ORDER_NBR, ORDER_CONSOL_ATTR, PACK_ZONE, PALLET_TYPE, PARTL_FILL, PICK_LOCN_ASSIGN_TYPE, PICK_LOCN_ID, PICKUP_REFERENCE_NUMBER, CRITCL_DIM_1, CRITCL_DIM_2, CRITCL_DIM_3, CUBE_MULTIPLE_QTY, LPN_SIZE, ORDER_QTY, ORIG_ORDER_QTY, PACK_RATE, DO_DTL_STATUS, HAS_ERRORS, IS_STACKABLE, PLANNED_SHIP_DATE, PPACK_GRP_CODE, PRICE_TKT_TYPE, REASON_CODE, SERIAL_NUMBER_REQUIRED_FLAG, SINGLE_UNIT_FLAG, SKU_BREAK_ATTR, SKU_SUB_CODE_ID, SKU_SUB_CODE_VALUE, PRICE, RETAIL_PRICE, RTL_TO_BE_DISTROED_QTY, REPL_PROC_TYPE, STACK_RANK, ALLOCATION_SOURCE, SUBSTITUTED_PARENT_LINE_ID, ALLOC_LINE_ID, CUSTOM_TAG, SHELF_DAYS, PRIORITY, CREATED_SOURCE_TYPE, CREATED_SOURCE, LAST_UPDATED_SOURCE_TYPE, LAST_UPDATED_SOURCE, FULFILLMENT_TYPE, WAVE_NBR, SHIP_WAVE_NBR, REPL_WAVE_NBR, QTY_CONV_FACTOR, PLANNED_WEIGHT, RECEIVED_WEIGHT, SHIPPED_WEIGHT, WEIGHT_UOM_ID_BASE, WEIGHT_UOM_ID, PLANNED_VOLUME, RECEIVED_VOLUME, SHIPPED_VOLUME, VOLUME_UOM_ID_BASE, VOLUME_UOM_ID, SIZE1_UOM_ID, SIZE1_VALUE, RECEIVED_SIZE1, SHIPPED_SIZE1, SIZE2_UOM_ID, SIZE2_VALUE, RECEIVED_SIZE2, SHIPPED_SIZE2, PURCHASE_ORDER_NUMBER, EXT_PURCHASE_ORDER, ORIGINAL_ORDERED_ITEM_ID, SUBSTITUTION_TYPE, BACK_ORD_REASON, ALLOW_REVIVAL_RECEIPT_ALLOC, EFFECTIVE_RANK, WM_RECEIPT_ALLOCATED) VALUES ((SELECT ORDER_ID FROM ORDERS WHERE PURCHASE_ORDER_ID = (Select PURCHASE_ORDERS_ID From PURCHASE_ORDERS where  TC_PURCHASE_ORDERS_ID  =  '"+tcPurchaseOrdersID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"') )), SEQ_LINE_ITEM_ID.nextval , (select tc_po_line_id from purchase_orders_line_item  where purchase_orders_id=(select purchase_orders_id from purchase_orders where tc_purchase_orders_id='"+tcPurchaseOrdersID+"')), (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"'), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, (Select PURCHASE_ORDERS_ID From PURCHASE_ORDERS where  TC_PURCHASE_ORDERS_ID  =  '"+tcPurchaseOrdersID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"')), (SELECT PURCHASE_ORDERS_LINE_ITEM_ID FROM PURCHASE_ORDERS_LINE_ITEM WHERE TC_PO_LINE_ID  =  (select tc_po_line_id from purchase_orders_line_item  where purchase_orders_id=(select purchase_orders_id from purchase_orders where tc_purchase_orders_id='"+tcPurchaseOrdersID+"'))  AND PURCHASE_ORDERS_ID  =  (Select PURCHASE_ORDERS_ID From PURCHASE_ORDERS where  TC_PURCHASE_ORDERS_ID  =  '"+tcPurchaseOrdersID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ) AND TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' )), null, null, (Select SIZE_UOM_ID From SIZE_UOM where  SIZE_UOM = 'U'  and  AUDIT_CREATED_SOURCE = 'Groovy'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"') ), null, null, null, null, (Select ITEM_ID From ITEM_CBO where  ITEM_NAME  =  '630306533003'  and  COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"') ), '630306533003', null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, 'USD', null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null, null, 0, 0, null, null, null, (select tc_po_line_id from purchase_orders_line_item  where purchase_orders_id=(select purchase_orders_id from purchase_orders where tc_purchase_orders_id='"+tcPurchaseOrdersID+"')), null, null, null, null, null, null, null, null, null, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 2, 2, null, (Select ORDER_STATUS From DO_STATUS where  DESCRIPTION  =  'Shipped' ), 0, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 10, null, null, null, null, null, 1, 'Groovy', 1, 'Groovy', null, null, null, null, 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '"+tcPurchaseOrdersID+"', null, (Select ITEM_ID From ITEM_CBO where  ITEM_NAME  =  '630306533003'  and  COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"') ), null, null, '0', 'a', 0)"
		String SQL5="INSERT INTO DO_LINE_ALLOCATION(ORDER_ID, ORDER_LINE_ITEM_ID, ALLOCATION_ID, INVENTORY_ID, IS_DELETED) VALUES ((Select ORDER_ID From ORDERS where  TC_ORDER_ID  =  '"+UniqueDONo+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"') ),(SELECT LINE_ITEM_ID FROM ORDER_LINE_ITEM WHERE MO_LINE_ITEM_ID = (select PURCHASE_ORDERS_LINE_ITEM_ID from purchase_orders_line_item where purchase_orders_id=(SELECT PURCHASE_ORDERS_ID FROM PURCHASE_ORDERS WHERE TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"'))),(SELECT A_IDENTITY FROM A_ORDERINVENTORYALLOCATION  WHERE A_ORDERLINEID =  (select PURCHASE_ORDERS_LINE_ITEM_ID from purchase_orders_line_item where purchase_orders_id=(SELECT PURCHASE_ORDERS_ID FROM PURCHASE_ORDERS WHERE TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"'))) , null , 0)"
		String SQL6="INSERT INTO ASN (ASN_ID, TC_COMPANY_ID, TC_ASN_ID, TC_ASN_ID_U, ASN_TYPE, ASN_STATUS, TRACTOR_NUMBER, DELIVERY_STOP_SEQ, ORIGIN_FACILITY_ALIAS_ID, ORIGIN_FACILITY_ID, PICKUP_END_DTTM, ACTUAL_DEPARTURE_DTTM, DESTINATION_FACILITY_ALIAS_ID, DESTINATION_FACILITY_ID, DELIVERY_START_DTTM, DELIVERY_END_DTTM, ACTUAL_ARRIVAL_DTTM, RECEIPT_DTTM, INBOUND_REGION_NAME, OUTBOUND_REGION_NAME, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, TgbAL_WEIGHT, APPOINTMENT_ID, APPOINTMENT_DTTM, APPOINTMENT_DURATION, PALLET_FOgbPRINT, LAST_TRANSMITTED_DTTM, TgbAL_SHIPPED_QTY, TgbAL_RECEIVED_QTY, ASSIGNED_CARRIER_CODE, BILL_OF_LADING_NUMBER, PRO_NUMBER, EQUIPMENT_CODE, EQUIPMENT_CODE_ID, ASSIGNED_CARRIER_CODE_ID, TgbAL_VOLUME, VOLUME_UOM_ID_BASE, FIRM_APPT_IND, TRACTOR_CARRIER_CODE_ID, TRACTOR_CARRIER_CODE, BUYER_CODE, REP_NAME, CONTACT_ADDRESS_1, CONTACT_ADDRESS_2, CONTACT_ADDRESS_3, CONTACT_CITY, CONTACT_STATE_PROV, CONTACT_ZIP, CONTACT_NUMBER, EXT_CREATED_DTTM, INBOUND_REGION_ID, OUTBOUND_REGION_ID, REGION_ID, ASN_LEVEL, RECEIPT_VARIANCE, HAS_IMPORT_ERROR, HAS_SOFT_CHECK_ERROR, HAS_ALERTS, SYSTEM_ALLOCATED, IS_COGI_GENERATED, ASN_PRIORITY, SCHEDULE_APPT, IS_ASSOCIATED_TO_OUTBOUND, IS_CANCELLED, IS_CLOSED, BUSINESS_PARTNER_ID, MANIF_NBR, MANIF_TYPE, WORK_ORD_NBR, CUT_NBR, MFG_PLNT, IS_WHSE_TRANSFER, QUALITY_CHECK_HOLD_UPON_RCPT, QUALITY_AUDIT_PERCENT, SHIPPED_LPN_COUNT, RECEIVED_LPN_COUNT, ACTUAL_SHIPPED_DTTM, LAST_RECEIPT_DTTM, VERIFIED_DTTM, LABEL_PRINT_REQD, INITIATE_FLAG, CREATED_SOURCE_TYPE, CREATED_SOURCE, CREATED_DTTM, LAST_UPDATED_SOURCE_TYPE, LAST_UPDATED_SOURCE, LAST_UPDATED_DTTM, SHIPMENT_ID, TC_SHIPMENT_ID, VERIFICATION_ATTEMPTED, FLOW_THRU_ALLOC_IN_PROG, ALLOCATION_COMPLETED, EQUIPMENT_TYPE, ASN_ORGN_TYPE, DC_ORD_NBR, CONTRAC_LOCN, MHE_SENT, FLOW_THROUGH_ALLOCATION_METHOD, FIRST_RECEIPT_DTTM, MISC_INSTR_CODE_1, MISC_INSTR_CODE_2, DOCK_DOOR_ID, MODE_ID, SHIPPING_COST, SHIPPING_COST_CURRENCY_CODE, FLOWTHROUGH_ALLOCATION_STATUS, HIBERNATE_VERSION, QTY_UOM_ID_BASE, WEIGHT_UOM_ID_BASE, DRIVER_NAME, TRAILER_NUMBER, DESTINATION_TYPE, CONTACT_COUNTY, CONTACT_COUNTRY_CODE, RECEIPT_TYPE, VARIANCE_TYPE, HAS_NgbES, ORIGINAL_ASN_ID, RETURN_REFERENCE_NUMBER, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, REF_NUM1, REF_NUM2, REF_NUM3, REF_NUM4, REF_NUM5, INVOICE_DATE, INVOICE_NUMBER, PRE_RECEIPT_STATUS, PRE_ALLOCATION_FIT_PERCENTAGE, TRAILER_CLOSED) VALUES (SEQ_ASN_ID.nextval ,(select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ), '"+UniqueASNID+"', '"+UniqueASNID+"', 20, 20, null, null, '03-TN', (Select FACILITY_ID From FACILITY_ALIAS where  FACILITY_ALIAS_ID  =  '03-TN'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), getDate(), null, '03-TN', (Select FACILITY_ID From FACILITY_ALIAS where  FACILITY_ALIAS_ID  =  '03-TN'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), getDate(), getDate(), null, null, 'RGNLESS_IB', 'RGNLESS_OB', null, null, null, null, null, null, 0, null, null, 2, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, (Select REGION_ID From REGION where  REGION_NAME = 'RGNLESS_IB'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), (Select REGION_ID From REGION where  REGION_NAME = 'RGNLESS_OB'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, 10, 0, 0, 0, 0, 0, 0, 1, 0, null, 0, 0, null, null, null, null, null, null, 'N', null, 0, 1, null, getDate(), null, null, null, null, 2, 'Groovy', getDate(), 2, 'Groovy', getDate(), null, null, null, null, null, null, 'M', null, null, null, null, getDate(), null, null, null, null, null, null, null, null, (Select SIZE_UOM_ID From SIZE_UOM where  SIZE_UOM = 'U'  and  AUDIT_CREATED_SOURCE = 'Groovy'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, null, null, null, null, null, null, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, getDate(), null, null, null, 0)"
		String SQL7="INSERT INTO LPN (LPN_ID, TC_LPN_ID, BUSINESS_PARTNER_ID, TC_COMPANY_ID, PARENT_LPN_ID, TC_PARENT_LPN_ID, LPN_TYPE, VERSION_NBR, LPN_MONETARY_VALUE, LPN_MONETARY_VALUE_CURR_CODE, TIER_QTY, STD_LPN_QTY, C_FACILITY_ALIAS_ID, C_FACILITY_ID, O_FACILITY_ALIAS_ID, O_FACILITY_ID, FINAL_DEST_FACILITY_ALIAS_ID, FINAL_DEST_FACILITY_ID, LPN_STATUS, LPN_STATUS_UPDATED_DTTM, LPN_FACILITY_STATUS, TC_REFERENCE_LPN_ID, ORIGINAL_TC_SHIPMENT_ID, PARCEL_SHIPMENT_NBR, TRACKING_NBR, MANIFEST_NBR, SHIP_VIA, MASTER_BOL_NBR, BOL_NBR, INIT_SHIP_VIA, PATH_ID, ROUTE_RULE_ID, IDEAL_PICK_DTTM, REPRINT_COUNT, LAST_FROZEN_DTTM, LAST_COUNTED_DTTM, BASE_CHARGE, BASE_CHARGE_CURR_CODE, ADDNL_OPTION_CHARGE, ADDNL_OPTION_CHARGE_CURR_CODE, INSUR_CHARGE, INSUR_CHARGE_CURR_CODE, ACTUAL_CHARGE, ACTUAL_CHARGE_CURR_CODE, PRE_BULK_BASE_CHARGE, PRE_BULK_BASE_CHARGE_CURR_CODE, PRE_BULK_ADD_OPT_CHR, PRE_BULK_ADD_OPT_CHR_CURR_CODE, DIST_CHARGE, DIST_CHARGE_CURR_CODE, FREIGHT_CHARGE, FREIGHT_CHARGE_CURR_CODE, RECEIVED_TC_SHIPMENT_ID, MANUFACTURED_DTTM, MANUFACT_PLANT_FACILITY_ALIAS, RCVD_DTTM, TC_PURCHASE_ORDERS_ID, PURCHASE_ORDERS_ID, TC_SHIPMENT_ID, SHIPMENT_ID, TC_ASN_ID, ASN_ID, RECEIPT_VARIANCE_INDICATOR, ESTIMATED_VOLUME, WEIGHT, ACTUAL_VOLUME, LPN_LABEL_TYPE, PACKAGE_TYPE_ID, MISC_INSTR_CODE_1, MISC_INSTR_CODE_2, MISC_INSTR_CODE_3, MISC_INSTR_CODE_4, MISC_INSTR_CODE_5, MISC_NUM_1, MISC_NUM_2, HIBERNATE_VERSION, CUBE_UOM, ERROR_INDICATOR, WARNING_INDICATOR, QA_FLAG, PALLET_OPEN_FLAG, QTY_UOM_ID_BASE, WEIGHT_UOM_ID_BASE, VOLUME_UOM_ID_BASE, SPLIT_LPN_ID, CREATED_SOURCE_TYPE, CREATED_SOURCE, CREATED_DTTM, EXT_CREATED_DTTM, LAST_UPDATED_SOURCE_TYPE, LAST_UPDATED_SOURCE, LAST_UPDATED_DTTM, CONVEYABLE_LPN_FLAG, ACTIVE_LPN_FLAG, LPN_SHIPPED_FLAG, STD_QTY_FLAG, PALLET_MASTER_LPN_FLAG, MASTER_LPN_FLAG, LPN_SIZE_TYPE_ID, PICK_SUB_LOCN_ID, CURR_SUB_LOCN_ID, PREV_SUB_LOCN_ID, DEST_SUB_LOCN_ID, INBOUND_OUTBOUND_INDICATOR, WORK_ORD_NBR, INTERNAL_ORDER_ID, SHIPPED_DTTM, TRAILER_STOP_SEQ_NBR, PACK_WAVE_NBR, WAVE_NBR, WAVE_SEQ_NBR, WAVE_STAT_CODE, DIRECTED_QTY, TRANSITIONAL_INVENTORY_TYPE, PICKER_USERID, PACKER_USERID, CHUTE_ID, CHUTE_ASSIGN_TYPE, STAGE_INDICATOR, OUT_OF_ZONE_INDICATOR, LABEL_PRINT_REQD, CONSUMPTION_SEQUENCE, CONSUMPTION_PRIORITY, CONSUMPTION_PRIORITY_DTTM, PUTAWAY_TYPE, RETURN_DISPOSITION_CODE, FINAL_DISPOSITION_CODE, LOADED_DTTM, LOADER_USERID, LOADED_POSN, SHIP_BY_DATE, PICK_DELIVERY_DURATION, LPN_BREAK_ATTR, LPN_PRINT_GROUP_CODE, SEQ_RULE_PRIORITY, SPUR_LANE, SPUR_POSITION, FIRST_ZONE, LAST_ZONE, NBR_OF_ZONES, LPN_DIVERT_CODE, INTERNAL_ORDER_CONSOL_ATTR, WHSE_INTERNAL_EVENT_CODE, SELECTION_RULE_ID, VOCO_INTRN_REVERSE_ID, VOCO_INTRN_REVERSE_PALLET_ID, LPN_CREATION_CODE, PALLET_X_OF_Y, INCUBATION_DATE, CONTAINER_TYPE, CONTAINER_SIZE, LPN_NBR_X_OF_Y, LOAD_SEQUENCE, MASTER_PACK_ID, SINGLE_LINE_LPN, NON_INVENTORY_LPN_FLAG, EPC_MATCH_FLAG, AUDITOR_USERID, PRINTING_RULE_ID, EXPIRATION_DATE, QUAL_AUD_STAT_CODE, DELVRECIPIENTNAME, DELVRECEIPTDATETIME, DELVONTIMEFLAG, DELVCOMPLFLAG, ITEM_NAME, ITEM_ID, LPN_FACILITY_STAT_UPDATED_DTTM, LPN_DISP_STATUS, OVERSIZE_LENGTH, BILLING_METHOD, CUSTOMER_ID, IS_ZONE_SKIPPED, ZONESKIP_HUB_FACILITY_ALIAS_ID, ZONESKIP_HUB_FACILITY_ID, PLANING_DEST_FACILITY_ALIAS_ID, PLANING_DEST_FACILITY_ID, SCHED_SHIP_DTTM, DISTRIBUTION_LEG_CARRIER_ID, DISTRIBUTION_LEG_MODE_ID, DISTRIBUTION_LEV_SVCE_LEVEL_ID, SERVICE_LEVEL_INDICATOR, COD_FLAG, COD_AMOUNT, COD_PAYMENT_METHOD, RATED_WEIGHT, RATE_WEIGHT_TYPE, RATE_ZONE, FRT_FORWARDER_ACCT_NBR, INTL_GOODS_DESC, SHIPMENT_PRINT_SED, EXPORT_LICENSE_NUMBER, EXPORT_LICENSE_SYMBOL, EXPORT_INFO_CODE, REPRINT_SHIPPING_LABEL, PLAN_LOAD_ID, DOCUMENTS_ONLY, SCHEDULED_DELIVERY_DTTM, NON_MACHINEABLE, D_FACILITY_ALIAS_ID, D_FACILITY_ID, VENDOR_LPN_NBR, PLANNED_TC_ASN_ID, ESTIMATED_WEIGHT, TC_ORDER_ID, ORDER_ID, MARK_FOR, PRE_RECEIPT_STATUS, CROSSDOCK_TC_ORDER_ID, PHYSICAL_ENTITY_CODE, PROCESS_IMMEDIATE_NEEDS, LENGTH, WIDTH, HEIGHT, TgbAL_LPN_QTY, STATIC_ROUTE_ID, HAS_ALERTS, ORDER_SPLIT_ID, MHE_LOADED, ALT_TRACKING_NBR, RECEIPT_TYPE, VARIANCE_TYPE, RETURN_TRACKING_NBR, CONTAINS_DRY_ICE, RETURN_REFERENCE_NUMBER, HAS_NgbES, RETURN_TRACKING_NBR_2, DELIVERY_TYPE, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, REF_NUM1, REF_NUM2, REF_NUM3, REF_NUM4, REF_NUM5, REGULATION_SET, DRY_ICE_WT, OVERPACK, SALVAGE_PACK, SALVAGE_PACK_QTY, Q_VALUE, ALL_PAKD_IN_ONE, SPECIAL_PERMITS, CN22_NBR, DISPOSITION_TYPE, DISPOSITION_SOURCE_ID, END_OLPN_DTTM, VOCOLLECT_ASSIGN_ID, CALCULATED_CUT_OFF_DTTM, FAILED_GUARANTEED_DELIVERY, EPI_PACKAGE_ID, EPI_SHIPMENT_ID, EPI_MANIFEST_ID, EPI_PACKAGE_STATUS, XREF_OLPN, PALLET_CONTENT_CODE) VALUES (LPN_ID_SEQ.nextval , '"+"LPN_"+UniqueASNID+"', null, (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ), null, null, 1, null, null, null, null, null, '03-TN', (Select FACILITY_ID From FACILITY_ALIAS where  FACILITY_ALIAS_ID  =  '03-TN'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, null, null, 35, getDate(), 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '"+UniqueASNID+"', (Select ASN_ID From ASN where  TC_ASN_ID  =  '"+UniqueASNID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, null, null, null, null, null, null, null, null, null, null, null, 1, null, 0, 0, '0', 0, null, null, null, null, 2, null, getDate(), null, 2, 'Groovy', getDate(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, getDate(), null, null, null, null, null, null, null, null, null, null, null, null, null, '1NPL_4NSA_10_CC_OX_05123SU', null, null, null, null, null, null, null, null, null, 'Y', null, null, null, null, null, null, null, null, null, null, '630306533003', (Select ITEM_ID From ITEM_CBO where  ITEM_NAME  =  '630306533003'  and  COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ' ', null, null, null, null, null, null, null, null, null, null, null, null, null, '"+UniqueASNID+"', null,(select TC_ORDER_ID from ORDERS where purchase_order_number='"+tcPurchaseOrdersID+"'), (Select ORDER_ID From ORDERS where  TC_ORDER_ID  =(select TC_ORDER_ID from orders where purchase_order_number='"+tcPurchaseOrdersID+"') and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, null, null, null, null, null, null, null, null, null, null, '0', null, null, null, null, null, null, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, 0, 0, null, null, 0, null, null, null, null, getDate(), null, getDate(), '-1', null, null, null, null, null, null)"
		String SQL8="INSERT INTO LPN_DETAIL (TC_COMPANY_ID, LPN_ID, LPN_DETAIL_ID, LPN_DETAIL_STATUS, INTERNAL_ORDER_DTL_ID, TC_ORDER_LINE_ID, DISTRIBUTION_ORDER_DTL_ID, RECEIVED_QTY, BUSINESS_PARTNER_ID, ITEM_NAME, ITEM_ID, GTIN, STD_PACK_QTY, STD_SUB_PACK_QTY, STD_BUNDLE_QTY, INCUBATION_DATE, EXPIRATION_DATE, SHIP_BY_DATE, SELL_BY_DTTM, CONSUMPTION_PRIORITY_DTTM, MANUFACTURED_DTTM, CNTRY_OF_ORGN, INVENTORY_TYPE, PRODUCT_STATUS, ITEM_ATTR_1, ITEM_ATTR_2, ITEM_ATTR_3, ITEM_ATTR_4, ITEM_ATTR_5, ASN_DTL_ID, PACK_WEIGHT, ESTIMATED_WEIGHT, ESTIMATED_VOLUME, SIZE_VALUE, WEIGHT, QTY_UOM_ID, WEIGHT_UOM_ID, VOLUME_UOM_ID, ASSORT_NBR, CUT_NBR, PURCHASE_ORDERS_ID, TC_PURCHASE_ORDERS_ID, PURCHASE_ORDERS_LINE_ID, TC_PURCHASE_ORDERS_LINE_ID, HIBERNATE_VERSION, INTERNAL_ORDER_ID, INSTRTN_CODE_1, INSTRTN_CODE_2, INSTRTN_CODE_3, INSTRTN_CODE_4, INSTRTN_CODE_5, CREATED_SOURCE_TYPE, CREATED_SOURCE, CREATED_DTTM, LAST_UPDATED_SOURCE_TYPE, LAST_UPDATED_SOURCE, LAST_UPDATED_DTTM, VENDOR_ITEM_NBR, MANUFACTURED_PLANT, BATCH_NBR, ASSIGNED_QTY, PREPACK_GROUP_CODE, PACK_CODE, SHIPPED_QTY, INITIAL_QTY, QTY_CONV_FACTOR, QTY_UOM_ID_BASE, WEIGHT_UOM_ID_BASE, VOLUME_UOM_ID_BASE, HAZMAT_UOM, HAZMAT_QTY, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, REF_NUM1, REF_NUM2, REF_NUM3, REF_NUM4, REF_NUM5) VALUES ((select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ), (Select LPN_ID From LPN where  TC_LPN_ID  =  '"+"LPN_"+UniqueASNID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), LPN_DETAIL_ID_SEQ.nextval , null, null, (select tc_po_line_id from purchase_orders_line_item  where purchase_orders_id=(select purchase_orders_id from purchase_orders where tc_purchase_orders_id='"+tcPurchaseOrdersID+"')), (Select LINE_ITEM_ID From ORDER_LINE_ITEM where  ORDER_ID= (Select ORDER_ID From ORDERS where  TC_ORDER_ID  =(select TC_ORDER_ID from orders where purchase_order_number='"+tcPurchaseOrdersID+"') and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) )), null, null, '630306533003', (Select ITEM_ID From ITEM_CBO where  ITEM_NAME  =  '630306533003'  and  COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 2, null, (Select SIZE_UOM_ID From SIZE_UOM where  SIZE_UOM = 'U'  and  AUDIT_CREATED_SOURCE = 'Groovy'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, null, null, null, null, null, null, 1, null, null, null, null, null, null, 1, 'Groovy', getDate(), 1, 'Groovy', getDate(), null, null, null, null, null, null, 2, null, 1, (Select SIZE_UOM_ID From SIZE_UOM where  SIZE_UOM = 'U'  and  AUDIT_CREATED_SOURCE = 'Groovy'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)"
		String SQL9="INSERT INTO LPN_MOVEMENT (TC_COMPANY_ID, LPN_MOVEMENT_ID, SEQUENCE, TC_LPN_ID, LPN_ID, PARENT_LPN_ID, O_FACILITY_ALIAS_ID, O_FACILITY_ID, D_FACILITY_ALIAS_ID, D_FACILITY_ID, SHIPPED_DTTM, RCVD_DTTM, TC_SHIPMENT_ID, SHIPMENT_ID, TC_ASN_ID, ASN_ID, PURCHASE_ORDERS_ID, TC_PURCHASE_ORDERS_ID, TC_ORDER_ID, ORDER_ID, EXT_CREATED_DTTM, CREATED_SOURCE_TYPE, CREATED_SOURCE, CREATED_DTTM, LAST_UPDATED_SOURCE_TYPE, LAST_UPDATED_SOURCE, LAST_UPDATED_DTTM) VALUES ((select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ), LPN_MOVEMENT_ID_SEQ.nextval , null, '"+"LPN_"+UniqueASNID+"', (Select LPN_ID From LPN where  TC_LPN_ID  =  '"+"LPN_"+UniqueASNID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, '03-TN', (Select FACILITY_ID From FACILITY_ALIAS where  FACILITY_ALIAS_ID  =  '03-TN'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), '03-TN', (Select FACILITY_ID From FACILITY_ALIAS where  FACILITY_ALIAS_ID  =  '03-TN'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), getDate(), null, null, null, '"+UniqueASNID+"', (Select ASN_ID From ASN where  TC_ASN_ID  =  '"+UniqueASNID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, null, (select TC_ORDER_ID from ORDERS where purchase_order_number='"+tcPurchaseOrdersID+"'), (Select ORDER_ID From ORDERS where  TC_ORDER_ID  =  (select TC_ORDER_ID from ORDERS where purchase_order_number='"+tcPurchaseOrdersID+"')  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), null, 2, 'Groovy', getDate(), 2, 'Groovy', getDate())"
		String SQL10="INSERT INTO A_ORDERLINE_QUANTITY_STATUS (ORDERLINE_QUANTITY_STATUS_ID, PURCHASE_ORDERS_ID, PURCHASE_ORDERS_LINE_ITEM_ID, ITEM_NAME, QUANTITY, CREATED_SOURCE, CREATED_SOURCE_TYPE, CREATED_DTTM, LAST_UPDATED_SOURCE, LAST_UPDATED_SOURCE_TYPE, LAST_UPDATED_DTTM, SUBSTITUTION_TYPE, SUBSTITUTION_RATIO, ITEM_STATUS) VALUES (SEQ_ORDERLINE_QTY_STATUS_ID.nextval , (Select PURCHASE_ORDERS_ID From PURCHASE_ORDERS where  TC_PURCHASE_ORDERS_ID  =  '"+tcPurchaseOrdersID+"'  and  TC_COMPANY_ID =(select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), (select PURCHASE_ORDERS_LINE_ITEM_ID from purchase_orders_line_item where purchase_orders_id=(SELECT PURCHASE_ORDERS_ID FROM PURCHASE_ORDERS WHERE TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"')), '630306533003', 2, 'Groovy', 1, getDate(), 'Groovy', 1, getDate(), null, 1, '850')"
		String SQL11="INSERT INTO A_PAYMENT_TRANSACTION (PAYMENT_TRANSACTION_ID,EXTERNAL_PAYMENT_TRANS_ID,COMPANY_ID,PAYMENT_TRANS_TYPE,PAYMENT_DETAIL_ID,MERCH_ID,MERCH_PASSWORD,REQUESTED_AMOUNT,REQUEST_ID,REQUEST_TOKEN,REQUESTED_DTTM,FOLLOW_ON_ID,FOLLOW_ON_TOKEN,PROCESSED_AMOUNT,TRANSACTION_DTTM,TRANSACTION_EXP_DATE,NO_OF_TRIES_LEFT,RECONCILLATION_ID,TRANS_RESP_DECISION,TRANS_RESP_DECISION_DESC,EXTERNAL_RESPONSE_CODE,EXTERNAL_RESPONSE_MSG,RECORD_STATUS,PROCESS_COUNT,CREATED_SOURCE,CREATED_SOURCE_TYPE,CREATED_DTTM,LAST_UPDATED_SOURCE,LAST_UPDATED_SOURCE_TYPE,LAST_UPDATED_DTTM,IS_DELETED,COMBO_REQUESTED_ID,COMBO_REQUESTED_TOKEN,PAYMENT_PROCESS_PARAMETER,BYPASS,LAST_UPDATED_PROCESS,TRANS_RESP_DECISION_CODE,MERCH_SIGNATURE) VALUES (SEQ_PAYMENT_TRANSACTION_ID.nextval , '83' , (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ), 2, (select PAYMENT_DETAIL_ID from A_PAYMENT_DETAIL WHERE ENTITY_NUMBER='"+tcPurchaseOrdersID+"' AND COMPANY_ID=(select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"')), 'matest', 'iw16Ct5Z70KgXXikwAqHKWVayyhR3NtR+TvLSfQQh6DylikE/pSzprFiAAI+SAHvQwpSgxebUmmiCJ5mgDsB2+u+Y7JDBgAKasMWG6CFudcEo0C6DpIByVlU+WcJZ5JbFoAVyc4Izkw6GC7ksjQtVtDIlbpZiCFr5FkW1PV/xKCzUEWW0DEK9oVBOxtnG5gpZVrLKFHc21H5O8tJ9BCHoPKWKQT+lLOmsWIAAj5IAe9DClKDF5tSaaIInmaAOwHb675jskMGAApqwxYboIW51wSjQLoOkgHJWVT5ZwlnklsWgbXJzgjgbDoYLuSyNC1W0MiVulmIIWvkWRbU9X/EoA==', 600, '12345', '12345', null, '1234', '1234', 600, getDate(), getDate(), 3, null, 1, 'Successful transaction.', '100', 'Successful transaction.', 2, 0, 'Groovy', 1, getDate(), 'Groovy', 1, getDate(), 0, null, null, 1, 0, 'Transaction record updated on receiving response from external payment service', '100', null)"
		String SQL12="INSERT INTO A_INVOICE (INVOICE_ID,INVOICE_NUMBER,TC_COMPANY_ID,INVOICE_CREATION_DTTM,ENTITY_TYPE,ENTITY_ID,INVOICE_TYPE,CURRENCY,HEADER_TAX,HEADER_CHARGE,HEADER_DISCOUNT,INVOICE_AMOUNT,STATUS,SHIPMENT_NUMBER,INVOICE_AMOUNT_PROCESSED,COLLECTED_EXTERNALLY,ENTITY_NUMBER,CREATED_SOURCE,CREATED_SOURCE_TYPE,LAST_UPDATED_SOURCE,LAST_UPDATED_SOURCE_TYPE,LAST_UPDATED_DTTM,RETURN_ORDER_ID,RETURN_ORDER_NUMBER,IS_PUBLISHED) VALUES (SEQ_INVOICE_ID.nextval ,'"+UniqueInvNo+"',(select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ),getDate(),10,(Select PURCHASE_ORDERS_ID From PURCHASE_ORDERS where  TC_PURCHASE_ORDERS_ID  =  '"+tcPurchaseOrdersID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ),10,null,0,0,0,600,20,null,10,0,null,'Groovy',1,null,10,getDate(),null,null,0)"
		String SQL13="INSERT INTO A_INVOICE_LINE (INVOICE_LINE_ID,INVOICE_LINE_NUMBER,INVOICE_ID,ENTITY_LINE_NUMBER,ORDERED_ITEM_UOM_ID,ORDERED_QTY,INVOICED_QTY,UNIT_PRICE,LINE_CHARGE,LINE_DISCOUNT,LINE_TAX,CREATED_SOURCE,CREATED_SOURCE_TYPE,CREATED_DTTM,LAST_UPDATED_SOURCE,LAST_UPDATED_SOURCE_TYPE,LAST_UPDATED_DTTM,ORDER_LINE_ID,ITEM_ID,RETURN_ORDER_LINE_ID,RETURN_ORDER_LINE_NUMBER,RETURN_TYPE,IS_TAX_INCLUDED,SHIPPED_QUANTITY,SHIPPED_ITEM_ID,ORDERED_ITEM_ID,SHIPPED_ITEM_UOM_ID) VALUES (SEQ_INVOICE_LINE_ID.nextval ,'18',(Select INVOICE_ID From A_INVOICE where  INVOICE_NUMBER  =  '"+UniqueInvNo+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ),(select tc_po_line_id from purchase_orders_line_item  where purchase_orders_id=(select purchase_orders_id from purchase_orders where tc_purchase_orders_id='"+tcPurchaseOrdersID+"')),(Select SIZE_UOM_ID From SIZE_UOM where  SIZE_UOM = 'U'  and  AUDIT_CREATED_SOURCE = 'Groovy'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ),2,2,100,0,0,0,'Groovy',1,getDate(),null,10,getDate(),(SELECT PURCHASE_ORDERS_LINE_ITEM_ID FROM PURCHASE_ORDERS_LINE_ITEM WHERE TC_PO_LINE_ID  =  (select tc_po_line_id from purchase_orders_line_item  where purchase_orders_id=(select purchase_orders_id from purchase_orders where tc_purchase_orders_id='"+tcPurchaseOrdersID+"'))  AND PURCHASE_ORDERS_ID  =  (Select PURCHASE_ORDERS_ID From PURCHASE_ORDERS where  TC_PURCHASE_ORDERS_ID  =  '"+tcPurchaseOrdersID+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ) AND TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' )),null,null,null,null,0,2,null,(Select ITEM_ID From ITEM_CBO where  ITEM_NAME  =  '630306533003'  and  COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ),null)"
		String SQL14="INSERT INTO A_PT_INVOICE_MAPPING (PT_INVOICE_MAPPING_ID,PAYMENT_TRANSACTION_ID,INVOICE_ID,INVOICE_NUMBER,PT_INVOICE_AMOUNT) VALUES (A_PT_INVOICE_MAPPING_SEQ.nextval , (SELECT PAYMENT_TRANSACTION_ID FROM A_PAYMENT_TRANSACTION WHERE EXTERNAL_PAYMENT_TRANS_ID = null AND COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' )), (Select INVOICE_ID From A_INVOICE where  INVOICE_NUMBER  =  '"+UniqueInvNo+"'  and  TC_COMPANY_ID = (select TC_COMPANY_ID from PURCHASE_ORDERS where TC_PURCHASE_ORDERS_ID='"+tcPurchaseOrdersID+"' ) ), '"+UniqueInvNo+"', 600)"
		
		
		def row1=dbExecuteUpdateQuery(SQL1)
		Assert.assertEquals(row1,1,SQL1+ "----->> UPDATE FAILED" );
		
		def row2=dbExecuteUpdateQuery(SQL2)
		Assert.assertEquals(row2,1,SQL2+ "----->> UPDATE FAILED" );
		
		def row3=dbExecuteUpdateQuery(SQL3)
		Assert.assertEquals(row3,1,SQL3+ "----->> UPDATE FAILED" );
		
		def row4=dbExecuteUpdateQuery(SQL4)
		Assert.assertEquals(row4,1,SQL4+ "----->> UPDATE FAILED" );
		
		def row5=dbExecuteUpdateQuery(SQL5)
		Assert.assertEquals(row5,1,SQL5+ "----->> UPDATE FAILED" );
		
		def row6=dbExecuteUpdateQuery(SQL6)
		Assert.assertEquals(row6,1,SQL6+ "----->> UPDATE FAILED" );
		
		def row7=dbExecuteUpdateQuery(SQL7)
		Assert.assertEquals(row7,1,SQL7+ "----->> UPDATE FAILED" );
		
		def row8=dbExecuteUpdateQuery(SQL8)
		Assert.assertEquals(row8,1,SQL8+ "----->> UPDATE FAILED" );
		
		def row9=dbExecuteUpdateQuery(SQL9)
		Assert.assertEquals(row9,1,SQL9+ "----->> UPDATE FAILED" );
		
		def row10=dbExecuteUpdateQuery(SQL10)
		Assert.assertEquals(row10,1,SQL10+"----->> UPDATE FAILED" );
		
		def row11=dbExecuteUpdateQuery(SQL11)
		Assert.assertEquals(row11,1,SQL11+"----->> UPDATE FAILED" );
		
		def row12=dbExecuteUpdateQuery(SQL12)
		Assert.assertEquals(row12,1,SQL12+"----->> UPDATE FAILED" );
		
		def row13=dbExecuteUpdateQuery(SQL13)
		Assert.assertEquals(row13,1,SQL13+"----->> UPDATE FAILED" );
		
		def row14=dbExecuteUpdateQuery(SQL14)
		Assert.assertEquals(row14,1,SQL14+"----->> UPDATE FAILED" );
		
	}
	
	
	//Ashwin 28-09-2015
	public void dbCheckReturnsItem(String saleBrcd, String returnBard){
		   
		   String cgbransID= dbcheckCgbxn(saleBrcd);
		   
		   def SQLpurchaseOrdersID="Select purchase_orders_id,MONETARY_VALUE from purchase_orders where TC_purchase_orders_id='"+cgbransID+"'"
	
		   def ListSQLpurchaseOrdersID=dbExecuteQuery(SQLpurchaseOrdersID)
		   def purchaseOrdersID=ListSQLpurchaseOrdersID[0][0];
		   def monetaryValue=ListSQLpurchaseOrdersID[0][1];
		   
		   def SQLreturnPurchaseOrderID="select parent_purchase_orders_id from purchase_orders where EXT_PURCHASE_ORDERS_ID='"+returnBard+"'"
		   def ListSQLretpurchaseOrdersID=dbExecuteQuery(SQLreturnPurchaseOrderID)
		   def retPurchaseOrdersID=ListSQLretpurchaseOrdersID[0][0];
		   
//		   //def SQLreturnParentPurchaseOrdersID="select parent_purchase_orders_id from purchase_orders where purchase_orders_id='"+retPurchaseOrdersID+"';"
//		   //def ListSQLreturnParentPurchaseOrdersID=dbExecuteQuery(SQLreturnParentPurchaseOrdersID);
//		   //def returnParentPurchaseOrdersID=ListSQLreturnParentPurchaseOrdersID[0][0];
		   
		   if(purchaseOrdersID.toString()==retPurchaseOrdersID.toString()){
				  println "Return is a success";
		   }
		   else{
				  Assert.fail("Return did ngb proceed");
		   }
	}
	
	
	
	
	public String getOnhandInventoryDetails(def itemName="",def facilityid="")
	
		   {
	
				  String quantity
	
				  Properties dbprop=new Properties();
				  URL url = ClassLoader.getSystemResource("testdata/properties/gb.properties");
				  dbprop.load(url.openStream());
	
				  //FileInputStream fs1=new FileInputStream(System.getProperty("user.dir")+"/resources/testdata/properties/SIF.properties");
				  //dbprop.load(fs1);

				
				  def COMPANY_ID=dbprop.getProperty("COMPANY_ID")
				  def ItemName = itemName
				  def Facility_ID = facilityid
	
				  List DBItemTable
				  List DBFacilityTable
				  List DBInvDetails
	
				  String AVAILABLE_QUANTITY
				  String AVAILABLESOON_QUANTITY
				  String UNAVAILABLE_QUANTITY
				  def FinalQty
	
				  String DBGetItemID = "select SKU_ID from sku where sku = '"+ItemName+"' and tc_company_id='"+COMPANY_ID+"'"
				  String DBGetFacilityID = "select FACILITY_ID from facility_alias where facility_alias_id like '%"+Facility_ID+"%' and tc_company_id='"+COMPANY_ID+"'"
	
				  Connection conn = getDOMConnection()
	
				  DBItemTable = SQLExecuteSupport.executeSelectQuery(DBGetItemID, conn)
				  String Item_Id = DBItemTable.get(0).values().toString().substring(1, DBItemTable.get(0).values().toString().length()-1)
	
	
				  DBFacilityTable = SQLExecuteSupport.executeSelectQuery(DBGetFacilityID, conn)
				  String Facility_Id = DBFacilityTable.get(0).values().toString().substring(1, DBFacilityTable.get(0).values().toString().length()-1)
	
				  //16653 for CI
				  //16661 for QA
				  String DBGetAvailableQuantity = "select available_quantity from i_perpetual where object_type_id= 16661 and tc_company_id = "+COMPANY_ID+" and item_id = "+Item_Id+" and facility_id = "+Facility_Id+""
				  println DBGetAvailableQuantity
	
				  String DBGetAvailableSoonQuantity = "select available_soon_quantity from i_perpetual where object_type_id= 16661 and tc_company_id = "+COMPANY_ID+" and item_id = "+Item_Id+" and facility_id = "+Facility_Id+""
				  println DBGetAvailableSoonQuantity
	
	
				  String DBGetUnAvailableQuantity = "select unavailable_quantity from i_perpetual where object_type_id= 16661 and tc_company_id = "+COMPANY_ID+" and item_id = "+Item_Id+" and facility_id = "+Facility_Id+""
				  println DBGetUnAvailableQuantity
	
	
				  //======AVAILABLE_QUANTITY=====
	
				  DBInvDetails = SQLExecuteSupport.executeSelectQuery(DBGetAvailableQuantity, conn)
		   
				  if(DBInvDetails.size()<=0 || DBInvDetails.get(0).values().toString().indexOf("null")>0){
						 AVAILABLE_QUANTITY = "0"
						 println AVAILABLE_QUANTITY
				  }else{
						 AVAILABLE_QUANTITY = DBInvDetails.get(0).values().toString().substring(1,DBInvDetails.get(0).values().toString().length()-1)
						 println AVAILABLE_QUANTITY
				  }
	
				  //======AVAILABLESOON_QUANTITY=====
	
				  DBInvDetails = SQLExecuteSupport.executeSelectQuery(DBGetAvailableSoonQuantity, conn)
				  if(DBInvDetails.size()<=0 || DBInvDetails.get(0).values().toString().indexOf("null")>0){
						 AVAILABLESOON_QUANTITY = "0"
						 println AVAILABLESOON_QUANTITY
				  }else{
						 AVAILABLESOON_QUANTITY = DBInvDetails.get(0).values().toString().substring(1,DBInvDetails.get(0).values().toString().length()-1)
						 println AVAILABLESOON_QUANTITY
				  }
	
				  //======UNAVAILABLE_QUANTITY=====
				  DBInvDetails = SQLExecuteSupport.executeSelectQuery(DBGetUnAvailableQuantity, conn)
				  if(DBInvDetails.size()<=0 || DBInvDetails.get(0).values().toString().indexOf("null")>0){
						 UNAVAILABLE_QUANTITY = "0"
						 println UNAVAILABLE_QUANTITY
				  }else{
						 UNAVAILABLE_QUANTITY = DBInvDetails.get(0).values().toString().substring(1,DBInvDetails.get(0).values().toString().length()-1)
						 println UNAVAILABLE_QUANTITY
				  }
	
	
				  FinalQty= Integer.valueOf(AVAILABLE_QUANTITY)+Integer.valueOf(AVAILABLESOON_QUANTITY)+Integer.valueOf(UNAVAILABLE_QUANTITY)
	
				  println FinalQty
	
				  return FinalQty
				  
	
		   }
	
	
	 public String getDONumber(String inPOSTxnID){
		 String doid
		 Connection con=getDOMConnection()
		 String tcPurchaseOrdersID=dbcheckCgbxn(inPOSTxnID)
		 println("Order id is "+tcPurchaseOrdersID)
		 PreparedStatement stm=con.prepareStatement("SELECT TC_ORDER_ID FROM ORDERS WHERE PURCHASE_ORDER_NUMBER=?")
		 stm.setString(1,tcPurchaseOrdersID)
		 for (int i=0;i<=20;i++) {
			 ResultSet rs=stm.executeQuery()
			 while(rs.next()){
				 doid=rs.getString("TC_ORDER_ID")
				 println("doid is ======= "+doid)
			 }
			 if(doid!=null)
				 break;
			 
				 sleep(2000)
				 
		}
		
		 
		 closeConnection(con)
		 return doid
	 }
	
	public ArrayList<String> getOrderIDs(String inPOSTxnID){
		Connection con=getDOMConnection()
		try {
			
		println("Transaction id is "+inPOSTxnID)
		PreparedStatement stm=con.prepareStatement("select order_id from orders where ext_purchase_order=?");
		ArrayList<String>al=new ArrayList<String>()
		ResultSet rs=null
		for(int i=0;i<=20;i++){
			
		
		stm.setString(1,inPOSTxnID);
		rs=stm.executeQuery();
		while(rs.next()){
			   al.add(rs.getString("ORDER_ID"));
		}
		
		println("order id size "+al.size())
		if(al.size()>0)
		break;
		
		
		sleep(2000)
		
		
		}
		return al
		
		} catch (Exception e) {
			e.printStackTrace()
		}
		finally{
			closeConnection(con)
		}
	}
	
	public HashMap<String, String> getOrderDetails(String Orderid,String lineid){
		Connection con=getDOMConnection()
		PreparedStatement getDONumFacilityIDSQL=con.prepareStatement("select tc_order_id,o_facility_alias_id from orders where order_id=?");
		PreparedStatement getLineItemQtySQL=con.prepareStatement("select tc_order_line_id,customer_item,order_qty from order_line_item where order_id=?");
		PreparedStatement getLineItemQtySQLline=con.prepareStatement("select tc_order_line_id,customer_item,order_qty from order_line_item where order_id=? and TC_ORDER_LINE_ID=?");
		
		HashMap<String, String> hm=new HashMap<String,String>()
		ResultSet rs=null
		try {
			
			getDONumFacilityIDSQL.setString(1, Orderid);
			rs=getDONumFacilityIDSQL.executeQuery();
			while(rs.next()){
				  hm.put("ORDER_ID", Orderid);
				  hm.put("DistID", rs.getString("TC_ORDER_ID"));
				  hm.put("facilityid", rs.getString("O_FACILITY_ALIAS_ID"));
				  println(hm)
				}

			
				getLineItemQtySQLline.setString(1, Orderid);
				getLineItemQtySQLline.setString(2, lineid);
				
			rs=getLineItemQtySQLline.executeQuery();
			while(rs.next()){
				  hm.put("DOLINENUMBER", rs.getString("TC_ORDER_LINE_ID"));
				  hm.put("skuid", rs.getString("CUSTOMER_ITEM"));
				  hm.put("quantity", rs.getString("ORDER_QTY"));
				  println(hm)

			}
			
			return hm
		} catch (Exception e) {
			e.printStackTrace()
		}finally{
		closeConnection(con)
		}
		
		
	}
	
	public ArrayList<String> getDOLines(String Orderid){
		Connection con=getDOMConnection()
		ArrayList<String> llist=new ArrayList<String>()
		PreparedStatement getLineItemQtySQL=con.prepareStatement("select tc_order_line_id,customer_item,order_qty from order_line_item where order_id=?");
		ResultSet rs
		getLineItemQtySQL.setString(1, Orderid);
		rs=getLineItemQtySQL.executeQuery();
		
		while(rs.next()){
			
			llist.add(rs.getString("tc_order_line_id"))
			println(llist)

	  }
		return llist
	}
	
	public String getOrderStatus(String inPOSTxnID){
		String coid=dbcheckCgbxn(inPOSTxnID)
		Connection con=getDOMConnection()
		PreparedStatement stm=con.prepareStatement("select toi.purchase_orders_status,DESCRIPTION from purchase_orders toi,purchase_orders_status pos where toi.purchase_orders_status=pos.purchase_orders_status and tc_purchase_orders_id=?")
		ResultSet rs
		String status
		try {
			stm.setString(1, coid)
			rs=stm.executeQuery()
			while(rs.next()){
				status=rs.getString("DESCRIPTION")
			}
			return status
		} catch (Exception e) {
			e.printStackTrace()
		}finally{
		closeConnection(con)
		}
		
	}
	
	public void runPaymentAgent(){
		Connection con=getDOMConnection()
		//PreparedStatement stm=con.prepareStatement("update om_sched_event set scheduled_dttm = sysdate,event_exp_date=null where event_id IN (select wf_batch_scheduler_event_id from wf_batch_scheduler_event where wf_batch_schedule_id in( select ws.wf_batch_schedule_id from wf_batch_schedule ws where upper(ws.batch_schedule_hql) like'%PRAVAS%'  AND upper(ws.batch_schedule_hql) like'%KUMAR%'))");
	  PreparedStatement stm=con.prepareStatement("update om_sched_event set scheduled_dttm = sysdate,event_exp_date=null where event_id = (select om_sched_event_id from wf_batch_scheduler_event where wf_batch_schedule_id= (select wf_batch_schedule_id from wf_batch_schedule where tc_company_id=87 and batch_schedule_hql like '%Pravas%'))")
		//PreparedStatement stm2=con.prepareStatement("commit");
		try {
			println "Run Payment Agent"
			stm.executeUpdate()
			//stm2.execute();
			//println stm2;
		} catch (Exception e) {
			e.printStackTrace()
		}finally{
		closeConnection(con)
		}
		
	}
	
	public int checkVasCO(String barcode){
		Connection conn = getDOMConnection()
		PreparedStatement stm= conn.prepareStatement("select purchase_orders_id from purchase_orders where EXT_PURCHASE_ORDERS_ID=?");
		PreparedStatement stm2= conn.prepareStatement("select charge_amount from a_charge_detail where entity_id=? and charge_category='110' and mark_for_deletion='0'");
		String entity_id=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		stm.setString(1,barcode);
		
		for(int i=0;i<=20;i++){
			println("in loop 1")
			Thread.sleep(15000)
			rs=stm.executeQuery();
			while(rs.next()){
			String camt=rs.getString("PURCHASE_ORDERS_ID");
			if (!camt.equalsIgnoreCase(null))
			break;
			
			}
			break;
		}
		
		rs=stm.executeQuery();
		while(rs.next()){
			entity_id=rs.getString("PURCHASE_ORDERS_ID");
		}
		ArrayList<Integer> al= new ArrayList<Integer>();
		stm2.setString(1,entity_id );
		
		for(int i=0;i<=20;i++){
			println("in loop 2")
			Thread.sleep(15000)
			rs2=stm2.executeQuery();
			while(rs2.next()){
			int camt=rs2.getInt("CHARGE_AMOUNT")
			println camt
			if (camt>0)
			break;
			}
			
			break;
		}
		rs2=stm2.executeQuery();
			while(rs2.next()){
				al.add(rs2.getInt("CHARGE_AMOUNT"));
			}
			int sum=0;
		for(int i=0;i<al.size();i++){
			sum+=al.get(i);
		}
		return sum;
	}
	
	
	public int checkVasPOS(String barcode){
		
		Connection conn =getDOMConnection()
		PreparedStatement stm= conn.prepareStatement("select a_pos_transaction_id from a_pos_transaction where special_order_number=?");
		PreparedStatement stm2= conn.prepareStatement("select charge_amount from a_pos_charge_detail where entity_id=? and charge_category_id='90'");
		String entity_id=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		stm.setString(1,barcode);
		
		
		for(int i=0;i<=20;i++){
			println("in loop 1")
			Thread.sleep(5000)
			rs=stm.executeQuery();
			while(rs.next()){
			String camt=rs.getString("A_POS_TRANSACTION_ID");
			if (!camt.equalsIgnoreCase(null))
			break;
			
			}
		}
		rs=stm.executeQuery();
		while(rs.next()){
			entity_id=rs.getString("A_POS_TRANSACTION_ID");
			println("Event id is "+ entity_id)
		}
		ArrayList<Integer> al= new ArrayList<Integer>();
		stm2.setString(1,entity_id );
		
		
		for(int i=0;i<=20;i++){
			println("in loop 2")
			Thread.sleep(5000)
			rs2=stm2.executeQuery();
			while(rs2.next()){
			int camt=rs2.getInt("CHARGE_AMOUNT")
			println camt
			if (camt>0){
			break;
			}
			
		}
			}
		rs2=stm2.executeQuery();
			while(rs2.next()){
				int camt=rs2.getInt("CHARGE_AMOUNT")
				println camt
				al.add(camt);
			}
			int sum=0;
		for(int i=0;i<al.size();i++){
			sum+=al.get(i);
		}
		println sum;
		return sum;
	}
	
	public String getPaymentStatus(String inPOSTxnID){
		Stopwatch stp=new Stopwatch()
		stp.start()
		String coid=dbcheckCgbxn(inPOSTxnID)
		println("CO number is "+coid)
		Connection con=getDOMConnection()
		PreparedStatement stm=con.prepareStatement("select status from a_invoice where entity_number=? and ROWNUM=1");
		ResultSet rs
		String status
		try {
			for(int i=0;i<=20;i++){
				
			
			stm.setString(1, coid)
			rs=stm.executeQuery()
			while(rs.next()){
				status=rs.getString("status")
			}
			
			println("Payment Status is "+status)
			if(status.equalsIgnoreCase("20"))
			break;
			
			sleep(20000);
			
			
			}
			stp.stop()
			return status
		} catch (Exception e) {
			e.printStackTrace()
		}finally{
		println("Time Taken to update payment status is "+stp)
		closeConnection(con)
		}
	}
	
	
	public def dbExecuteQuery(String query){
		Connection conn =getDOMConnection()
		def DBItemTable
		for (int x=0 ;x<=50;x++)
		{
			DBItemTable = SQLExecuteSupport.executeSelectQuery(query, conn)
			if (DBItemTable.size()==0){
				Thread.sleep(3000)
			}
			else
			{
				break
			}
		}
		
				
		return DBItemTable
		conn.close();
	}
	
	
	

	
	public String dbcheckCgbxn(String inPOSTxnID){
		
		String Cgbxn="select TC_PURCHASE_ORDERS_ID from purchase_orders where EXT_PURCHASE_ORDERS_ID='"+inPOSTxnID+"'"
		Thread.sleep(2000)
		boolean rc
		def ListCgbxn=dbExecuteQuery(Cgbxn)
		def dbtc_Purchse_ordes_id=ListCgbxn[0][0]
		if (dbtc_Purchse_ordes_id==""){
			rc=false
			Assert.assertEquals(rc,true,"TC_PURCHASE_ORDERS_ID is ngb found for transaction id: "+inPOSTxnID );
		}
		else
		{
			rc=true
		}
		return dbtc_Purchse_ordes_id.toString()
	}
	
	
	public def dbExecuteUpdateQuery(String query){
		
		Connection conn = getDOMConnection()
		def DBrow = SQLExecuteSupport.executeUpdate(query, conn)
//		try {
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace()
//		}
		
		return DBrow
		
	}
	
	
	public def dbDeleteQuery(String query){
		
		Connection conn = getDOMConnection()
		def DBrow = SQLExecuteSupport.executeUpdate(query, conn)
		
		conn.close();
		return DBrow
		
	}
	public String generateID(String prifix){
		
		Random rand = new Random();
		int max=99999
		int min=11111
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return prifix+randomNum.toString()
		
	}
	
	public String generateUniqueASN(){
		generateID("ASN")
	}
	
	public String generateUniqueINV(){
		generateID("INV")
	
	}
	public String generateUniqueDO(){
		generateID("DO")
	
	}
	
	
	//=====================================================gb DB Setup=======================================================================
	
	
	
	public Connection getgbConnection(){
		
		
		def DB_TYPE=gbProperties.getProperty("DB_TYPE_gb")
		def JDBC_URL
		def DB_USER
		def DB_PASSWORD
		

		if (DB_TYPE.equalsIgnoreCase("Oracle"))
		{
			Class.forName("oracle.jdbc.OracleDriver");
			
			JDBC_URL=gbProperties.getProperty("JDBC_URL_gb_ora")
			DB_USER=gbProperties.getProperty("DB_USER_gb_ora")
			DB_PASSWORD=gbProperties.getProperty("DB_PASSWORD_gb_ora")
		}
		else if(DB_TYPE.equalsIgnoreCase("Sqlserver"))
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			JDBC_URL=gbProperties.getProperty("JDBC_URL_gb_sql")
			DB_USER=gbProperties.getProperty("DB_USER_gb_sql")
			DB_PASSWORD=gbProperties.getProperty("DB_PASSWORD_gb_sql")
		}
		else if(DB_TYPE.equalsIgnoreCase("DB2"))
		{

			Class.forName("com.ibm.db2.jcc.DB2Driver");
		}
		
		Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
		return conn
	}
	
	public void closeConnection(Connection Con){
		try {
			Con.close()
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
	
	public def dbExecuteQueryDefaultConn(String query){
			use(SQLExecuteSupport){
			def resultListDB=query.executeSelectQuery()
			return resultListDB
		}
	}
	
	
	public def dbDeleteQuery_gb(String query){
		
		Connection conn = getgbConnection()
		def DBrow = SQLExecuteSupport.executeUpdate(query, conn)
		conn.close();
		return DBrow
		
	}
	
	
	public def dbExecuteQuery_gb(String query){
		Connection conn =getgbConnection()
		def DBItemTable
		for (int x=0 ;x<=50;x++)
		{
			DBItemTable = SQLExecuteSupport.executeSelectQuery(query, conn)
			if (DBItemTable.size()==0){
				Thread.sleep(3000)
			}
			else
			{
				break
			}
		}
		
				
		return DBItemTable
		conn.close();
	}
	
	public String dbGetObjectID_gb(String inTxnID){
		String ObjID="select objectid from postrtransactions where barcode='"+inTxnID+"'"
		Thread.sleep(2000)
		def ListObjID=dbExecuteQuery_gb(ObjID)
		def dbObjID=ListObjID[0][0]
	
		return dbObjID.toString()
		
	}
	
	public void dbVerifyPostVoidTxn_gb(String inPostVoidTxnID, String inTxnID){
		
		boolean rc
		String SQL1="select ORIGINALTRANSACTIONOBJECTID from postrtransactions where barcode='"+inPostVoidTxnID+"'"
				
		def ListOrigObjID=dbExecuteQuery_gb(SQL1)
		String objID=ListOrigObjID[0][0].toString()
		String SQL2="select barcode from postrtransactions where objectid='"+objID+"'"
		
		def Listbrcd=dbExecuteQuery_gb(SQL2)
		String barcode=Listbrcd[0][0].toString()
		if (barcode==inTxnID){
			rc=true
		}
		else
		{
			rc=false
		}
		
		Assert.assertEquals(rc,true,"Post Void ngb happen correctly" );
		
	}
	
	public String dbGetTokenGenerator_gb(int clientID,String userName){
		String TokenGenSQL="select userpassword,apikey from ssusers where clientid="+clientID+" and username='"+userName+"'"
		Thread.sleep(2000)
		def ListTokenGen=dbExecuteQuery_gb(TokenGenSQL)
		String userpassword=ListTokenGen[0][0].toString()
		String apiKey=ListTokenGen[0][1].toString()
	
		String tokenGen="{\"apikey\" : \""+ apiKey +"\", \"uid\":\""+ apiKey +"\",\"pwd\" : \""+userpassword+"\"}"
		return tokenGen
		
	}
	
	public String dbVerifyTxn_gb(String inTxnID){
		String ObjID="select barcode from postrtransactions where barcode='"+inTxnID+"'"
		Thread.sleep(2000)
		def ListObjID=dbExecuteQuery_gb(ObjID)
		def dbObjID=ListObjID[0][0]
		boolean rc
		return 
		if (dbObjID.toString() ==inTxnID){
			rc=true			
		}
		else
		{
			rc=false
		}
		Assert.assertEquals(rc,true,"Barcode ngb found" );
	}
	
	
	public void dbDeleteTLogFromgb(){
		boolean rc
		Properties dbprop=new Properties();

				URL url = ClassLoader.getSystemResource("testdata/properties/Env.properties");
		dbprop.load(url.openStream());

		def DEVICE_ID=dbprop.getProperty("deviceID")
		def CLIENT_ID=dbprop.getProperty("ClientID")
		String SQL1="delete from POSTRTRANSACTIONS where clientid="+CLIENT_ID+" and deviceid='"+DEVICE_ID+"'"
		try {
			def row1=dbDeleteQuery_gb(SQL1)
		} catch (Exception e) {
			e.printStackTrace()
		}
		
//		//if (row1>=1){
//			//rc=true
//		//}
//		//else
//		//{
//			//rc=false
//		//}
//		//Assert.assertEquals(rc,true,SQL1+ "----->> DELETE FAILED" );
		
				
	}
	
	public String getPostProcessingStatus(String barcode){
		Stopwatch stp=new Stopwatch()
		stp.start()
		Connection con=getgbConnection()
		PreparedStatement stm=con.prepareStatement("select * from postrtranspostprocessqueue where objectid in (select objectid from postrtransactions where barcode=?)")
		ResultSet rs
		String status ="failure"
		stm.setString(1,barcode)
		for(int i=0;i<20;i++){
			rs=stm.executeQuery()
			String val=""
			while(rs.next()){
				val=rs.getString("objectid")
			}
			if(val==""){
				status="success"
				println("Transaction Processed Successfully")
				break
			}else{
				status ="failure"
			}
			sleep(15000)
			println("Transaction still is post processing queue")
		}
		
		stp.stop()
		println("Time Take to process Transaction from post processing queue is  "+stp)
		
		org.testng.Reporter.log("Time Take to process Transaction from post processing queue is  "+stp)
		con.close()
		return status
	}
	
	public void cleanPostProcessingQueue(){
		Connection con=getgbConnection()
		PreparedStatement stm=con.prepareStatement("delete from postrtranspostprocessqueue")
		int rows=stm.executeUpdate()
		
		con.close()
		
	}

    public void checkTillStatus(String clientid,String tillid){
		Connection con=getgbConnection()
		ResultSet rs
		String tillstatus=""
		PreparedStatement stm=con.prepareStatement("select * from postills where clientid=? and tillid=?")
		PreparedStatement stm2=con.prepareStatement("update postills set state='AVAILABLE' where clientid=? and tillid=? ")
		
		stm.setString(1, clientid)
		stm.setString(2, tillid)
		rs=stm.executeQuery()
		while(rs.next()){
			   tillstatus=rs.getString("state")
			   
		}
		
		if(!tillstatus.equalsIgnoreCase("AVAILABLE")){
			   
			   stm2.setString(1, clientid)
			   stm2.setString(2, tillid)
			   stm2.executeUpdate()
		}
		
 }

   	public String dbCheckReturnFee(String sql){
		try{
			   Connection con=getDOMConnection();
			   PreparedStatement stm1= con.prepareStatement(sql);
			   ResultSet rs=null;
			   rs=stm1.executeQuery();
			   String amount=null;
			   while(rs.next){
					 amount=rs.getString("CHARGE_AMOUNT")
					 if(amount!=null){
							return amount
					 }
					 else{
							return "0"
					 }
			   }
			   con.close();
		}
		catch(Exception e){
			   e.printStackTrace();
		}
   

}
}