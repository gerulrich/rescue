package net.cloudengine.web.map;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import net.cloudengine.api.Datastore;
import net.cloudengine.forms.shp.FileSelectForm;
import net.cloudengine.forms.shp.POIForm;
import net.cloudengine.forms.shp.StreetForm;
import net.cloudengine.forms.shp.ZoneForm;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.service.map.ShapefileService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShapeController {
	

	private Datastore<FileDescriptor, ObjectId> fileStore;
	private ShapefileService shapeService;
	
	@Autowired
	public ShapeController(@Qualifier("fileStore") Datastore<FileDescriptor, ObjectId> fileStore,
			ShapefileService shapeService) {
		this.fileStore = fileStore;
		this.shapeService = shapeService;
	}

	@RequestMapping(value = "/shp/upload", method = RequestMethod.GET)
	public ModelAndView submitForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("files", fileStore.createQuery().field("type").eq("shp").list());
		mav.setViewName("/map/uploadShapefile");
		return mav;
	}
	
	@RequestMapping(value = "/shp/upload", method = RequestMethod.POST)
	public ModelAndView submitForm(@Valid @ModelAttribute("form") FileSelectForm form, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/shp/upload");
			return mav; 
		}
		
		if ("poi".equals(form.getType())) {
			return new ModelAndView("redirect:/shp/upload/poi/"+form.getFile());
		}
		
		if ("street".equals(form.getType())) {
			return new ModelAndView("redirect:/shp/upload/street/"+form.getFile());
		}
		
		if ("zone".equals(form.getType())) {
			return new ModelAndView("redirect:/shp/upload/zone/"+form.getFile());
		}
		
		
		return new ModelAndView("redirect:/shp/upload");
		
	}
	
	@RequestMapping(value = "/shp/upload/poi/{id}", method = RequestMethod.GET)
	public ModelAndView submitFormPOI(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		FileDescriptor descriptor = fileStore.get(id);
		String fields[] = shapeService.readFileFields(descriptor);
		mav.addObject("fields", fields);
		mav.addObject("file", descriptor);
		mav.setViewName("/map/uploadPOI");
		return mav;
	}
	
	@RequestMapping(value = "/shp/upload/poi/{id}", method = RequestMethod.POST)
	public ModelAndView submitFormPOI(@PathVariable("id") ObjectId id, @Valid POIForm form, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/map/uploadPOI");
			return mav;
		}
		
		try {
			
			FileDescriptor descriptor = fileStore.get(id);
			shapeService.shp2Poi(descriptor, form.getNameField(), form.getTypeField(), form.getOverwrite());
			
		} catch (Exception e) {

		}
		
		mav.setViewName("redirect:/admin/mongo/show/poi");
		return mav;
	}
	
	@RequestMapping(value = "/shp/upload/street/{id}", method = RequestMethod.GET)
	public ModelAndView submitFormStreet(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		FileDescriptor descriptor = fileStore.get(id);
		String fields[] = shapeService.readFileFields(descriptor);
		mav.addObject("fields", fields);
		mav.addObject("file", descriptor);
		mav.setViewName("/map/uploadStreet");
		return mav;
	}
	
	@RequestMapping(value = "/shp/upload/street/{id}", method = RequestMethod.POST)
	public ModelAndView submitFormStreet(@PathVariable("id") ObjectId id, @Valid StreetForm form, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/map/uploadStreet");
			return mav;
		}
		
		try {
			
			FileDescriptor descriptor = fileStore.get(id);
			
			Map<String,String> fieldNames = new HashMap<String, String>();
			fieldNames.put(ShapefileService.NOMBRE, form.getNameField());
			fieldNames.put(ShapefileService.TIPO, form.getTypeField());
			fieldNames.put(ShapefileService.ALT_II, form.getFromLeftField());
			fieldNames.put(ShapefileService.ALT_IF, form.getToLeftField());
			fieldNames.put(ShapefileService.ALT_DI, form.getFromRightField());
			fieldNames.put(ShapefileService.ALT_DF, form.getToRightField());
			fieldNames.put(ShapefileService.VINICIO, form.getVstartField());
			fieldNames.put(ShapefileService.VFIN, form.getVendField());
			
			shapeService.shp2Street(descriptor, fieldNames, form.getOverwrite());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.setViewName("redirect:/admin/mongo/show/poi");
		return mav;
	}
	
	
	
	@RequestMapping(value = "/shp/upload/zone/{id}", method = RequestMethod.GET)
	public ModelAndView submitFormZone(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		FileDescriptor descriptor = fileStore.get(id);
		String fields[] = shapeService.readFileFields(descriptor);
		mav.addObject("fields", fields);
		mav.addObject("file", descriptor);
		mav.setViewName("/map/uploadZone");
		return mav;
	}
	
	@RequestMapping(value = "/shp/upload/zone/{id}", method = RequestMethod.POST)
	public ModelAndView submitFormZone(@PathVariable("id") ObjectId id, @Valid ZoneForm form, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/map/uploadZone");
			return mav;
		}
		
		try {
			
			FileDescriptor descriptor = fileStore.get(id);
			shapeService.shp2Zone(descriptor, form.getNameField(), form.getType(), form.getOverwrite());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.setViewName("redirect:/admin/mongo/show/poi");
		return mav;
	}
	
	
		
}
