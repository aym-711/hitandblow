package com.mycompany.hitandblow ;

import java.util.* ;
import org.apache.ibatis.annotations.Mapper ;
import org.apache.ibatis.annotations.Insert ;
import org.apache.ibatis.annotations.Select ;
import org.apache.ibatis.annotations.Delete ;

/**
 * MyBatis 用 SQL マッパーインターフェース（履歴用）。
 */
@Mapper
public interface HistoryMapper
{
	/**
	 * 履歴を追加（INSERT）。
	 * 
	 * @param history 入力値・Hit・Blow を持つ履歴データ
	 */
	@Insert ( """
		    INSERT INTO history (input, hit, blow)
		    VALUES (#{input}, #{hit}, #{blow})
		""" )
	public void insert ( History history ) ;

	/**
	 * 履歴を全件取得（SELECT）。
	 * 
	 * @return 履歴データの一覧（List）
	 */
	@Select ( "SELECT * FROM history ORDER BY id DESC" )
	public List< History > findAll ( ) ;

	@Delete ( "DELETE FROM history" )
	public void deleteAll ( ) ;
}
