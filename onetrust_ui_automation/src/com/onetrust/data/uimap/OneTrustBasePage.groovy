package com.onetrust.data.uimap;

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

public class OneTrustBasePage {


//	WebDriver driver;
//
//
//	By OTCompany_LNK = By.xpath("//*[@id='navigation']/ul/li[6]/a");
//	By OTCareer_LNK = By.xpath("//*[@id='navigation']/ul/li[6]/ul/li[1]/a");
//	By Citieslist_LNK = By.xpath("//ul[@class='nav nav-tabs' and @role='tablist']/li");
//	By CitySpecific_LNK = By.xpath("//ul[@class='nav nav-tabs' and @role='tablist']");
//
//	public OneTrustBasePage(WebDriver driver)
//	{
//
//		this.driver = driver;
//	}
//
//
//	//Click on CompanyLink
//	public void clickCompanyCarreerLink(){
//
//		actionClickByXpath(OTCompany_LNK,OTCareer_LNK)
//
//	}
//	
//	public void actionClickByXpath(String xpathMainMenu,String xpathSubMenu){
//		Actions action = new Actions(driver);
//		WebElement mainMenu = driver.findElement(OTCompany_LNK);
//		action.moveToElement(mainMenu).moveToElement(driver.findElement(OTCareer_LNK)).click().build().perform();
//	}
	

  /**
	 * Home Page
	 */
	public static final String OTCompany_LNK="//*[@id='navigation']/ul/li[6]/a"
	public static final String OTCareer_LNK="//*[@id='navigation']/ul/li[6]/ul/li[1]/a"

	/*
	 * Career Page objects :
	 */

	public static final String Citieslist_LNK="//ul[@class='nav nav-tabs' and @role='tablist']/li"
	public static final String CitySpecific_LNK="//ul[@class='nav nav-tabs' and @role='tablist']"
	public static final String JobsTitleRowCount_TXT="//*[@id='##']/div[@class='container-block']/div[@class='row']"
	public static final String JobsRowAndTitle_TXT="//*[@id='##']/div[@class='container-block']"



	public static final String logoutdropmenu="//nav[@id='top-bar']/ul[2]/li[2]/a"
	public static final String logoutbtn="//nav[@id='top-bar']/ul[2]/li[2]/ul/li[5]/a"
	public static final String LoginHeader="//div[@id='login']/h3"
	//	public static String Tab_StoreSelection="//ul[@id='main-nav']/li[3]/a/i[@class='fa activityIcon_store']"
	public static final String BO_SCREENTITLE="screenTitle"
	public static String Tab_StoreSelection="//li[@id='menu_act_store']/a"
	public static final String Tab_StoreSelectionid="menu_act_store"
	public static final String BO_STORETAB ="//li[@id='menu_act_store']/a"
	public static final String BO_STARTTHEDAY="//li[@id='menu_act_startOfDay']/a"
	public static final String BO_ENDOFDAY="//li[@id='menu_act_endOfDay']/a"
	public static final String BO_STORELOGEDIN=".//*[@id='top-bar']/ul[2]/li[2]/a"
	public static final String BO_STORELOGOUT=".//*[@id='top-bar']/ul[2]/li[2]/ul/li[5]/a"
	public static final String BO_TOTALAMT="store_totalAmount"
	public static final String BO_STARTDAY=".//*[@id='startAction']"
	public static final String BO_CLOSEBTN=".//*[@id='systemModalDialog']/div/div/div[4]/button"
	public static final String BO_DASHBOARDHEADER=".//*[@id='content-header']/h1"
	public static final String BO_SEARCH=".//*[@id='menu_act_search']/a"
	public static final String BO_SETTINGS="menu_act_settings"
	public static final String BO_APPLICATIONSETTINGS="menu_act_applicationSettings"
	public static final String BO_STARTMSG=".//*[@id='systemModalDialog']/div/div/div[2]"
	public static final String BO_OPENTILLTAB="menu_act_openTill"
	public static final String BO_CLOSETILLTAB="menu_act_closeTill"
	public static final String BO_RECONCILETILLTAB="menu_act_reconcileTill"
	public static final String BO_BANKDEPOSIT="menu_act_bankDeposit"
	public static final String BO_CLOSEBUTTON="//div[@id='systemModalDialog']/div/div/div[4]/button[contains(text(),'Close')]"
	public static final String BO_CONFIRMALERT="//div[@id='systemModalDialog']//button[text()='Confirm']"
	public static final String BO_REENTERTALERT="//div[@id='systemModalDialog']//button[text()='Re-enter']"
	public static final String BO_CLOSESTOREBUTTON="closeStore"
	public static final String BO_DISENGAGETILLTAB="menu_act_disengageTill"
	public static final String BO_EMPLOYEETAB="//li[@id='menu_act_employees']/a"
	public static final String BO_ADDEMPLOYEE="menu_act_addEmployee"
	public static final String BO_SEARCHTEXTBOX="commonSearchText"
	public static final String BO_SEARCHPAGEHEAD="//div[@id='content-header']//small"
	public static final String BO_VIEWEMPLOYEETAB="//li[@id='menu_act_viewEmployee']/a"
	public static final String BO_PERMISSIONMANAGEMENT="menu_act_permissionManagement"
	public static final String BO_NAVUSERROLE="navUserRole"
	public static final String BO_REPORTSTAB="menu_act_reports"
	public static final String BO_STORESTATUS="navStoreStatus"
	public static final String BO_CANCELACTION="cancelAction"
	public static final String BO_DASHBOARD="menu_act_dashboard"
	public static final String BO_ALERTDIALOG="systemModalDialog"
	public static final String BO_ALERTMESSAGE="//div[@id='systemModalDialog']/div/div/div[2]"
	public static final String BO_STARTDAY_BTN="//button[@id='startAction' and contains(text(),'Start Day')]"
	/*
	 * 	Application Settings Page objects
	 */
	public static final String BO_STOREOPENCOUNTINGMETHOD_INPUT="storeOpenCountingMethod_input"
	public static final String BO_STORECLOSECOUNTINGMETHOD_INPUT="storeCloseCountingMethod_input"
	public static final String BO_storeOpenCountingBlind_input="//label[input[@id='storeOpenCountingBlind_input' and @value='YES']]"
	public static final String BO_storeOpenCountingBlind_input_NO="//label[input[@id='storeOpenCountingBlind_input' and @value='NO']]"
	public static final String BO_DEFAULTSTOREFLOAT="//a[@id='storeDefaultFloat_input_storeDefaultFloat_list_0']/input[@type='text']"
	public static final String BO_SAVEAPPSETTING="//button[@id='saveSettings']"
	public static final String BO_CLOSE="//button[text()='Close']"
	public static final String BO_TILLOPENCOUNTINGMETHOD_INPUT="tillOpenCountingMethod_input"
	public static final String BO_TILLOPENCOUNTINGBLIND_INPUT_YES="//label[input[@id='tillOpenCountingBlind_input' and @value='YES']]"
	public static final String BO_TILLOPENCOUNTINGBLIND_INPUT_NO="//label[input[@id='tillOpenCountingBlind_input' and @value='NO']]"
	public static final String BO_DEFAULTTILLFLOAT="//a[@id='tillDefaultFloat_input_tillDefaultFloat_list_0']/input[@type='text']"
	public static final String BO_TILLRECONCILECOUNTINGMETHOD_INPUT="tillReconcileCountingMethod_input"
	public static final String BO_TILLRECONCILEFLOATCOUNTINGMETHOD_INPUT="tillReconcileFloatCountingMethod_input"
	public static final String BO_STORECOUNTBLIND="//label[input[@id='storeCloseCountingBlind_input' and @value='YES']]"
	public static final String BO_STORECOUNTBLIND_NO="//label[input[@id='storeCloseCountingBlind_input' and @value='NO']]"
	public static final String BO_STOREAUTOCLOSEYES="//input[@id='storeAutoCloseEnabled_input' and @value='YES']"
	public static final String BO_STOREAUTOCLOSENO="//input[@id='storeAutoCloseEnabled_input' and @value='NO']"
	public static final String BO_AUTOMATICSTORECLOSETIME="//input[@id='storeAutoCloseTimestamp_input']"
	public static final String BO_RECONCILABLETENDERHEADRE="//div[@id='settingUIContainer_reconcilableTenderTypes']/h5"
	public static final String BO_RECONSILECASH="reconcilableTenderTypes_input_reconcilableTenderTypes_input_0"
	public static final String BO_RECONSILETRAV="reconcilableTenderTypes_input_reconcilableTenderTypes_input_1"
	public static final String BO_RECONSILEPRISIONAL="reconcilableTenderTypes_input_reconcilableTenderTypes_input_2"
	public static final String BO_BANKDEPOSITCOUNTMETHOD="storeDepositCountingMethod_input"
	public static final String BO_BLINDRECONCILENO="tillReconcileCountingBlind_input_NO"
	public static final String BO_BLINDRECONCILEYES="tillReconcileCountingBlind_input_YES"
	public static final String BO_COMBINEDRECONCILEYES="tillCombineReconcileAmount_input_YES"
	public static final String BO_COMBINEDRECONCILENO="tillCombineReconcileAmount_input_NO"


	/*
	 * Start of the day page objects
	 */

	public static final String BO_BUSINESSDAY="businessDay"
	public static final String BO_STORETOTALAMOUNT="//input[@id='store_totalAmount']"
	public static final String BO_STARTDAYBUTTON="//button[@id='startAction']"
	public static final String BO_NEWBUDATEERRMSG="//div[@id='systemModalDialog']/div/div/div[2]"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_PENNIES="store_CURRENCY_USD_DENOMINATION_PENNIES"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_NICKELS="store_CURRENCY_USD_DENOMINATION_NICKELS"
	public static final String BO_store_CURRENCY_USD_DENOMINATION_DIMES="store_CURRENCY_USD_DENOMINATION_DIMES"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_QUARTERS="store_CURRENCY_USD_DENOMINATION_QUARTERS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_HALFDOLLARS="store_CURRENCY_USD_DENOMINATION_HALFDOLLARS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_1COINS="store_CURRENCY_USD_DENOMINATION_1COINS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_1BILLS="store_CURRENCY_USD_DENOMINATION_1BILLS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_2BILLS="store_CURRENCY_USD_DENOMINATION_2BILLS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_10BILLS="store_CURRENCY_USD_DENOMINATION_10BILLS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_20BILLS="store_CURRENCY_USD_DENOMINATION_20BILLS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_50BILLS="store_CURRENCY_USD_DENOMINATION_50BILLS"
	public static final String BO_STORE_CURRENCY_USD_DENOMINATION_100BILLS="store_CURRENCY_USD_DENOMINATION_100BILLS"
	public static final String BO_store_CURRENCY_USD_DENOMINATION_5BILLS="store_CURRENCY_USD_DENOMINATION_5BILLS"


	/*
	 * Open or close Till
	 */

	public static final String BO_TILLID="tillId"
	public static final String BO_TILLSTATUS="//tr[td[text()='105']]/td[2]"
	public static final String BO_NEXTBUTTON ="startWithTill"
	public static final String BO_CLSOETILLBUTTON="closeTill"
	public static final String BO_TILLTOTALAMOUNT="till_totalAmount"
	public static final String BO_OPENTILLBUTTON="openTill"
	public static final String BO_ERRORMESSAGETEXT="errorMessageText"
	public static final String BO_NOTALLTILLS="//span[text()='Not all tills are reconciled']"
	public static final String BO_STORE_TOTALAMOUNT="store_totalAmount"

	/*
	 *Disengage Till
	 */

	public static final String BO_TILLISNOTENGAGED="tillIsNotEngaged"



	/*
	 * Reconcil Till
	 */

	public static final String BO_RECONCILETILLBUTTON="reconcileTill"
	public static final String BO_FLOAT_TOTALAMOUNT="float_amount_CASH_USD"

	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_PENNIES="float_CURRENCY_USD_DENOMINATION_PENNIES"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_NICKELS="float_CURRENCY_USD_DENOMINATION_NICKELS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_DIMES="float_CURRENCY_USD_DENOMINATION_DIMES"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_QUARTERS="float_CURRENCY_USD_DENOMINATION_QUARTERS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_HALFDOLLARS="float_CURRENCY_USD_DENOMINATION_HALFDOLLARS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_1COINS="float_CURRENCY_USD_DENOMINATION_1COINS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_1BILLS="float_CURRENCY_USD_DENOMINATION_1BILLS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_2BILLS="float_CURRENCY_USD_DENOMINATION_2BILLS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_5BILLS="float_CURRENCY_USD_DENOMINATION_5BILLS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_10BILLS="float_CURRENCY_USD_DENOMINATION_10BILLS"
	public static final String BO_FLOAT_CURRENCY_USD_DENOMINATION_20BILLS="float_CURRENCY_USD_DENOMINATION_20BILLS"
	public static final String BO_TRAVELERSCHECK_TXT="till_amount_TRAVELERS_CHECK_USD"
	public static final String BO_CASHTILL_TXT="till_amount_CASH_USD"
	public static final String BO_TRAVELERS_COUNT="till_count_TRAVELERS_CHECK_USD"

	public static final String BO_DIMECURRENCY="till_CURRENCY_USD_DENOMINATION_DIMES"
	public static final String BO_PENNIESCURRENCY="till_CURRENCY_USD_DENOMINATION_PENNIES"
	public static final String BO_1COINCURRENCY="till_CURRENCY_USD_DENOMINATION_1COINS"
	public static final String BO_1COINFLOATCURRENCY="float_CURRENCY_USD_DENOMINATION_1COINS"

	/*
	 * Add Employee
	 */

	public static final String BO_USERNAME="userName"
	public static final String BO_FIRSTNAME="firstName"
	public static final String BO_LASTNAME="lastName"
	public static final String BO_ROLE="role"
	public static final String BO_STATUS="baseStatus"
	public static final String BO_PASSWORD="newPassword"
	public static final String BO_CONFIRMPASSWORD="confirmPassword"
	public static final String BO_SAVEEMPLOYEE="updateEmployee"
	public static final String BO_INVALIDCONFIRMPASSWORD="fld_invalid_confirmPassword"
	public static final String BO_FLD_INVALID_FIRSTNAME="fld_invalid_firstName"
	public static final String BO_FLD_INVALID_LASTNAME="fld_invalid_lastName"
	public static final String BO_FLD_INVALID_NEWPASSWORD="fld_invalid_newPassword"
	public static final String BO_FLD_INVALID_CONFIRMPASSWORD="fld_invalid_confirmPassword"
	public static final String BO_NORESULT="td[colspan='6']"



	/*
	 * Search Page
	 */

	public static final String BO_EDITEMP="//tr[td[text()='search1@2000']]//button"
	public static final String BO_RESETPASSWORDTAB="resetTabButton"
	public static final String BO_RESETPASSWORDBUTTON="resetPassword"
	public static final String BO_RESETPWDNO="//button[@type='button' and text()='NO']"
	public static final String BO_RESETPWDYES="//button[@type='button' and text()='YES']"
	public static final String BO_TEMPASSWORD="//div[@id='temporaryPasswordArea']//b"
	public static final String BO_OLDPASSWORD="oldPassword"
	public static final String BO_CHANGEPWDTAB="changeTabButton"
	public static final String BO_UPDATEPASSWORD="updatePassword"
	public static final String BO_NEWPASSWORD="newPassword"
	public static final String BO_confset="//div[@id='systemModalDialog']/div/div/div[4]/button"
	public static final String BO_OLDPASSWORD_xpt="//input[@id='oldPassword']"
	public static final String BO_UPDATEPASSWORD_XPT="//button[@id='updatePassword']"

	/*
	 * Permission Management
	 */

	public static final String BO_ADDROLE="addRole"
	public static final String BO_ROLENAME="roleName"
	public static final String BO_ROLEDESCRIPTION="roleDescription"
	public static final String BO_PERMISSIONSAVEBUTTON="save"
	public static final String BO_INVALIDROLENAME="fld_invalid_roleName"
	public static final String BO_STOCKMANGAGERROLE='allRoles_role_stock_manager'
	public static final String BO_ADDANDEDITROLE="rolePermissionSelectionCol1_role_permission_BO_ROLE_ADD_EDIT"
	public static final String BO_ROLEDELETEDISABLE="//button[@id='removeRole' and @style='display:none;']"
	public static final String BO_REMOVEROLE="removeRole"
	public static final String BO_DONTREMOVE="//div[div[@id='systemModalDialogMoreInfo']]//button[contains(text(),'Don')]"
	public static final String BO_ROLECONFIRMROLEALERT="//div[@id='systemModalDialog']/div/div/div[2]"
	public static final String BO_REMOVEROLECONFIRMBUTTON="//div[div[@id='systemModalDialogMoreInfo']]//button[contains(text(),'Remove Role')]"
	public static final String BO_ADMINTAB="allRoles_role_role_admin"
	public static final String BO_CASHIER="allRoles_role_cashier"
	public static final String BO_STOREMANAGER="allRoles_role_manager"
	public static final String BO_ADDEMPLOYEECOLUMN="//a[span[text()='Add Employee']]"
	public static final String BO_EDITEMPLOYEECOLUMN="//a[span[text()='Edit Employee']]"
	public static final String BO_PASSWORDCOLUMN="//a[span[text()='Password Reset']]"
	public static final String BO_ADDEDITCOLUMN="//a[span[text()='Add & Edit Role']]"
	public static final String BO_DELETEROLECOLUMN="//a[span[text()='Delete Role']]"
	public static final String BO_BUSINESSSETTINGCOLUMN="//a[span[text()='Business Settings']]"
	public static final String BO_LOGGINGCOLUMN="//a[span[text()='Logging Console']]"
	public static final String BO_OPERATIONSCOLUMN="//a[span[text()='Operations']]"
	public static final String BO_SEARCHCOLUMN="//a[span[text()='Search']]"
	public static final String BO_SYSTEMSETTINGSCOLUMN="//a[span[text()='System Settings']]"
	public static final String BO_OVERSHORTCOLUMN="//a[span[text()='Over/Short Report']]"
	public static final String BO_REPORTSCOLUMN="//a[span[text()='Reports']]"
	public static final String BO_SALESSUMMARYREPORTCOLUMN="//a[span[text()='Sales Summary Report']]"
	public static final String BO_STORESUMMARYREPORTCOLUMN="//a[span[text()='Store Summary Report']]"
	public static final String BO_TILLSUMMARYREPORTCOLUMN="//a[span[text()='Till Summary Report']]"
	public static final String BO_TRANSACTIONSUMMARYREPORTCOLUMN="//a[span[text()='Transaction Summary Report']]"
	public static final String BO_BANKDEPOSITCOLUMN="//a[span[text()='Bank Deposit']]"
	public static final String BO_CLOSETILLCOLUMN="//a[span[text()='Close Till']]"
	public static final String BO_DISENGAGETILLCOLUMN="//a[span[text()='Disengage Till']]"
	public static final String BO_ENDOFDAYCOLUMN="//a[span[text()='End Of Day']]"
	public static final String BO_OPENTILLCOLUMN="//a[span[text()='Open Till']]"
	public static final String BO_ReconcileTILLCOLUMN="//a[span[text()='Reconcile Till']]"
	public static final String BO_STARTOFDAYCOLUMN="//a[span[text()='Start Of Day']]"



	/*
	 * Reports 
	 */

	public static final String BO_TRANSACTIONSUMMARY="transactionSummary"
	public static final String BO_PRINTBUTTON="genericReportView_print"
	public static final String BO_REPORTVIEWEXPORT="genericReportView_export"
	public static final String BO_GENERATEREPORTVIEWTITLE="genericReportView_title"
	public static final String BO_STORENUMBER="//div[@id='genericReportView_reportContent']//h3"
	public static final String BO_REPORTDATE="//*[@id='genericReportView_reportContent']/table[1]/tbody/tr/td[1]"
	public static final String BO_SALESSUMMARY="salesSummary"
	public static final String BO_BANKDEPOSITREPORTS="onDemandBankDeposit"
	public static final String BO_DEMANDBANKDEPOSIT="onDemandBankDeposit-reportPeriod"
	public static final String BO_STORESUMMARYREPORT="storeSummary"
	public static final String BO_TILLSUMMARYREPORT="tillSummary"
	public static final String BO_TILLNUMBER="tillNumber"

	public static final String BO_OPENSAFEAMOUNT="//tr[td[text()='Open Safe Amount']]/td[2]"
	public static final String BO_CLOSESAFEAMOUNT="//tr[td[text()='Closing Safe Amount']]/td[2]"
	public static final String BO_BANKDEPOTITLE="//div[@id='onDemandBankDeposit_Form']//h3"
	public static final String BO_TILLFLOATVARIANCE="//tr[td[text()='Till Float Variance']]/td[2]"
	public static final String BO_STORESAFEVARIANCE="//tr[td[text()='Store Safe Variance']]/td[2]"
	public static final String BO_OPENTILLFLOATAMOUNT="//tr[td[text()='Open Till Float Amount']]/td[2]"
	public static final String BO_CLOSE_TILLFLOATAMOUNT="//tr[td[text()='Closing Till Float Amount']]/td[2]"
	public static final String BO_NETTAXTRANSACTIONCOUNT="//tr[td[text()='Net Taxable Transactions']]/td[2]"
	public static final String BO_NETTAXTRANSACTIONAMOUNT="//tr[td[text()='Net Taxable Transactions']]/td[3]"
	public static final String BO_VISAIN="//tr[td[text()='VISA']]/td[2]"

	public static final String BO_CHOOSEREPORT_MENU=".//*[@id='content-header']/table/tbody/tr/td[3]/ul/li/button"


	public static String Link_Reconciletill="//li[@id='menu_act_reconcileTill']/a"
	public static String TextBox_Tillid="//input[@id='tillId']"
	public static String Button_Next="//button[@id='startWithTill' and contains(text(),'Next')]"
	public static String Button_ReconcileTill="//button[@id='reconcileTill' and contains(text(),'Reconcile Till')]"
	public static String PopupButton_Close="//div[@id='systemModalDialog']/div/div/div[4]/button[contains(text(),'Close')]"

	public static String STORESUMMARUREPORT="storeSummary"
	public static String OVERSHORTREPORT="overShort"
	public static String SALESSUMMARYREPORT="salesSummary"
	public static String TRANSACTIONSUMMARYREPORT="transactionSummary"

	/**
	 * Open Till Page	
	 */
	public static String CASHDRAWER_DROPDOWN="cashDrawerId"



	/**
	 * Disengage Till button 
	 */
	public static String DISENGAGETILL_BTN="disengageTill"
	public static String ALERTBUTTONYES="//button[text()='Yes']"
	public static String ENGAGESTATUS="//tr[td[text()='?']]/td[4]/span"
	public static String ISENGAGESTATUS="//table[@id='till-list-table']/tbody/tr/td[4]"

	/**
	 * 	Bank Deposit
	 */


	public static String DEPOSITNUMBER_TXT="depositNumber"
	public static String COMPLETEDEPOSIT_BTN="completeDeposit"
	public static String VIEWREPORT_BTN="//button[text()='View Report']"
	public static String TRAVTENDER_TXT="//td[contains(text(),\"Traveler's Checks\")]"
	public static String TRAVAMOUNT_TXT="//tr[td[contains(text(),\"Traveler's Checks\")]]/td[3]"

	public static String CASHDEPOSITAMOUNT="deposit_amount_CASH"
	public static String PERSONALCHECKDEPOSITAMOUNT="deposit_amount_PERSONAL_CHECK_USD"
	public static String TRAVELERSCHECKDEPOSITAMOUNT="deposit_amount_TRAVELERS_CHECK_USD"
	public static String TRAVELERSCHECKDEPOSITCOUNT="deposit_count_TRAVELERS_CHECK_USD"



	/**
	 * Over Short Report
	 */

	public static String TENDERCASH="//tr[td[contains(text(),'Cash')]and td[text()='?']]/td[3]"
	public static String TENDERCASHTILLId="//tr[td[contains(text(),'Cash')] and td[text()='?']]/td[2]"
	public static String TENDERCASHAMOUNT="//tr[td[contains(text(),'Cash')]and td[text()='?']]/td[4]"

	public static String TENDERTRAVELERSCHECK="//tr[td[text()=\"Traveler's Checks\"]and td[text()='?']]/td[3]"
	public static String TENDERTRAVELERSCHECKTILLId="//tr[td[text()=\"Traveler's Checks\"]and td[text()='?']]/td[2]"
	public static String TENDERTRAVELERSCHECKAMOUNT="//tr[td[text()=\"Traveler's Checks\"]and td[text()='?']]/td[4]"

	public static String TENDERTRAVELERSCHECK1="//tbody[tr[td[text()='?']]]/tr[3]//td[contains(text(),\"Traveler's Checks\")]"
	public static String TENDERTRAVELERSCHECKAMOUNT1="//tbody[tr[td[text()='?']]]/tr[3]/td[3]"
	public static String TENDERTRAVELERSCHECKTILLId1="//tbody[tr[td[contains(text(),\"Traveler's Checks\")]]]/tr[2]/td[2]"
	public static String OVERSHORTTILLID="//td[contains(text(),'?')]"





	/**
	 * Till summary
	 */

	public static String TRAVELERCHECKROW="//td[contains(text(),\"Traveler's Checks\")]"
	public static String INVALUETRAVELERCHECKROW= "//tr[td[contains(text(),\"Traveler's Checks\")]]/td[3]"
	public static String CASHROW="//td[contains(text(),'Cash')]"

	public static String INVALUECASHROW= "//tr[td[contains(text(),'Cash')]]/td[3]"
	public static String VARIANCECASHROW= "//tr[td[contains(text(),'Cash')]]/td[7]"

	public static String OPENTILLFLOATROW="//tr[td[contains(text(),'Open Till Float Amount')]]"
	public static String CLOSETILLFLOATROW="//tr[td[contains(text(),'Closing Till Float Amount')]]"
	public static String TILLFLOATVARIANCROW="//tr[td[contains(text(),'Till Float Variance')]]"

	public static String VARIANCETRAVELERCHECKROW= "//tr[td[contains(text(),\"Traveler's Checks\")]]/td[7]"
	public static String OPENTILLFLOATAMOUNT="//tr[td[contains(text(),'Open Till Float Amount')]]/td[2]"
	public static String CLOSETILLFLOATAMOUNT="//tr[td[contains(text(),'Closing Till Float Amount')]]/td[2]"
	public static String TILLFLOATVARIANCE="//tr[td[contains(text(),'Till Float Variance')]]/td[2]"
	public static String TILLLOAN="//tr[td[contains(text(),'Till Float Variance')]]/td[2]"
	public static String TILLPICKUP="//tr[td[contains(text(),'Till Float Variance')]]/td[2]"
	public static String PAYIN="//tr[td[contains(text(),'Till Float Variance')]]/td[2]"
	public static String PAYOUT="//tr[td[contains(text(),'Till Float Variance')]]/td[2]"


	/**
	 * Store summary report
	 */


	public static String visa="//td[contains(text(),'Visa')]"
	public static String visacount="//tr[td[contains(text(),'Visa')]]/td[2]"
	public static String visainamount="//tr[td[contains(text(),'Visa')]]/td[3]"
	public static String DEBIT="//td[contains(text(),'Debit')]"
	public static String DEBITCOUNT="//tr[td[contains(text(),'Debit')]]/td[2]"
	public static String DEBITNAMOUNT="//tr[td[contains(text(),'Debit')]]/td[3]"

	public static String TRAVELER="//td[contains(text(),'Traveler')]"
	public static String TRAVELERCOUNT="//tr[td[contains(text(),'Traveler')]]/td[2]"
	public static String TRAVELERAMOUNT="//tr[td[contains(text(),'Traveler')]]/td[3]"


	/**
	 * Sales Summary Report	
	 */

	public static String deviceid="//td[text()='?']"
	public static String notransactions="//tr[td[text()='?']]/td[2]"
	public static String avgsaletransactions="//tr[td[text()='?']]/td[3]"
	public static String nounits="//tr[td[text()='?']]/td[4]"
	public static String avounitstransaction="//tr[td[text()='?']]/td[5]"
	public static String totalsale="//tr[td[text()='?']]/td[6]"


	/**
	 * Transaction summary report
	 */

	public static String trdeviceid="//td[contains(text(),'?')]"
	public static String totalcount="//tr[td[contains(text(),'?')]]/td[3]"
	public static String nosales="//tr[td[contains(text(),'?')]]/td[4]"
	public static String prcsales="//tr[td[contains(text(),'?')]]/td[5]"

}
