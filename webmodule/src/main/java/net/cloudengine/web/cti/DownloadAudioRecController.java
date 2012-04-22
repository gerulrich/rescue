package net.cloudengine.web.cti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.cloudengine.service.cti.RecordingService;
import net.cloudengine.web.map.MapController;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DownloadAudioRecController {
	
	private static Logger logger = LoggerFactory.getLogger(MapController.class);
	private static final String URI = "/audio/{filename}/";
	
	private RecordingService service;

	@Autowired
	public DownloadAudioRecController(RecordingService service) {
		super();
		this.service = service;
	}
	
	@RequestMapping(value = URI, method = RequestMethod.GET)
	public void downloadAudioRec(@PathVariable("filename") String filename, HttpServletResponse response) throws Exception {
		
		logger.debug("Petición de archivo de audio: {}", filename);
		
		OutputStream tempOS = null;
		InputStream is = null;
		
		try {
			File tempFile = File.createTempFile("audiorec_xxx", ".wav");
			tempOS = new FileOutputStream(tempFile);

			boolean success = service.getAudioRecord(filename, tempOS);
			
			if (success) {
				response.setContentType("audio/wav");
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
				is = new FileInputStream(tempFile);
				IOUtils.copy(is, response.getOutputStream());
				return;
			}
			
		} catch (IOException e) {
			// no se pudo obtener el archivo.
			
		} finally {
			IOUtils.closeQuietly(tempOS);
			IOUtils.closeQuietly(is);
		}
		
		// si llego aca es porque no se pudo enviar el archivo.
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		
	}
	
}
