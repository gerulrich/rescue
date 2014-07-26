package net.cloudengine.model.report

import java.text.SimpleDateFormat

import javax.script.Bindings
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

import net.cloudengine.model.auth.User

import org.apache.commons.lang.StringUtils
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Field

class ReportParameter {
	
	String name;
	String label;
	String tooltip;
	@Field("default")
	String defaultValue;
	String options;
	String type;
	String pattern;
	List<String> allowsTo;
	boolean required;
	boolean visible;
	
	boolean hasPermission(User user) {
		if (allowsTo == null || allowsTo.isEmpty()) {
			return true;
		} else {
			boolean hasPermission = false;
			for(String permission : allowsTo) {
				if (user.getRoles().contains(permission)) {
					hasPermission = true;
					break;
				}
			}
			return hasPermission;
		}
	}
	
	@Transient
	String getValue() {
		if (!StringUtils.isBlank(defaultValue)) {
			Object result = evaluate(defaultValue, pattern);
			return result != null ? result.toString() : '';
		}
		return '';
	}
	
	Object evaluate(String expression, String pattern) {
		println expression;
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName('JavaScript');
		StringBuilder buff = new StringBuilder();
		buff.append('function Value(val){return val};\n');
		buff.append('function Map(keyValueMap){for (var i in keyValueMap) { opts.put(i, keyValueMap[i]) }};\n');
		buff.append('function Sql(id,description,table){mapResult = sql.query(id, description, table); return Map(mapResult)};\n');
		buff.append('function Mongo(id,description,collection){mapResult = mongo.query(id, description, table); return Map(mapResult)};\n');
		buff.append('function Format(date){return format.format(date,pattern)};\n');
		buff.append('function Time(){return new TTime(new Date());};\n');
		buff.append('function TTime(date){this.date=date;}\n');
		buff.append('TTime.prototype.plusDays=function(days){return new TTime(new Date(this.date.getTime()+days*86400000))}\n');
		buff.append('TTime.prototype.plusHours=function(hours){return new TTime(new Date(this.date.getTime()+hours*3600000))}\n');
		buff.append('TTime.prototype.truncate=function(){return new TTime(new Date(this.date.getTime()-(this.date.getTime()%3600000)))}\n');
		buff.append('TTime.prototype.value = function(){ return this.date;}\n');
		buff.append(expression);

		Object result = null;
		Map<String, String> opts = new HashMap<String, String>();
		try {
			Bindings bindings = engine.createBindings();
			bindings.put("format", this);
			bindings.put("pattern", pattern);
			result = engine.eval(buff.toString(), bindings);
		} catch (ScriptException e) {
			//e.printStackTrace();
			result = "error in expression";
		} catch (Exception e) {
			//e.printStackTrace();
			result = "error in expression";
		}		
		return result;
	}
	
	String format(Date date, String pattern) {
	    println date;
		println pattern;
		return new SimpleDateFormat(pattern).format(date);
	}
	
}