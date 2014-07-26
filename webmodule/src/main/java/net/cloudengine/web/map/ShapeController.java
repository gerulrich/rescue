package net.cloudengine.web.map;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.dao.support.SearchParametersBuilder;
import net.cloudengine.forms.shp.FileSelectForm;
import net.cloudengine.forms.shp.POIForm;
import net.cloudengine.forms.shp.StreetForm;
import net.cloudengine.forms.shp.ZoneForm;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.service.ShapefileService;
import net.cloudengine.web.ajax.ResponseStatus;
import net.cloudengine.web.files.FileUploadProgress;
import net.cloudengine.web.files.UploadListener;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shp")
public class ShapeController {
	
	private Repository<FileDescriptor, ObjectId> fileRepository;
	private ShapefileService shapeService;
	
	@Autowired
	public ShapeController(RepositoryLocator repositoryLocator,
			ShapefileService shapeService) {
		this.fileRepository = repositoryLocator.getRepository(FileDescriptor.class);
		this.shapeService = shapeService;
	}

	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public ModelAndView submitForm() {
		ModelAndView mav = new ModelAndView();
		SearchParametersBuilder builder = SearchParametersBuilder.forClass(FileDescriptor.class);
		builder.eq("type", "shp");
		mav.addObject("files", fileRepository.findAll(builder.build()));
		mav.setViewName("/map/uploadShapefile");
		return mav;
	}
	
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public ModelAndView submitForm(@Valid @ModelAttribute("form") FileSelectForm form, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("upload");
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
	
	@RequestMapping(value = "upload/poi/{id}", method = RequestMethod.GET)
	public ModelAndView submitFormPOI(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		FileDescriptor descriptor = fileRepository.get(id);
		String fields[] = shapeService.readFileFields(descriptor);
		mav.addObject("fields", fields);
		mav.addObject("file", descriptor);
		mav.setViewName("/map/uploadPOI");
		return mav;
	}
	
	@RequestMapping(value = "upload/poi/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus submitFormPOI(
				@PathVariable("id") ObjectId id, 
				@Valid POIForm form, 
				BindingResult result,
				HttpSession session) {
		
		ResponseStatus response = new ResponseStatus();
		if (result.hasErrors()) {
			response.setCode(-1);
			response.setData("Compruebe el formulario e intente nuevamente");
			return response;
		}
		
		try {
			UploadListener progressListener = new UploadListener();
			session.setAttribute("PROGRESO", progressListener);
			
			FileDescriptor descriptor = fileRepository.get(id);
			long count = shapeService.shp2Poi(descriptor, form.getNameField(), form.getTypeField(), form.getOverwrite(), progressListener);
			
			response.setCode(0);
			response.setData(String.valueOf(count));
		} catch (Exception e) {
			response.setCode(-1);
			response.setData("Se produjo un error inesperado");
		}
		
		return response;
	}
	
	@RequestMapping(value = "upload/street/{id}", method = RequestMethod.GET)
	public ModelAndView submitFormStreet(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		FileDescriptor descriptor = fileRepository.get(id);
		String fields[] = shapeService.readFileFields(descriptor);
		mav.addObject("fields", fields);
		mav.addObject("file", descriptor);
		mav.setViewName("/map/uploadStreet");
		return mav;
	}
	
	@RequestMapping(value = "upload/street/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus submitFormStreet(
			@PathVariable("id") ObjectId id, 
			@Valid StreetForm form, BindingResult result,
			HttpSession session) {
		
		ResponseStatus response = new ResponseStatus();
		if (result.hasErrors()) {
			response.setCode(-1);
			response.setData("Compruebe el formulario e intente nuevamente");
		}
		
		try {
			
			FileDescriptor descriptor = fileRepository.get(id);
			
			Map<String,String> fieldNames = new HashMap<String, String>();
			fieldNames.put(ShapefileService.NOMBRE, form.getNameField());
			fieldNames.put(ShapefileService.TIPO, form.getTypeField());
			fieldNames.put(ShapefileService.ALT_II, form.getFromLeftField());
			fieldNames.put(ShapefileService.ALT_IF, form.getToLeftField());
			fieldNames.put(ShapefileService.ALT_DI, form.getFromRightField());
			fieldNames.put(ShapefileService.ALT_DF, form.getToRightField());
			fieldNames.put(ShapefileService.VINICIO, form.getVstartField());
			fieldNames.put(ShapefileService.VFIN, form.getVendField());
			
			UploadListener progressListener = new UploadListener();
			session.setAttribute("PROGRESO", progressListener);
			
			long count = shapeService.shp2Street(descriptor, fieldNames, form.getOverwrite(), progressListener);
			
			response.setCode(0);
			response.setData(String.valueOf(count));
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode(-1);
			response.setData("Se produjo un error inesperado.");
		}
		
		return response;
	}
	
	
	/**
	 * Calcula el progreso del archivo que se esta subioendo.
	 * @param key
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "processstatus/", method = RequestMethod.GET)
	public @ResponseBody FileUploadProgress progress(HttpSession session) {
		long read = 0L;
		long total = 100L;
		UploadListener progressListener = (UploadListener) session.getAttribute("PROGRESO");
		if (progressListener != null && progressListener.getItem() > 0) {
			read = progressListener.getBytesRead();
			total = progressListener.getContentLength();
			if (read == total) {
				session.removeAttribute("PROGRESO");
			}
		}
		return new FileUploadProgress(read, total);
	}
	
	
	@RequestMapping(value = "upload/zone/{id}", method = RequestMethod.GET)
	public ModelAndView seputFormZone(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		FileDescriptor descriptor = fileRepository.get(id);
		String fields[] = shapeService.readFileFields(descriptor);
		mav.addObject("fields", fields);
		mav.addObject("file", descriptor);
		mav.setViewName("/map/uploadZone");
		return mav;
	}
	
	@RequestMapping(value = "upload/zone/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus submitFormZone(
			@PathVariable("id") ObjectId id, 
			@Valid ZoneForm form, 
			BindingResult result,
			HttpSession session) {
				
		ResponseStatus response = new ResponseStatus();
		if (result.hasErrors()) {
			response.setCode(-1);
			response.setData("Compruebe el formulario e intente nuevamente");
			return response;
		}
		
		UploadListener progressListener = new UploadListener();
		session.setAttribute("PROGRESO", progressListener);
		
		try {
			FileDescriptor descriptor = fileRepository.get(id);
			long count = shapeService.shp2Zone(descriptor, form.getNameField(), form.getType(), form.getOverwrite(), progressListener);
			response.setCode(0);
			response.setData(String.valueOf(count));
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode(-1);
			response.setData("Se produjo un error inesperado");
		}
		
		return response;
	}
		
}
