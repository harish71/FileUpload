package com.example.FileUpload.Controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class FileUpoadController {
	@PostMapping("/upload")
	public ResponseEntity<Object> uploadFile(@RequestParam("filename") MultipartFile filename){
		File file=new File("E:\\Downloads\\uploaded",filename.getOriginalFilename());
		System.out.println("file name::"+file);
		
		try {
			file.createNewFile();
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(filename.getBytes());
			fos.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		ResponseEntity<Object> re =new ResponseEntity<>("successfully uploaded",HttpStatus.OK);
		return re;
	}
	/*@RequestMapping(value="/upload",method=RequestMethod.POST,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> uploadFile(@RequestParam("filename") MultipartFile filename){
		File file=new File("E:\\Downloads\\uploaded",filename.getOriginalFilename());
		System.out.println("file name::"+file);
		
		try {
			file.createNewFile();
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(filename.getBytes());
			fos.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		Response9Entity<Object> re =new ResponseEntity<>("successfully uploaded",HttpStatus.OK);
		return re;
	}*/
	/*@RequestMapping(value="/uploadmultiple",method=RequestMethod.POST,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)*/
	@PostMapping("uploadmultiple")
	public ResponseEntity<Object> multipleUploadFiles(@RequestParam("files") MultipartFile[] files){
		FileOutputStream fos=null;
		for(MultipartFile f:files){
			File file=new File("E:\\Downloads\\uploaded",f.getOriginalFilename());
			System.out.println("file name::"+file);
			try {
				fos=new FileOutputStream(file);
				fos.write(f.getBytes());
				fos.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		ResponseEntity<Object> re =new ResponseEntity<>("all files successfully uploaded",HttpStatus.OK);
		return re;
	}
	@GetMapping("download/{filename}")
	public ResponseEntity<Object> downloadFile(@PathVariable String filename,HttpServletResponse responce){
		String filePath="E:\\Downloads\\uploaded\\"+filename;
		System.out.println("filepath"+filePath);
		Path path=Paths.get(filePath);
		System.out.println("path::"+path);
		ObjectMapper o=new ObjectMapper();
		Resource resource=null;
		try {
			resource=new UrlResource(path.toUri());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(resource.exists()){
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+resource.getFilename()+"\"").body(resource);

			
		}
		else{
			return new ResponseEntity<Object>("file not found",HttpStatus.NOT_FOUND);
		}
		
		
	}

}
