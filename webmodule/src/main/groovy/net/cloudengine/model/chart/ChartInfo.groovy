package net.cloudengine.model.chart

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.cloudengine.web.crud.support.CrudProperty;

import org.apache.bval.constraints.NotEmpty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="chart_info")
class ChartInfo {
	
@Id ObjectId id;

	@NotNull
	@NotEmpty
	@Size(min=1,max=30)
	@CrudProperty
	String name

	@NotNull
	@NotEmpty
	@CrudProperty
	String url;
}