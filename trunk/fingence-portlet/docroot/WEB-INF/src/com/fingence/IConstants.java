package com.fingence;

public interface IConstants {
	int USER_TYPE_INVESTOR = 1;
	int USER_TYPE_WEALTH_ADVISOR = 2;
	int USER_TYPE_BANK_ADMIN = 3;
	int USER_TYPE_REL_MANAGER = 4;
	
	String ROLE_RELATIONSHIP_MANAGER = "Relationship Manager";

	String VOCABULARY_FINGENCE_REPORT = "Fingence Reports";
	String PAGE_REPORTS_HOME = "Summary";
	String PAGE_ASSET_REPORT = "Asset Report";
	String PAGE_PERFORMANCE = "Performance";
	String PAGE_FIXED_INCOME = "Fixed Income";
	String PAGE_RISK_REPORT = "Risk Report";
	String PAGE_VIOLATIONS = "Violations";
	String ADD_PORTFOLIO = "Add Portfolio";
	String ADD_USER = "Add User";
	
	String PAGE_PORTFOLIO = "Portfolio";
	
//	String[] REPORT_MENU_ITEMS = {
//		PAGE_REPORTS_HOME,
//		PAGE_ASSET_REPORT,
//		PAGE_PERFORMANCE,
//		PAGE_FIXED_INCOME,
//		PAGE_RISK_REPORT,
//		PAGE_VIOLATIONS
//	};
	
	String PARENT_ORG_FIRMS = "Firms";
	String PARENT_ORG_BANKS = "Banks";
	
	String CMD_CHECK_DUPLICATE = "checkDuplicate";
	String CMD_SET_PORTFOLIO_ID = "setPortfolioId";
	String CMD_SET_ALLOCATION_BY = "setAllocationBy";
	String CMD_CHECK_DUPLICATE_PORTFOLIO = "checkDuplicatePortfolio";
	String CMD_SET_ASSETS_TO_SHOW = "setAssetsToShow";
	String CMD_ADD_PORTFOLIO_ID = "addPortfolioId";
	String CMD_CHANGE_FIXED_INCOME_RPT = "changeFixedIncomeReport";
	String CMD_GET_RATING_DETAILS = "getRatingDetails";
	String CMD_GET_NET_WORTH = "getNetWorth";
	String CMD_ENABLE_REPORT = "enableReport";
	
	int BREAKUP_BY_RISK_COUNTRY = 1;
	int BREAKUP_BY_CURRENCY = 2;
	int BREAKUP_BY_SECURITY_CLASS = 3;
	int BREAKUP_BY_INDUSTRY_SECTOR = 4;
	
	int SECURITY_CLASS_FIXED_INCOME = 1;
	int SECURITY_CLASS_FUND = 2;
	int SECURITY_CLASS_EQUITY = 3;
	
	String LBL_BREAKUP_BY_RISK_COUNTRY = "Risk Country";
	String LBL_BREAKUP_BY_CURRENCY = "Currency";
	String LBL_BREAKUP_BY_SECURITY_CLASS = "Security Class";
	String LBL_BREAKUP_BY_INDUSTRY_SECTOR = "Industry Sector";
	
	String THEME_ICON_EDIT = "/common/edit.png";
	String THEME_ICON_DELETE = "/common/delete.png";
	String THEME_ICON_VIEW = "/common/portlet.png";
	String THEME_ICON_MORE_DETAILS = "/common/open_window.png";
	String THEME_ICON_PRIMARY_OPTIONS = "/portlet/pop_up.png";
	String THEME_ICON_DISCUSSION = "/common/conversation.png";
	
	String CURRENCY_USD = "USD";
	String CURRENCY_UNIT = "1";
	
	String SELECTED = "selected";
	String UN_SPECIFIED = "Un-Specified";
	
	String FIXED_INCOME_TYPE_BONDS_MATURITY = "Bond Collateral Type Breakdown";
	String FIXED_INCOME_TYPE_BONDS_QUALITY = "Exposure As Per Rating Table";
	String FIXED_INCOME_TYPE_CASH_FLOW = "Cash Flow Report";	
	String FIXED_INCOME_TYPE_COLLATERAL = "Collateral Breakdown";
	String FIXED_INCOME_YLD_TO_MATURITY = "YTM and Duration Table";
	String FIXED_INCOME_CPN_TYP_VS_MTY_TYP = "Coupon Type to Maturity Type";
	
	int HISTORY_TYPE_EQUITY = 1;
	int HISTORY_TYPE_BOND = 2;
	int HISTORY_TYPE_BOND_CASHFLOW = 3;
	int HISTORY_TYPE_DIVIDENDS = 4;
}