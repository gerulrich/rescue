--set GLOBAL log_bin_trust_function_creators = true$$

drop function if exists true_function$$
drop function if exists get_next_value$$
drop procedure if exists create_index$$


--create function true_function(p_param int) returns int
--  deterministic
--  sql security invoker
--  return true;
--$$

--create function get_next_value(p_name varchar(30)) returns int
--  deterministic
--  sql security invoker
--begin
--  declare current_val integer;

--  update sequences
--  	set value = value + 1
--  	where name = p_name
--  	  and true_function((@current_val := sequences.value) is not null);
  	  
--  return @current_val;
--end
--$$

CREATE PROCEDURE create_index
(
    given_table    VARCHAR(64),
    given_index    VARCHAR(64),
    given_columns  VARCHAR(64)
)
BEGIN
 
    DECLARE IndexIsThere INTEGER;
    SELECT COUNT(1) INTO IndexIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema COLLATE utf8_unicode_ci = database()
    AND   table_name   COLLATE utf8_unicode_ci = given_table
    AND   index_name   COLLATE utf8_unicode_ci = given_index;
 
   IF IndexIsThere = 0 THEN
        SET @sqlstmt = CONCAT('CREATE INDEX ',given_index,' ON ',
        database(),'.',given_table,' (',given_columns,')');
        PREPARE st FROM @sqlstmt;
        EXECUTE st;
        DEALLOCATE PREPARE st;
        SELECT CONCAT('Created index ', given_table,'.', given_index, ' on columns ', given_columns)
                AS 'CreateIndex status';
    ELSE
        SELECT CONCAT('Index ',given_index,' Already Exists on Table ', database(),'.',given_table)
                AS 'CreateIndex status';
    END IF;
 
END $$