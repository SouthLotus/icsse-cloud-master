package com.cloud.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.dao.TintucRepository;
import com.cloud.model.Tintuc;

@Service
@Transactional
public class TintucService {
	
	public final String IMG_FOLDER = "img";
	
	@Autowired
	AmazonService amazonService;
	private final TintucRepository tintucRepository;
	
	public TintucService(TintucRepository tintucRepository) {
		this.tintucRepository=tintucRepository;
	}
	public List<Tintuc> findAll(){
		List<Tintuc> tintucs = new ArrayList<>();
		for(Tintuc tintuc : tintucRepository.findAll())
		{
			tintucs.add(tintuc);
		}
		return tintucs;
	}
	
	public Tintuc findTintuc(int id) {
		return tintucRepository.findById(id).get();
	}
	public void save(Tintuc tintuc) {
		tintucRepository.save(tintuc);
	}
	public boolean delete(int id) {
		try {
			Tintuc news = tintucRepository.findById(id).get();
			String imgName =
					String.format("new.%d.%s", 
							news.getId(), 
							Tool.getExtension(news.getHinhanh()));
			String pathToImg = IMG_FOLDER + "/" + imgName;
			boolean flag = amazonService.deleteFile(pathToImg);
			tintucRepository.deleteById(id);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Tintuc xem() {
		return tintucRepository.HienthitinChinh();
	}
	public List<Tintuc> lienquan(){
		List<Tintuc> tintucs = new ArrayList<>();
		for(Tintuc tintuc : tintucRepository.lienquan())
		{
			tintucs.add(tintuc);
		}
		return tintucs;
	}
	
	public List<Tintuc> layMoiNhat() {
		return tintucRepository.layMoiNhat();
	}
	
	public boolean uploadImage(Tintuc news, MultipartFile file) {
		try {
			String imgName =
					String.format("new.%d.%s", 
							news.getId(), 
							Tool.getExtension(file.getOriginalFilename()));
			String pathToImg = IMG_FOLDER + "/" + imgName;
			String imgPath = amazonService.uploadFile(file, pathToImg);
			if(imgPath.equals("")) {
				return false;
			}
			news.setHinhanh(imgPath);
			if(tintucRepository.save(news) == null) {
				amazonService.deleteFile(pathToImg);
				return false;
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
}
