package net.cloudengine.web.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.cloudengine.dao.support.BlobStore;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.web.map.MapController;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/file")
public class FileController {

	private static Logger logger = LoggerFactory.getLogger(MapController.class);
	
	private BlobStore blobStore;
	private Repository<FileDescriptor, ObjectId> fileRepository;
	
	@Autowired
	public FileController(BlobStore blobStore, 
			RepositoryLocator repositoryLocator) {
		this.blobStore = blobStore;
		this.fileRepository = repositoryLocator.getRepository(FileDescriptor.class);
	}
	
	/**
	 * Descarga un archivo de la base de datos MongoDB.
	 * @param id id del FileDescriptor
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "download/{id}", method = RequestMethod.GET)
	public void downloadFile(@PathVariable("id") ObjectId id, HttpServletResponse response) throws Exception {
		
		FileDescriptor fileDescriptor = fileRepository.get(id);
		
		logger.debug("Petición de archivo : {}", fileDescriptor.getFilename());
		
		OutputStream tempOS = null;
		InputStream is = null;
		
		File tempFile = null;
		try {
			tempFile = File.createTempFile("tempfile", ".tmp");
			tempOS = new FileOutputStream(tempFile);

			blobStore.retrieveFile(new ObjectId(fileDescriptor.getFileId()), tempOS);
			
//			response.setContentType("");
			response.setHeader("Content-Disposition", "attachment;filename="+fileDescriptor.getFilename());
			is = new FileInputStream(tempFile);
			IOUtils.copy(is, response.getOutputStream());
			return;
		} catch (IOException e) {
			// no se pudo obtener el archivo.
			
		} finally {
			
			IOUtils.closeQuietly(tempOS);
			IOUtils.closeQuietly(is);
			if (tempFile != null) {
				tempFile.delete();
			}
		}
		
		// si llego aca es porque no se pudo enviar el archivo.
		response.sendError(HttpServletResponse.SC_NOT_FOUND);		
	}
	
	/**
	 * Elimina un archivo de la base de datos de MongoDB.
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView removeFile(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();

		FileDescriptor fileDescriptor = fileRepository.get(id);
		
		logger.debug("Eliminar archivo : {}", fileDescriptor.getFilename());
		
		blobStore.remove(new ObjectId(fileDescriptor.getFileId()));
		fileRepository.delete(fileDescriptor.getId());
		
		mav.setViewName("redirect:/file/list");
		return mav;
	}
	
	
	
	
	
	/**
	 * Calcula el progreso del archivo que se esta subioendo.
	 * @param key
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "uploadstatus/", method = RequestMethod.GET)
	public @ResponseBody FileUploadProgress progress(HttpSession session) {
		long read = 0L;
		long total = 0L;
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
	
	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public ModelAndView submitForm() {
		return new ModelAndView("/crud/file/uploadForm");
	}
	
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String uploadFile(HttpServletRequest request, HttpSession session) {
		
		ServletFileUpload upload = new ServletFileUpload();
		UploadListener progressListener = new UploadListener();
		session.setAttribute("PROGRESO", progressListener);
		upload.setProgressListener(progressListener);

		String description = null;
		String version = null;
		String type = null;
		try {
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				InputStream stream = item.openStream();
				if (item.isFormField()) {
					if ("version".equals(item.getFieldName())) {
						version = Streams.asString(stream);
					} else if ("description".equals(item.getFieldName())) {
						description = Streams.asString(stream);						
					} else {
						type = Streams.asString(stream);
						String contentType = item.getContentType();
						if ("other".equals(type) && StringUtils.isNotEmpty(contentType) ) {
							type = contentType;
						}						
					}
				} else {
					Path path = Paths.get(item.getName());
					String file = path.getFileName().toString();
					blobStore.storeFile(file, stream, description, type, version);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "redirect:/file/upload";
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/file/list/1/10");
		return mav;
	}
	
	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("files", fileRepository.list(page, size));
		mav.setViewName("crud/file/list");
		return mav;
	}
	
}
