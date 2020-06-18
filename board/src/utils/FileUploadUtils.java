package utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadUtils {
	//첨부파일 내용과 일반 폼 필드 내용을 HashMap에 담고
	//리턴
	
	//request를 받아서 사용자의 요청 분리하기
	public HashMap<String, String> upload(HttpServletRequest req){
		HashMap<String, String> uploadMap = new HashMap<String, String>();
		//file upload 요청이 있는지 확인하기
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);

		if(isMultipart){
			// 전송된 파일을 디스크에 저장하기 위한 객체 생성
			DiskFileItemFactory factory = new DiskFileItemFactory();
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			//업로드 가능 사이즈 지정
			upload.setSizeMax(2000*1024);
			
			//사용자의 request 담기
			List<FileItem> fileItems=null;
			try {
				fileItems = upload.parseRequest(req);
			} catch (FileUploadException e1) {				
				e1.printStackTrace();
			}
			
			String fieldName = null;
			
			Iterator<FileItem> iter = fileItems.iterator();
			while(iter.hasNext()){
				FileItem item = iter.next();
				
				if(item.isFormField()){
					fieldName = item.getFieldName();
					String value=null;
					try {
						value = item.getString("utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					uploadMap.put(fieldName, value);				
				}else{
					fieldName = item.getFieldName();
					String fileName = item.getName();
					
					if(!fileName.isEmpty() && item.getSize()>0) {					
									
						String path = "d:\\upload";
						//동일한 파일명이 들어올 경우 처리하기
						
						//중복되지 않는 고유한 키 값 설정
						UUID uuid = UUID.randomUUID();
											
						File uploadFile = new File(path+"\\"+uuid.toString()+"_" +fileName);
						try {
							item.write(uploadFile);
						} catch (Exception e) {							
							e.printStackTrace();
						}
						
						uploadMap.put(fieldName, uploadFile.getName());				
					}
				}
			}
		}
		return uploadMap;
		}	
}







