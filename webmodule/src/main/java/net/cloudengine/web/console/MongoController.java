package net.cloudengine.web.console;

import javax.servlet.http.HttpServletRequest;

import net.cloudengine.service.MongoService;
import net.cloudengine.util.HexString;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
/**
 * Mustra información basica de la DB de Mongo.
 * @author German Ulrich
 *
 */
@Controller
public class MongoController {
	
	private MongoService service;

	@Autowired
	public MongoController(MongoService service) {
		super();
		this.service = service;
	}
	
	/**
	 * Obtiene la lista de colecciones de la base de datos de MongoDB.
	 */
	@RequestMapping(value="/mongo/list", method = RequestMethod.GET)
	public ModelAndView mongoCollections() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("collections", service.getCollections());
		mav.setViewName("/console/mongo/collections");
		return mav;
	}
	
	/**
	 * Elimina la coleccion de la base de datos de MongoDB.
	 */
	@RequestMapping(value="/mongo/drop/{collection}", method = RequestMethod.GET)
	public ModelAndView dropCollection(@PathVariable("collection") String collection) {
		ModelAndView mav = new ModelAndView();
		service.drop(HexString.decode(collection));
		mav.setViewName("redirect:/mongo/list");
		return mav;
	}
	
	@RequestMapping(value="/mongo/show/{collection}", method = RequestMethod.GET)
	public ModelAndView showCollection(@PathVariable("collection") String collection) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/mongo/show/{collection}/1/25");
		return mav;
	}
	
	@RequestMapping(value="/mongo/show/{collection}/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView showCollection(@PathVariable("collection") String collection, @PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		String collectionName = HexString.decode(collection);
		if (!service.isValidCollection(collectionName)) {
			mav.setViewName("redirect:/mongo/list");
			return mav;
		}
		
		mav.addObject("collectionName", collectionName);
		mav.addObject("collectionEncodedName", collection);
		mav.addObject("objects", service.getObjects(collectionName, page, size));
		mav.addObject("headers", service.getHeaders(collectionName, page, size));
		mav.setViewName("/console/mongo/collection");
		return mav;
	}
	
	@ExceptionHandler(DecoderException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid collection name")
	public void handleDecoderException(DecoderException ex, HttpServletRequest request) {
		ex.printStackTrace();
	}
}
