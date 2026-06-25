package com.mycompany.hitandblow ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.ui.Model ;
import java.util.List ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RequestParam ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import jakarta.validation.Valid ;
import org.springframework.validation.BindingResult ;

@Controller
@RequestMapping ( "/" )
public class GameController
{

	@Autowired
	private GameService gameService ;
	protected static final Logger log = LoggerFactory.getLogger ( GameController.class ) ;

	// トップ画面
	@RequestMapping ( path = { "/" , "/index" } , method = RequestMethod.GET )
	public String index ( )
	{
		log.debug ( "index" ) ;
		return "index" ;
	}

	// ゲーム開始（履歴リセットしてゲーム画面へ）
	@RequestMapping ( path = "/start" , method = RequestMethod.GET )
	public String start ( @RequestParam int len )
	{
		gameService.clearHistory ( ) ;
		gameService.setLen ( len ) ;
		return "redirect:/game" ;
	}

	// ゲーム画面（入力フォーム表示）
	@RequestMapping ( path = "/game" , method = RequestMethod.GET )
	public String game ( Model model )
	{
		log.debug ( "game" ) ;
		// フォーム用オブジェクトを画面に渡す
		if ( ! model.containsAttribute ( "gameCmdBean" ) )
		{
			model.addAttribute ( "gameCmdBean" , new GameCmdBean ( ) ) ;
		}

		// 履歴一覧を取得、画面に渡す
		List< History > list = gameService.getHistory ( ) ;
		model.addAttribute ( "historyList" , list ) ;
		model.addAttribute ( "len" , gameService.getLen ( ) ) ;
		// 残り回数
		int count = list.size ( ) ;
		int max = gameService.getMaxCount ( ) ;
		int remain = max - count ;
		model.addAttribute ( "remain" , remain ) ;

		return "game" ;
	}

	// ゲーム画面（スマホUI）
	@RequestMapping ( path = "/game2" , method = RequestMethod.GET )
	public String game2 ( Model model )
	{
		log.debug ( "game_keypad" ) ;

		if ( ! model.containsAttribute ( "gameCmdBean" ) )
		{
			model.addAttribute ( "gameCmdBean" , new GameCmdBean ( ) ) ;
		}

		List< History > list = gameService.getHistory ( ) ;
		model.addAttribute ( "historyList" , list ) ;
		model.addAttribute ( "len" , gameService.getLen ( ) ) ;

		int count = list.size ( ) ;
		int max = gameService.getMaxCount ( ) ;
		int remain = max - count ;
		model.addAttribute ( "remain" , remain ) ;

		return "game_keypad" ;
	}

	// 判定処理（フロントから値受け取る）
	@RequestMapping ( path = "/judge" , method = RequestMethod.POST )
	public String judge ( @Valid @ModelAttribute GameCmdBean form , BindingResult result ,
		Model model )
	{
		// CmdBeanで入力エラーチェック
		if ( result.hasErrors ( ) )
		{
			model.addAttribute ( "gameCmdBean" , form ) ;

			List< History > list = gameService.getHistory ( ) ;
			model.addAttribute ( "historyList" , list ) ;

			int count = list.size ( ) ;
			int max = gameService.getMaxCount ( ) ;
			int remain = max - count ;
			model.addAttribute ( "remain" , remain ) ;
			model.addAttribute ( "len" , gameService.getLen ( ) ) ;

			return "game" ;
		}

		String input = form.getInput ( ) ;
		log.debug ( "judge:{}" , input ) ;

		if ( ! gameService.checkInput ( input ) )
		{
			model.addAttribute ( "gameCmdBean" , form ) ;

			List< History > list = gameService.getHistory ( ) ;
			model.addAttribute ( "historyList" , list ) ;

			int count = list.size ( ) ;
			int max = gameService.getMaxCount ( ) ;
			int remain = max - count ;
			model.addAttribute ( "remain" , remain ) ;
			model.addAttribute ( "len" , gameService.getLen ( ) ) ;
			model.addAttribute ( "errorKey" , "message.error.length" ) ;
			model.addAttribute ( "errorArg" , gameService.getLen ( ) ) ;

			return "game" ;
		}

		// Serviceに任せる

		List< History > list = null ;

		try
		{
			list = gameService.judge ( input ) ;
		}
		catch ( GameException e )
		{
			log.error ( "システムエラー発生" , e ) ;
			model.addAttribute ( "error" , "システムエラーが発生しました" ) ;


			List< History > errorList = gameService.getHistory ( ) ;
			model.addAttribute ( "historyList" , errorList ) ;

			int count = errorList.size ( ) ;
			int max = gameService.getMaxCount ( ) ;
			int remain = max - count ;

			model.addAttribute ( "remain" , remain ) ;
			model.addAttribute ( "len" , gameService.getLen ( ) ) ;

			return "game" ;
		}

		// 回数取得
		int count = list.size ( ) ;
		int max = gameService.getMaxCount ( ) ;

		// あと何回
		int remain = max - count ;

		// 残り回数を渡す
		model.addAttribute ( "remain" , remain ) ;

		// 履歴最後から取得
		History last = list.get ( 0 ) ;
		// 仮（lastでいくの？あとで差し替えかも）
		int hit = last.getHit ( ) ;
		int blow = last.getBlow ( ) ;

		// 正解だった場合の処理
		if ( hit == gameService.getLen ( ) )
		{
			gameService.clearHistory ( ) ;
			model.addAttribute ( "answer" , input ) ;
			return "correct" ;
		}

		// 回数オーバー
		if ( count >= max )
		{
			String answer = gameService.getAnswer ( ) ;
			gameService.clearHistory ( ) ;
			model.addAttribute ( "answer" , answer ) ;
			return "gameover" ;
		}

		model.addAttribute ( "historyList" , list ) ;
		model.addAttribute ( "input" , input ) ;
		model.addAttribute ( "hit" , hit ) ;
		model.addAttribute ( "blow" , blow ) ;

		return "result" ;
	}

	// ギブアップ処理（ギブアップ画面で答えを表示）
	@RequestMapping ( path = "/giveup" , method = RequestMethod.POST )
	public String giveup ( Model model )
	{
		// 仮（あとでロジック差し替え）
		String answer = gameService.getAnswer ( ) ;
		gameService.clearHistory ( ) ;
		model.addAttribute ( "answer" , answer ) ;
		return "giveup" ;
	}
}
