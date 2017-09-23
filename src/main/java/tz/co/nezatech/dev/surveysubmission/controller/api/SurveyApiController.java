package tz.co.nezatech.dev.surveysubmission.controller.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import tz.co.nezatech.dev.surveysubmission.model.Form;
import tz.co.nezatech.dev.surveysubmission.model.FormData;
import tz.co.nezatech.dev.surveysubmission.model.FormRepos;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.model.User;
import tz.co.nezatech.dev.surveysubmission.repository.FormDataRepository;
import tz.co.nezatech.dev.surveysubmission.repository.FormReposRepository;
import tz.co.nezatech.dev.surveysubmission.repository.FormRepository;
import tz.co.nezatech.dev.surveysubmission.repository.ProjectRepository;
import tz.co.nezatech.dev.surveysubmission.repository.UserRepository;
import tz.co.nezatech.dev.surveysubmission.storage.StorageService;

@Controller
@RestController
@RequestMapping("/survey")
@PreAuthorize("hasRole('API') or hasRole('Administrator')")
@PropertySource("classpath:config.properties")
public class SurveyApiController {
	@Value("${survey.form.files.upload.path}")
	private String uploadPath;
	@Value("${survey.form.files.upload.folder.video}")
	private String videos;
	@Value("${survey.form.files.upload.folder.picture}")
	private String pictures;
	@Value("${survey.form.files.upload.folder.other}")
	private String others;
	@Autowired
	StorageService storageService;
	@Autowired
	ServletContext servletContext;

	@Autowired
	FormReposRepository reposRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FormRepository formRepository;
	@Autowired
	FormDataRepository formDataRepository;

	private final Logger LOG = LoggerFactory.getLogger(SurveyApiController.class);

	@RequestMapping(value = "/form/{id}", method = RequestMethod.POST)
	public @ResponseBody ApiResponse post(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails user,
			MultipartHttpServletRequest request) {
		long contentLength = request.getContentLength();
		LOG.debug("Content-length: " + contentLength);

		User capturedBy = userRepository.getAll("username", user.getUsername()).get(0);

		try {

			String formIdStr = request.getParameter("form_id");
			String formNameStr = request.getParameter("form_name");
			String reposIdStr = request.getParameter("repository_id");

			LOG.debug(String.format("FormId: %s, FormName: %s, ReposId: %s, Captured By: %s", formIdStr, formNameStr,
					reposIdStr, capturedBy.getUsername()));
			Form form = new Form(0, formNameStr, reposRepository.findById(Integer.parseInt(reposIdStr)), capturedBy);
			Status fStatus = formRepository.create(form);
			String[] excludes = { "form_id", "form_name", "repository_id" };
			form = formRepository.findById(fStatus.getGeneratedId());

			String formFolder = String.format("%08d/", capturedBy.getId()) + String.format("%05d/", form.getId());
			LOG.debug(String.format("Form folder: %s", formFolder));

			Collection<Part> parts = request.getParts();
			for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();) {

				try {
					Part part = (Part) iterator.next();

					String contentType = part.getContentType();
					String name = part.getName();

					if (Arrays.asList(excludes).contains(name)) {
						continue;
					}

					String rawValue = "NA";
					if (contentType.startsWith("text/plain")) {
						rawValue = IOUtils.toString(part.getInputStream(), "UTF-8");
					} else {
						MultipartFile file = request.getFile(name);
						if (file != null) {
							rawValue = file.getOriginalFilename();
						} else {
							rawValue = "Invalid part";
						}
					}

					LOG.debug(String.format("Type: %s, Name: %s, Value: %s", contentType, name, rawValue));

					FormData data = new FormData(0, name, rawValue, name.split(":")[0], form);
					formDataRepository.create(data);

				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(e.getMessage());
				}

			}

			Iterator<String> files = request.getFileNames();
			while (files.hasNext()) {
				String fn = files.next();

				MultipartFile file = request.getFile(fn);
				// Get the file and save it somewhere
				byte[] bytes = file.getBytes();
				LOG.debug(String.format("File: %s, Size: %d, Filename: %s", fn, bytes.length,
						file.getOriginalFilename()));

				String filePath;
				String fileName = file.getOriginalFilename();
				if (file.getContentType().contains("video")) {
					filePath = videos;
				} else if (file.getContentType().contains("image")) {
					filePath = pictures;
				} else {
					filePath = others;
				}
				String myFolder = uploadPath + formFolder + filePath;
				String fullPath = myFolder + fileName;
				LOG.debug(fullPath);
				Path tmp = Paths.get(myFolder);
				if (!Files.exists(tmp)) {
					try {
						Files.createDirectories(tmp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				try {
					Path path = Paths.get(fullPath);
					Files.write(path, bytes);
				} catch (IOException e) {
					e.printStackTrace();
					LOG.debug(String.format("Exception: %s", e));
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.debug(String.format("Exception: %s", e));
		}

		ApiResponse response = new ApiResponse();
		response.setMessage("Successfully received");
		response.setStatus("200");

		return response;
	}

	@RequestMapping(value = "/repos", method = RequestMethod.GET)
	public @ResponseBody ApiResponse getMyRepos(@AuthenticationPrincipal UserDetails user) {
		String username = user.getUsername();
		System.out.println(String.format("Username: %s", username));
		User u = userRepository.getAll("username", username).get(0);

		ApiResponse response = new ApiResponse();
		response.setMessage("Successfully received");
		response.setStatus("200");
		response.setData(u.getProjects());

		return response;
	}

	@RequestMapping(value = "/repos/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getReposFile(@AuthenticationPrincipal UserDetails user, @PathVariable("id") int id,
			HttpServletResponse response) {
		FormRepos formRepos = reposRepository.findById(id);
		Resource file = storageService.loadAsResource(formRepos.getFilepath());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@RequestMapping(value = "/media/{formDataId}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImageAsResource(@PathVariable("formDataId") int formDataId) throws IOException {
		FormData fd = formDataRepository.findById(formDataId);
		Form f = fd.getForm();
		int formId = f.getId();
		int capturedBy = f.getCapturedBy().getId();
		String mediaType = fd.getDatatype().equals("INPVID") ? "videos" : "pictures";
		String fileName = fd.getRawvalue();
		String formFolder = String.format("%08d/", capturedBy) + String.format("%05d/", formId);
		String fullPath = uploadPath + formFolder + mediaType + "/" + fileName;
		LOG.debug(fullPath);

		Path path = Paths.get(fullPath);
		HttpHeaders headers = new HttpHeaders();
		byte[] media = Files.readAllBytes(path);
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());

		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/zip/{formId}", method = RequestMethod.GET, produces={"application/zip"})
	public ResponseEntity<byte[]> getZipedForm(@PathVariable("formId") int formId, HttpServletResponse response ) throws IOException {
		Form form=formRepository.findById(formId);
		List<FormData> formDataList = form.getDataList();
		try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet formSheet =workbook.createSheet("Form");
            HSSFRow row1 = formSheet.createRow((short)0);
            row1.createCell(0).setCellValue("PROJECT");
            row1.createCell(1).setCellValue(form.getFormRepos().getProject().getName());
            
            HSSFRow row2 = formSheet.createRow((short)1);
            row2.createCell(0).setCellValue("REPOSITORY");
            row2.createCell(1).setCellValue(form.getFormRepos().getName());
            
            HSSFRow row3 = formSheet.createRow((short)2); 
            row3.createCell(0).setCellValue("FORM NAME");
            row3.createCell(1).setCellValue(form.getName());
            
            HSSFRow row4 = formSheet.createRow((short)3);
            row4.createCell(0).setCellValue("CAPTURED BY");
            row4.createCell(1).setCellValue(form.getCapturedBy().getUsername());
  
            
            HSSFSheet sheet = workbook.createSheet("Form Data");  

            HSSFRow rowhead = sheet.createRow((short)0);
            rowhead.createCell(0).setCellValue("ID");
            rowhead.createCell(1).setCellValue("NAME");
            rowhead.createCell(2).setCellValue("VALUE");
            rowhead.createCell(3).setCellValue("TYPE");
            
            Map<String, String> mediaFiles=new LinkedHashMap<>();

            int i=1;
            for (Iterator<FormData> iterator = formDataList.iterator(); iterator.hasNext();) {
				FormData formData = (FormData) iterator.next();
				HSSFRow row = sheet.createRow((short)(i++));
	            row.createCell(0).setCellValue(formData.getId());
	            row.createCell(1).setCellValue(formData.getMetadata());
	            row.createCell(2).setCellValue(formData.getRawvalue());
	            row.createCell(3).setCellValue(formData.getDatatype() );
	            
	            if(formData.getDatatype().equals("INPPIC")) {
	            	mediaFiles.put(formData.getRawvalue(), "pictures");
	            }else if(formData.getDatatype().equals("INPVID")) {
	            	mediaFiles.put(formData.getRawvalue(), "videos");
	            }
			}
            String fname=formId+"-excel";
            File temp = File.createTempFile(fname , ".xslx");   
            FileOutputStream fileOut = new FileOutputStream(temp );
            workbook.write(fileOut); 
            fileOut.close();
            System.out.println("Your excel file has been generated!");
            
            workbook.close(); 
            
            File zipFile = File.createTempFile(form.getName() , ".zip"); 
            zipIt(zipFile, mediaFiles, form.getCapturedBy().getId(), formId, temp);
            
            
            Path path = Paths.get(zipFile.getAbsolutePath());
    		HttpHeaders headers = new HttpHeaders();
    		byte[] media = Files.readAllBytes(path);
    		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    	    response.setHeader("Content-Disposition", "attachment; filename=" + form.getFormRepos().getName()+"-"+form.getId()+".zip");

    		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
    		return responseEntity;

        } catch ( Exception ex ) {
            System.out.println(ex);
        }
		return null;
	}
	
	private void zipIt(File zipFile, Map<String, String> files, int capturedBy, int formId, File excel){
		
		String formFolder = String.format("%08d/", capturedBy) + String.format("%05d/", formId);
		

	     byte[] buffer = new byte[1024];

	     try{

	    	FileOutputStream fos = new FileOutputStream(zipFile);
	    	ZipOutputStream zos = new ZipOutputStream(fos);

	    	System.out.println("Output to Zip : " + zipFile);

	    	for(String key : files.keySet()){
	    		
	    		String fullPath = uploadPath + formFolder + files.get(key)+"/"+key;
	    		LOG.debug(fullPath);

	    		System.out.println("File Added : " + key);
	    		ZipEntry ze= new ZipEntry(key);
	        	zos.putNextEntry(ze);

	        	FileInputStream in = new FileInputStream(fullPath);
 
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}

	        	in.close();
	    	}
	    	
	    	{
	    		ZipEntry ze= new ZipEntry(excel.getName());
	        	zos.putNextEntry(ze);

	        	FileInputStream in = new FileInputStream(excel);
 
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}

	        	in.close();
	    	}

	    	zos.closeEntry();
	    	//remember close it
	    	zos.close();

	    	System.out.println("Done");
	    }catch(IOException ex){
	       ex.printStackTrace();
	    }
	   }


}
