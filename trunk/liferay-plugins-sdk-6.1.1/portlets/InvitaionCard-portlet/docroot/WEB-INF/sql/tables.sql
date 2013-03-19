create table mpower_InvitationCard (
	invitationId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	inviteeEmail VARCHAR(75) null,
	status INTEGER,
	inviteeNewUserId LONG
);