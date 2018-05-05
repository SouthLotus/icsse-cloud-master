package com.cloud.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cloud.model.Tintuc;



public interface TintucRepository extends CrudRepository<Tintuc,Integer>{
	 @Query(value = "select * from tintuc order by id desc limit 1", nativeQuery = true)
	  Tintuc HienthitinChinh();
	 
	 @Query(value = "select * from tintuc order by rand() limit 3", nativeQuery = true)
	  List<Tintuc> lienquan();
	 
	 @Query(value= "select * from tintuc order by thoigian desc, id desc limit 5", nativeQuery = true)
	 List<Tintuc> layMoiNhat();
}
