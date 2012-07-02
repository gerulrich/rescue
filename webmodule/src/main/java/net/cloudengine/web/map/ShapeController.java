package net.cloudengine.web.map;

import javax.validation.Valid;

import net.cloudengine.api.BlobStore;
import net.cloudengine.api.Datastore;
import net.cloudengine.forms.shp.FileSelectForm;
import net.cloudengine.forms.shp.POIForm;
import net.cloudengine.forms.shp.StreetForm;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.service.admin.ConfigurationService;
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
	
	private ConfigurationService service; 
	private Datastore<FileDescriptor, ObjectId> fileStore;
	private ShapefileService shapeService;
	
	@Autowired
	public ShapeController(BlobStore blobStore, ConfigurationService service, 
			@Qualifier("fileStore") Datastore<FileDescriptor, ObjectId> fileStore,
			ShapefileService shapeService) {
		this.service = service;
		this.fileStore = fileStore;
		this.shapeService = shapeService;
	}	
	
	@RequestMapping(value = "/map/test", method = RequestMethod.GET)
	public ModelAndView testMap() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("map.google.street", service.getProperty("map.google.street"));
		mav.addObject("map.osm", service.getProperty("map.osm"));
		mav.setViewName("/map/testMap");
		return mav;
	}
	
	
	@RequestMapping(value = "/shp/upload", method = RequestMethod.GET)
	public ModelAndView submitForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("files", fileStore.createQuery().list());
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
		
		
		return new ModelAndView("redirect:/shp/upload");
		
	}
	
	@RequestMapping(value = "/shp/upload/poi/{id}", method = RequestMethod.GET)
	public ModelAndView submitFormPOI(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		FileDescriptor descriptor = fileStore.get(id);
		String fields[] = shapeService.readFileFields(descriptor);
		mav.addObject("fields", fields);
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
			shapeService.shp2Street(descriptor, form.getNameField(), form.getTypeField(),
					form.getFromLeftField(), form.getToLeftField(), form.getFromRightField(), form.getToRightField());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.setViewName("redirect:/admin/mongo/show/poi");
		return mav;
	}
	
	
	
//	@RequestMapping(value = "/admin/shp/{id}", method = RequestMethod.GET)
//	public ModelAndView shape2Mongo(@PathVariable("id") ObjectId id) throws Exception {
//		ModelAndView mav = new ModelAndView();

//		FileDescriptor descriptor = fileStore.get(id);
//		shapeService.shp2Poi(descriptor, "NAME", "CATEGORY");
//		String fields[] = shapeService.readFileFields(descriptor);
//		System.out.println(Arrays.asList(fields));
		
//		mav.setViewName("redirect:/file/list");
//		return mav;
//	}		
}
