package com.mycompany.hitandblow ;

import java.util.* ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Service ;

@Service
public class GameService
{
	private HitAndBlowData data ;
	@Autowired
	private HistoryMapper mapper ;

	// 桁数（初期値は中間の4）
	private int len = 4 ;

	// 桁数設定、ランダムなanswerを生成する
	public void setLen ( int len )
	{
		if ( len < 3 )
			len = 3 ;
		if ( len > 5 )
			len = 5 ;
		this.len = len ;
		this.data = new HitAndBlowData ( len ) ;
	}


	// 入力された値でHit/Blowを計算、履歴保存
	public List< History > judge ( String input )
	{
		try
		{
			int hit = data.judgeHit ( input ) ;
			int blow = data.judgeBlow ( input ) ;

			// 判定結果をDBに保存
			History history = new History ( ) ;
			history.setInput ( input ) ;
			history.setHit ( hit ) ;
			history.setBlow ( blow ) ;

			mapper.insert ( history ) ;

			// 履歴取得
			return mapper.findAll ( ) ;
		}
		catch ( Exception e )
		{
			throw new GameException ( "システムエラーが発生しました" , e ) ;
		}
	}

	// len取得
	public int getLen ( )
	{
		return len ;
	}

	// answer取得
	public String getAnswer ( )
	{
		return data.toString ( ) ;
	}


	public boolean checkInput ( String input )
	{
		return data.checkInput ( input ) ;
	}


	// 回数制限数取得（len × 4）
	public int getMaxCount ( )
	{
		return len * 4 ;
	}

	// 履歴一覧を取得
	public List< History > getHistory ( )
	{
		return mapper.findAll ( ) ;
	}

	// 履歴削除（ゲームリセット用）
	public void clearHistory ( )
	{
		mapper.deleteAll ( ) ;
	}

}
