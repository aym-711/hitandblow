package com.mycompany.hitandblow ;

import java.util.regex.Pattern ;

public class HitAndBlowData
{
	// 出題桁数
	protected int len ;
	// 解答数列
	protected int [ ] answer ;

	// 入力チェック用正規表現
	public static final Pattern ZIPPATTERN = Pattern.compile ( "^[0-9]+$" ) ;

	// セッター:len
	public void setlen ( int len )
	{
		if ( len < 3 )
		{
			len = 3 ;
		}
		if ( len > 5 )
		{
			len = 5 ;
		}
		this.len = len ;
	}

	// ゲッター:len
	public int getLen ( )
	{
		return len ;
	}

	// コンストラクター
	public HitAndBlowData ( int len )
	{
		setlen ( len ) ;
		// answerに0～9を入れる
		answer = new int [ 10 ] ;
		for ( int i = 0 ; i < answer.length ; i ++ )
		{
			answer [ i ] = i ;
		}

		// answerをシャッフルする
		int last = answer.length - 1 ;
		for ( int i = 0 ; i < answer.length - 1 ; i ++ , last -- )
		{
			// 未シャッフル部分のインデックスを乱数で一つ選ぶ
			int r = ( int ) ( Math.random ( ) * ( last + 1 ) ) ;
			// 要素の交換(交換は斜めになるので覚える)
			int swap = answer [ r ] ;
			answer [ r ] = answer [ last ] ;
			answer [ last ] = swap ;
		}
	}

	// 文字列化
	public String toString ( )
	{
		// answerの先頭からlen要素を結合した文字列列を返す
		StringBuilder sb = new StringBuilder ( ) ;
		for ( int i = 0 ; i < len ; i ++ )
		{
			// answer[0],answer[1],answer[2],answer[3],answer[len]になる処理
			sb.append ( answer [ i ] ) ;
		}
		// 最後1回でStringに変える
		return sb.toString ( ) ;
	}

	// キー入力内容チェック
	public boolean checkInput ( String input )
	{
		// inpuがlen桁でなければfalse
		if ( input.length ( ) != len )
		{
			return false ;
		}
		// inputのすべての桁が数字でなければfalse
		if ( ! ZIPPATTERN.matcher ( input ).matches ( ) )
		{
			return false ;
		}
		// inputに重複文字があればfalse
		// '0'～'9'までの出現回数を調べる
		for ( int i = 0 ; i < 10 ; i ++ )
		{
			// 調べる文字（int型）なので文字列変換
			char c = ( char ) ( '0' + i ) ;
			// inputの中のcの個数をcntに数える
			int cnt = 0 ;
			for ( int j = 0 ; j < len ; j ++ )
			{
				// jの要素とCが同じならcntに数える
				if ( input.charAt ( j ) == c )
				{
					cnt ++ ;
				}
			}
			// cntが2以上ならfalseを返す
			if ( cnt >= 2 )
			{
				return false ;
			}
		}
		return true ;
	}

	// ヒット数判定
	public int judgeHit ( String input )
	{
		// answer要素とinput要素を比較して同じだったらhitに数える
		int hit = 0 ;
		for ( int i = 0 ; i < len ; i ++ )
		{
			// answer(int型)とinput(String型)の比較
			if ( ( char ) ( '0' + answer [ i ] ) == input.charAt ( i ) )
			{
				hit ++ ;
			}
		}
		return hit ;
	}

	// ブロー数判定
	public int judgeBlow ( String input )
	{
		int blow = 0 ;
		for ( int i = 0 ; i < len ; i ++ )
		{
			// inputからanswer[i]（indexで）を探す、返ってきたら＋していく(無い場合-1が返ってくる)
			if ( input.indexOf ( '0' + answer [ i ] ) != - 1 )
			{
				blow ++ ;
			}
		}
		return blow - judgeHit ( input ) ;
	}
}
