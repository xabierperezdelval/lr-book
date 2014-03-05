package com.fingence;

public interface IConstants {
	int USER_TYPE_INVESTOR = 1;
	int USER_TYPE_WEALTH_ADVISOR = 2;
	int USER_TYPE_BANK_ADMIN = 3;
	int USER_TYPE_REL_MANAGER = 4;
	
	String ROLE_RELATIONSHIP_MANAGER = "Relationship Manager";

	String PAGE_REPORTS_HOME = "Summary";
	String PAGE_ASSET_REPORT = "Asset Report";
	String PAGE_FIXED_INCOME = "Fixed Income";
	String PAGE_RISK_REPORT = "Risk Report";
	String PAGE_PERFORMANCE = "Performance";
	String PAGE_VIOLATIONS = "Violations";
	String ADD_PORTFOLIO = "Add Portfolio";
	String ADD_USER = "Add User";
	
	String PAGE_PORTFOLIO = "Portfolio";
	
	String[] REPORT_MENU_ITEMS = {
		PAGE_REPORTS_HOME,
		PAGE_ASSET_REPORT,
		PAGE_PERFORMANCE,
		PAGE_FIXED_INCOME,
		PAGE_RISK_REPORT,
		PAGE_VIOLATIONS
	};
	
	String PARENT_ORG_FIRMS = "Firms";
	String PARENT_ORG_BANKS = "Banks";
	
	String CMD_CHECK_DUPLICATE = "checkDuplicate";
	String CMD_SET_PORTFOLIO_ID = "setPortfolioId";
	String CMD_SET_ALLOCATION_BY = "setAllocationBy";
	
	int BREAKUP_BY_RISK_COUNTRY = 1;
	int BREAKUP_BY_CURRENCY = 2;
	int BREAKUP_BY_SECURITY_TYPE = 3;
	int BREAKUP_BY_SECTOR = 4;
	
	String LBL_BREAKUP_BY_RISK_COUNTRY = "Risk Country";
	String LBL_BREAKUP_BY_CURRENCY = "Currency";
	String LBL_BREAKUP_BY_SECURITY_TYPE = "Security Type";
	String LBL_BREAKUP_BY_SECTOR = "Sector";
	
	String THEME_ICON_EDIT = "/common/edit.png";
	String THEME_ICON_DELETE = "/common/delete.png";
	String THEME_ICON_VIEW = "/common/portlet.png";
}