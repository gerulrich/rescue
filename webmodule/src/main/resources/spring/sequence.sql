
drop function if exists true_function$$
drop function if exists get_next_value$$


create function true_function(p_param int) returns int
  deterministic
  sql security invoker
  return true;
$$

create function get_next_value(p_name varchar(30)) returns int
  deterministic
  sql security invoker
begin
  declare current_val integer;

  update sequences
  	set value = value + 1
  	where name = p_name
  	  and true_function((@current_val := sequences.value) is not null);
  	  
  return @current_val;
end
$$