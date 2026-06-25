package com.mycompany.hitandblow ;

import lombok.* ;
import jakarta.validation.constraints.NotEmpty ;
import jakarta.validation.constraints.Pattern ;
// import jakarta.validation.constraints.Size ;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameCmdBean
{
	@NotEmpty ( message = "{message.error.required}" )
	@Pattern ( regexp = "^(?!.*(.).*\\1)\\d+$" , message = "{message.error.duplicate}" )
	private String input ;
}
