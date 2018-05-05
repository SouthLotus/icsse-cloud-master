package com.cloud.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AmazonService {
	private AmazonS3Client s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		builder.withCredentials(provider);
		builder.withRegion(Regions.AP_SOUTHEAST_1);
		this.s3client = (AmazonS3Client) builder.build();
	}
	
	public String uploadFile(MultipartFile file, String desiredName) throws IOException {
		try(InputStream inp = file.getInputStream();
				) {
			String fileName = desiredName;
			ObjectMetadata metaData = new ObjectMetadata();
			metaData.setContentType(file.getContentType());
			PutObjectRequest requestObject = new PutObjectRequest(bucketName, fileName, inp, metaData);
			requestObject.setCannedAcl(CannedAccessControlList.PublicRead);
			s3client.putObject(requestObject);
			return s3client.getResourceUrl(bucketName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String createFolder(String name) throws IOException {
		try(InputStream inp = new ByteArrayInputStream(new byte[0])) {
			ObjectMetadata metaData = new ObjectMetadata();
			metaData.setContentLength(0);
			PutObjectRequest requestObject = new PutObjectRequest(bucketName, name+"/", inp, metaData);
			requestObject.setCannedAcl(CannedAccessControlList.PublicRead);
			s3client.putObject(requestObject);
			return name;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "";
	}
	
	public boolean deleteFile(String name) {
		try {
			s3client.deleteObject(bucketName, name);
			return true;
		}catch(Exception exp) {
			exp.printStackTrace();
		}
		return false;
	}
	
	public AmazonS3Client getClient() {
		return s3client;
	}
	
}
