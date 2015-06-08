ALTER TABLE eg_roleaction_map RENAME TO eg_roleaction;
ALTER TABLE eg_action DROP COLUMN entityid;
ALTER TABLE eg_action DROP COLUMN taskid;
ALTER TABLE eg_action DROP COLUMN updatedtime;
ALTER TABLE eg_action DROP COLUMN urlorderid;
ALTER TABLE eg_action DROP COLUMN action_help_url;
ALTER TABLE eg_action RENAME module_id TO parentModule;
ALTER TABLE eg_action RENAME order_number TO ordernumber;
ALTER TABLE eg_action RENAME display_name TO displayname;
ALTER TABLE eg_action RENAME is_enabled TO enabled;
ALTER TABLE eg_action RENAME context_root TO contextroot;
ALTER TABLE eg_action ADD COLUMN "version" numeric default 0;
ALTER TABLE eg_action ADD COLUMN createdby numeric default 1;
ALTER TABLE eg_action ADD COLUMN createddate timestamp default now();
ALTER TABLE eg_action ADD COLUMN lastmodifiedby numeric default 1;
ALTER TABLE eg_action ADD COLUMN lastmodifieddate timestamp default now();

update eg_module set contextroot = LOWER(contextroot);
update eg_action set contextroot = LOWER(contextroot);
select nextval('seq_eg_heirarchy_type');
select nextval('seq_eg_heirarchy_type');